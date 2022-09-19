package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence

import com.alphasystem.morphologicalanalysis.morphology.model.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.model.*

trait TestData {

  private[persistence] val nounProperties: NounProperties =
    NounProperties(
      id = "1",
      partOfSpeech = NounPartOfSpeechType.NOUN,
      status = NounStatus.NOMINATIVE,
      number = NumberType.SINGULAR,
      gender = GenderType.MASCULINE
    )

  private[persistence] val proNounProperties =
    ProNounProperties(
      id = "2",
      partOfSpeech = ProNounPartOfSpeechType.PRONOUN,
      status = NounStatus.NOMINATIVE,
      number = NumberType.DUAL,
      gender = GenderType.FEMININE,
      conversationType = ConversationType.SECOND_PERSON,
      proNounType = ProNounType.ATTACHED
    )

  private[persistence] val location =
    Location(
      id = "1",
      chapterNumber = 1,
      verseNumber = 1,
      tokenNumber = 1,
      locationNumber = 1,
      hidden = false,
      startIndex = 1,
      endIndex = 3,
      derivedText = "derivedText",
      text = "text",
      translation = Some("translation")
    )
}
