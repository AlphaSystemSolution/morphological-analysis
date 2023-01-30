package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package verb

import arabic.model.ArabicLetters

import java.lang.Enum

enum FormIV(override val rootWord: RootWord) extends Enum[FormIV] with VerbSupport {

  case PastTense
      extends FormIV(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.AlifHamzaAboveWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha
        )
      )

  case PresentTense
      extends FormIV(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.YaWithDamma,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        )
      )

  case PastPassiveTense
      extends FormIV(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.AlifHamzaAboveWithDamma,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha
        )
      )

  case PresentPassiveTense
      extends FormIV(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.YaWithDamma,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDamma
        )
      )

  case Imperative
      extends FormIV(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.AlifHamzaAboveWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithSukun
        )
      )

  case Forbidden
      extends FormIV(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.TaWithDamma,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithSukun
        )
      )

  override def code: String = name()
}
