package com.alphasystem
package arabic
package model

import ArabicLetterType.*

import java.lang.Enum

enum WeakVerbType(val label: ArabicWord) extends Enum[WeakVerbType] {

  case FIRST_RADICAL_WEAK_WAW
      extends WeakVerbType(
        ArabicWord(
          MEEM,
          THA,
          ALIF,
          LAM,
          SPACE,
          ALIF,
          LAM,
          WAW,
          ALIF,
          WAW,
          YA
        )
      )

  case FIRST_RADICAL_WEAK_YA
      extends WeakVerbType(
        ArabicWord(
          MEEM,
          THA,
          ALIF,
          LAM,
          SPACE,
          ALIF,
          LAM,
          YA,
          ALIF,
          YA_HAMZA_ABOVE,
          YA
        )
      )

  case SECOND_RADICAL_WEAK_WAW
      extends WeakVerbType(
        ArabicWord(
          ALIF_HAMZA_ABOVE,
          JEEM,
          WAW,
          FA,
          SPACE,
          ALIF,
          LAM,
          WAW,
          ALIF,
          WAW,
          YA
        )
      )

  case SECOND_RADICAL_WEAK_YA
      extends WeakVerbType(
        ArabicWord(
          ALIF_HAMZA_ABOVE,
          JEEM,
          WAW,
          FA,
          SPACE,
          ALIF,
          LAM,
          YA,
          ALIF,
          YA_HAMZA_ABOVE,
          YA
        )
      )

  case THIRD_RADICAL_WEAK_WAW
      extends WeakVerbType(
        ArabicWord(
          NOON,
          ALIF,
          QAF,
          SAD,
          SPACE,
          ALIF,
          LAM,
          WAW,
          ALIF,
          WAW,
          YA
        )
      )

  case THIRD_RADICAL_WEAK_YA
      extends WeakVerbType(
        ArabicWord(
          NOON,
          ALIF,
          QAF,
          SAD,
          SPACE,
          ALIF,
          LAM,
          YA,
          ALIF,
          YA_HAMZA_ABOVE,
          YA
        )
      )
}
