package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package verb

import arabic.model.ArabicLetters
import conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord

object FormI {

  case object PastTenseV1
      extends PastTenseSupport(
        RootWord(
          MorphologicalTermType.PastTense,
          0,
          1,
          2,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha
        )
      )

  case object PastTenseV2
      extends PastTenseSupport(
        RootWord(
          MorphologicalTermType.PastTense,
          0,
          1,
          2,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha
        )
      )

  case object PastTenseV3
      extends PastTenseSupport(
        RootWord(
          MorphologicalTermType.PastTense,
          0,
          1,
          2,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithDamma,
          ArabicLetters.LamWithFatha
        )
      )

  case object PresentTenseV1
      extends PresentTenseSupport(
        RootWord(
          MorphologicalTermType.PresentTense,
          1,
          2,
          3,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithDamma,
          ArabicLetters.LamWithDamma
        )
      )

  case object PresentTenseV2
      extends PresentTenseSupport(
        RootWord(
          MorphologicalTermType.PresentTense,
          1,
          2,
          3,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        )
      )

  case object PresentTenseV3
      extends PresentTenseSupport(
        RootWord(
          MorphologicalTermType.PresentTense,
          1,
          2,
          3,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDamma
        )
      )

  case object PastPassiveTense
      extends PastTenseSupport(
        RootWord(
          MorphologicalTermType.PastPassiveTense,
          0,
          1,
          2,
          ArabicLetters.FaWithDamma,
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

  case object ImperativeV1
      extends ImperativeTenseSupport(
        RootWord(
          MorphologicalTermType.Imperative,
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithDamma,
          ArabicLetters.LamWithDamma
        )
      )

  case object ImperativeV2
      extends ImperativeTenseSupport(
        RootWord(
          MorphologicalTermType.Imperative,
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        )
      )

  case object ImperativeV3
      extends ImperativeTenseSupport(
        RootWord(
          MorphologicalTermType.Imperative,
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDamma
        )
      )

  case object ForbiddenV1
      extends ForbiddenTenseSupport(
        RootWord(
          MorphologicalTermType.Forbidden,
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithDamma,
          ArabicLetters.LamWithDamma
        )
      )

  case object ForbiddenV2
      extends ForbiddenTenseSupport(
        RootWord(
          MorphologicalTermType.Forbidden,
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        )
      )

  case object ForbiddenV3
      extends ForbiddenTenseSupport(
        RootWord(
          MorphologicalTermType.Forbidden,
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDamma
        )
      )

}
