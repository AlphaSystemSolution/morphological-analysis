package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicSupportEnum, ArabicWord }

import java.lang.Enum

enum ProNounType(override val code: String, override val word: ArabicWord)
    extends Enum[ProNounType]
    with ArabicSupportEnum {

  case Detached extends ProNounType("Detached", ArabicWord(Meem, Noon, Fa, Sad, Lam))

  case Attached
      extends ProNounType(
        "Attached",
        ArabicWord(Meem, Ta, Sad, Lam)
      )
}
