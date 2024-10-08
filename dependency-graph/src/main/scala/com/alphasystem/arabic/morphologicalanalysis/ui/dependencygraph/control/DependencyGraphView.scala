package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Token
import morphologicalanalysis.morphology.utils.*
import morphologicalanalysis.graph.model.GraphNodeType
import dependencygraph.utils.*
import fx.ui.util.UiUtilities
import morphology.graph.model.{ DependencyGraph, GraphNode, Line, PhraseInfo, Point, RelationshipInfoOld }
import skin.DependencyGraphSkin
import ui.commons.service.ServiceFactory
import javafx.application.Platform
import javafx.scene.control.{ Control, Skin }
import org.slf4j.LoggerFactory
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.beans.property.{ ObjectProperty, ReadOnlyBooleanProperty, ReadOnlyBooleanWrapper }
import scalafx.embed.swing.SwingFXUtils
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData

import java.util.UUID
import javax.imageio.ImageIO
import scala.util.{ Failure, Success, Try }

class DependencyGraphView(serviceFactory: ServiceFactory) extends Control {

  import ServiceFactory.*
  import DependencyGraphView.*

  private val logger = LoggerFactory.getLogger(classOf[DependencyGraphView])

  private[control] val selectedNodeProperty: ObjectProperty[GraphNode] =
    ObjectProperty[GraphNode](this, "selectedNode", defaultTerminalNode)

  private val transientGraphWrapperProperty: ReadOnlyBooleanWrapper = ReadOnlyBooleanWrapper(true)

  private lazy val openDialog = DependencyGraphOpenDialog(serviceFactory)
  private lazy val createDialog = NewGraphDialog(serviceFactory)
  private[control] val canvasView = CanvasView(serviceFactory)
  private[control] val graphSettingsView = GraphSettingsView()
  private val graphBuilderService = GraphBuilderService(serviceFactory)

  graphSettingsView.graphMetaInfo = canvasView.graphMetaInfo
  canvasView.graphMetaInfoWrapperProperty.bindBidirectional(graphSettingsView.graphMetaInfoProperty)
  canvasView.selectedNodeProperty.bindBidirectional(selectedNodeProperty)

  canvasView
    .graphOperationRequestProperty
    .onChange((_, _, nv) => {
      if Option(nv).isDefined then {
        nv match
          case AddTerminalNodeRequest(dependencyGraph, inputs) => recreateGraph(dependencyGraph, inputs, Seq.empty)
          case RemoveTerminalNodeRequest(dependencyGraph, inputs, nodes) =>
            recreateGraph(dependencyGraph, inputs, nodes)
          case CreateRelationshipRequest(dependencyGraph, relationshipInfo, owner, dependent) =>
            createRelationship(dependencyGraph, relationshipInfo, owner, dependent)
          case CreatePhraseRequest(dependencyGraph, phraseInfo, line) =>
            createPhrase(dependencyGraph, phraseInfo, line)
          case RemoveNodeRequest(dependencyGraph, id) => removeNode(dependencyGraph, id)
          case SaveGraphRequest                       => saveGraph()
          case NoOp                                   => // do nothing
      }
    })

  setSkin(createDefaultSkin())

  private def transientGraph: Boolean = transientGraphWrapperProperty.value
  private def transientGraph_=(value: Boolean): Unit = transientGraphWrapperProperty.value = value
  def transientGraphProperty: ReadOnlyBooleanProperty = transientGraphWrapperProperty.readOnlyProperty

  private def loadGraph(dependencyGraph: DependencyGraph): Unit = {
    val (graph, transient) =
      if Option(dependencyGraph).isDefined then (dependencyGraph, false) else (defaultDependencyGraph, true)
    transientGraph = transient
    selectedNode = null
    canvasView.dependencyGraph = null
    canvasView.dependencyGraph = graph
  }

