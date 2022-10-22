package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicSupportEnum, ArabicWord }

import java.lang.Enum
enum NounType(override val code: String, override val word: ArabicWord) extends Enum[NounType] with ArabicSupportEnum {

  case Indefinite extends NounType("Indefinite", ArabicWord(Noon, Kaf, Ra, TaMarbuta))

  case Definite extends NounType("Definite", ArabicWord(Meem, Ain, Ra, Fa, TaMarbuta))
}
