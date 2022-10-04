package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.{
  ArabicLetters,
  ArabicSupportEnum,
  ArabicWord
}

import java.lang.Enum

enum VerbMode(override val code: String, override val word: ArabicWord)
    extends Enum[VerbMode]
    with ArabicSupportEnum {

  case NONE extends VerbMode("None", ArabicLetters.WORD_TATWEEL)

  case DEFAULT extends VerbMode("Default", ArabicWord(MEEM, RA, FA, AIN))

  case SUBJUNCTIVE
      extends VerbMode("Subjunctive", ArabicWord(MEEM, NOON, SAD, WAW, BA))

  case JUSSIVE
      extends VerbMode("Jussive", ArabicWord(MEEM, JEEM, ZAIN, WAW, MEEM))
}
