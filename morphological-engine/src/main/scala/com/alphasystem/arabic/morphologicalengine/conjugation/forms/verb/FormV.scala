package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package verb

import arabic.model.ArabicLetters
import conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord

object FormV {

  case object PastTense
      extends PastTenseSupport(
        RootWord(
          MorphologicalTermType.PastTense,
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndFatha,
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
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndFatha,
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
          ArabicLetters.TaWithDamma,
          ArabicLetters.FaWithDamma,
          ArabicLetters.AinWithShaddaAndKasra,
          ArabicLetters.LamWithFatha
        )
      )

  case object PresentPassiveTense
      extends PresentTenseSupport(
        RootWord(
          MorphologicalTermType.PresentPassiveTense,
          2,
          3,
          4,
          ArabicLetters.YaWithDamma,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndFatha,
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
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndFatha,
          ArabicLetters.LamWithSukun
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
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndFatha,
          ArabicLetters.LamWithSukun
        )
      )
}
