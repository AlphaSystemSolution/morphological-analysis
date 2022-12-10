package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import morphologicalanalysis.morphology.utils.*
import morphologicalanalysis.graph.model.GraphNodeType
import utils.{ GraphBuilderService, TerminalNodeInput }
import fx.ui.util.UiUtilities
import morphology.graph.model.{ DependencyGraph, GraphNode }
import skin.DependencyGraphSkin
import ui.commons.service.ServiceFactory
import javafx.application.Platform
import javafx.scene.control.{ Control, Skin }
import org.slf4j.LoggerFactory
import scalafx.Includes.*
import scalafx.beans.property.ObjectProperty

class DependencyGraphView(serviceFactory: ServiceFactory) extends Control {

  import ServiceFactory.*

  private val logger = LoggerFactory.getLogger(classOf[DependencyGraphView])

  val selectedNodeProperty: ObjectProperty[GraphNode] =
    ObjectProperty[GraphNode](this, "selectedNode", defaultTerminalNode)

  private lazy val openDialog = DependencyGraphOpenDialog(serviceFactory)
  private lazy val createDialog = NewGraphDialog(serviceFactory)
  private[control] val canvasView = CanvasView(serviceFactory)
  private[control] val graphSettingsView = GraphSettingsView()
  private val graphBuilderService = GraphBuilderService(serviceFactory)

  graphSettingsView.graphMetaInfo = canvasView.graphMetaInfo
  canvasView.graphMetaInfoWrapperProperty.bindBidirectional(graphSettingsView.graphMetaInfoProperty)
  canvasView.selectedNodeProperty.bindBidirectional(selectedNodeProperty)

  canvasView
    .addNodeProperty
    .onChange((_, _, nv) => {
      if Option(nv).isDefined then recreateGraph(nv.dependencyGraph, nv.inputs)
    })

  setSkin(createDefaultSkin())

  def createNewGraph(): Unit = {
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
          val inputs = tokens.map(token => TerminalNodeInput(token = token))
          canvasView.currentChapter = chapter
          graphBuilderService.createGraph(chapter, inputs, canvasView.loadGraph)
          UiUtilities.toDefaultCursor(this)

        case _ => UiUtilities.toDefaultCursor(this)
    )
  }

  private def recreateGraph(dependencyGraph: DependencyGraph, inputs: Seq[TerminalNodeInput]): Unit =
    Platform.runLater(() => graphBuilderService.recreateGraph(dependencyGraph, inputs, canvasView.loadGraph))

  def saveGraph(): Unit = {
    UiUtilities.toWaitCursor(this)

    val service = serviceFactory.createDependencyGraphService(
      SaveDependencyGraphRequest(canvasView.dependencyGraph.copy(nodes = canvasView.graphNodes))
    )
    service.onSucceeded = event => {
      UiUtilities.toDefaultCursor(this)
      event.consume()
    }

    service.onFailed = event => {
      Console.err.println(s"Failed to save dependency graph: $event")
      event.getSource.getException.printStackTrace()
      UiUtilities.toDefaultCursor(this)
      event.consume()
    }

    Platform.runLater(() => service.start())
  }

  def openGraph(): Unit = {
    UiUtilities.toWaitCursor(this)
    Platform.runLater(() => {
      openDialog.showAndWait() match
        case Some(OpenDialogResult(Some(chapter), Some(dependencyGraph))) =>
          canvasView.currentChapter = chapter
          canvasView.loadGraph(dependencyGraph)
          UiUtilities.toDefaultCursor(this)

        case _ => UiUtilities.toDefaultCursor(this)
    })
  }

  def selectedNode: GraphNode = selectedNodeProperty.value
  def selectedNode_=(value: GraphNode): Unit = selectedNodeProperty.value = value

  override def createDefaultSkin(): Skin[_] = DependencyGraphSkin(this)
}

object DependencyGraphView {

  def apply(serviceFactory: ServiceFactory): DependencyGraphView = new DependencyGraphView(serviceFactory)
}
