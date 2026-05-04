package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package noun

import arabic.model.ArabicLetters
import com.alphasystem.arabic.morphologicalengine.conjugation.transformer.noun.AbstractNounTransformer.PluralType
import conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord

object FormVII {

  case object MasculineActiveParticiple
      extends MasculineBasedNoun(
        RootWord(
          MorphologicalTermType.ActiveParticipleMasculine,
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

  case object FeminineActiveParticiple
      extends FeminineBasedNoun(
        RootWord(
          MorphologicalTermType.ActiveParticipleFeminine,
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

  case object NounOfPlaceAndTime
      extends MasculineBasedNoun(
        RootWord(
          MorphologicalTermType.NounOfPlaceAndTime,
          2,
          3,
          4,
          ArabicLetters.MeemWithDamma,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )
}
