package com.alphasystem
package arabic
package model

import ArabicLetterType.*
import DiacriticType.*

import java.lang.Enum
enum ProNoun(val label: ArabicWord) extends Enum[ProNoun] {

  case ThirdPersonMasculineSingular
      extends ProNoun(
        ArabicWord(
          ArabicLetter(Ha, Damma),
          ArabicLetter(Waw, Fatha)
        )
      )

  case ThirdPersonMasculineDual
      extends ProNoun(
        ArabicWord(
          ArabicLetter(Ha, Damma),
          ArabicLetter(Meem, Fatha),
          ArabicLetter(Alif)
        )
      )

  case ThirdPersonMasculinePlural
      extends ProNoun(
        ArabicWord(
          ArabicLetter(Ha, Damma),
          ArabicLetter(Meem, Sukun)
        )
      )

  case ThirdPersonFeminineSingular
      extends ProNoun(
        ArabicWord(
          ArabicLetter(Ha, Kasra),
          ArabicLetter(Ya, Fatha)
        )
      )

  case ThirdPersonFeminineDual
      extends ProNoun(
        ArabicWord(
          ArabicLetter(Ha, Damma),
          ArabicLetter(Meem, Fatha),
          ArabicLetter(Alif)
        )
      )

  case ThirdPersonFemininePlural
      extends ProNoun(
        ArabicWord(
          ArabicLetter(Ha, Damma),
          ArabicLetter(Noon, Shadda, Fatha)
        )
      )

  case SecondPersonMasculineSingular
      extends ProNoun(
        ArabicWord(
          ArabicLetter(AlifHamzaAbove, Fatha),
          ArabicLetter(Noon, Sukun),
          ArabicLetter(Ta, Fatha)
        )
      )

  case SecondPersonMasculineDual
      extends ProNoun(
        ArabicWord(
          ArabicLetter(AlifHamzaAbove, Fatha),
          ArabicLetter(Noon, Sukun),
          ArabicLetter(Ta, Damma),
          ArabicLetter(Meem, Fatha),
          ArabicLetter(Alif)
        )
      )

  case SecondPersonMasculinePlural
      extends ProNoun(
        ArabicWord(
          ArabicLetter(AlifHamzaAbove, Fatha),
          ArabicLetter(Noon, Sukun),
          ArabicLetter(Ta, Damma),
          ArabicLetter(Meem, Sukun)
        )
      )

  case SecondPersonFeminineSingular
      extends ProNoun(
        ArabicWord(
          ArabicLetter(AlifHamzaAbove, Fatha),
          ArabicLetter(Noon, Sukun),
          ArabicLetter(Ta, Kasra)
        )
      )

  case SecondPersonFeminineDual
      extends ProNoun(
        ArabicWord(
          ArabicLetter(AlifHamzaAbove, Fatha),
          ArabicLetter(Noon, Sukun),
          ArabicLetter(Ta, Damma),
          ArabicLetter(Meem, Fatha),
          ArabicLetter(Alif)
        )
      )

  case SecondPersonFemininePlural
      extends ProNoun(
        ArabicWord(
          ArabicLetter(AlifHamzaAbove, Fatha),
          ArabicLetter(Noon, Sukun),
          ArabicLetter(Ta, Damma),
          ArabicLetter(Noon, Shadda, Fatha)
        )
      )

  case FirstPersonSingular
      extends ProNoun(
        ArabicWord(
          ArabicLetter(AlifHamzaAbove, Fatha),
          ArabicLetter(Noon, Fatha),
          ArabicLetter(Alif)
        )
      )

  case FirstPersonPlural
      extends ProNoun(
        ArabicWord(
          ArabicLetter(Noon, Fatha),
          ArabicLetter(Hha, Sukun),
          ArabicLetter(Noon, Damma)
        )
      )
}
