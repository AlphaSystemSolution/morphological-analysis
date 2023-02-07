package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package verb

import arabic.model.ArabicLetters

import java.lang.Enum

enum FormX(override val rootWord: RootWord) extends Enum[FormX] with VerbSupport {

  case PastTense
      extends FormX(
        RootWord(
          3,
          4,
          5,
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.SeenWithSukun,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha
        )
      )

  case PresentTense
      extends FormX(
        RootWord(
          3,
          4,
          5,
          ArabicLetters.YaWithFatha,
          ArabicLetters.SeenWithSukun,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        )
      )

  case PastPassiveTense
      extends FormX(
        RootWord(
          3,
          4,
          5,
          ArabicLetters.AlifHamzaAboveWithDamma,
          ArabicLetters.SeenWithSukun,
          ArabicLetters.TaWithDamma,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha
        )
      )

  case PresentPassiveTense
      extends FormX(
        RootWord(
          3,
          4,
          5,
          ArabicLetters.YaWithDamma,
          ArabicLetters.SeenWithSukun,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDamma
        )
      )

  case Imperative
      extends FormX(
        RootWord(
          3,
          4,
          5,
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.SeenWithSukun,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithSukun
        )
      )

  case Forbidden
      extends FormX(
        RootWord(
          3,
          4,
          5,
          ArabicLetters.TaWithFatha,
          ArabicLetters.SeenWithSukun,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithSukun
        )
      )
  override def code: String = name()
}
