package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package verb

import arabic.model.ArabicLetters
import conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord

object FormIV {

  case object PastTense
      extends PastTenseSupport(
        RootWord(
          MorphologicalTermType.PastTense,
          1,
          2,
          3,
          ArabicLetters.HamzaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha
        )
      )

  case object PresentTense
      extends PresentTenseSupport(
        RootWord(
          MorphologicalTermType.PresentTense,
          1,
          2,
          3,
          ArabicLetters.YaWithDamma,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        )
      )

  case object PastPassiveTense
      extends PastTenseSupport(
        RootWord(
          MorphologicalTermType.PastPassiveTense,
          1,
          2,
          3,
          ArabicLetters.HamzaWithDamma,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha
        )
      )

  case object PresentPassiveTense
      extends PresentTenseSupport(
        RootWord(
          MorphologicalTermType.PresentPassiveTense,
          1,
          2,
          3,
          ArabicLetters.YaWithDamma,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDamma
        )
      )

  case object Imperative
      extends ImperativeTenseSupport(
        RootWord(
          MorphologicalTermType.Imperative,
          1,
          2,
          3,
          ArabicLetters.TaWithDamma,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithSukun
        )
      )

  case object Forbidden
      extends ForbiddenTenseSupport(
        RootWord(
          MorphologicalTermType.Forbidden,
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
