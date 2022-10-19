package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicSupportEnum, ArabicWord }

import java.lang.Enum

enum ConversationType(override val code: String, override val word: ArabicWord)
    extends Enum[ConversationType]
    with ArabicSupportEnum {

  case THIRD_PERSON
      extends ConversationType(
        "Third person",
        ArabicWord(GHAIN, ALIF, YA_HAMZA_ABOVE, BA)
      )

  case SECOND_PERSON
      extends ConversationType(
        "Second person",
        ArabicWord(MEEM, KHA, ALIF, TTA, BA)
      )

  case FIRST_PERSON
      extends ConversationType(
        "First person",
        ArabicWord(MEEM, TA, KAF, LAM, MEEM)
      )
}
