package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package noun

import arabic.model.ArabicLetters
import conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord
import conjugation.transformer.noun.AbstractNounTransformer.PluralType

object FormI {

  case object MasculineActiveParticiple
      extends MasculineBasedNoun(
        RootWord(
          MorphologicalTermType.ActiveParticipleMasculine,
          0,
          2,
          3,
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

  case object MasculinePassiveParticiple
      extends MasculineBasedNoun(
        RootWord(
          MorphologicalTermType.PassiveParticipleMasculine,
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

  case object FemininePassiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          MorphologicalTermType.PassiveParticipleFeminine,
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

  case object Category6MasculineActiveParticiple
      extends MasculineBasedNoun(
        RootWord(
          MorphologicalTermType.ActiveParticipleMasculine,
          0,
          1,
          3,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.YaWithSukun,
          ArabicLetters.LamWithDammatan
        )
      )

  case object Category6FeminineActiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          MorphologicalTermType.ActiveParticipleFeminine,
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

  case object NounOfPlaceAndTimeV1
      extends MasculineBasedNoun(
        RootWord(
          MorphologicalTermType.NounOfPlaceAndTime,
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

  case object NounOfPlaceAndTimeV2
      extends MasculineBasedNoun(
        RootWord(
          MorphologicalTermType.NounOfPlaceAndTime,
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

  case object NounOfPlaceAndTimeV3
      extends FeminineBasedNoun(
        RootWord(
          MorphologicalTermType.NounOfPlaceAndTime,
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
