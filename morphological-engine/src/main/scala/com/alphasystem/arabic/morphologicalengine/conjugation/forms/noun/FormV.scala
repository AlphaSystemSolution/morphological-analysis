package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package noun

import arabic.model.ArabicLetters
import conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord

object FormV {

  case object MasculineActiveParticiple
      extends MasculineBasedNoun(
        RootWord(
          MorphologicalTermType.ActiveParticipleMasculine,
          2,
          3,
          4,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndKasra,
          ArabicLetters.LamWithDammatan
        )
      )

  case object FeminineActiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          MorphologicalTermType.ActiveParticipleFeminine,
          2,
          3,
          4,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.TaWithFatha,
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
          2,
          3,
          4,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndFatha,
          ArabicLetters.LamWithDammatan
        )
      )

  case object FemininePassiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          MorphologicalTermType.PassiveParticipleFeminine,
          2,
          3,
          4,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithShaddaAndFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )
}
