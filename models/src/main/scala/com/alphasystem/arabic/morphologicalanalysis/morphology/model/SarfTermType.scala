package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicSupport, ArabicSupportEnum, ArabicWord }

import java.lang.Enum
enum SarfTermType(override val word: ArabicWord) extends Enum[SarfTermType] with ArabicSupportEnum {

  case PastTense
      extends SarfTermType(
        ArabicWord(Fa, Ain, Lam, Space, Meem, Alif, Ddad, Ya)
      )

  case PresentTense
      extends SarfTermType(
        ArabicWord(Fa, Ain, Lam, Space, Meem, Ddad, Alif, Ra, Ain)
      )

  case VerbalNoun extends SarfTermType(ArabicWord(Meem, Sad, Dal, Ra))

  case ActiveParticipleMasculine
      extends SarfTermType(
        ArabicWord(
          AlifHamzaBelow,
          Seen,
          Meem,
          Space,
          Fa,
          Alif,
          Ain,
          Lam,
          Space,
          Meem,
          Thal,
          Kaf,
          Ra
        )
      )

  case ActiveParticipleFeminine
      extends SarfTermType(
        ArabicWord(
          AlifHamzaBelow,
          Seen,
          Meem,
          Space,
          Fa,
          Alif,
          Ain,
          Lam,
          Space,
          Meem,
          WawHamzaAbove,
          Noon,
          Tha
        )
      )

  case PastPassiveTense
      extends SarfTermType(
        ArabicWord(
          Fa,
          Ain,
          Lam,
          Space,
          Meem,
          Alif,
          Ddad,
          Ya,
          Space,
          Meem,
          Ba,
          Noon,
          Ya,
          Space,
          Ain,
          Lam,
          AlifMaksura,
          Space,
          Alif,
          Lam,
          Meem,
          Jeem,
          Ha,
          Waw,
          Lam
        )
      )

  case PresentPassiveTense
      extends SarfTermType(
        ArabicWord(
          Fa,
          Ain,
          Lam,
          Space,
          Meem,
          Ddad,
          Alif,
          Ra,
          Ain,
          Space,
          Meem,
          Ba,
          Noon,
          Ya,
          Space,
          Ain,
          Lam,
          AlifMaksura,
          Space,
          Alif,
          Lam,
          Meem,
          Jeem,
          Ha,
          Waw,
          Lam
        )
      )

  case PassiveParticipleMasculine
      extends SarfTermType(
        ArabicWord(
          AlifHamzaBelow,
          Seen,
          Meem,
          Space,
          Meem,
          Fa,
          Ain,
          Waw,
          Lam,
          Space,
          Meem,
          Thal,
          Kaf,
          Ra
        )
      )

  case PassiveParticipleFeminine
      extends SarfTermType(
        ArabicWord(
          AlifHamzaBelow,
          Seen,
          Meem,
          Space,
          Meem,
          Fa,
          Ain,
          Waw,
          Lam,
          Space,
          Meem,
          WawHamzaAbove,
          Noon,
          Tha
        )
      )

  case Imperative extends SarfTermType(ArabicWord(AlifHamzaAbove, Meem, Ra))

  case Forbidding extends SarfTermType(ArabicWord(Noon, Ha, Ya))

  case NounOfPlaceAndTime extends SarfTermType(ArabicWord(Dtha, Ra, Fa))

  override def code: String = name
}
