package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicSupportEnum, ArabicWord }

import java.lang.Enum

enum NumberType(override val code: String, override val word: ArabicWord)
    extends Enum[NumberType]
    with ArabicSupportEnum {

  case Singular extends NumberType("Singular", ArabicWord(Meem, Fa, Ra, Dal))

  case Dual extends NumberType("Dual", ArabicWord(Meem, Tha, Noon, Ya))

  case Plural extends NumberType("Plural", ArabicWord(Jeem, Meem, Ain))
}
