package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package utils

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
    terminalNodes: Seq[TerminalNode]
  ): Seq[TerminalNodeMetaInfo] = {
    reset(graphMetaInfo)

    val nodeCoordinates = calculateTokenCoordinates(graphMetaInfo, terminalNodes.size)
    terminalNodes.zip(nodeCoordinates).map { case (node, line) =>
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
    terminalNode: TerminalNode,
    line: Line
  ): TerminalNodeMetaInfo = {
    val arabicText = terminalNode.text
    val translationText = terminalNode.translation
    val midX = (line.p1.x + line.p2.x) / 2
    TerminalNodeMetaInfo(
      dependencyGraphId = dependencyGraphId,
      textPoint = Point(midX - arabicText.length, line.p1.y - 20),
      translate = Point(0, 0),
      line = line,
      translationPoint = Point(midX - translationText.length, line.p1.y - 50),
      font = terminalFont,
      translationFont = translationFont,
      terminalNode = terminalNode,
      partOfSpeechNodes = buildPartOfSpeechNodes(dependencyGraphId, line, terminalNode.partOfSpeechNodes)
    )
  }

  private def buildPartOfSpeechNodes(
    dependencyGraphId: UUID,
    line: Line,
    partOfSpeechNodes: Seq[PartOfSpeechNode]
  ): Seq[PartOfSpeechNodeMetaInfo] = {
    val numOfLocations = partOfSpeechNodes.size
    val groups = (line.p2.x - line.p1.x) / numOfLocations
    var x1 = line.p1.x
    val cy = line.p1.y + 15
    val cs =
      (0 until numOfLocations).map { _ =>
        val cx = (x1 + groups) - (groups / 2)
        x1 = x1 + groups
        Point(cx, cy)
      }

    partOfSpeechNodes.reverse.zip(cs).map { case (node, circle) =>
      buildPartOfSpeechNodeMetaInfo(dependencyGraphId, node, circle)
    }
  }

  private def buildPartOfSpeechNodeMetaInfo(
    dependencyGraphId: UUID,
    partOfSpeechNode: PartOfSpeechNode,
    circle: Point
  ) =
    PartOfSpeechNodeMetaInfo(
      dependencyGraphId = dependencyGraphId,
      textPoint = Point(circle.x - 20, circle.y + 25),
      translate = Point(0, 0),
      circle = circle,
      font = posFont,
      partOfSpeechNode = partOfSpeechNode
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
