package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package verb

import arabic.model.ArabicLetters

import java.lang.Enum

enum FormI(override val rootWord: RootWord) extends Enum[FormI] with VerbSupport {

  case PastTenseV1
      extends FormI(
        RootWord(0, 1, 2, ArabicLetters.FaWithFatha, ArabicLetters.AinWithFatha, ArabicLetters.LamWithFatha)
      )

  case PastTenseV2
      extends FormI(
        RootWord(0, 1, 2, ArabicLetters.FaWithFatha, ArabicLetters.AinWithKasra, ArabicLetters.LamWithFatha)
      )

  case PastTenseV3
      extends FormI(
        RootWord(0, 1, 2, ArabicLetters.FaWithFatha, ArabicLetters.AinWithDamma, ArabicLetters.LamWithFatha)
      )

  case PresentTenseV1
      extends FormI(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithDamma,
          ArabicLetters.LamWithDamma
        )
      )

  case PresentTenseV2
      extends FormI(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        )
      )

  case PresentTenseV3
      extends FormI(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDamma
        )
      )

  case PastPassiveTense
      extends FormI(
        RootWord(0, 1, 2, ArabicLetters.FaWithDamma, ArabicLetters.AinWithKasra, ArabicLetters.LamWithFatha)
      )

  case PresentPassiveTense
      extends FormI(
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

  case ImperativeV1
      extends FormI(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.AlifHamzaAboveWithDamma,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithDamma,
          ArabicLetters.LamWithSukun
        )
      )

  case ImperativeV2
      extends FormI(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithSukun
        )
      )

  case ImperativeV3
      extends FormI(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithSukun
        )
      )

  case ForbiddenV1
      extends FormI(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithDamma,
          ArabicLetters.LamWithSukun
        )
      )

  case ForbiddenV2
      extends FormI(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithSukun
        )
      )

  case ForbiddenV3
      extends FormI(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithSukun
        )
      )

  override def code: String = name()
}
