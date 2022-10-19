package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicSupportEnum, ArabicWord }

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
