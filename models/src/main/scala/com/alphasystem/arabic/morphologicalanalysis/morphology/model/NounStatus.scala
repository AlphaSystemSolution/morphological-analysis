package com.alphasystem
package arabic
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

  case Nominative
      extends NounStatus(
        "Nominative",
        ArabicWord(Meem, Ra, Fa, Waw, Ain),
        ArabicWord(Ra, Fa, Ain)
      )

  case Accusative
      extends NounStatus(
        "Accusative",
        ArabicWord(Meem, Noon, Sad, Waw, Ba),
        ArabicWord(Noon, Sad, Ba)
      )

  case Genitive
      extends NounStatus(
        "Genitive",
        ArabicWord(Meem, Jeem, Ra, Waw, Ra),
        ArabicWord(Jeem, Ra)
      )

  case None extends NounStatus("None", ArabicWord(Meem, Ba, Noon, Ya), ArabicWord(Meem, Ba, Noon, Ya))
}
