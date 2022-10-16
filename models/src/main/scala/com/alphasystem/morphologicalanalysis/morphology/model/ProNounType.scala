package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.{ ArabicSupportEnum, ArabicWord }

import java.lang.Enum

enum ProNounType(override val code: String, override val word: ArabicWord)
    extends Enum[ProNounType]
    with ArabicSupportEnum {

  case DETACHED extends ProNounType("Detached", ArabicWord(MEEM, NOON, FA, SAD, LAM))

  case ATTACHED
      extends ProNounType(
        "Attached",
        ArabicWord(MEEM, TA, SAD, LAM)
      )
}
