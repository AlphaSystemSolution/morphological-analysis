package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package verb

import arabic.model.ArabicLetters
import conjugation.model.RootWord

object FormIII {

  object PastTense
      extends PastTenseSupport(
        RootWord(
          0,
          2,
          3,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
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
          ArabicLetters.YaWithDamma,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        )
      )

  object PastPassiveTense
      extends PastTenseSupport(
        RootWord(
          0,
          2,
          3,
          ArabicLetters.FaWithDamma,
          ArabicLetters.LetterWaw,
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
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDamma
        )
      )

  object Imperative
      extends ImperativeTenseSupport(
        RootWord(
          0,
          2,
          3,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
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
          ArabicLetters.TaWithDamma,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithSukun
        )
      )
}
