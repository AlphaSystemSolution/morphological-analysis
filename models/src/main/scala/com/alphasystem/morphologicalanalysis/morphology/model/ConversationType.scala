package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.{ArabicSupportEnum, ArabicWord}

enum ConversationType(override val code: String, override val label: ArabicWord)
    extends ArabicSupportEnum {

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
