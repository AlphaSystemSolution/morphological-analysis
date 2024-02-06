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
import morphologicalanalysis.morphology.graph.model.{ Line as GraphLine, * }
import utils.{ DrawingTool, TerminalNodeInput }
import javafx.scene.Node as JfxNode
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.collections.ObservableMap
import scalafx.geometry.Pos
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ Alert, ContextMenu, Menu, MenuItem }
import scalafx.scene.image.Image
import scalafx.scene.input.MouseEvent
import scalafx.scene.{ Group, Node }
import scalafx.scene.layout.{ BorderPane, Pane, Region }
import scalafx.scene.paint.Color
import scalafx.scene.shape.{ Circle, Line, Polyline }
import scalafx.scene.text.{ Text, TextAlignment }

import java.util.UUID
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.*

class CanvasSkin(control: CanvasView, serviceFactory: ServiceFactory) extends SkinBase[CanvasView](control) {

  import CanvasSkin.*

  private lazy val addNodeDialog = AddNodeDialog(serviceFactory)
  private lazy val createRelationshipTypeDialog = CreateRelationshipDialog()
  private lazy val createPhraseDialog = CreatePhraseDialog()
  private val styleText = (hex: String) => s"-fx-background-color: $hex"

  // map containing master list of nodes
  private val nodesMap = ObservableMap.empty[String, GraphNodeView[?]]

  // map containing part of speech nodes, serves to keep track POS for later saving purpose, key of this map is token id
  private val posNodesMap = ObservableMap.empty[Long, Seq[PartOfSpeechNodeView]]

  // map containing nodes of LinkSupport type (part of speech and phrase), serves to draw Relationship nodes
  private val linkSupportNodesMap = mutable.Map.empty[UUID, LinkSupportView[?]]

  // map containing LinkSupport nodes to text coordinate of relationship node, serves to draw Phrase node, the line of
  // Phrase node will be drawn below Y axis
  private val linkSupportToRelationshipMap = mutable.Map.empty[UUID, Point]

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
  private var selectedPosNode: Option[PartOfSpeechNodeView] = None

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

  control.dependencyGraphProperty.onChange((_, _, nv) => if Option(nv).isDefined then loadGraph(nv.metaInfo, nv.nodes))

  getChildren.addAll(initializeSkin)

