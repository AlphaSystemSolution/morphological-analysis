package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model

import com.alphasystem.arabic.model.{ ArabicLetterType, ArabicSupport, ArabicWord }

import java.lang.Enum

enum MorphologicalTermType(word: ArabicWord) extends Enum[MorphologicalTermType] with ArabicSupport {

  case PastTense
      extends MorphologicalTermType(
        ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Alif,
          ArabicLetterType.Ddad,
          ArabicLetterType.Ya
        )
      )

  case PresentTense
      extends MorphologicalTermType(
        ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Ddad,
          ArabicLetterType.Alif,
          ArabicLetterType.Ra,
          ArabicLetterType.Ain
        )
      )

  case VerbalNoun
      extends MorphologicalTermType(
        ArabicWord(ArabicLetterType.Meem, ArabicLetterType.Sad, ArabicLetterType.Dal, ArabicLetterType.Ra)
      )

  case ActiveParticipleMasculine
      extends MorphologicalTermType(
        ArabicWord(
          ArabicLetterType.AlifHamzaBelow,
          ArabicLetterType.Seen,
          ArabicLetterType.Meem,
          ArabicLetterType.Space,
          ArabicLetterType.Fa,
          ArabicLetterType.Alif,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Thal,
          ArabicLetterType.Kaf,
          ArabicLetterType.Ra
        )
      )

  case ActiveParticipleFeminine
      extends MorphologicalTermType(
        ArabicWord(
          ArabicLetterType.AlifHamzaBelow,
          ArabicLetterType.Seen,
          ArabicLetterType.Meem,
          ArabicLetterType.Space,
          ArabicLetterType.Fa,
          ArabicLetterType.Alif,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.WawHamzaAbove,
          ArabicLetterType.Noon,
          ArabicLetterType.Tha
        )
      )

  case PastPassiveTense
      extends MorphologicalTermType(
        ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Alif,
          ArabicLetterType.Ddad,
          ArabicLetterType.Ya,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Ba,
          ArabicLetterType.Noon,
          ArabicLetterType.Ya,
          ArabicLetterType.Space,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.AlifMaksura,
          ArabicLetterType.Space,
          ArabicLetterType.Alif,
          ArabicLetterType.Lam,
          ArabicLetterType.Meem,
          ArabicLetterType.Jeem,
          ArabicLetterType.Ha,
          ArabicLetterType.Waw,
          ArabicLetterType.Lam
        )
      )

  case PresentPassiveTense
      extends MorphologicalTermType(
        ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Ddad,
          ArabicLetterType.Alif,
          ArabicLetterType.Ra,
          ArabicLetterType.Ain,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Ba,
          ArabicLetterType.Noon,
          ArabicLetterType.Ya,
          ArabicLetterType.Space,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.AlifMaksura,
          ArabicLetterType.Space,
          ArabicLetterType.Alif,
          ArabicLetterType.Lam,
          ArabicLetterType.Meem,
          ArabicLetterType.Jeem,
          ArabicLetterType.Ha,
          ArabicLetterType.Waw,
          ArabicLetterType.Lam
        )
      )

  case PassiveParticipleMasculine
      extends MorphologicalTermType(
        ArabicWord(
          ArabicLetterType.AlifHamzaBelow,
          ArabicLetterType.Seen,
          ArabicLetterType.Meem,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Waw,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Thal,
          ArabicLetterType.Kaf,
          ArabicLetterType.Ra
        )
      )

  case PassiveParticipleFeminine
      extends MorphologicalTermType(
        ArabicWord(
          ArabicLetterType.AlifHamzaBelow,
          ArabicLetterType.Seen,
          ArabicLetterType.Meem,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Waw,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.WawHamzaAbove,
          ArabicLetterType.Noon,
          ArabicLetterType.Tha
        )
      )

  case Imperative
      extends MorphologicalTermType(
        ArabicWord(ArabicLetterType.AlifHamzaAbove, ArabicLetterType.Meem, ArabicLetterType.Ra)
      )

  case Forbidden
      extends MorphologicalTermType(ArabicWord(ArabicLetterType.Noon, ArabicLetterType.Ha, ArabicLetterType.Ya))

  case NounOfPlaceAndTime
      extends MorphologicalTermType(ArabicWord(ArabicLetterType.Dtha, ArabicLetterType.Ra, ArabicLetterType.Fa))

  override val label: String = word.label
}
