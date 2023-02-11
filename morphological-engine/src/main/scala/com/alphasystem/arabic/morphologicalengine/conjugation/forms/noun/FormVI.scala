package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package noun

import arabic.model.ArabicLetters
import conjugation.model.internal.RootWord

object FormVI {

  object MasculineActiveParticiple
      extends MasculineBasedNoun(
        RootWord(
          2,
          4,
          5,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDammatan
        )
      )

  object FeminineActiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          2,
          4,
          5,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  object MasculinePassiveParticiple
      extends MasculineBasedNoun(
        RootWord(
          2,
          4,
          5,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDammatan
        )
      )

  object FemininePassiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          2,
          4,
          5,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )
}
