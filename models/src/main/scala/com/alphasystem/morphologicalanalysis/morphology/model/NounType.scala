package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.{ ArabicSupportEnum, ArabicWord }

import java.lang.Enum
enum NounType(override val code: String, override val word: ArabicWord)
    extends Enum[NounType]
    with ArabicSupportEnum {

  case INDEFINITE
      extends NounType("Indefinite", ArabicWord(NOON, KAF, RA, TA_MARBUTA))

  case DEFINITE
      extends NounType("Definite", ArabicWord(MEEM, AIN, RA, FA, TA_MARBUTA))
}
