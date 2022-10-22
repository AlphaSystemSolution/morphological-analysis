package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicLetters, ArabicSupportEnum, ArabicWord }

import java.lang.Enum
enum RelationshipType(override val word: ArabicWord) extends Enum[RelationshipType] with ArabicSupportEnum {

  case None extends RelationshipType(ArabicLetters.WordSpace)

  case Mudaf extends RelationshipType(ArabicWord(Meem, Ddad, Alif, Fa))

  case MudafIlaih
      extends RelationshipType(
        ArabicWord(Meem, Ddad, Alif, Fa, Space, AlifHamzaBelow, Lam, Ya, Ha)
      )

  case Idafah
      extends RelationshipType(
        ArabicWord(AlifHamzaBelow, Ddad, Alif, Fa, TaMarbuta)
      )

  case DoubleIdafah
      extends RelationshipType(
        ArabicWord(AlifHamzaBelow, Ddad, Alif, Fa, Ta, Alif, Noon)
      )

  case Mowsoof extends RelationshipType(ArabicWord(Meem, Waw, Sad, Waw, Fa))

  case Sifah extends RelationshipType(ArabicWord(Sad, Fa, TaMarbuta))

  case PointingWord
      extends RelationshipType(
        ArabicWord(Meem, Sheen, Alif, Ra, Space, AlifHamzaBelow, Lam, Ya, Ha)
      )

  case JarMajroor
      extends RelationshipType(
        ArabicWord(Jeem, Alif, Ra, Space, Waw, Space, Meem, Jeem, Ra, Waw, Ra)
      )

  case Majroor extends RelationshipType(ArabicWord(Meem, Jeem, Ra, Waw, Ra))

  case Sillah extends RelationshipType(ArabicWord(Sad, Lam, TaMarbuta))

  case Mubtada extends RelationshipType(ArabicWord(Meem, Ba, Ta, Dal, AlifHamzaAbove))

  case Khabar extends RelationshipType(ArabicWord(Kha, Ba, Ra))

  case Mutaliq extends RelationshipType(ArabicWord(Meem, Ta, Ain, Lam, Qaf))

  case MutaliqToKhabar
      extends RelationshipType(
        ArabicWord(Meem, Ta, Ain, Lam, Qaf, Space, Ba, Lam, Kha, Ba, Ra)
      )

  case Maatoof extends RelationshipType(ArabicWord(Meem, Ain, Tta, Waw, Fa))

  case Ism
      extends RelationshipType(
        ArabicWord(AlifHamzaBelow, Seen, Meem)
      )

  case Forbidden
      extends RelationshipType(
        ArabicWord(Fa, Ain, Lam, Space, Noon, Ha, Ya)
      )

  case Faiil extends RelationshipType(ArabicWord(Fa, Alif, Ain, Lam))

  case AlternateDoer
      extends RelationshipType(
        ArabicWord(Noon, Alif, YaHamzaAbove, Ba, Space, Fa, Alif, Ain, Lam)
      )

  case Mafool extends RelationshipType(ArabicWord(Meem, Fa, Ain, Waw, Lam))

  case MafoolBhi
      extends RelationshipType(
        ArabicWord(Meem, Fa, Ain, Waw, Lam, Space, Ba, Ha)
      )

  case MafoolFihi
      extends RelationshipType(
        ArabicWord(Meem, Fa, Ain, Waw, Lam, Space, Fa, Ya, Ha)
      )

  case MafoolLahu
      extends RelationshipType(
        ArabicWord(Meem, Fa, Ain, Waw, Lam, Space, Lam, Ha)
      )

  case MafoolHall
      extends RelationshipType(
        ArabicWord(Meem, Fa, Ain, Waw, Lam, Space, Hha, Alif, Lam)
      )

  case MafoolMutliq
      extends RelationshipType(
        ArabicWord(Meem, Fa, Ain, Waw, Lam, Space, Meem, Tta, Lam, Qaf)
      )

  case Tawkeed extends RelationshipType(ArabicWord(Ta, Waw, Kaf, Ya, Dal))

  case Munadi extends RelationshipType(ArabicWord(Meem, Noon, Alif, Dal, Ya))

  case NounBasedSentence
      extends RelationshipType(
        ArabicWord(
          Jeem,
          Meem,
          Lam,
          TaMarbuta,
          Space,
          AlifHamzaBelow,
          Seen,
          Meem,
          Ya,
          TaMarbuta
        )
      )

  case VerbBasedSentence
      extends RelationshipType(
        ArabicWord(
          Jeem,
          Meem,
          Lam,
          TaMarbuta,
          Space,
          Fa,
          Ain,
          Lam,
          Ya,
          TaMarbuta
        )
      )

  case HalSentence
      extends RelationshipType(
        ArabicWord(
          Jeem,
          Meem,
          Lam,
          TaMarbuta,
          Space,
          Hha,
          Alif,
          Lam,
          Ya,
          TaMarbuta
        )
      )

  case QuotationSentence
      extends RelationshipType(
        ArabicWord(Meem, Qaf, Waw, Lam, Space, Alif, Lam, Qaf, Waw, Lam)
      )

  case Condition extends RelationshipType(ArabicWord(Sheen, Ra, Tta))

  case AnswerToCondition
      extends RelationshipType(
        ArabicWord(Jeem, Waw, Alif, Ba, Space, Alif, Lam, Sheen, Ra, Tta)
      )

  case Kaaf extends RelationshipType(ArabicWord(Kaf, Alif, Fa))

  override val code: String = name
}
