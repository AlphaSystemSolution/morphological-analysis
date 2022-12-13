package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicLetters, ArabicSupportEnum, ArabicWord }

import java.lang.Enum

enum PhraseType(override val word: ArabicWord, val colorCode: String) extends Enum[PhraseType] with ArabicSupportEnum {

  override def code: String = name()

  case None extends PhraseType(ArabicLetters.WordTatweel, "#000000")

  case JarMajroor extends PhraseType(ArabicWord(Jeem, Alif, Ra, Space, Waw, Space, Meem, Jeem, Ra, Waw, Ra), "#DC381F")

  case Idafah extends PhraseType(ArabicWord(AlifHamzaBelow, Ddad, Alif, Fa, TaMarbuta), "#336633")

  case DoubleIdafah extends PhraseType(ArabicWord(AlifHamzaBelow, Ddad, Alif, Fa, Ta, Alif, Noon), "#336633")

  case NounBasedSentence
      extends PhraseType(
        ArabicWord(Jeem, Meem, Lam, TaMarbuta, Space, AlifHamzaBelow, Seen, Meem, Ya, TaMarbuta),
        "#FF00FF"
      )

  case VerbBasedSentence
      extends PhraseType(ArabicWord(Jeem, Meem, Lam, TaMarbuta, Space, Fa, Ain, Lam, Ya, TaMarbuta), "#8A4117")

  case HalSentence
      extends PhraseType(ArabicWord(Jeem, Meem, Lam, TaMarbuta, Space, Hha, Alif, Lam, Ya, TaMarbuta), "#8B77FD")

  case QuotationSentence extends PhraseType(ArabicWord(Meem, Qaf, Waw, Lam, Space, Alif, Lam, Qaf, Waw, Lam), "#8B77FD")
}
