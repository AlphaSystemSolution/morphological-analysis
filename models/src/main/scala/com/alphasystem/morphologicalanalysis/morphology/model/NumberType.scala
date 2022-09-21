package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.{ ArabicSupportEnum, ArabicWord }

import java.lang.Enum

enum NumberType(override val code: String, override val label: ArabicWord)
    extends Enum[NumberType]
    with ArabicSupportEnum {

  case SINGULAR extends NumberType("Singular", ArabicWord(MEEM, FA, RA, DAL))

  case DUAL extends NumberType("Dual", ArabicWord(MEEM, THA, NOON, YA))

  case PLURAL extends NumberType("Plural", ArabicWord(JEEM, MEEM, AIN))

  case NONE extends NumberType("None", ArabicWord())
}
