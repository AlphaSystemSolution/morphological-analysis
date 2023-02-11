package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package verb

import arabic.model.ArabicLetters
import conjugation.model.internal.RootWord

object FormIX {

  object PastTense
      extends PastTenseSupport(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithShaddaAndFatha
        )
      )

  object PresentTense
      extends PresentTenseSupport(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithShaddaAndFatha
        )
      )

  object Imperative
      extends ImperativeTenseSupport(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithKasra,
          ArabicLetters.LamWithSukun
        )
      )

  object Forbidden
      extends ForbiddenTenseSupport(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithKasra,
          ArabicLetters.LamWithSukun
        )
      )
}
