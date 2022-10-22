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

  case Masculine extends GenderType("Masculine", ArabicWord(Meem, Thal, Kaf, Ra))

  case Feminine
      extends GenderType(
        "Feminine",
        ArabicWord(Meem, WawHamzaAbove, Noon, Tha)
      )
}
