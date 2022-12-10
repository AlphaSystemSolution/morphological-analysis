package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import ui.commons.service.ServiceFactory
import morphologicalanalysis.morphology.utils.*
import morphologicalanalysis.graph.model.GraphNodeType
import morphology.model.{ Location, Token }
import morphology.graph.model.*
import utils.{ DrawingTool, TerminalNodeInput }
import javafx.scene.Node as JfxNode
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.geometry.Pos
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ Alert, ContextMenu, Menu, MenuItem }
import scalafx.scene.{ Group, Node }
import scalafx.scene.layout.{ BorderPane, Pane, Region }
import scalafx.scene.paint.Color
import scalafx.scene.shape.Line
import scalafx.scene.text.{ Text, TextAlignment }

import scala.collection.mutable

class CanvasSkin(control: CanvasView, serviceFactory: ServiceFactory) extends SkinBase[CanvasView](control) {

  import CanvasSkin.*

  private lazy val addNodeDialog = AddNodeDialog(serviceFactory)
  private val styleText = (hex: String) => s"-fx-background-color: $hex"
  private val nodesMap = mutable.Map.empty[String, GraphNodeView[?]]
  private val posNodesMap = mutable.Map.empty[Long, Seq[PartOfSpeechNodeView]]
  private val contextMenu = new ContextMenu()
  private val canvasPane = new Pane() {
    minWidth = Region.USE_PREF_SIZE
    minHeight = Region.USE_PREF_SIZE
    maxWidth = Region.USE_PREF_SIZE
    maxHeight = Region.USE_PREF_SIZE
    prefWidth = control.graphMetaInfo.width
    prefHeight = control.graphMetaInfo.height
    style = styleText(control.graphMetaInfo.toColor.toHex)
  }
  private var gridLines: Option[Node] = None

  control
    .selectedNodeProperty
    .onChange((_, _, nv) => {
      if Option(nv).isDefined then {
        nodesMap.get(nv.id.toString) match
          case Some(node) =>
            nv match
              case n: TerminalNode     => node.asInstanceOf[TerminalNodeView].source = n
              case n: PartOfSpeechNode => node.asInstanceOf[PartOfSpeechNodeView].source = n
              case n: PhraseNode       => node.asInstanceOf[PhraseNodeView].source = n
              case n: RelationshipNode => node.asInstanceOf[RelationshipNodeView].source = n
          case None => ()
      }
      canvasPane.requestLayout()
    })

  getChildren.addAll(initializeSkin)

  private[control] def graphNodes: Seq[GraphNode] = {
    val nodes = nodesMap.values.map(_.source.asInstanceOf[GraphNode]).toSeq
    val otherNodes = nodes.filterNot(_.graphNodeType == GraphNodeType.PartOfSpeech)
    otherNodes.map {
      case n: TerminalNode =>
        val posNodes =
          posNodesMap(n.token.id)
            .map(_.source)
            .sortWith { case (p1, p2) =>
              p1.location.locationNumber > p2.location.locationNumber
            }
        n.copy(partOfSpeechNodes = posNodes)
      case n => n
    }
  }

  private def initializeSkin = {
    control
      .graphMetaInfoProperty
      .onChange((_, _, nv) => {
        canvasPane.prefWidth = nv.width
        canvasPane.prefHeight = nv.height
        canvasPane.style = styleText(nv.toColor.toHex)
        toggleGridLines(nv)
      })

    BorderPane.setAlignment(canvasPane, Pos.TopLeft)
    new BorderPane() {
      center = canvasPane
      minHeight = roundToNearest20(screenHeight * 0.90)
    }
  }

