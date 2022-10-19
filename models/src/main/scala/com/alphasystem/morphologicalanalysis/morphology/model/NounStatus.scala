package com.alphasystem
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicSupportEnum, ArabicWord }

import java.lang.Enum

enum NounStatus(
  override val code: String,
  override val word: ArabicWord,
  val shortLabel: ArabicWord)
    extends Enum[NounStatus]
    with ArabicSupportEnum {

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
