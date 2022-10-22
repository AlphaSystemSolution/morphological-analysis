package com.alphasystem
package arabic
package model

import ArabicLetterType.*

import java.lang.Enum

enum WeakVerbType(val label: ArabicWord) extends Enum[WeakVerbType] {

  case FirstRadicalWeakWaw
      extends WeakVerbType(
        ArabicWord(
          Meem,
          Tha,
          Alif,
          Lam,
          Space,
          Alif,
          Lam,
          Waw,
          Alif,
          Waw,
          Ya
        )
      )

  case FirstRadicalWeakYa
      extends WeakVerbType(
        ArabicWord(
          Meem,
          Tha,
          Alif,
          Lam,
          Space,
          Alif,
          Lam,
          Ya,
          Alif,
          YaHamzaAbove,
          Ya
        )
      )

  case SecondRadicalWeakWaw
      extends WeakVerbType(
        ArabicWord(
          AlifHamzaAbove,
          Jeem,
          Waw,
          Fa,
          Space,
          Alif,
          Lam,
          Waw,
          Alif,
          Waw,
          Ya
        )
      )

  case SecondRadicalWeakYa
      extends WeakVerbType(
        ArabicWord(
          AlifHamzaAbove,
          Jeem,
          Waw,
          Fa,
          Space,
          Alif,
          Lam,
          Ya,
          Alif,
          YaHamzaAbove,
          Ya
        )
      )

  case ThirdRadicalWeakWaw
      extends WeakVerbType(
        ArabicWord(
          Noon,
          Alif,
          Qaf,
          Sad,
          Space,
          Alif,
          Lam,
          Waw,
          Alif,
          Waw,
          Ya
        )
      )

  case ThirdRadicalWeakYa
      extends WeakVerbType(
        ArabicWord(
          Noon,
          Alif,
          Qaf,
          Sad,
          Space,
          Alif,
          Lam,
          Ya,
          Alif,
          YaHamzaAbove,
          Ya
        )
      )
}