  private[control] def graphNodes: Seq[GraphNode] = {
    val nodes = nodesMap.values.map(_.source.asInstanceOf[GraphNode]).toSeq
    val posNodesMap =
      nodes
        .filter(_.graphNodeType == GraphNodeType.PartOfSpeech)
        .map(_.asInstanceOf[PartOfSpeechNode])
        .groupBy(_.location.tokenId)

    val terminalNodes =
      nodes
        .filter(node => isTerminalNode(node.graphNodeType))
        .map(_.asInstanceOf[TerminalNode])
        .sortBy(_.index)
        .map { node =>
          val posNodes =
            posNodesMap(node.token.id)
              .sortWith { case (p1, p2) =>
                p1.location.locationNumber > p2.location.locationNumber
              }
          node.copy(partOfSpeechNodes = posNodes)
        }

    val otherNodes = nodes.filterNot { node =>
      val nodeType = node.graphNodeType
      nodeType == GraphNodeType.PartOfSpeech || isTerminalNode(nodeType)
    }

    terminalNodes ++ otherNodes
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

  private def loadGraph(graphMetaInfo: GraphMetaInfo, nodes: Seq[GraphNode]): Unit = {
    clear()
    canvasPane.children = parseNodes(nodes)
    toggleGridLines(graphMetaInfo)
  }

  private def clear(): Unit = {
    nodesMap.clear()
    posNodesMap.clear()
    linkSupportNodesMap.clear()
    linkSupportToRelationshipMap.clear()
    canvasPane.children.clear()
    contextMenu.items.clear()
  }

  private def drawTerminalNode(terminalNode: TerminalNode): Group = {
    def addContextMenu(view: TerminalNodeView, text: Text)(event: MouseEvent): Unit = {
      showContextMenu(event, text, view.getId, initTerminalNodeContextMenu(view))
      selectedDependentLinkedNode = None
      event.consume()
    }

    val terminalNodeView = TerminalNodeView()
    terminalNodeView.source = terminalNode
    nodesMap.addOne(terminalNodeView.getId -> terminalNodeView)
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
      showContextMenu(event, text, view.getId, initRelationshipNodeContextMenu(view))
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
    val ownerId = relationshipInfo.owner.id
    val dependentId = relationshipInfo.dependent.id
    val owner = linkSupportNodesMap(ownerId)
    val dependent = linkSupportNodesMap(dependentId)

    val textCoordinate = node.textPoint
    val ownerCurrentCoordinate = linkSupportToRelationshipMap.getOrElse(ownerId, Point(0, 0))
    linkSupportToRelationshipMap += (ownerId -> YOrdering.max(textCoordinate, ownerCurrentCoordinate))

    val dependentCurrentCoordinate = linkSupportToRelationshipMap.getOrElse(dependentId, Point(0, 0))
    linkSupportToRelationshipMap += (dependentId -> YOrdering.max(textCoordinate, dependentCurrentCoordinate))

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
      view.arrowX,
      view.arrowY,
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
      .arrowXProperty
      .onChange((_, _, nv) =>
        updateArrowPoints(
          nv.doubleValue(),
          view.arrowY,
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
      .arrowYProperty
      .onChange((_, _, nv) =>
        updateArrowPoints(
          view.arrowX,
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
          view.arrowX,
          view.arrowY,
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
          view.arrowX,
          view.arrowY,
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
          view.arrowX,
          view.arrowY,
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
          view.arrowX,
          view.arrowY,
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
          view.arrowX,
          view.arrowY,
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
          view.arrowX,
          view.arrowY,
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
          view.arrowX,
          view.arrowY,
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
          view.arrowX,
          view.arrowY,
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

    nodesMap.addOne(view.getId -> view)
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

  private def drawPhraseNode(node: PhraseNode): Group = {
    def addContextMenu(view: PhraseNodeView, text: Text)(event: MouseEvent): Unit = {
      showContextMenu(event, text, view.getId, initPhraseNodeContextMenu(view))
      event.consume()
    }

    val view = PhraseNodeView()
    view.source = node
    val color = Color.web(node.phraseInfo.phraseTypes.head.colorCode)
    val line = drawLine(view)
    val arabicText = drawArabicText(view, color)
    arabicText.fill = color
    arabicText.onMousePressed = addContextMenu(view, arabicText)
    arabicText.onMouseReleased = addContextMenu(view, arabicText)
    val circle = drawCircle(view)
    nodesMap.addOne(view.getId -> view)
    linkSupportNodesMap += (node.id -> view)

    new Group() {
      id = s"phrase_${view.getId}"
      children = Seq(line, arabicText, circle)
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

  private def drawCircle[N <: LinkSupport, V <: LinkSupportView[N]](view: V): Circle = {
    val circle = DrawingTool.drawCircle(s"c_${view.getId}", view.cx, view.cy, DefaultCircleRadius)
    circle.centerXProperty().bindBidirectional(view.cxProperty)
    circle.centerYProperty().bindBidirectional(view.cyProperty)
    circle
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
    def addContextMenu(view: PartOfSpeechNodeView, text: Text)(event: MouseEvent): Unit = {
      showContextMenu(event, text, view.getId, initPartOfSpeechNodeContextMenu(view))
      handleCreateRelationship(view)
      handleCreatePhrase(view)
      event.consume()
    }

    val posView = PartOfSpeechNodeView()
    posView.source = posNode
    posView.terminalNode = terminalNodeView.source
    nodesMap.addOne(posView.getId -> posView)
    val id = terminalNodeView.source.token.id
    val seq = posNodesMap.getOrElse(id, Seq.empty)
    posNodesMap.addOne(id -> (seq :+ posView))
    if posNode.hidden then Seq.empty
    else {
      posView.translateX = terminalNodeView.translateX
      posView.translateY = terminalNodeView.translateY
      linkSupportNodesMap += (posNode.id -> posView)
      val color =
        if derivedTerminalNode then DerivedTerminalNodeColor
        else Color.web(posNode.location.partOfSpeechType.colorCode)
      val arabicText = drawArabicText(posView, color)
      arabicText.onMousePressed = addContextMenu(posView, arabicText)
      arabicText.onMouseReleased = addContextMenu(posView, arabicText)

      val circle = drawCircle(posView)
      linkSupportToRelationshipMap += (posNode.id -> posNode.circle)
      posView.translateXProperty().bind(terminalNodeView.translateXProperty())
      posView.translateYProperty().bind(terminalNodeView.translateYProperty())
      Seq(arabicText, circle)
    }
  }

  private def handleCreateRelationship(posView: PartOfSpeechNodeView): Unit =
    if selectedDependentLinkedNode.isDefined then {
      createRelationshipTypeDialog.relationshipType = RelationshipType.None
      val posNode = posView.source
      createRelationshipTypeDialog.ownerNode = posNode.location

      val dependent = selectedDependentLinkedNode.get
      val ownerLink = RelationshipLink(posNode.id, posNode.graphNodeType)
      val dependentLink =
        dependent.source.asInstanceOf[LinkSupport] match
          case l: PartOfSpeechNode =>
            createRelationshipTypeDialog.dependentNode = l.location
            RelationshipLink(l.id, l.graphNodeType)
          case l: PhraseNode =>
            createRelationshipTypeDialog.dependentNode = l.phraseInfo
            RelationshipLink(l.id, l.graphNodeType)

      createRelationshipTypeDialog.showAndWait() match
        case Some(CreateRelationshipResult(Some(text), relationshipType))
            if relationshipType != RelationshipType.None =>
          val relationshipInfo =
            RelationshipInfo(
              text = text,
              relationshipType = relationshipType,
              owner = ownerLink,
              dependent = dependentLink
            )
          control.createRelationship(relationshipInfo, posView, dependent)

        case _ => // do nothing

      selectedDependentLinkedNode = None
    }

  private def handleCreatePhrase(posView: PartOfSpeechNodeView): Unit = {
    if selectedPosNode.isDefined then {
      val initialView = selectedPosNode.get
      val initialLocationId = initialView.source.location.id
      val possibleLastLocation = posView.source.location
      // similar to when selecting initial node, take account of any hidden location after the selected location
      val possibleNextLocationId =
        possibleLastLocation.copy(locationNumber = possibleLastLocation.locationNumber + 1).id
      val maybePossibleNextNode =
        posNodesMap(possibleLastLocation.tokenId).find(_.source.location.id == possibleNextLocationId)

      val lastLocationId =
        if maybePossibleNextNode.isDefined then {
          // we do have hidden location
          maybePossibleNextNode.get.source.location.id
        } else posView.source.location.id

      val selectedNodes =
        posNodesMap
          .values
          .flatten
          .toSeq
          .filter { view =>
            val id = view.source.location.id
            initialLocationId <= id && id <= lastLocationId
          }
          .sortBy(_.source.location.id)

      val refPoint =
        selectedNodes.foldLeft(Point(0, 0)) { case (ref, node) =>
          val currentPoint = linkSupportToRelationshipMap.getOrElse(node.source.id, Point(0, 0))
          YOrdering.max(ref, currentPoint)
        }

      val firstPosNode = initialView.source
      val firstLocation = firstPosNode.location
      val firstTnNodeId = firstLocation.tokenId.toUUID
      val lastPosNode = selectedNodes.last.source
      val lastLocation = lastPosNode.location
      val lastTnNodeId = lastLocation.tokenId.toUUID

      val firstTnNode = nodesMap(firstTnNodeId.toString).asInstanceOf[TerminalNodeView].source
      val lastTnNode = nodesMap(lastTnNodeId.toString).asInstanceOf[TerminalNodeView].source

      val x2 = if firstLocation.locationNumber == 1 then firstTnNode.line.p2.x else firstPosNode.circle.x
      val x1 =
        if lastTnNode.partOfSpeechNodes.size == lastLocation.locationNumber then lastTnNode.line.p1.x
        else lastPosNode.circle.x
      val y = refPoint.y + 15

      val line = GraphLine(
        Point(x1 + firstTnNode.translate.x, y + firstTnNode.translate.y),
        Point(x2 + lastTnNode.translate.x, y + lastTnNode.translate.y)
      )

      createPhraseDialog.nounStatus = None
      createPhraseDialog.phraseTypes = Seq.empty
      createPhraseDialog.locationIds = selectedNodes.map(_.source.location).map { location =>
        (location.id, location.locationNumber)
      }
      createPhraseDialog.displayText = derivePhraseText(selectedNodes.map(_.source.location))
      createPhraseDialog.showAndWait() match
        case Some(CreatePhraseResult(Some(phraseInfo))) => control.createPhrase(phraseInfo, line)
        case _                                          => // do nothing
    }
    selectedPosNode = None
  }

  private def showContextMenu(
    event: MouseEvent,
    node: Node,
    nodeId: String,
    menuItems: => Seq[MenuItem]
  ): Unit =
    if event.isPopupTrigger then {
      contextMenu.items.clear()
      contextMenu.items.addAll(menuItems.map(_.delegate))
      contextMenu.show(node, event.getSceneX, event.getSceneY)
    } else
      nodesMap.get(nodeId).foreach { view =>
        control.selectedNode = view.source
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

  private def initPartOfSpeechNodeContextMenu(view: PartOfSpeechNodeView): Seq[MenuItem] =
    Seq(
      new MenuItem() {
        text = "Hide ..."
        onAction = event => {
          new Alert(AlertType.Confirmation) {
            initOwner(JFXApp3.Stage)
            title = "Hide Node"
            headerText = "Hide selected node."
            contentText = "Are you Sure?"
          }.showAndWait() match
            case Some(buttonType) if buttonType.buttonData == ButtonData.OKDone =>
              val updatedView = nodesMap(view.getId).asInstanceOf[PartOfSpeechNodeView]
              updatedView.source = updatedView.source.copy(hidden = true)
              nodesMap.replace(view.getId, updatedView)
              control.saveGraph()

            case _ => // do nothing

          event.consume()
        }
      },
      new MenuItem() {
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
      },
      new MenuItem() {
        text = "Create Phrase"
        onAction = event => {
          new Alert(AlertType.Information) {
            initOwner(JFXApp3.Stage)
            title = "Create Phrase"
            headerText = "Create Phrase from selected nodes"
            contentText = "Click last node to start creating phrase."
          }.showAndWait() match
            case Some(buttonType) if buttonType.buttonData == ButtonData.OKDone =>
              val location = view.source.location
              // some locations are hidden, for example definite article,if we have chosen a location which has
              // a hidden location before, choose it as the selected node
              val locationNumber = location.locationNumber
              selectedPosNode =
                if locationNumber <= 1 then Some(view)
                else {
                  val posNodes = posNodesMap(location.tokenId)
                  val previousLocationId = location.copy(locationNumber = locationNumber - 1).id
                  val previousNode = posNodes.filter(_.source.location.id == previousLocationId).head
                  if previousNode.source.hidden then Some(previousNode) else Some(view)
                }

            case _ => // do nothing

          event.consume()
        }
      }
    )

  private def initRelationshipNodeContextMenu(view: RelationshipNodeView): Seq[MenuItem] =
    Seq(new MenuItem() {
      text = "Remove"
      onAction = event => {
        new Alert(AlertType.Confirmation) {
          initOwner(JFXApp3.Stage)
          title = "Remove Relationship"
          contentText = "Remove selected relationship."
        }.showAndWait() match
          case Some(buttonType) if buttonType.buttonData == ButtonData.OKDone => control.removeNode(view.source.id)
          case _                                                              => // do nothing

        event.consume()
      }
    })

  private def initPhraseNodeContextMenu(view: PhraseNodeView): Seq[MenuItem] =
    Seq(
      new MenuItem() {
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
      },
      new MenuItem() {
        text = "Remove"
        onAction = event => {
          new Alert(AlertType.Confirmation) {
            initOwner(JFXApp3.Stage)
            title = "Remove Phrase"
            contentText = "Remove selected phrase."
          }.showAndWait() match
            case Some(buttonType) if buttonType.buttonData == ButtonData.OKDone => control.removeNode(view.source.id)
            case _                                                              => // do nothing

          event.consume()
        }
      }
    )

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

    val terminalNodes = nodes.filter(node => isTerminalNode(node.graphNodeType))
    val phraseNodes = nodes.filter(node => isPhraseNode(node.graphNodeType))
    val relationshipNodes = nodes.filter(node => isRelationshipNode(node.graphNodeType))

    val terminalNodeViews =
      terminalNodes.flatMap {
        case n: TerminalNode =>
          val group = drawTerminalNode(n.copy(index = index))
          index += 1
          Some(group)
        case _ => None
      }

    val phraseNodeViews =
      phraseNodes.flatMap {
        case n: PhraseNode => Some(drawPhraseNode(n))
        case _             => None
      }

    val relationshipNodeViews =
      relationshipNodes.flatMap {
        case n: RelationshipNode => Some(drawRelationshipNode(n))
        case _                   => None
      }

    terminalNodeViews ++ phraseNodeViews ++ relationshipNodeViews
  }

  private[control] def toImage: Image = canvasPane.snapshot(null, null)
}

object CanvasSkin {

  private val LineColor: Color = Color.web("#B6B6B4")
  private val DerivedTerminalNodeColor: Color = Color.LightGray.darker
  private val DefaultTerminalNodeColor: Color = Color.Black
  private val DefaultCircleRadius = 2.0

  def apply(control: CanvasView, serviceFactory: ServiceFactory): CanvasSkin = new CanvasSkin(control, serviceFactory)
}