  private def toggleGridLines(graphMetaInfo: GraphMetaInfo): Unit = {
    gridLines.foreach(canvasPane.children.remove(_))
    gridLines = None
    val showGridLines = graphMetaInfo.showGridLines
    if graphMetaInfo.showOutLines || showGridLines then {
      gridLines = Some(
        DrawingTool.drawGridLines(
          showGridLines,
          graphMetaInfo.width.intValue(),
          graphMetaInfo.height.intValue()
        )
      )
      canvasPane.children.addOne(gridLines.get)
    }
    canvasPane.requestLayout()
  }

  private[control] def loadGraph(graphMetaInfo: GraphMetaInfo, nodes: Seq[GraphNode]): Unit = {
    clear()
    canvasPane.children = parseNodes(nodes)
    toggleGridLines(graphMetaInfo)
  }

  private def clear(): Unit = {
    nodesMap.clear()
    posNodesMap.clear()
    canvasPane.children.clear()
    contextMenu.items.clear()
  }

  private def drawTerminalNode(terminalNode: TerminalNode): Group = {
    val terminalNodeView = TerminalNodeView()
    terminalNodeView.source = terminalNode
    nodesMap += (terminalNodeView.getId -> terminalNodeView)
    val line = drawLine(terminalNodeView)
    val derivedTerminalNode = DerivedTerminalNodeTypes.contains(terminalNode.graphNodeType)
    val color = if derivedTerminalNode then DerivedTerminalNodeColor else DefaultTerminalNodeColor
    val arabicText = drawArabicText(terminalNodeView, color)
    arabicText.onMouseClicked = event => {
      // if event.isPopupTrigger then {
      contextMenu.items.addAll(initTerminalNodeContextMenu(terminalNodeView).map(_.delegate))
      contextMenu.show(arabicText, event.getSceneX, event.getSceneY)
      // }
      control.selectedNode = terminalNodeView.source
      event.consume()
    }

    val translationText = drawTranslationText(terminalNodeView, color)

    val posNodeComponents = terminalNode
      .partOfSpeechNodes
      .flatMap(drawPartOfSpeechNodes(terminalNodeView, derivedTerminalNode))

    val group = new Group()
    group.children = Seq(line, arabicText, translationText) ++ posNodeComponents
    group.translateXProperty().bind(terminalNodeView.txProperty)
    group.translateYProperty().bind(terminalNodeView.tyProperty)

    group
  }

  private def drawLine[N <: LineSupport, V <: LineSupportView[N]](view: V): Line = {
    val line = DrawingTool.drawLine(view.getId, view.x1, view.y1, view.x2, view.y2, LineColor, 0.5)
    line.startXProperty().bindBidirectional(view.x1Property)
    line.startYProperty().bindBidirectional(view.y1Property)
    line.endXProperty().bindBidirectional(view.x2Property)
    line.endYProperty().bindBidirectional(view.y2Property)
    line
  }

  private def drawArabicText(view: GraphNodeView[?], color: Color): Text = {
    val arabicText =
      DrawingTool.drawText(view.getId, view.text, view.x, view.y, TextAlignment.Right, color, view.font.toFont)

    arabicText.textProperty().bindBidirectional(view.textProperty)
    arabicText.xProperty().bindBidirectional(view.xProperty)
    arabicText.yProperty().bindBidirectional(view.yProperty)
    view.fontProperty.onChange((_, _, nv) => arabicText.font = nv.toFont)
    arabicText
  }

  private def drawTranslationText(view: TerminalNodeView, color: Color) = {
    val translationText = DrawingTool.drawText(
      view.getId,
      view.translationText,
      view.translationX,
      view.translationY,
      TextAlignment.Center,
      color,
      view.translationFont.toFont
    )

    translationText.textProperty().bindBidirectional(view.translationTextProperty)
    translationText.xProperty().bindBidirectional(view.translationXProperty)
    translationText.yProperty().bindBidirectional(view.translationYProperty)
    view.translationFontProperty.onChange((_, _, nv) => translationText.font = nv.toFont)
    translationText
  }

