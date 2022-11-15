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
    dependencyGraphId: String,
    graphMetaInfo: GraphMetaInfo,
    tokens: Seq[Token],
    locations: Map[String, Seq[Location]]
  ): (Seq[TerminalNode], Map[String, Seq[PartOfSpeechNode]]) = {
    reset(graphMetaInfo)

    val tokenCoordinates = calculateTokenCoordinates(graphMetaInfo, tokens.size)

    val terminalNodes =
      tokens.zip(tokenCoordinates).map { case (token, line) =>
        (buildTerminalNode(dependencyGraphId, token, line), token)
      }

    val posNodes: Map[String, Seq[PartOfSpeechNode]] =
      terminalNodes.map { case (terminalNode, token) =>
        terminalNode.id -> buildPartOfSpeechNodes(terminalNode, locations(token.id))
      }.toMap

    (terminalNodes.map(_._1), posNodes)
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
      Line(x1, y, x2, y)
    }.toList
  }

  private def buildTerminalNode(dependencyGraphId: String, token: Token, line: Line) = {
    val arabicText = token.token
    val translationText = token.translation.getOrElse("")
    val midX = (line.x1 + line.x2) / 2
    TerminalNode(
      id = UUID.randomUUID().toString,
      dependencyGraphId = dependencyGraphId,
      chapterNumber = token.chapterNumber,
      verseNumber = token.verseNumber,
      tokenNumber = token.tokenNumber,
      version = 1,
      text = arabicText,
      x = midX - arabicText.length,
      y = line.y1 - 20,
      translateX = 0,
      translateY = 0,
      x1 = line.x1,
      y1 = line.y1,
      x2 = line.x2,
      y2 = line.y2,
      translationText = translationText,
      translationX = midX - translationText.length,
      translationY = line.y1 - 50,
      tokenId = token.id,
      font = terminalFont,
      translationFont = translationFont
    )
  }

  private def buildPartOfSpeechNodes(terminalNode: TerminalNode, locations: Seq[Location]): Seq[PartOfSpeechNode] = {
    val numOfLocations = locations.size
    val groups = (terminalNode.x2 - terminalNode.x1) / numOfLocations
    var x1 = terminalNode.x1
    val cy = terminalNode.y1 + 15
    val cs =
      (0 until numOfLocations).map { _ =>
        val cx = (x1 + groups) - (groups / 2)
        x1 = x1 + groups
        (cx, cy)
      }

    locations.reverse.zip(cs).map { case (location, (cx, cy)) =>
      buildPartOfSpeechNode(terminalNode.dependencyGraphId, location, cx, cy)
    }
  }

  private def buildPartOfSpeechNode(dependencyGraphId: String, location: Location, cx: Double, cy: Double) =
    PartOfSpeechNode(
      id = UUID.randomUUID().toString,
      dependencyGraphId = dependencyGraphId,
      chapterNumber = location.chapterNumber,
      verseNumber = location.verseNumber,
      tokenNumber = location.tokenNumber,
      locationNumber = location.locationNumber,
      version = 1,
      text = location.properties.toText,
      x = cx - 20,
      y = cy + 25,
      translateX = 0,
      translateY = 0,
      x1 = 0,
      y1 = 0,
      x2 = 0,
      y2 = 0,
      cx = cx,
      cy = cy,
      font = posFont,
      linkId = location.id,
      hidden = false
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
  val MinGapFromRight = 20
  val MinGapFromTop = 20

  case class Line(x1: Double, y1: Double, x2: Double, y2: Double)

  def apply(): GraphBuilder = new GraphBuilder()
}
