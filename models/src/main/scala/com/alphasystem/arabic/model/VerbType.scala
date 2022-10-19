package com.alphasystem
package arabic
package model

import ArabicLetterType.*

import java.lang.Enum

enum VerbType(val label: ArabicWord) extends Enum[VerbType] {

  case CONSONANT
      extends VerbType(
        ArabicWord(SAD, HHA, YA, HHA, SPACE, SEEN, ALIF, LAM, MEEM)
      )

  case DOUBLE_LETTERED
      extends VerbType(
        ArabicWord(SAD, HHA, YA, HHA, SPACE, MEEM, DDAD, ALIF, FA)
      )

  case FIRST_RADICAL_HAMZA
      extends VerbType(
        ArabicWord(
          SAD,
          HHA,
          YA,
          HHA,
          SPACE,
          MEEM,
          HA,
          MEEM,
          WAW,
          ZAIN,
          SPACE,
          ALIF,
          LAM,
          FA,
          ALIF,
          HAMZA
        )
      )

  case SECOND_RADICAL_HAMZA
      extends VerbType(
        ArabicWord(
          SAD,
          HHA,
          YA,
          HHA,
          SPACE,
          MEEM,
          HA,
          MEEM,
          WAW,
          ZAIN,
          SPACE,
          ALIF,
          LAM,
          AIN,
          YA,
          NOON
        )
      )

  case THIRD_RADICAL_HAMZA
      extends VerbType(
        ArabicWord(
          SAD,
          HHA,
          YA,
          HHA,
          SPACE,
          MEEM,
          HA,
          MEEM,
          WAW,
          ZAIN,
          SPACE,
          ALIF,
          LAM,
          LAM,
          ALIF,
          MEEM
        )
      )

  case FIRST_RADICAL_WEAK
      extends VerbType(
        ArabicWord(
          MEEM,
          AIN,
          TA,
          LAM,
          SPACE,
          ALIF,
          LAM,
          FA,
          ALIF,
          HAMZA
        )
      )

  case SECOND_RADICAL_WEAK
      extends VerbType(
        ArabicWord(
          MEEM,
          AIN,
          TA,
          LAM,
          SPACE,
          ALIF,
          LAM,
          AIN,
          YA,
          NOON
        )
      )

  case THIRD_RADICAL_WEAK
      extends VerbType(
        ArabicWord(
          MEEM,
          AIN,
          TA,
          LAM,
          SPACE,
          ALIF,
          LAM,
          LAM,
          ALIF,
          MEEM
        )
      )

  case TWO_SEPARATE_RADICALS_WEAK
      extends VerbType(
        ArabicWord(LAM, FA, YA, FA, SPACE, MEEM, FA, RA, WAW, QAF)
      )

  case TWO_CONSECUTIVE_RADICALS_WEAK
      extends VerbType(
        ArabicWord(LAM, FA, YA, FA, SPACE, MEEM, QAF, RA, WAW, NOON)
      )
}
