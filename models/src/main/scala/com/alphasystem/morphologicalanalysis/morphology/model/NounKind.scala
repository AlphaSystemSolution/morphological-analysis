package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.{ ArabicLetters, ArabicSupportEnum, ArabicWord }

import java.lang.Enum

enum NounKind(override val code: String, override val word: ArabicWord) extends Enum[NounKind] with ArabicSupportEnum {

  case NONE extends NounKind("None", ArabicLetters.WORD_TATWEEL)

  case ACTIVE_PARTICIPLE extends NounKind("Active participle", ArabicWord(FA, ALIF, AIN, LAM))

  case PASSIVE_PARTICIPLE
      extends NounKind(
        "Passive participle",
        ArabicWord(MEEM, FA, AIN, WAW, LAM)
      )

  case VERBAL_NOUN extends NounKind("Verbal noun", ArabicWord(MEEM, SAD, DAL, RA))

  case ADJECTIVE extends NounKind("Adjective", ArabicWord(SAD, FA, TA))
}
