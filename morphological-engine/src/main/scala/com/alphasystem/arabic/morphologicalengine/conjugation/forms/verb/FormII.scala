package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package verb

import arabic.model.ArabicLetters
import conjugation.model.internal.RootWord

object FormII {

  object PastTense
      extends PastTenseSupport(
        RootWord(0, 1, 2, ArabicLetters.FaWithFatha, ArabicLetters.AinWithShaddaAndFatha, ArabicLetters.LamWithFatha)
      )

  object PresentTense
      extends PresentTenseSupport(
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

  object PastPassiveTense
      extends PastTenseSupport(
        RootWord(0, 1, 2, ArabicLetters.FaWithDamma, ArabicLetters.AinWithShaddaAndKasra, ArabicLetters.LamWithFatha)
      )

  object PresentPassiveTense
      extends PresentTenseSupport(
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

  object Imperative
      extends ImperativeTenseSupport(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndKasra,
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
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndKasra,
          ArabicLetters.LamWithSukun
        )
      )
}
