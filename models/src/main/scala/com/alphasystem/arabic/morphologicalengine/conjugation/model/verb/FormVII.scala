package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package verb

import arabic.model.ArabicLetters

import java.lang.Enum

enum FormVII(override val rootWord: RootWord) extends Enum[FormVII] with VerbSupport {

  case PastTense
      extends FormVII(
        RootWord(
          2,
          3,
          4,
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha
        )
      )

  case PresentTense
      extends FormVII(
        RootWord(
          2,
          3,
          4,
          ArabicLetters.YaWithFatha,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        )
      )

  case Imperative
      extends FormVII(
        RootWord(
          2,
          3,
          4,
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithSukun
        )
      )

  case Forbidden
      extends FormVII(
        RootWord(
          2,
          3,
          4,
          ArabicLetters.TaWithFatha,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithSukun
        )
      )

  override def code: String = name()
}
