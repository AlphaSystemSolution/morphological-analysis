package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.{ ArabicSupportEnum, ArabicWord }

enum VerbType(override val code: String, override val label: ArabicWord)
    extends ArabicSupportEnum {

  case PERFECT extends VerbType("Perfect", ArabicWord(MEEM, ALIF, DDAD))

  case IMPERFECT
      extends VerbType("Imperfect", ArabicWord(MEEM, DDAD, ALIF, RA, AIN))

  case PERFECT_PASSIVE
      extends VerbType(
        "Perfect passive",
        ArabicWord(
          MEEM,
          ALIF,
          DDAD,
          SPACE,
          MEEM,
          BA,
          NOON,
          YA,
          SPACE,
          ALIF,
          LAM,
          LAM,
          MEEM,
          JEEM,
          HA,
          WAW,
          LAM
        )
      )

  case IMPERFECT_PASSIVE
      extends VerbType(
        "Imperfect passive",
        ArabicWord(
          MEEM,
          DDAD,
          ALIF,
          RA,
          AIN,
          SPACE,
          MEEM,
          BA,
          NOON,
          YA,
          SPACE,
          ALIF,
          LAM,
          LAM,
          MEEM,
          JEEM,
          HA,
          WAW,
          LAM
        )
      )

  case COMMAND
      extends VerbType("Imperative", ArabicWord(ALIF_HAMZA_ABOVE, MEEM, RA))
}
