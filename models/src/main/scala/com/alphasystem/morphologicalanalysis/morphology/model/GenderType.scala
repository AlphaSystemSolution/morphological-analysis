package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.{ ArabicSupportEnum, ArabicWord }

import java.lang.Enum

enum GenderType(override val code: String, override val word: ArabicWord)
    extends Enum[GenderType]
    with ArabicSupportEnum {

  case MASCULINE extends GenderType("Masculine", ArabicWord(MEEM, THAL, KAF, RA))

  case FEMININE
      extends GenderType(
        "Feminine",
        ArabicWord(MEEM, WAW_HAMZA_ABOVE, NOON, THA)
      )
}
