package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package verb

import arabic.model.ArabicLetters

import java.lang.Enum

enum FormII(override val rootWord: RootWord) extends Enum[FormII] with VerbSupport {

  case PastTense
      extends FormII(
        RootWord(0, 1, 2, ArabicLetters.FaWithFatha, ArabicLetters.AinWithShaddaAndFatha, ArabicLetters.LamWithFatha)
      )

  case PresentTense
      extends FormII(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.YaWithDamma,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndKasra,
          ArabicLetters.LamWithDamma
        )
      )

  case PastPassiveTense
      extends FormII(
        RootWord(0, 1, 2, ArabicLetters.FaWithDamma, ArabicLetters.AinWithShaddaAndKasra, ArabicLetters.LamWithFatha)
      )

  case PresentPassiveTense
      extends FormII(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.YaWithDamma,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndFatha,
          ArabicLetters.LamWithDamma
        )
      )

  case Imperative
      extends FormII(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndKasra,
          ArabicLetters.LamWithSukun
        )
      )

  case Forbidden
      extends FormII(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.TaWithDamma,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndKasra,
          ArabicLetters.LamWithSukun
        )
      )

  override def code: String = name()
}
