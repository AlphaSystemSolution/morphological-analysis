package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package utils

import com.alphasystem.arabic.morphologicalanalysis.graph.model.GraphNodeType
import morphology.model.{ Location, Token }
import morphology.graph.model.*

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
    tokens: Seq[Token]
  ): Seq[TerminalNode] = {
    reset(graphMetaInfo)

    val nodeCoordinates = calculateTokenCoordinates(graphMetaInfo, tokens.size)
    tokens.zip(nodeCoordinates).map { case (node, line) =>
      buildTerminalNodeMetaInfo(dependencyGraphId, node, line)
    }
  }

  private def calculateTokenCoordinates(graphMetaInfo: GraphMetaInfo, numOfTokens: Int) = {
    val graphWidth = graphMetaInfo.width
    val tokenWidth = graphMetaInfo.tokenWidth
    val gapBetweenTokens = graphMetaInfo.gapBetweenTokens
    val occupiedSpace = (numOfTokens * tokenWidth) + ((numOfTokens - 1) * gapBetweenTokens)
    var lastPos = graphWidth - (graphWidth % MinGapFromRight) - MinGapFromRight
    val remainingSpace = lastPos - occupiedSpace
    val y = graphMetaInfo.tokenHeight + MinGapFromTop
    lastPos = lastPos - (remainingSpace / 2)
    (0 until numOfTokens).map { _ =>
      val x2 = lastPos
      val x1 = x2 - tokenWidth
      lastPos = x1 - gapBetweenTokens
      Line(Point(x1, y), Point(x2, y))
    }.toList
  }

  private def buildTerminalNodeMetaInfo(
    dependencyGraphId: UUID,
    token: Token,
    line: Line
  ): TerminalNode = {
    val arabicText = token.token
    val translationText = token.translation.getOrElse("")
    val midX = (line.p1.x + line.p2.x) / 2
    TerminalNode(
      dependencyGraphId = dependencyGraphId,
      graphNodeType = GraphNodeType.Terminal,
      textPoint = Point(midX - arabicText.length, line.p1.y - 20),
      translate = Point(0, 0),
      line = line,
      translationPoint = Point(midX - translationText.length, line.p1.y - 50),
      font = terminalFont,
      translationFont = translationFont,
      token = token,
      partOfSpeechNodes = buildPartOfSpeechNodes(dependencyGraphId, line, token.locations)
    )
  }

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

    locations.reverse.zip(cs).map { case (node, circle) =>
      buildPartOfSpeechNodeMetaInfo(dependencyGraphId, node, circle)
    }
  }

  private def buildPartOfSpeechNodeMetaInfo(
    dependencyGraphId: UUID,
    location: Location,
    circle: Point
  ) =
    PartOfSpeechNode(
      dependencyGraphId = dependencyGraphId,
      textPoint = Point(circle.x - 20, circle.y + 25),
      translate = Point(0, 0),
      circle = circle,
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

  // this is the minimum distance right side of the graph, if graph width is 1420 then last x coordinate of the
  // last token will be 1400
  private val MinGapFromRight = 20
  private val MinGapFromTop = 20

  def apply(): GraphBuilder = new GraphBuilder()
}
