package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicLetters, ArabicSupportEnum, ArabicWord }

import java.lang.Enum

enum VerbMode(override val code: String, override val word: ArabicWord) extends Enum[VerbMode] with ArabicSupportEnum {

  case NONE extends VerbMode("None", ArabicLetters.WORD_TATWEEL)

  case DEFAULT extends VerbMode("Default", ArabicWord(MEEM, RA, FA, AIN))

  case SUBJUNCTIVE extends VerbMode("Subjunctive", ArabicWord(MEEM, NOON, SAD, WAW, BA))

  case JUSSIVE extends VerbMode("Jussive", ArabicWord(MEEM, JEEM, ZAIN, WAW, MEEM))
}
