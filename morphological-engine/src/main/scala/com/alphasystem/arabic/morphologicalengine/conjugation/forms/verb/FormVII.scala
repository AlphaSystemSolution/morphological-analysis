package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package verb

import arabic.model.ArabicLetters
import conjugation.model.internal.RootWord

object FormVII {

  case object PastTense
      extends PastTenseSupport(
        RootWord(
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
          2,
          3,
          4,
          ArabicLetters.HamzaWithKasra,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithSukun
        )
      )

  case object Forbidden
      extends ForbiddenTenseSupport(
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
}
