package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.graph.model.{ DependencyGraph, GraphMetaInfo }
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
      tokenCount = 1,
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
      id = "test",
      chapterNumber = 1,
      verseNumber = 1,
      startTokenNumber = 1,
      endTokenNumber = 4,
      text = "بِسْمِ اللَّهِ الرَّحْمَـٰنِ الرَّحِيم",
      metaInfo = GraphMetaInfo()
    )
}
