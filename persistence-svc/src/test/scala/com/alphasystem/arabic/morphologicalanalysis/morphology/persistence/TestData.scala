package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import morphology.model.*

trait TestData {

  private[persistence] val nounProperties: NounProperties =
    NounProperties(
      partOfSpeech = NounPartOfSpeechType.NOUN,
      status = NounStatus.NOMINATIVE,
      number = NumberType.SINGULAR,
      gender = GenderType.MASCULINE,
      nounType = NounType.INDEFINITE,
      nounKind = NounKind.NONE
    )

  private[persistence] val proNounProperties =
    ProNounProperties(
      partOfSpeech = ProNounPartOfSpeechType.PRONOUN,
      status = NounStatus.NOMINATIVE,
      number = NumberType.DUAL,
      gender = GenderType.FEMININE,
      conversationType = ConversationType.SECOND_PERSON,
      proNounType = ProNounType.ATTACHED
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
      translation = Some("translation")
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
}
