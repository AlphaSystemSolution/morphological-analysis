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

  private var x: Double = 0
  private var y: Double = 0
  private var x1: Double = 0
  private var y1: Double = 0
  private var x2: Double = 0
  private var y2: Double = 0
  private var translationX: Double = 0
  private var translationY: Double = 0
  private var posX: Double = 0
  private var rectX: Double = 0
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

    val terminalNodes = tokens.reverse.map { token =>
      (buildTerminalNode(dependencyGraphId, token), token)
    }

    reset()
    y = 155

    val posNodes =
      terminalNodes.map { case (terminalNode, token) =>
        terminalNode.id -> buildPartOfSpeechNodes(terminalNode, locations(token.id))
      }.toMap

    (terminalNodes.map(_._1), posNodes)
  }

  private def buildTerminalNode(dependencyGraphId: String, token: Token) = {
    val terminalNode =
      TerminalNode(
        id = UUID.randomUUID().toString,
        dependencyGraphId = dependencyGraphId,
        chapterNumber = token.chapterNumber,
        verseNumber = token.verseNumber,
        tokenNumber = token.tokenNumber,
        version = 1,
        text = token.token,
        x = x,
        y = y,
        translateX = 0,
        translateY = -80,
        x1 = x1,
        y1 = y1,
        x2 = x2,
        y2 = y2,
        translationText = token.translation.getOrElse(""),
        translationX = translationX,
        translationY = translationY,
        tokenId = token.id,
        font = terminalFont,
        translationFont = translationFont
      )

    // update coordinates
    rectX = x2 + gapBetweenTokens
    x = rectX + 30
    x1 = rectX
    x2 = tokenWidth + rectX
    translationX = rectX + 30

    terminalNode
  }

  private def buildPartOfSpeechNodes(terminalNode: TerminalNode, locations: Seq[Location]) = {
    posX = terminalNode.x1
    locations.reverse.map(buildPartOfSpeechNode(terminalNode.dependencyGraphId))
  }

  private def buildPartOfSpeechNode(dependencyGraphId: String)(location: Location) = {
    val partOfSpeechNode = PartOfSpeechNode(
      id = UUID.randomUUID().toString,
      dependencyGraphId = dependencyGraphId,
      chapterNumber = location.chapterNumber,
      verseNumber = location.verseNumber,
      tokenNumber = location.tokenNumber,
      version = 1,
      text = location.alternateText,
      x = posX,
      y = y,
      translateX = 0,
      translateY = 0,
      x1 = 0,
      y1 = 0,
      x2 = 0,
      y2 = 0,
      cx = posX + 20,
      cy = y + 15,
      font = posFont,
      linkId = location.id,
      hidden = false
    )

    // update coordinates
    posX += 50
    partOfSpeechNode
  }

  private def reset(graphMetaInfo: GraphMetaInfo): Unit = {
    terminalFont = graphMetaInfo.terminalFont
    posFont = graphMetaInfo.partOfSpeechFont
    translationFont = graphMetaInfo.translationFont
    tokenWidth = graphMetaInfo.tokenWidth
    tokenHeight = graphMetaInfo.tokenHeight
    gapBetweenTokens = graphMetaInfo.gapBetweenTokens
    reset()
  }

  private def reset(): Unit = {
    rectX = InitialX
    x = rectX + 10
    y = 125
    x1 = rectX
    y1 = tokenHeight + InitialY
    x2 = tokenWidth + rectX
    y2 = y1
    translationX = rectX + 30
    translationY = 95
  }
}

object GraphBuilder {

  val InitialX: Double = 0
  val InitialY: Double = 40

  def apply(): GraphBuilder = new GraphBuilder()
}
