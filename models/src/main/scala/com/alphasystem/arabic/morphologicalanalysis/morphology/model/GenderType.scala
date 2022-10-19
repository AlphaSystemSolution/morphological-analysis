package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicSupportEnum, ArabicWord }

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
