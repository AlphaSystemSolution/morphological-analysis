package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package verb

import arabic.model.ArabicLetters

import java.lang.Enum

enum FormIII(override val rootWord: RootWord) extends Enum[FormIII] with VerbSupport {

  case PastTense
      extends FormIII(
        RootWord(
          0,
          2,
          3,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha
        )
      )

  case PresentTense
      extends FormIII(
        RootWord(
          1,
          3,
          4,
          ArabicLetters.YaWithDamma,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        )
      )

  case PastPassiveTense
      extends FormIII(
        RootWord(
          0,
          2,
          3,
          ArabicLetters.FaWithDamma,
          ArabicLetters.LetterWaw,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha
        )
      )

  case PresentPassiveTense
      extends FormIII(
        RootWord(
          1,
          3,
          4,
          ArabicLetters.YaWithDamma,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDamma
        )
      )

  case Imperative
      extends FormIII(
        RootWord(
          0,
          2,
          3,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithSukun
        )
      )

  case Forbidden
      extends FormIII(
        RootWord(
          1,
          3,
          4,
          ArabicLetters.TaWithDamma,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithSukun
        )
      )

  override def code: String = name()
}
