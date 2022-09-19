package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.{ ArabicSupportEnum, ArabicWord }

enum NounStatus(
  override val code: String,
  override val label: ArabicWord,
  val shortLabel: ArabicWord)
    extends ArabicSupportEnum {

  case NOMINATIVE
      extends NounStatus(
        "Nominative",
        ArabicWord(MEEM, RA, FA, WAW, AIN),
        ArabicWord(RA, FA, AIN)
      )

  case ACCUSATIVE
      extends NounStatus(
        "Accusative",
        ArabicWord(MEEM, NOON, SAD, WAW, BA),
        ArabicWord(NOON, SAD, BA)
      )

  case GENITIVE
      extends NounStatus(
        "Genitive",
        ArabicWord(MEEM, JEEM, RA, WAW, RA),
        ArabicWord(JEEM, RA)
      )
}
