package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.ArabicWord

enum NounPartOfSpeechType(
  override val code: String,
  override val label: ArabicWord)
    extends PartOfSpeechType {

  case NOUN
      extends NounPartOfSpeechType(
        "Noun",
        ArabicWord(ALIF_HAMZA_BELOW, SEEN, MEEM)
      )

  case PROPER_NOUN
      extends NounPartOfSpeechType(
        "Proper Noun",
        ArabicWord(ALIF_HAMZA_BELOW, SEEN, MEEM, SPACE, AIN, LAM, MEEM)
      )

  case TIME_ADVERB
      extends NounPartOfSpeechType(
        "Time adverb",
        ArabicWord(DTHA, RA, FA, SPACE, ZAIN, MEEM, ALIF, NOON)
      )

  case LOCATION_ADVERB
      extends NounPartOfSpeechType(
        "Location adverb",
        ArabicWord(DTHA, RA, FA, SPACE, MEEM, KAF, ALIF, NOON)
      )
}
