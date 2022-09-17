package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.{ArabicSupportEnum, ArabicWord}

enum VerbMode(override val code: String, override val label: ArabicWord)
    extends ArabicSupportEnum {

  case SUBJUNCTIVE
      extends VerbMode("Subjunctive", ArabicWord(MEEM, NOON, SAD, WAW, BA))

  case JUSSIVE
      extends VerbMode("Jussive", ArabicWord(MEEM, JEEM, ZAIN, WAW, MEEM))
}
