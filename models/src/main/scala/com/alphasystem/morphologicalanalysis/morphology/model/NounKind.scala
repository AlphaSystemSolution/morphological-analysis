package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.{ ArabicSupportEnum, ArabicWord }

enum NounKind(override val code: String, override val label: ArabicWord)
    extends ArabicSupportEnum {

  case NONE extends NounKind("None", ArabicWord())

  case ACTIVE_PARTICIPLE
      extends NounKind("Active participle", ArabicWord(FA, ALIF, AIN, LAM))

  case PASSIVE_PARTICIPLE
      extends NounKind(
        "Passive participle",
        ArabicWord(MEEM, FA, AIN, WAW, LAM)
      )

  case VERBAL_NOUN
      extends NounKind("Verbal noun", ArabicWord(MEEM, SAD, DAL, RA))

  case ADJECTIVE extends NounKind("Adjective", ArabicWord(SAD, FA, TA))
}