  private def drawPartOfSpeechNodes(
    terminalNodeView: TerminalNodeView,
    derivedTerminalNode: Boolean
  )(posNode: PartOfSpeechNode
  ) = {
    val posView = PartOfSpeechNodeView()
    posView.source = posNode
    nodesMap += (posView.getId -> posView)
    val id = terminalNodeView.source.token.id
    val seq = posNodesMap.getOrElse(id, Seq.empty)
    posNodesMap += (id -> (seq :+ posView))
    if posNode.hidden then Seq.empty
    else {
      posView.translateX = terminalNodeView.translateX
      posView.translateY = terminalNodeView.translateY
      nodesMap += (posView.getId -> posView)
      val color =
        if derivedTerminalNode then DerivedTerminalNodeColor
        else Color.web(posNode.location.partOfSpeechType.colorCode)
      val arabicText = drawArabicText(posView, color)
      arabicText.onMouseClicked = event => {
        if event.isPopupTrigger then {
          // TODO: init ContextMenu
        }
        control.selectedNode = posView.source
        event.consume()
      }

      val circle =
        DrawingTool.drawCircle(s"c_${posNode.id.toString}", posNode.circle.x, posNode.circle.y, DefaultCircleRadius)
      circle.centerXProperty().bindBidirectional(posView.cxProperty)
      circle.centerYProperty().bindBidirectional(posView.cyProperty)
      posView.translateXProperty().bind(terminalNodeView.translateXProperty())
      posView.translateYProperty().bind(terminalNodeView.translateYProperty())
      Seq(arabicText, circle)
    }
  }

  private def initTerminalNodeContextMenu(node: TerminalNodeView): Seq[MenuItem] = {
    contextMenu.items.clear()

    val terminalNode = node.source
    val index = terminalNode.index
    if DerivedTerminalNodeTypes.contains(terminalNode.graphNodeType) then {
      Seq(
        new MenuItem() {
          text = "Remove"
          onAction = event => {
            showRemoveNodeDialog(index)
            event.consume()
          }
        }
      )
    } else
      Seq(
        new MenuItem() {
          text = "Add Node to the left ..."
          onAction = event => {
            showAddNodeDialog(index + 1)
            event.consume()
          }
        },
        new MenuItem() {
          text = "Add Node to the right ..."
          onAction = event => {
            showAddNodeDialog(index)
            event.consume()
          }
        }
      )
  }

  private def showAddNodeDialog(index: Int): Unit = {
    addNodeDialog.currentChapter = control.currentChapter
    addNodeDialog.showReferenceType = index == 0
    addNodeDialog.showAndWait() match
      case Some(AddNodeResult(Some(terminalNodeInput))) => control.addNode(terminalNodeInput, index)
      case _                                            => // do nothing
  }

  private def showRemoveNodeDialog(index: Int): Unit = {
    new Alert(AlertType.Confirmation) {
      initOwner(JFXApp3.Stage)
      title = "Remove Node"
      contentText = "Are you sure you want to permanently remove this node?"
    }.showAndWait() match
      case Some(buttonType) if buttonType.buttonData == ButtonData.OKDone => control.removeTerminalNode(index)
      case _                                                              => // do nothing
  }

  private def parseNodes(
    nodes: Seq[GraphNode]
  ): Seq[Node] = {
    var index = 0
    nodes.flatMap {
      case n: TerminalNode =>
        val group = drawTerminalNode(n.copy(index = index))
        index += 1
        Some(group)
      case _: PhraseNode       => None
      case _: RelationshipNode => None
      case _: PartOfSpeechNode => None
    }
  }
}

object CanvasSkin {

  private val LineColor: Color = Color.web("#B6B6B4")
  private val DerivedTerminalNodeColor: Color = Color.LightGray.darker
  private val DefaultTerminalNodeColor: Color = Color.Black
  private val DefaultCircleRadius = 2.0

  def apply(control: CanvasView, serviceFactory: ServiceFactory): CanvasSkin = new CanvasSkin(control, serviceFactory)
}
