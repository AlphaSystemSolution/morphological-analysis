package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package noun

import arabic.model.ArabicLetters
import conjugation.transformer.noun.AbstractNounTransformer.PluralType

object FormI {

  object FormIMasculineActiveParticiple
      extends MasculineBasedNoun(
        RootWord(
          0,
          2,
          3,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDammatan
        )
      )

  object FormIFeminineActiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          0,
          2,
          3,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  object FormIMasculinePassiveParticiple
      extends MasculineBasedNoun(
        RootWord(
          1,
          2,
          4,
          ArabicLetters.MeemWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.LamWithDammatan
        )
      )

  object FormIFemininePassiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          1,
          2,
          4,
          ArabicLetters.MeemWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  object FormICategory6MasculineActiveParticiple
      extends MasculineBasedNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.YaWithSukun,
          ArabicLetters.LamWithDammatan
        )
      )

  object FormICategory6FeminineActiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.YaWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  object NounOfPlaceAndTimeV1
      extends MasculineBasedNoun(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.MeemWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  object NounOfPlaceAndTimeV2
      extends MasculineBasedNoun(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.MeemWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  object NounOfPlaceAndTimeV3
      extends FeminineBasedNoun(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.MeemWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )
}
