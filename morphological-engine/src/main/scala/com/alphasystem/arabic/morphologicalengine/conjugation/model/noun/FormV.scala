package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package noun

import arabic.model.ArabicLetters

object FormV {

  object MasculineActiveParticiple
      extends MasculineBasedNoun(
        RootWord(
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

  object FeminineActiveParticiple
      extends FeminineBasedNoun(
        RootWord(
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

  object MasculinePassiveParticiple
      extends MasculineBasedNoun(
        RootWord(
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

  object FemininePassiveParticiple
      extends FeminineBasedNoun(
        RootWord(
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
