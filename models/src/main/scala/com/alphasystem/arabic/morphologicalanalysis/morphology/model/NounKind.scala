package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicLetters, ArabicSupportEnum, ArabicWord }

import java.lang.Enum

enum NounKind(override val code: String, override val word: ArabicWord) extends Enum[NounKind] with ArabicSupportEnum {

  case None extends NounKind("None", ArabicLetters.WordTatweel)

  case ActiveParticiple extends NounKind("Active participle", ArabicWord(Fa, Alif, Ain, Lam))

  case PassiveParticiple
      extends NounKind(
        "Passive participle",
        ArabicWord(Meem, Fa, Ain, Waw, Lam)
      )

  case VerbalNoun extends NounKind("Verbal noun", ArabicWord(Meem, Sad, Dal, Ra))

  case Adjective extends NounKind("Adjective", ArabicWord(Sad, Fa, Ta))
}
