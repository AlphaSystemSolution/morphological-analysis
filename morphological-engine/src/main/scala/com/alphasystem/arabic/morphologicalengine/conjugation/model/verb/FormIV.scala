package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package verb

import arabic.model.ArabicLetters
import conjugation.model.verb.*

object FormIV {

  object PastTense
      extends PastTenseSupport(
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

  object PresentTense
      extends PresentTenseSupport(
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

  object PastPassiveTense
      extends PastTenseSupport(
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

  object PresentPassiveTense
      extends PresentTenseSupport(
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

  object Imperative
      extends ImperativeTenseSupport(
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

  object Forbidden
      extends ForbiddenTenseSupport(
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
}
