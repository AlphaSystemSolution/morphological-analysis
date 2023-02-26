package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model

import com.alphasystem.arabic.model.{ ArabicLetterType, ArabicSupport, ArabicWord }

import java.lang.Enum

enum MorphologicalTermType(val title: ArabicWord, val shortTitle: ArabicWord)
    extends Enum[MorphologicalTermType]
    with ArabicSupport {

  case PastTense
      extends MorphologicalTermType(
        title = ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Alif,
          ArabicLetterType.Ddad,
          ArabicLetterType.Ya
        ),
        shortTitle = ArabicWord(
          ArabicLetterType.Meem,
          ArabicLetterType.Alif,
          ArabicLetterType.Ddad,
          ArabicLetterType.Ya
        )
      )

  case PresentTense
      extends MorphologicalTermType(
        title = ArabicWord(
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Ddad,
          ArabicLetterType.Alif,
          ArabicLetterType.Ra,
          ArabicLetterType.Ain
        ),
        shortTitle = ArabicWord(
          ArabicLetterType.Meem,
          ArabicLetterType.Ddad,
          ArabicLetterType.Alif,
          ArabicLetterType.Ra,
          ArabicLetterType.Ain
        )
      )

  case VerbalNoun
      extends MorphologicalTermType(
        title = ArabicWord(ArabicLetterType.Meem, ArabicLetterType.Sad, ArabicLetterType.Dal, ArabicLetterType.Ra),
        shortTitle = ArabicWord(ArabicLetterType.Meem, ArabicLetterType.Sad, ArabicLetterType.Dal, ArabicLetterType.Ra)
      )

  case ActiveParticipleMasculine
      extends MorphologicalTermType(
        title = ArabicWord(
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
        ),
        shortTitle = ArabicWord(
          ArabicLetterType.AlifHamzaBelow,
          ArabicLetterType.Seen,
          ArabicLetterType.Meem,
          ArabicLetterType.Space,
          ArabicLetterType.Fa,
          ArabicLetterType.Alif,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam
        )
      )

  case ActiveParticipleFeminine
      extends MorphologicalTermType(
        title = ArabicWord(
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
        ),
        shortTitle = ArabicWord(
          ArabicLetterType.AlifHamzaBelow,
          ArabicLetterType.Seen,
          ArabicLetterType.Meem,
          ArabicLetterType.Space,
          ArabicLetterType.Fa,
          ArabicLetterType.Alif,
          ArabicLetterType.Ain,
          ArabicLetterType.Lam
        )
      )

  case PastPassiveTense
      extends MorphologicalTermType(
        title = ArabicWord(
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
        ),
        shortTitle = ArabicWord(
          ArabicLetterType.Meem,
          ArabicLetterType.Alif,
          ArabicLetterType.Ddad,
          ArabicLetterType.Ya,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Jeem,
          ArabicLetterType.Ha,
          ArabicLetterType.Waw,
          ArabicLetterType.Lam
        )
      )

  case PresentPassiveTense
      extends MorphologicalTermType(
        title = ArabicWord(
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
        ),
        shortTitle = ArabicWord(
          ArabicLetterType.Meem,
          ArabicLetterType.Ddad,
          ArabicLetterType.Alif,
          ArabicLetterType.Ra,
          ArabicLetterType.Ain,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Jeem,
          ArabicLetterType.Ha,
          ArabicLetterType.Waw,
          ArabicLetterType.Lam
        )
      )

  case PassiveParticipleMasculine
      extends MorphologicalTermType(
        title = ArabicWord(
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
        ),
        shortTitle = ArabicWord(
          ArabicLetterType.AlifHamzaBelow,
          ArabicLetterType.Seen,
          ArabicLetterType.Meem,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Waw,
          ArabicLetterType.Lam
        )
      )

  case PassiveParticipleFeminine
      extends MorphologicalTermType(
        title = ArabicWord(
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
        ),
        shortTitle = ArabicWord(
          ArabicLetterType.AlifHamzaBelow,
          ArabicLetterType.Seen,
          ArabicLetterType.Meem,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Fa,
          ArabicLetterType.Ain,
          ArabicLetterType.Waw,
          ArabicLetterType.Lam
        )
      )

  case Imperative
      extends MorphologicalTermType(
        title = ArabicWord(ArabicLetterType.AlifHamzaAbove, ArabicLetterType.Meem, ArabicLetterType.Ra),
        shortTitle = ArabicWord(ArabicLetterType.AlifHamzaAbove, ArabicLetterType.Meem, ArabicLetterType.Ra)
      )

  case Forbidden
      extends MorphologicalTermType(
        title = ArabicWord(ArabicLetterType.Noon, ArabicLetterType.Ha, ArabicLetterType.Ya),
        shortTitle = ArabicWord(ArabicLetterType.Noon, ArabicLetterType.Ha, ArabicLetterType.Ya)
      )

  case NounOfPlaceAndTime
      extends MorphologicalTermType(
        title = ArabicWord(ArabicLetterType.Dtha, ArabicLetterType.Ra, ArabicLetterType.Fa),
        shortTitle = ArabicWord(ArabicLetterType.Dtha, ArabicLetterType.Ra, ArabicLetterType.Fa)
      )

  override val label: String = title.label
}
