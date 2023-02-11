package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package verb

import arabic.model.ArabicLetters
import conjugation.model.internal.RootWord

object FormVIII {

  object PastTense
      extends PastTenseSupport(
        RootWord(
          1,
          3,
          4,
          ArabicLetters.HamzaWithKasra,
          ArabicLetters.FaWithSukun,
          ArabicLetters.TaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha
        )
      )

  object PresentTense
      extends PresentTenseSupport(
        RootWord(
          1,
          3,
          4,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.TaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        )
      )

  object PastPassiveTense
      extends PastTenseSupport(
        RootWord(
          1,
          3,
          4,
          ArabicLetters.HamzaWithDamma,
          ArabicLetters.FaWithSukun,
          ArabicLetters.TaWithDamma,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha
        )
      )

  object PresentPassiveTense
      extends PresentTenseSupport(
        RootWord(
          1,
          3,
          4,
          ArabicLetters.YaWithDamma,
          ArabicLetters.FaWithSukun,
          ArabicLetters.TaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDamma
        )
      )

  object Imperative
      extends ImperativeTenseSupport(
        RootWord(
          1,
          3,
          4,
          ArabicLetters.HamzaWithKasra,
          ArabicLetters.FaWithSukun,
          ArabicLetters.TaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithSukun
        )
      )

  object Forbidden
      extends ForbiddenTenseSupport(
        RootWord(
          1,
          3,
          4,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.TaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithSukun
        )
      )
}
