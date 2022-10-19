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

  case SINGULAR extends NumberType("Singular", ArabicWord(MEEM, FA, RA, DAL))

  case DUAL extends NumberType("Dual", ArabicWord(MEEM, THA, NOON, YA))

  case PLURAL extends NumberType("Plural", ArabicWord(JEEM, MEEM, AIN))
}
