package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package noun

import arabic.model.ArabicLetters
import conjugation.model.internal.RootWord

object FormIV {

  case object MasculineActiveParticiple
      extends MasculineBasedNoun(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDammatan
        )
      )

  case object FeminineActiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  case object MasculinePassiveParticiple
      extends MasculineBasedNoun(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDammatan
        )
      )

  case object FemininePassiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )
}
