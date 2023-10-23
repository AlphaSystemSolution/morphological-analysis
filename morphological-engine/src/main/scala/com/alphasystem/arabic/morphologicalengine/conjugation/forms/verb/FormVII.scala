package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package verb

import arabic.model.ArabicLetters
import conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord

object FormVII {

  case object PastTense
      extends PastTenseSupport(
        RootWord(
          MorphologicalTermType.PastTense,
          2,
          3,
          4,
          ArabicLetters.HamzaWithKasra,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha
        )
      )

  case object PresentTense
      extends PresentTenseSupport(
        RootWord(
          MorphologicalTermType.PresentTense,
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

  case object Imperative
      extends ImperativeTenseSupport(
        RootWord(
          MorphologicalTermType.Imperative,
          2,
          3,
          4,
          ArabicLetters.TaWithFatha,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        )
      )

  case object Forbidden
      extends ForbiddenTenseSupport(
        RootWord(
          MorphologicalTermType.Forbidden,
          2,
          3,
          4,
          ArabicLetters.TaWithFatha,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        )
      )
}
