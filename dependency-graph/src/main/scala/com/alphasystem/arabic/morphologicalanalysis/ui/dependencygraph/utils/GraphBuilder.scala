package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package utils

import morphologicalanalysis.graph.model.GraphNodeType
import ui.dependencygraph.control.{ LinkSupportView, PartOfSpeechNodeView }
import morphologicalanalysis.morphology.model.{ Location, Token }
import morphologicalanalysis.morphology.graph.model.*

import java.util.UUID

class GraphBuilder {

  import GraphBuilder.*

  private var tokenWidth: Double = 0
  private var tokenHeight: Double = 0
  private var gapBetweenTokens: Double = 0
  private var terminalFont: FontMetaInfo = _
  private var posFont: FontMetaInfo = _
  private var translationFont: FontMetaInfo = _

  def createNewGraph(
    dependencyGraphId: UUID,
    graphMetaInfo: GraphMetaInfo,
    inputs: Seq[TerminalNodeInput]
  ): Seq[TerminalNode] = {
    reset(graphMetaInfo)

    val nodeCoordinates = calculateTokenTranslatePoints(graphMetaInfo, inputs.size)
    inputs.zip(nodeCoordinates).map { case (node, point) =>
      buildTerminalNode(dependencyGraphId, node, point)
    }
  }

  def createRelationship(
                          dependencyGraphId: UUID,
                          graphMetaInfo: GraphMetaInfo,
                          relationshipInfo: RelationshipInfoOld,
                          owner: LinkSupportView[?],
                          dependent: LinkSupportView[?]
  ): RelationshipNode = {
    val startX = dependent.cx + dependent.translateX
    val startY = dependent.cy + dependent.translateY + 5d
    val endX = owner.cx + dependent.translateX
    val endY = owner.cy + owner.translateY
    var controlY1 = startY + 65d
    var controlY2 = endY + 65d
    if controlY1 != controlY2 then {
      val max = math.max(controlY1, controlY2)
      controlY1 = max
      controlY2 = max
    }
    RelationshipNode(
      id = UUID.nameUUIDFromBytes(s"${owner.getId}_${dependent.getId}".getBytes),
      dependencyGraphId = dependencyGraphId,
      textPoint = Point((startX + endX) / 2, (controlY1 + controlY2) / 2),
      translate = Point(0, 0),
      control1 = Point(startX, controlY1),
      control2 = Point(endX, controlY1),
      arrow = Point(0.50, 0.55),
      font = graphMetaInfo.partOfSpeechFont,
      relationshipInfo = relationshipInfo
    )
  }

  def createPhrase(
    dependencyGraphId: UUID,
    graphMetaInfo: GraphMetaInfo,
    phraseInfo: PhraseInfo,
    line: Line
  ): PhraseNode = {
    val id = UUID.nameUUIDFromBytes(
      phraseInfo
        .locations
        .foldLeft("") { case (s, locationId) =>
          s"${s}_$locationId"
        }
        .getBytes
    )

    val text = phraseInfo.text
    val midX = (line.p1.x + line.p2.x) / 2
    val textMid = text.length / 2
    val textPoint = Point(midX - textMid, line.p1.y + 15)

    PhraseNode(
      id = id,
      dependencyGraphId = dependencyGraphId,
      textPoint = textPoint,
      translate = Point(0, 0),
      line = line,
      circle = Point(midX, textPoint.y + 15),
      phraseInfo = phraseInfo,
      font = graphMetaInfo.partOfSpeechFont
    )
  }

  private def calculateTokenTranslatePoints(graphMetaInfo: GraphMetaInfo, numOfTokens: Int): Seq[Point] = {
    val graphWidth = graphMetaInfo.width
    val tokenWidth = graphMetaInfo.tokenWidth
    val gapBetweenTokens = graphMetaInfo.gapBetweenTokens
    val lastPos = graphWidth - (graphWidth % gapBetweenTokens) - gapBetweenTokens
    val occupiedSpace = (numOfTokens * tokenWidth) + ((numOfTokens - 1) * gapBetweenTokens)
    val remainingSpace = lastPos - occupiedSpace

    var x = graphWidth - (remainingSpace / 2)
    (0 until numOfTokens).map { _ =>
      x = x - gapBetweenTokens - tokenWidth
      Point(x, 0)
    }
  }

  private def buildTerminalNode(
    dependencyGraphId: UUID,
    input: TerminalNodeInput,
    translate: Point
  ): TerminalNode =
    input.maybeTerminalNode match
      case Some(terminalNode) => terminalNode.copy(translate = translate)
      case None =>
        val token = input.token
        val arabicText = token.token
        val translationText = token.translation.getOrElse("")
        val y = MinGapFromTop + tokenHeight
        val line = Line(Point(0, y), Point(tokenWidth, y))
        val midX = (line.p1.x + line.p2.x) / 2

        TerminalNode(
          id = input.id,
          dependencyGraphId = dependencyGraphId,
          graphNodeType = input.graphNodeType,
          textPoint = Point(midX - arabicText.length, line.p1.y - 20),
          translate = translate,
          line = line,
          translationPoint = Point(midX - translationText.length - 10, line.p1.y - 50),
          font = terminalFont,
          translationFont = translationFont,
          token = token,
          partOfSpeechNodes = buildPartOfSpeechNodes(dependencyGraphId, line, token.locations)
        )

  private def buildPartOfSpeechNodes(
    dependencyGraphId: UUID,
    line: Line,
    locations: Seq[Location]
  ): Seq[PartOfSpeechNode] = {
    val numOfLocations = locations.size
    val groups = (line.p2.x - line.p1.x) / numOfLocations
    var x1 = line.p1.x
    val cy = line.p1.y + 15
    val cs =
      (0 until numOfLocations).map { _ =>
        val cx = (x1 + groups) - (groups / 2)
        x1 = x1 + groups
        Point(cx, cy)
      }

    locations.reverse.zip(cs).map { case (node, point) =>
      buildPartOfSpeechNodeMetaInfo(dependencyGraphId, node, point)
    }
  }

  private def buildPartOfSpeechNodeMetaInfo(
    dependencyGraphId: UUID,
    location: Location,
    refPoint: Point
  ) =
    PartOfSpeechNode(
      id = location.id.toUUID,
      dependencyGraphId = dependencyGraphId,
      textPoint = Point(refPoint.x - 20, refPoint.y),
      translate = Point(0, 0),
      circle = Point(refPoint.x, refPoint.y + 25),
      font = posFont,
      location = location
    )

  private def reset(graphMetaInfo: GraphMetaInfo): Unit = {
    terminalFont = graphMetaInfo.terminalFont
    posFont = graphMetaInfo.partOfSpeechFont
    translationFont = graphMetaInfo.translationFont
    tokenWidth = graphMetaInfo.tokenWidth
    tokenHeight = graphMetaInfo.tokenHeight
    gapBetweenTokens = graphMetaInfo.gapBetweenTokens
  }
}

object GraphBuilder {

  private val MinGapFromTop = 20

  def apply(): GraphBuilder = new GraphBuilder()
}
