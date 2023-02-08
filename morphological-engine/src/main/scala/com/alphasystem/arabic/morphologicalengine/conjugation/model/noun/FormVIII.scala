package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package noun

import arabic.model.ArabicLetters

object FormVIII {

  object MasculineActiveParticiple
    extends MasculineBasedNoun(
      RootWord(
        1,
        3,
        4,
        ArabicLetters.MeemWithDamma,
        ArabicLetters.FaWithSukun,
        ArabicLetters.TaWithFatha,
        ArabicLetters.AinWithKasra,
        ArabicLetters.LamWithDammatan
      )
    )

  object FeminineActiveParticiple
    extends FeminineBasedNoun(
      RootWord(
        1,
        3,
        4,
        ArabicLetters.MeemWithDamma,
        ArabicLetters.FaWithSukun,
        ArabicLetters.TaWithFatha,
        ArabicLetters.AinWithKasra,
        ArabicLetters.LamWithFatha,
        ArabicLetters.TaMarbutaWithDammatan
      )
    )

  object MasculinePassiveParticiple
    extends MasculineBasedNoun(
      RootWord(
        1,
        3,
        4,
        ArabicLetters.MeemWithDamma,
        ArabicLetters.FaWithSukun,
        ArabicLetters.TaWithFatha,
        ArabicLetters.AinWithFatha,
        ArabicLetters.LamWithDammatan
      )
    )

  object FemininePassiveParticiple
    extends FeminineBasedNoun(
      RootWord(
        1,
        3,
        4,
        ArabicLetters.MeemWithDamma,
        ArabicLetters.FaWithSukun,
        ArabicLetters.TaWithFatha,
        ArabicLetters.AinWithFatha,
        ArabicLetters.LamWithFatha,
        ArabicLetters.TaMarbutaWithDammatan
      )
    )
}
