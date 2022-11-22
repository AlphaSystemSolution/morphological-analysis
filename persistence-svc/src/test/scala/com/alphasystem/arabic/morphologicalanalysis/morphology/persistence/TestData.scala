package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import morphologicalanalysis.graph.model.GraphNodeType
import com.alphasystem.arabic.morphologicalanalysis.morphology.graph.model.{
  DependencyGraph,
  FontMetaInfo,
  GraphMetaInfo,
  PartOfSpeechNode,
  PhraseNode,
  RelationshipNode,
  RootNode,
  TerminalNode
}
import morphology.model.*

import java.util.UUID

trait TestData {

  private[persistence] val nounProperties: NounProperties =
    NounProperties(
      partOfSpeech = NounPartOfSpeechType.Noun,
      status = NounStatus.Nominative,
      number = NumberType.Singular,
      gender = GenderType.Masculine,
      nounType = NounType.Indefinite,
      nounKind = NounKind.None
    )

  private[persistence] val proNounProperties =
    ProNounProperties(
      partOfSpeech = ProNounPartOfSpeechType.Pronoun,
      status = NounStatus.Nominative,
      number = NumberType.Dual,
      gender = GenderType.Feminine,
      conversationType = ConversationType.SecondPerson,
      proNounType = ProNounType.Attached
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

  private[persistence] val token =
    Token(
      chapterNumber = 1,
      verseNumber = 1,
      tokenNumber = 1,
      token = "someToken",
      translation = None
    )

  private[persistence] val verse =
    Verse(
      chapterNumber = 1,
      verseNumber = 1,
      text = "verse text",
      tokenCount = 0,
      translation = Some("translation")
    )

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

  private[persistence] val dependencyGraph =
    DependencyGraph(
      chapterNumber = 1,
      chapterName = "some name",
      text = "some text",
      metaInfo = GraphMetaInfo(),
      verseTokensMap = Map(1 -> Seq(1, 2, 3, 4))
    )

  private[persistence] val dependencyGraph2 =
    DependencyGraph(
      chapterNumber = 1,
      chapterName = "some name",
      text = "some text",
      metaInfo = GraphMetaInfo(),
      verseTokensMap = Map(1 -> Seq(5, 6, 7), 2 -> Seq(1, 2))
    )

  private val defaultFont: FontMetaInfo =
    FontMetaInfo(family = "Arial", weight = "NORMAL", posture = "REGULAR", size = 14.0)

  private[persistence] val nodes = Seq(
    TerminalNode(
      id = UUID.randomUUID().toString,
      graphNodeType = GraphNodeType.Terminal,
      dependencyGraphId = dependencyGraph.id,
      chapterNumber = 1,
      verseNumber = 1,
      tokenNumber = 1,
      version = 1,
      text = "text",
      x = 80,
      y = 120,
      translateX = 0,
      translateY = 0,
      x1 = 200,
      y1 = 280,
      x2 = 250,
      y2 = 280,
      translationText = "",
      translationX = 40,
      translationY = 80,
      tokenId = 1.toTokenId(1, 1),
      font = defaultFont,
      translationFont = defaultFont
    ),
    PartOfSpeechNode(
      dependencyGraphId = dependencyGraph.id,
      chapterNumber = 1,
      verseNumber = 1,
      tokenNumber = 1,
      locationNumber = 1,
      version = 1,
      text = "text",
      x = 80,
      y = 120,
      translateX = 0,
      translateY = 0,
      x1 = 0,
      y1 = 0,
      x2 = 0,
      y2 = 0,
      cx = 130,
      cy = 150,
      font = defaultFont,
      linkId = "test",
      hidden = false,
      partOfSpeechType = NounPartOfSpeechType.Noun
    ),
    PhraseNode(
      dependencyGraphId = dependencyGraph.id,
      chapterNumber = 1,
      verseNumber = 1,
      tokenNumber = 1,
      version = 1,
      text = "text",
      x = 80,
      y = 120,
      x1 = 200,
      y1 = 280,
      x2 = 250,
      y2 = 280,
      cx = 130,
      cy = 150,
      translateX = 0,
      translateY = 0,
      linkId = "test",
      font = defaultFont
    ),
    RelationshipNode(
      dependencyGraphId = dependencyGraph.id,
      relationshipType = RelationshipType.Ism,
      chapterNumber = 1,
      verseNumber = 1,
      tokenNumber = 1,
      version = 1,
      text = "text",
      x = 80,
      y = 120,
      controlX1 = 30,
      controlY1 = 40,
      controlX2 = 50,
      controlY2 = 60,
      t1 = 0,
      t2 = 0,
      translateX = 0,
      translateY = 0,
      dependentId = "test",
      ownerId = "test",
      font = defaultFont
    ),
    RootNode(
      dependencyGraphId = dependencyGraph.id,
      chapterNumber = 1,
      verseNumber = 1,
      tokenNumber = 1,
      version = 1,
      text = "text",
      x = 80,
      y = 120,
      translateX = 0,
      translateY = 0,
      font = defaultFont,
      childNodeType = GraphNodeType.Terminal
    )
  )

}
