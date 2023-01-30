package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package verb

import com.alphasystem.arabic.model.ArabicLetters

import java.lang.Enum

enum FormV(override val rootWord: RootWord) extends Enum[FormV] with VerbSupport {

  case PastTense
      extends FormV(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndFatha,
          ArabicLetters.LamWithFatha
        )
      )

  case PresentTense
      extends FormV(
        RootWord(
          2,
          3,
          4,
          ArabicLetters.YaWithFatha,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndFatha,
          ArabicLetters.LamWithDamma
        )
      )

  case PastPassiveTense
      extends FormV(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.TaWithDamma,
          ArabicLetters.FaWithDamma,
          ArabicLetters.AinWithShaddaAndKasra,
          ArabicLetters.LamWithFatha
        )
      )

  case PresentPassiveTense
      extends FormV(
        RootWord(
          2,
          3,
          4,
          ArabicLetters.YaWithDamma,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndFatha,
          ArabicLetters.LamWithDamma
        )
      )

  case Imperative
      extends FormV(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndFatha,
          ArabicLetters.LamWithSukun
        )
      )

  case Forbidden
      extends FormV(
        RootWord(
          2,
          3,
          4,
          ArabicLetters.TaWithFatha,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndFatha,
          ArabicLetters.LamWithSukun
        )
      )

  override def code: String = name()
}
