package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.{ ArabicSupportEnum, ArabicWord }

enum GenderType(override val code: String, override val label: ArabicWord)
    extends ArabicSupportEnum {

  case MASCULINE
      extends GenderType("Masculine", ArabicWord(MEEM, THAL, KAF, RA))

  case FEMININE
      extends GenderType(
        "Feminine",
        ArabicWord(MEEM, WAW_HAMZA_ABOVE, NOON, THA)
      )

  case NONE extends GenderType("None", ArabicWord())
}