  def createGraph(): Unit = {
    UiUtilities.toWaitCursor(this)
    Platform.runLater(() =>
      createDialog.showAndWait() match
        case Some(NewDialogResult(Some(chapter), tokens)) if tokens.nonEmpty =>
          lazy val tokenIds = tokens.map(_.id).mkString("[", ", ", "]")
          logger.debug(
            "Creating graph for chapter: {}, verse: {}, and tokens: {}",
            chapter.chapterNumber,
            tokens.head.verseNumber,
            tokenIds
          )
          val inputs = tokens.map(_.toTerminalNodeInput)
          canvasView.currentChapter = chapter
          graphBuilderService.createGraph(chapter, inputs, loadGraph)
          UiUtilities.toDefaultCursor(this)

        case _ => UiUtilities.toDefaultCursor(this)
    )
  }

  private def recreateGraph(
    dependencyGraph: DependencyGraph,
    inputs: Seq[TerminalNodeInput],
    otherNodes: Seq[GraphNode]
  ): Unit =
    Platform.runLater(() => graphBuilderService.recreateGraph(dependencyGraph, inputs, otherNodes, loadGraph))

  private def createRelationship(
                                  dependencyGraph: DependencyGraph,
                                  relationshipInfo: RelationshipInfoOld,
                                  owner: LinkSupportView[?],
                                  dependent: LinkSupportView[?]
  ): Unit =
    Platform.runLater(() =>
      graphBuilderService.createRelationship(dependencyGraph, relationshipInfo, owner, dependent, loadGraph)
    )

  private def createPhrase(
    dependencyGraph: DependencyGraph,
    phraseInfo: PhraseInfo,
    line: Line
  ): Unit =
    Platform.runLater(() => graphBuilderService.createPhrase(dependencyGraph, phraseInfo, line, loadGraph))

  private def removeNode(dependencyGraph: DependencyGraph, nodeId: UUID): Unit =
    Platform.runLater(() => graphBuilderService.removeNode(dependencyGraph, nodeId, loadGraph))

  def saveGraph(): Unit =
    Platform.runLater(() =>
      graphBuilderService.saveGraph(canvasView.dependencyGraph.copy(nodes = canvasView.graphNodes), loadGraph)
    )

  def openGraph(): Unit = {
    UiUtilities.toWaitCursor(this)
    Platform.runLater(() => {
      openDialog.showAndWait() match
        case Some(OpenDialogResult(Some(chapter), Some(dependencyGraph))) =>
          canvasView.currentChapter = chapter
          loadGraph(dependencyGraph)
          UiUtilities.toDefaultCursor(this)

        case _ => UiUtilities.toDefaultCursor(this)
    })
  }

  def removeGraph(): Unit =
    Platform.runLater(() => graphBuilderService.removeGraph(canvasView.dependencyGraph, loadGraph))

  def exportToPNG(): Unit = {
    Platform.runLater(() => {
      val dependencyGraph = canvasView.dependencyGraph
      if dependencyGraph.chapterNumber > 0 then {
        val path = dependencyGraph.toFileName(RootPath, PngFormat)
        Try {
          ImageIO.write(SwingFXUtils.fromFXImage(canvasView.toImage.delegate, null), PngFormat, path.toFile)
        } match
          case Failure(ex) => ex.printStackTrace()
          case Success(_) =>
            new Alert(AlertType.Information) {
              initOwner(JFXApp3.Stage)
              contentText = s"File ${path.toAbsolutePath.toString} has been created."
            }.showAndWait()
      }
    })
  }

  def selectedNode: GraphNode = selectedNodeProperty.value
  def selectedNode_=(value: GraphNode): Unit = selectedNodeProperty.value = value

  override def createDefaultSkin(): Skin[?] = DependencyGraphSkin(this)
}

object DependencyGraphView {

  private val PngFormat = "png"

  private val RootPath = System.getProperty("user.home").toPath + Seq(".morphological_analysis", "exports")

  extension (src: Token) {
    private def toTerminalNodeInput: TerminalNodeInput = TerminalNodeInput(id = src.id.toUUID, token = src)
  }

  def apply(serviceFactory: ServiceFactory): DependencyGraphView = new DependencyGraphView(serviceFactory)
}
