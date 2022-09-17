package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.{ArabicSupportEnum, ArabicWord}

enum NounType(override val code: String, override val label: ArabicWord)
    extends ArabicSupportEnum {

  case INDEFINITE
      extends NounType("Indefinite", ArabicWord(NOON, KAF, RA, TA_MARBUTA))

  case DEFINITE
      extends NounType("Definite", ArabicWord(MEEM, AIN, RA, FA, TA_MARBUTA))
}
