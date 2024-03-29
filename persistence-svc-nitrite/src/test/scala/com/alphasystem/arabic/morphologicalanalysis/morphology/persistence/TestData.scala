package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import com.alphasystem.arabic.morphologicalanalysis.graph.model.GraphNodeType
import morphology.graph.model.{
  DependencyGraph,
  FontMetaInfo,
  GraphMetaInfo,
  Line,
  PartOfSpeechNode,
  Point,
  TerminalNode
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

  private[persistence] val verses =
    (1 to 10).map { index =>
      Verse(
        chapterNumber = 1,
        verseNumber = index,
        text = s"Verse test$index",
        tokenCount = 2,
        translation = None
      )
    }

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

  private[persistence] def partOfSpeechNode(
    dependencyGraphId: UUID,
    location: Location
  ): PartOfSpeechNode =
    PartOfSpeechNode(
      dependencyGraphId = dependencyGraphId,
      textPoint = Point(60, 120),
      translate = Point(0, 0),
      circle = Point(20, 60),
      font = FontMetaInfo(family = "Arial", weight = "NORMAL", posture = "REGULAR", size = 14.0),
      location = location,
      hidden = false
    )

  private[persistence] def terminalNode(
    dependencyGraphId: UUID,
    token: Token
  ): TerminalNode =
    TerminalNode(
      dependencyGraphId = dependencyGraphId,
      graphNodeType = GraphNodeType.Terminal,
      textPoint = Point(60, 120),
      translate = Point(0, 0),
      line = Line(Point(30, 50), Point(60, 50)),
      translationPoint = Point(6, 5),
      font = FontMetaInfo(family = "Arial", weight = "NORMAL", posture = "REGULAR", size = 14.0),
      translationFont = FontMetaInfo(family = "Arial", weight = "NORMAL", posture = "REGULAR", size = 14.0),
      token = token,
      partOfSpeechNodes = token.locations.map(n => partOfSpeechNode(dependencyGraphId, n))
    )

  private[persistence] val dependencyGraph = {
    val id = UUID.randomUUID()
    DependencyGraph(
      id = id,
      chapterNumber = updatedToken.chapterNumber,
      chapterName = chapter.chapterName,
      metaInfo = GraphMetaInfo(),
      verseNumbers = Seq(updatedToken.verseNumber),
      tokens = Seq(updatedToken),
      nodes = Seq(terminalNode(id, updatedToken))
    )
  }
}
