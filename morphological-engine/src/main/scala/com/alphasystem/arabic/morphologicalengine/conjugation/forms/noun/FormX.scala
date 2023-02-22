package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package noun

import arabic.model.ArabicLetters
import conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord

object FormX {

  case object MasculineActiveParticiple
      extends MasculineBasedNoun(
        RootWord(
          MorphologicalTermType.ActiveParticipleMasculine,
          3,
          4,
          5,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.SeenWithSukun,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDammatan
        )
      )

  case object FeminineActiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          MorphologicalTermType.ActiveParticipleFeminine,
          3,
          4,
          5,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.SeenWithSukun,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        ),
        feminine = true
      )

  case object MasculinePassiveParticiple
      extends MasculineBasedNoun(
        RootWord(
          MorphologicalTermType.PassiveParticipleMasculine,
          3,
          4,
          5,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.SeenWithSukun,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDammatan
        )
      )

  case object FemininePassiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          MorphologicalTermType.PassiveParticipleFeminine,
          3,
          4,
          5,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.SeenWithSukun,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )
}
