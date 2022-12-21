package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicLetters, ArabicSupportEnum, ArabicWord }

import java.lang.Enum

enum VerbMode(override val code: String, override val word: ArabicWord) extends Enum[VerbMode] with ArabicSupportEnum {

  case None extends VerbMode("None", ArabicLetters.WordTatweel)

  case Default extends VerbMode("Default", ArabicWord(Meem, Ra, Fa, Waw, Ain))

  case Subjunctive extends VerbMode("Subjunctive", ArabicWord(Meem, Noon, Sad, Waw, Ba))

  case Jussive extends VerbMode("Jussive", ArabicWord(Meem, Jeem, Zain, Waw, Meem))
}
