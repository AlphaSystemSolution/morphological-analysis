package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package noun

import arabic.model.ArabicLetters
import conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord

object FormIII {

  case object MasculineActiveParticiple
      extends MasculineBasedNoun(
        RootWord(
          MorphologicalTermType.ActiveParticipleMasculine,
          1,
          3,
          4,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDammatan
        )
      )

  case object FeminineActiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          MorphologicalTermType.ActiveParticipleFeminine,
          1,
          3,
          4,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  case object MasculinePassiveParticiple
      extends MasculineBasedNoun(
        RootWord(
          MorphologicalTermType.PassiveParticipleMasculine,
          1,
          3,
          4,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDammatan
        )
      )

  case object FemininePassiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          MorphologicalTermType.PassiveParticipleFeminine,
          1,
          3,
          4,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )
}
