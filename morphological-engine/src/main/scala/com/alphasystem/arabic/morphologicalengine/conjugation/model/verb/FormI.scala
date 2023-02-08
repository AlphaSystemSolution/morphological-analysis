package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package verb

import arabic.model.ArabicLetters
import conjugation.model.verb.*

object FormI {

  object PastTenseV1
      extends PastTenseSupport(
        RootWord(0, 1, 2, ArabicLetters.FaWithFatha, ArabicLetters.AinWithFatha, ArabicLetters.LamWithFatha)
      )

  object PastTenseV2
      extends PastTenseSupport(
        RootWord(0, 1, 2, ArabicLetters.FaWithFatha, ArabicLetters.AinWithKasra, ArabicLetters.LamWithFatha)
      )

  object PastTenseV3
      extends PastTenseSupport(
        RootWord(0, 1, 2, ArabicLetters.FaWithFatha, ArabicLetters.AinWithDamma, ArabicLetters.LamWithFatha)
      )

  object PresentTenseV1
      extends PresentTenseSupport(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithDamma,
          ArabicLetters.LamWithDamma
        )
      )

  object PresentTenseV2
      extends PresentTenseSupport(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDamma
        )
      )

  object PresentTenseV3
      extends PresentTenseSupport(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.YaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDamma
        )
      )

  object PastPassiveTense
      extends PastTenseSupport(
        RootWord(0, 1, 2, ArabicLetters.FaWithDamma, ArabicLetters.AinWithKasra, ArabicLetters.LamWithFatha)
      )

  object PresentPassiveTense
      extends PresentTenseSupport(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.YaWithDamma,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDamma
        )
      )

  object ImperativeV1
      extends ImperativeTenseSupport(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.AlifHamzaAboveWithDamma,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithDamma,
          ArabicLetters.LamWithSukun
        )
      )

  object ImperativeV2
      extends ImperativeTenseSupport(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithSukun
        )
      )

  object ImperativeV3
      extends ImperativeTenseSupport(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithSukun
        )
      )

  object ForbiddenV1
      extends ForbiddenTenseSupport(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithDamma,
          ArabicLetters.LamWithSukun
        )
      )

  object ForbiddenV2
      extends ForbiddenTenseSupport(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithSukun
        )
      )

  object ForbiddenV3
      extends ForbiddenTenseSupport(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithSukun
        )
      )

}
