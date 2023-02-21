package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package verb

import arabic.model.ArabicLetters
import conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord

object FormIX {

  case object PastTense
      extends PastTenseSupport(
        RootWord(
          MorphologicalTermType.PastTense,
          1,
          2,
          3,
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithShaddaAndFatha
        )
      )

  case object PresentTense
      extends PresentTenseSupport(
        RootWord(
          MorphologicalTermType.PresentTense,
          1,
          2,
          3,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithShaddaAndFatha
        )
      )

  case object Imperative
      extends ImperativeTenseSupport(
        RootWord(
          MorphologicalTermType.Imperative,
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

  case object Forbidden
      extends ForbiddenTenseSupport(
        RootWord(
          MorphologicalTermType.Forbidden,
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
