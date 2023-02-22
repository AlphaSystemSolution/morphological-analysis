package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package noun

import arabic.model.ArabicLetters
import conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord

object FormII {

  case object MasculineActiveParticiple
      extends MasculineBasedNoun(
        RootWord(
          MorphologicalTermType.ActiveParticipleMasculine,
          1,
          2,
          3,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndKasra,
          ArabicLetters.LamWithDammatan
        )
      )

  case object FeminineActiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          MorphologicalTermType.ActiveParticipleFeminine,
          1,
          2,
          3,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndKasra,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  case object MasculinePassiveParticiple
      extends MasculineBasedNoun(
        RootWord(
          MorphologicalTermType.PassiveParticipleMasculine,
          1,
          2,
          3,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndFatha,
          ArabicLetters.LamWithDammatan
        )
      )

  case object FemininePassiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          MorphologicalTermType.PassiveParticipleFeminine,
          1,
          2,
          3,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )
}
