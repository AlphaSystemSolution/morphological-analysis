package com.alphasystem
package arabic
package model

import ArabicLetterType.*

import java.lang.Enum

enum VerbType(val label: ArabicWord) extends Enum[VerbType] {

  case Consonant
      extends VerbType(
        ArabicWord(Sad, Hha, Ya, Hha, Space, Seen, Alif, Lam, Meem)
      )

  case DoubleLettered
      extends VerbType(
        ArabicWord(Sad, Hha, Ya, Hha, Space, Meem, Ddad, Alif, Fa)
      )

  case FirstRadicalHamza
      extends VerbType(
        ArabicWord(
          Sad,
          Hha,
          Ya,
          Hha,
          Space,
          Meem,
          Ha,
          Meem,
          Waw,
          Zain,
          Space,
          Alif,
          Lam,
          Fa,
          Alif,
          Hamza
        )
      )

  case SecondRadicalHamza
      extends VerbType(
        ArabicWord(
          Sad,
          Hha,
          Ya,
          Hha,
          Space,
          Meem,
          Ha,
          Meem,
          Waw,
          Zain,
          Space,
          Alif,
          Lam,
          Ain,
          Ya,
          Noon
        )
      )

  case ThirdRadicalHamza
      extends VerbType(
        ArabicWord(
          Sad,
          Hha,
          Ya,
          Hha,
          Space,
          Meem,
          Ha,
          Meem,
          Waw,
          Zain,
          Space,
          Alif,
          Lam,
          Lam,
          Alif,
          Meem
        )
      )

  case FirstRadicalWeak
      extends VerbType(
        ArabicWord(
          Meem,
          Ain,
          Ta,
          Lam,
          Space,
          Alif,
          Lam,
          Fa,
          Alif,
          Hamza
        )
      )

  case SecondRadicalWeak
      extends VerbType(
        ArabicWord(
          Meem,
          Ain,
          Ta,
          Lam,
          Space,
          Alif,
          Lam,
          Ain,
          Ya,
          Noon
        )
      )

  case ThirdRadicalWeak
      extends VerbType(
        ArabicWord(
          Meem,
          Ain,
          Ta,
          Lam,
          Space,
          Alif,
          Lam,
          Lam,
          Alif,
          Meem
        )
      )

  case TwoSeparateRadicalsWeak
      extends VerbType(
        ArabicWord(Lam, Fa, Ya, Fa, Space, Meem, Fa, Ra, Waw, Qaf)
      )

  case TwoConsecutiveRadicalsWeak
      extends VerbType(
        ArabicWord(Lam, Fa, Ya, Fa, Space, Meem, Qaf, Ra, Waw, Noon)
      )
}
