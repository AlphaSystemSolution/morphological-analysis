package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model
package incomplete_verb

import com.alphasystem.arabic.model.{ ArabicLetters, ArabicSupportEnum, ArabicWord }

import java.lang.Enum

enum IncompleteVerbCategory(override val word: ArabicWord) extends Enum[IncompleteVerbCategory] with ArabicSupportEnum {

  override def code: String = name()

  case None extends IncompleteVerbCategory(ArabicLetters.WordTatweel)

  case Kana
      extends IncompleteVerbCategory(
        ArabicWord(ArabicLetters.LetterKaf, ArabicLetters.LetterAlif, ArabicLetters.LetterNoon)
      )
}
