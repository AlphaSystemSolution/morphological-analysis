package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package noun

import arabic.model.ArabicLetters

object FormVII {

  object MasculineActiveParticiple
      extends MasculineBasedNoun(
        RootWord(
          2,
          3,
          4,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDammatan
        )
      )

  object FeminineActiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          2,
          3,
          4,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  object FemininePassiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          2,
          3,
          4,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )
}
