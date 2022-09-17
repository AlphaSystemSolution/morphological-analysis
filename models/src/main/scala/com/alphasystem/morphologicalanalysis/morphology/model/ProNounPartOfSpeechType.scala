package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.ArabicWord

enum ProNounPartOfSpeechType(
  override val code: String,
  override val label: ArabicWord)
    extends PartOfSpeechType {

  case PRONOUN
      extends ProNounPartOfSpeechType(
        "Pronoun",
        ArabicWord(DDAD, MEEM, YA, RA)
      )

  case RELATIVE_PRONOUN
      extends ProNounPartOfSpeechType(
        "Relative pronoun",
        ArabicWord(
          ALIF_HAMZA_BELOW,
          SEEN,
          MEEM,
          SPACE,
          MEEM,
          WAW,
          SAD,
          WAW,
          LAM
        )
      )

  case DEMONSTRATIVE_PRONOUN
      extends ProNounPartOfSpeechType(
        "Demonstrative pronoun",
        ArabicWord(
          ALIF_HAMZA_BELOW,
          SEEN,
          MEEM,
          SPACE,
          ALIF_HAMZA_BELOW,
          SHEEN,
          ALIF,
          RA,
          TA_MARBUTA
        )
      )

}
