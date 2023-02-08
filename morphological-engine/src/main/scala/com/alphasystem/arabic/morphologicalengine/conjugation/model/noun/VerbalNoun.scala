package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package noun

import arabic.model.ArabicLetters
import conjugation.transformer.noun.AbstractNounTransformer.PluralType
import morphologicalanalysis.morphology.model.Flexibility

object VerbalNoun {

  object FormIV1
      extends MasculineBasedNoun(
        RootWord(0, 1, 2, ArabicLetters.FaWithFatha, ArabicLetters.AinWithSukun, ArabicLetters.LamWithDammatan),
        pluralType = PluralType.Feminine
      )

  object FormIV2
      extends MasculineBasedNoun(
        RootWord(0, 1, 2, ArabicLetters.FaWithDamma, ArabicLetters.AinWithDamma, ArabicLetters.LamWithDammatan)
      )

  object FormIV3
      extends MasculineBasedNoun(
        RootWord(0, 1, 2, ArabicLetters.FaWithDamma, ArabicLetters.AinWithSukun, ArabicLetters.LamWithDammatan),
        pluralType = PluralType.Feminine
      )

  object FormIV4
      extends MasculineBasedNoun(
        RootWord(0, 1, 2, ArabicLetters.FaWithFatha, ArabicLetters.AinWithFatha, ArabicLetters.LamWithDammatan),
        pluralType = PluralType.Feminine
      )

  object FormIV5
      extends MasculineBasedNoun(
        RootWord(0, 1, 2, ArabicLetters.FaWithFatha, ArabicLetters.AinWithKasra, ArabicLetters.LamWithDammatan),
        pluralType = PluralType.Feminine
      )

  object FormIV6
      extends MasculineBasedNoun(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithDamma,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  object FormIV7
      extends MasculineBasedNoun(
        RootWord(0, 1, 2, ArabicLetters.FaWithKasra, ArabicLetters.AinWithSukun, ArabicLetters.LamWithDammatan),
        pluralType = PluralType.Feminine
      )

  object FormIV8
      extends MasculineBasedNoun(
        RootWord(0, 1, 2, ArabicLetters.FaWithKasra, ArabicLetters.AinWithFatha, ArabicLetters.LamWithDammatan),
        pluralType = PluralType.Feminine
      )

  object FormIV9
      extends FeminineBasedNoun(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  object FormIV10
      extends FeminineBasedNoun(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithKasra,
          ArabicLetters.AinWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  object FormIV11
      extends FeminineBasedNoun(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithDamma,
          ArabicLetters.AinWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  object FormIV12
      extends FeminineBasedNoun(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  object FormIV13
      extends FeminineBasedNoun(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  object FormIV14
      extends FeminineBasedNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  object FormIV15
      extends FeminineBasedNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithKasra,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  object FormIV16
      extends MasculineBasedNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithDamma,
          ArabicLetters.AinWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  object FormIV17
      extends MasculineBasedNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  object FormIV18
      extends FeminineBasedNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  object FormIV19
      extends MasculineBasedNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  object FormIV20
      extends MasculineBasedNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithKasra,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  object FormIV21
      extends MasculineBasedNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithDamma,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  object FormIV22
      extends MasculineBasedNoun(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithKasra,
          ArabicLetters.AinWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterAlifMaksura
        ),
        flexibility = Flexibility.NonFlexible
      )

  object FormIV23
      extends MasculineBasedNoun(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterAlifMaksura
        ),
        flexibility = Flexibility.NonFlexible
      )

  object FormIV24
      extends MasculineBasedNoun(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithDamma,
          ArabicLetters.AinWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterAlifMaksura
        ),
        flexibility = Flexibility.NonFlexible
      )

  object FormIV25
      extends MasculineBasedNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.YaWithSukun,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  object FormIV26
      extends MasculineBasedNoun(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithDamma,
          ArabicLetters.AinWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.NoonWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  object FormIV27
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

  object FormIV28
      extends FeminineBasedNoun(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.MeemWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  object FormII
      extends MasculineBasedNoun(
        RootWord(
          1,
          2,
          4,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.YaWithSukun,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  object FormIIDefectiveVerb
      extends FeminineBasedNoun(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  object FormIIIV1
      extends MasculineBasedNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithKasra,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  object FormIIIV2
      extends FeminineBasedNoun(
        RootWord(
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

  object FormIIIDefectiveVerb
      extends FeminineBasedNoun(
        RootWord(
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

  object FormIV
      extends MasculineBasedNoun(
        RootWord(
          1,
          2,
          4,
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  object FormV
      extends MasculineBasedNoun(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithShaddaAndDamma,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  object FormVI
      extends MasculineBasedNoun(
        RootWord(
          1,
          3,
          4,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithDamma,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  object FormVII
      extends MasculineBasedNoun(
        RootWord(
          2,
          3,
          5,
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.FaWithKasra,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  object FormVIII
      extends MasculineBasedNoun(
        RootWord(
          1,
          3,
          5,
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.FaWithSukun,
          ArabicLetters.TaWithKasra,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  object FormX
      extends MasculineBasedNoun(
        RootWord(
          3,
          4,
          6,
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.SeenWithSukun,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )
}
