package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicLetters, ArabicSupportEnum, ArabicWord }

import java.lang.Enum

enum RelationshipType(override val word: ArabicWord, val colorCode: String)
    extends Enum[RelationshipType]
    with ArabicSupportEnum {

  case None extends RelationshipType(ArabicLetters.WordTatweel, "#000000")

  case Mudaf extends RelationshipType(ArabicWord(Meem, Ddad, Alif, Fa), "#000000")

  case MudafIlaih
      extends RelationshipType(
        ArabicWord(Meem, Ddad, Alif, Fa, Space, AlifHamzaBelow, Lam, Ya, Ha),
        "#4AA02C"
      )

  case Mowsoof extends RelationshipType(ArabicWord(Meem, Waw, Sad, Waw, Fa), "#993838")

  case Sifah extends RelationshipType(ArabicWord(Sad, Fa, TaMarbuta), "#993838")

  case PointingWord
      extends RelationshipType(
        ArabicWord(Meem, Sheen, Alif, Ra, Space, AlifHamzaBelow, Lam, Ya, Ha),
        "#008080"
      )

  case Majroor extends RelationshipType(ArabicWord(Meem, Jeem, Ra, Waw, Ra), "#8D38C9")

  case Sillah extends RelationshipType(ArabicWord(Sad, Lam, TaMarbuta), "#6DEF9B")

  case Mubtada extends RelationshipType(ArabicWord(Meem, Ba, Ta, Dal, AlifHamzaAbove), "#000000")

  case Khabar extends RelationshipType(ArabicWord(Kha, Ba, Ra), "#0041C2")

  case Mutaliq extends RelationshipType(ArabicWord(Meem, Ta, Ain, Lam, Qaf), "#3BB9FF")

  case Maatoof extends RelationshipType(ArabicWord(Meem, Ain, Tta, Waw, Fa), "#659EC7")

  case Ism extends RelationshipType(ArabicWord(AlifHamzaBelow, Seen, Meem), "#800000")

  case Forbidden extends RelationshipType(ArabicWord(Fa, Ain, Lam, Space, Noon, Ha, Ya), "#&d3C98")

  case Faiil extends RelationshipType(ArabicWord(Fa, Alif, Ain, Lam), "#4C5F6A")

  case AlternateDoer
      extends RelationshipType(ArabicWord(Noon, Alif, YaHamzaAbove, Ba, Space, Fa, Alif, Ain, Lam), "#4C5F6A")

  case Mafool extends RelationshipType(ArabicWord(Meem, Fa, Ain, Waw, Lam), "#FF8040")

  case MafoolBhi extends RelationshipType(ArabicWord(Meem, Fa, Ain, Waw, Lam, Space, Ba, Ha), "#FF8040")

  case MafoolFihi extends RelationshipType(ArabicWord(Meem, Fa, Ain, Waw, Lam, Space, Fa, Ya, Ha), "#FF8040")

  case MafoolLahu extends RelationshipType(ArabicWord(Meem, Fa, Ain, Waw, Lam, Space, Lam, Ha), "#FF8040")

  case MafoolHall extends RelationshipType(ArabicWord(Meem, Fa, Ain, Waw, Lam, Space, Hha, Alif, Lam), "#FF8040")

  case MafoolMutliq extends RelationshipType(ArabicWord(Meem, Fa, Ain, Waw, Lam, Space, Meem, Tta, Lam, Qaf), "#FF8040")

  case Tawkeed extends RelationshipType(ArabicWord(Ta, Waw, Kaf, Ya, Dal), "#BBAADD")

  case Munadi extends RelationshipType(ArabicWord(Meem, Noon, Alif, Dal, Ya), "#48C9B0")

  case Condition extends RelationshipType(ArabicWord(Sheen, Ra, Tta), "#239B56")

  case AnswerToCondition
      extends RelationshipType(ArabicWord(Jeem, Waw, Alif, Ba, Space, Alif, Lam, Sheen, Ra, Tta), "239B56")

  case Kaaf extends RelationshipType(ArabicWord(Kaf, Alif, Fa), "#008080")

  override val code: String = name
}
