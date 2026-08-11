package com.alphasystem
package arabic
package model

import ArabicLetterType.*
import DiacriticType.*
import ArabicLetters.*

enum JussiveParticle(override val word: ArabicWord) extends Enum[JussiveParticle] with ArabicSupportEnum {

  override val code: String = name()

  case DidNot extends JussiveParticle(ArabicWord(LamWithFatha, MeemWithSukun, LetterSpace))

  case NotYet extends JussiveParticle(ArabicWord(LamWithFatha, MeemWithShaddaAndFatha, LetterAlif, LetterSpace))

  case LamOfCommand extends JussiveParticle(ArabicWord(LamWithKasra))

  case LamOfProhibition extends JussiveParticle(ArabicWord(LamWithFatha, LetterAlif, LetterSpace))
}
