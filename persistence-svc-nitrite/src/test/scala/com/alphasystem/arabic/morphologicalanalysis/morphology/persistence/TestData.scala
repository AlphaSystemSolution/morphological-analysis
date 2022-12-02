package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import morphology.graph.model.{
  FontMetaInfo,
  Line,
  PartOfSpeechNode,
  PartOfSpeechNodeMetaInfo,
  Point,
  TerminalNode,
  TerminalNodeMetaInfo
}
import morphology.model.{ Chapter, Location, Token, Verse }

import java.util.UUID

trait TestData {

  private[persistence] val chapter =
    Chapter(
      chapterName = "درس",
      chapterNumber = 1,
      verseCount = 1
    )

  private[persistence] val chapters =
    (2 to 12).map { index =>
      Chapter(
        chapterName = "درس",
        chapterNumber = index,
        verseCount = index
      )
    }.toList

  private[persistence] val verse =
    Verse(
      chapterNumber = 1,
      verseNumber = 1,
      text = "verse text",
      tokenCount = 0,
      translation = Some("translation")
    )

  private[persistence] val token =
    Token(
      chapterNumber = 1,
      verseNumber = 1,
      tokenNumber = 1,
      token = "someToken",
      hidden = false,
      translation = None
    )

  private[persistence] val location =
    Location(
      chapterNumber = 1,
      verseNumber = 1,
      tokenNumber = 1,
      locationNumber = 1,
      hidden = false,
      startIndex = 1,
      endIndex = 3,
      derivedText = "derivedText",
      text = "text",
      alternateText = "text",
      translation = Some("translation")
    )

  private[persistence] val updatedToken =
    token.copy(translation = Some("token translation"), locations = Seq(location))

  private[persistence] val terminalNode: TerminalNode = TerminalNode.createTerminalNode(updatedToken)

  private[persistence] def partOfSpeechNodeMetaInfo(
    dependencyGraphId: UUID,
    partOfSpeechNode: PartOfSpeechNode
  ): PartOfSpeechNodeMetaInfo =
    PartOfSpeechNodeMetaInfo(
      dependencyGraphId = dependencyGraphId,
      text = Point(60, 120),
      translate = Point(0, 0),
      circle = Point(20, 60),
      font = FontMetaInfo(family = "Arial", weight = "NORMAL", posture = "REGULAR", size = 14.0),
      partOfSpeechNode = partOfSpeechNode
    )

  private[persistence] def terminalNodeMetaInfo(
    dependencyGraphId: UUID,
    terminalNode: TerminalNode
  ): TerminalNodeMetaInfo =
    TerminalNodeMetaInfo(
      dependencyGraphId = dependencyGraphId,
      text = Point(60, 120),
      translate = Point(0, 0),
      line = Line(Point(30, 50), Point(60, 50)),
      font = FontMetaInfo(family = "Arial", weight = "NORMAL", posture = "REGULAR", size = 14.0),
      translationFont = FontMetaInfo(family = "Arial", weight = "NORMAL", posture = "REGULAR", size = 14.0),
      terminalNode = terminalNode,
      partOfSpeechNodes = terminalNode.partOfSpeechNodes.map(n => partOfSpeechNodeMetaInfo(dependencyGraphId, n))
    )
}
