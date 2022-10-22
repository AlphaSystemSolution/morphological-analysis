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

  case ThirdPerson
      extends ConversationType(
        "Third person",
        ArabicWord(Ghain, Alif, YaHamzaAbove, Ba)
      )

  case SecondPerson
      extends ConversationType(
        "Second person",
        ArabicWord(Meem, Kha, Alif, Tta, Ba)
      )

  case FirstPerson
      extends ConversationType(
        "First person",
        ArabicWord(Meem, Ta, Kaf, Lam, Meem)
      )
}
