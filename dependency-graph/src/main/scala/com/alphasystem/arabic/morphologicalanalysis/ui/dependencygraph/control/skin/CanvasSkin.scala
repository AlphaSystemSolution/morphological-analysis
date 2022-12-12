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
import morphologicalanalysis.morphology.model.{ Linkable, Location, RelationshipType, Token }
import morphologicalanalysis.morphology.graph.model.{ RelationshipNode, * }
import utils.{ DrawingTool, TerminalNodeInput }
import javafx.scene.Node as JfxNode
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.geometry.Pos
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ Alert, ContextMenu, Menu, MenuItem }
import scalafx.scene.input.MouseEvent
import scalafx.scene.{ Group, Node }
import scalafx.scene.layout.{ BorderPane, Pane, Region }
import scalafx.scene.paint.Color
import scalafx.scene.shape.{ Line, Polyline }
import scalafx.scene.text.{ Text, TextAlignment }

import java.util.UUID
import scala.collection.mutable
import scala.jdk.CollectionConverters.*

class CanvasSkin(control: CanvasView, serviceFactory: ServiceFactory) extends SkinBase[CanvasView](control) {

  import CanvasSkin.*

  private lazy val addNodeDialog = AddNodeDialog(serviceFactory)
  private lazy val createRelationshipTypeDialog = CreateRelationshipDialog()
  private val styleText = (hex: String) => s"-fx-background-color: $hex"
  private val nodesMap = mutable.Map.empty[String, GraphNodeView[?]]
  private val posNodesMap = mutable.Map.empty[Long, Seq[PartOfSpeechNodeView]]
  private val linkSupportNodesMap = mutable.Map.empty[UUID, LinkSupportView[?]]
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
  private var selectedDependentLinkedNode: Option[LinkSupportView[?]] = None

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
        loadGraph(nv, control.dependencyGraph.nodes)
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
    linkSupportNodesMap.clear()
    canvasPane.children.clear()
    contextMenu.items.clear()
  }

  private def drawTerminalNode(terminalNode: TerminalNode): Group = {
    def addContextMenu(terminalNodeView: TerminalNodeView, text: Text)(event: MouseEvent): Unit = {
      showContextMenu(event, text, initTerminalNodeContextMenu(terminalNodeView))
      control.selectedNode = terminalNodeView.source
      selectedDependentLinkedNode = None
      event.consume()
    }

    val terminalNodeView = TerminalNodeView()
    terminalNodeView.source = terminalNode
    nodesMap += (terminalNodeView.getId -> terminalNodeView)
    val line = drawLine(terminalNodeView)
    val derivedTerminalNode = DerivedTerminalNodeTypes.contains(terminalNode.graphNodeType)
    val color = if derivedTerminalNode then DerivedTerminalNodeColor else DefaultTerminalNodeColor
    val arabicText = drawArabicText(terminalNodeView, color)
    // as per Java doc for "isPopupTrigger": "Popup menus are triggered differently on different systems. Therefore,
    // popupTrigger should be checked in both onMousePressed and mouseReleased for proper cross-platform functionality."
    arabicText.onMousePressed = addContextMenu(terminalNodeView, arabicText)
    arabicText.onMouseReleased = addContextMenu(terminalNodeView, arabicText)

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

  private def drawRelationshipNode(node: RelationshipNode): Group = {
    def addContextMenu(view: RelationshipNodeView, text: Text)(event: MouseEvent): Unit = {
      // TODO: initialize context menu
      control.selectedNode = view.source
      event.consume()
    }

    def updateArrowPoints(
      t1: Double,
      t2: Double,
      startX: Double,
      startY: Double,
      controlX1: Double,
      controlY1: Double,
      controlX2: Double,
      controlY2: Double,
      endX: Double,
      endY: Double,
      arrow: Polyline
    ): Unit = {
      val newPoints =
        DrawingTool.arrowPoints(t1, t2, startX, startY, controlX1, controlY1, controlX2, controlY2, endX, endY)

      arrow.points.setAll(newPoints.map(Double.box)*)
    }

    val view = RelationshipNodeView()
    view.source = node
    val relationshipInfo = node.relationshipInfo
    val color = Color.web(relationshipInfo.relationshipType.colorCode)
    val owner = linkSupportNodesMap(relationshipInfo.owner.id)
    val dependent = linkSupportNodesMap(relationshipInfo.dependent.id)

    val cubicCurve = DrawingTool.drawCubicCurve(
      curveId = view.getId,
      x = dependent.cx + dependent.translateX,
      y = dependent.cy + dependent.translateY,
      x1 = view.controlX1,
      y1 = view.controlY1,
      x2 = view.controlX2,
      y2 = view.controlY2,
      ex = owner.cx + owner.translateX,
      ey = owner.cy + owner.translateY,
      color = color
    )

    cubicCurve.startX.bind(dependent.cxProperty.add(dependent.translateX))
    cubicCurve.startY.bind(dependent.cyProperty.add(dependent.translateY))
    cubicCurve.controlX1.bind(view.controlX1Property)
    cubicCurve.controlY1.bind(view.controlY1Property)
    cubicCurve.controlX2.bind(view.controlX2Property)
    cubicCurve.controlY2.bind(view.controlY2Property)
    cubicCurve.endX.bind(owner.cxProperty.add(owner.translateX))
    cubicCurve.endY.bind(owner.cyProperty.add(owner.translateY))

    val arabicText = drawArabicText(view, color)
    arabicText.fill = color
    arabicText.onMousePressed = addContextMenu(view, arabicText)
    arabicText.onMouseReleased = addContextMenu(view, arabicText)

    // small arrow pointing towards the relationship direction
    val arrowPoints = DrawingTool.arrowPoints(
      view.t1,
      view.t2,
      cubicCurve.startX.value,
      cubicCurve.startY.value,
      cubicCurve.controlX1.value,
      cubicCurve.controlY1.value,
      cubicCurve.controlX2.value,
      cubicCurve.controlY2.value,
      cubicCurve.endX.value,
      cubicCurve.endY.value
    )
    val arrow = DrawingTool.drawPolyline(color, arrowPoints*)

    view
      .t1Property
      .onChange((_, _, nv) =>
        updateArrowPoints(
          nv.doubleValue(),
          view.t2,
          cubicCurve.startX.value,
          cubicCurve.startY.value,
          cubicCurve.controlX1.value,
          cubicCurve.controlY1.value,
          cubicCurve.controlX2.value,
          cubicCurve.controlY2.value,
          cubicCurve.endX.value,
          cubicCurve.endY.value,
          arrow
        )
      )

    view
      .t2Property
      .onChange((_, _, nv) =>
        updateArrowPoints(
          view.t1,
          nv.doubleValue(),
          cubicCurve.startX.value,
          cubicCurve.startY.value,
          cubicCurve.controlX1.value,
          cubicCurve.controlY1.value,
          cubicCurve.controlX2.value,
          cubicCurve.controlY2.value,
          cubicCurve.endX.value,
          cubicCurve.endY.value,
          arrow
        )
      )

    cubicCurve
      .startX
      .onChange((_, _, nv) =>
        updateArrowPoints(
          view.t1,
          view.t2,
          nv.doubleValue(),
          cubicCurve.startY.value,
          cubicCurve.controlX1.value,
          cubicCurve.controlY1.value,
          cubicCurve.controlX2.value,
          cubicCurve.controlY2.value,
          cubicCurve.endX.value,
          cubicCurve.endY.value,
          arrow
        )
      )

    cubicCurve
      .startY
      .onChange((_, _, nv) =>
        updateArrowPoints(
          view.t1,
          view.t2,
          cubicCurve.startX.value,
          nv.doubleValue(),
          cubicCurve.controlX1.value,
          cubicCurve.controlY1.value,
          cubicCurve.controlX2.value,
          cubicCurve.controlY2.value,
          cubicCurve.endX.value,
          cubicCurve.endY.value,
          arrow
        )
      )

    cubicCurve
      .controlX1
      .onChange((_, _, nv) =>
        updateArrowPoints(
          view.t1,
          view.t2,
          cubicCurve.startX.value,
          cubicCurve.startY.value,
          nv.doubleValue(),
          cubicCurve.controlY1.value,
          cubicCurve.controlX2.value,
          cubicCurve.controlY2.value,
          cubicCurve.endX.value,
          cubicCurve.endY.value,
          arrow
        )
      )

    cubicCurve
      .controlY1
      .onChange((_, _, nv) =>
        updateArrowPoints(
          view.t1,
          view.t2,
          cubicCurve.startX.value,
          cubicCurve.startY.value,
          cubicCurve.controlX1.value,
          nv.doubleValue(),
          cubicCurve.controlX2.value,
          cubicCurve.controlY2.value,
          cubicCurve.endX.value,
          cubicCurve.endY.value,
          arrow
        )
      )

    cubicCurve
      .controlX2
      .onChange((_, _, nv) =>
        updateArrowPoints(
          view.t1,
          view.t2,
          cubicCurve.startX.value,
          cubicCurve.startY.value,
          cubicCurve.controlX1.value,
          cubicCurve.controlY1.value,
          nv.doubleValue(),
          cubicCurve.controlY2.value,
          cubicCurve.endX.value,
          cubicCurve.endY.value,
          arrow
        )
      )

    cubicCurve
      .controlY2
      .onChange((_, _, nv) =>
        updateArrowPoints(
          view.t1,
          view.t2,
          cubicCurve.startX.value,
          cubicCurve.startY.value,
          cubicCurve.controlX1.value,
          cubicCurve.controlY1.value,
          cubicCurve.controlX2.value,
          nv.doubleValue(),
          cubicCurve.endX.value,
          cubicCurve.endY.value,
          arrow
        )
      )

    cubicCurve
      .endX
      .onChange((_, _, nv) =>
        updateArrowPoints(
          view.t1,
          view.t2,
          cubicCurve.startX.value,
          cubicCurve.startY.value,
          cubicCurve.controlX1.value,
          cubicCurve.controlY1.value,
          cubicCurve.controlX2.value,
          cubicCurve.controlY2.value,
          nv.doubleValue(),
          cubicCurve.endY.value,
          arrow
        )
      )

    cubicCurve
      .endY
      .onChange((_, _, nv) =>
        updateArrowPoints(
          view.t1,
          view.t2,
          cubicCurve.startX.value,
          cubicCurve.startY.value,
          cubicCurve.controlX1.value,
          cubicCurve.controlY1.value,
          cubicCurve.controlX2.value,
          cubicCurve.controlY2.value,
          cubicCurve.endX.value,
          nv.doubleValue(),
          arrow
        )
      )

    nodesMap += (view.getId -> view)
    val debugPath =
      if control.dependencyGraph.metaInfo.debugMode then
        Seq(
          DrawingTool.drawCubicCurveBounds(
            cubicCurve.startX.value,
            cubicCurve.startY.value,
            cubicCurve.controlX1.value,
            cubicCurve.controlY1.value,
            cubicCurve.controlX2.value,
            cubicCurve.controlY2.value,
            cubicCurve.endX.value,
            cubicCurve.endY.value
          )
        )
      else Seq.empty

    new Group() {
      id = s"rln_${view.getId}"
      children = Seq(cubicCurve, arabicText, arrow) ++ debugPath
    }
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
    def addContextMenu(posView: PartOfSpeechNodeView, text: Text)(event: MouseEvent): Unit = {
      showContextMenu(event, text, initPartOfSpeechNodeContextMenu(posView))
      val source = posView.source
      control.selectedNode = source
      if selectedDependentLinkedNode.isDefined then {
        createRelationshipTypeDialog.ownerNode = posNode.location

        val dependent = selectedDependentLinkedNode.get
        val ownerLink = RelationshipLink(source.id, source.graphNodeType)
        val dependentLink =
          dependent.source.asInstanceOf[LinkSupport] match
            case l: PartOfSpeechNode =>
              createRelationshipTypeDialog.dependentNode = l.location
              RelationshipLink(l.id, l.graphNodeType)
            case l: PhraseNode =>
              createRelationshipTypeDialog.dependentNode = l.phraseInfo
              RelationshipLink(l.id, l.graphNodeType)

        createRelationshipTypeDialog.showAndWait() match
          case Some(CreateRelationshipResult(relationshipType)) if relationshipType != RelationshipType.None =>
            val relationshipInfo =
              RelationshipInfo(
                text = relationshipType.label,
                relationshipType = relationshipType,
                owner = ownerLink,
                dependent = dependentLink
              )
            control.createRelationship(relationshipInfo, posView, dependent)

          case _ => // do nothing

        selectedDependentLinkedNode = None
      }
      event.consume()
    }

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
      linkSupportNodesMap += (posNode.id -> posView)
      val color =
        if derivedTerminalNode then DerivedTerminalNodeColor
        else Color.web(posNode.location.partOfSpeechType.colorCode)
      val arabicText = drawArabicText(posView, color)
      arabicText.onMousePressed = addContextMenu(posView, arabicText)
      arabicText.onMouseReleased = addContextMenu(posView, arabicText)

      val circle =
        DrawingTool.drawCircle(s"c_${posNode.id.toString}", posNode.circle.x, posNode.circle.y, DefaultCircleRadius)
      circle.centerXProperty().bindBidirectional(posView.cxProperty)
      circle.centerYProperty().bindBidirectional(posView.cyProperty)
      posView.translateXProperty().bind(terminalNodeView.translateXProperty())
      posView.translateYProperty().bind(terminalNodeView.translateYProperty())
      Seq(arabicText, circle)
    }
  }

  private def showContextMenu(event: MouseEvent, node: Node, menuItems: => Seq[MenuItem]): Unit = {
    if event.isPopupTrigger then {
      contextMenu.items.clear()
      contextMenu.items.addAll(menuItems.map(_.delegate))
      contextMenu.show(node, event.getSceneX, event.getSceneY)
    }
  }

  private def initTerminalNodeContextMenu(node: TerminalNodeView): Seq[MenuItem] = {
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

  private def initPartOfSpeechNodeContextMenu(view: PartOfSpeechNodeView): Seq[MenuItem] = {
    Seq(new MenuItem() {
      text = "Create Relationship ..."
      onAction = event => {
        new Alert(AlertType.Information) {
          initOwner(JFXApp3.Stage)
          title = "Create Relationship"
          headerText = "Create relationship between two nodes."
          contentText = "Click second node to start creating relationship."
        }.showAndWait() match
          case Some(buttonType) if buttonType.buttonData == ButtonData.OKDone =>
            selectedDependentLinkedNode = Some(view)
          case _ => // do nothing

        event.consume()
      }
    })
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
      case n: RelationshipNode => Some(drawRelationshipNode(n))
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
