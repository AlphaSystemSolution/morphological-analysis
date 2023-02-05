package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package noun

import arabic.model.ArabicLetters
import morphologicalanalysis.morphology.model.Flexibility

import java.lang.Enum

enum VerbalNoun(
  override val rootWord: RootWord,
  override val feminine: Boolean = false,
  override val flexibility: Flexibility = Flexibility.FullyFlexible)
    extends Enum[VerbalNoun]
    with NounSupport {

  case FormIV1
      extends VerbalNoun(
        RootWord(0, 1, 2, ArabicLetters.FaWithFatha, ArabicLetters.AinWithSukun, ArabicLetters.LamWithDammatan)
      )

  case FormIV2
      extends VerbalNoun(
        RootWord(0, 1, 2, ArabicLetters.FaWithDamma, ArabicLetters.AinWithDamma, ArabicLetters.LamWithDammatan)
      )

  case FormIV3
      extends VerbalNoun(
        RootWord(0, 1, 2, ArabicLetters.FaWithDamma, ArabicLetters.AinWithSukun, ArabicLetters.LamWithDammatan)
      )

  case FormIV4
      extends VerbalNoun(
        RootWord(0, 1, 2, ArabicLetters.FaWithFatha, ArabicLetters.AinWithFatha, ArabicLetters.LamWithDammatan)
      )

  case FormIV5
      extends VerbalNoun(
        RootWord(0, 1, 2, ArabicLetters.FaWithFatha, ArabicLetters.AinWithKasra, ArabicLetters.LamWithDammatan)
      )

  case FormIV6
      extends VerbalNoun(
        RootWord(0, 1, 2, ArabicLetters.FaWithDamma, ArabicLetters.AinWithFatha, ArabicLetters.LamWithDammatan)
      )

  case FormIV7
      extends VerbalNoun(
        RootWord(0, 1, 2, ArabicLetters.FaWithKasra, ArabicLetters.AinWithSukun, ArabicLetters.LamWithDammatan)
      )

  case FormIV8
      extends VerbalNoun(
        RootWord(0, 1, 2, ArabicLetters.FaWithKasra, ArabicLetters.AinWithFatha, ArabicLetters.LamWithDammatan)
      )

  case FormIV9
      extends VerbalNoun(
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

  case FormIV10
      extends VerbalNoun(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithKasra,
          ArabicLetters.AinWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        ),
        feminine = true
      )

  case FormIV11
      extends VerbalNoun(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithDamma,
          ArabicLetters.AinWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        ),
        feminine = true
      )

  case FormIV12
      extends VerbalNoun(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        ),
        feminine = true
      )

  case FormIV13
      extends VerbalNoun(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        ),
        feminine = true
      )

  case FormIV14
      extends VerbalNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        ),
        feminine = true
      )

  case FormIV15
      extends VerbalNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithKasra,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        ),
        feminine = true
      )

  case FormIV16
      extends VerbalNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithDamma,
          ArabicLetters.AinWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.LamWithDammatan
        )
      )

  case FormIV17
      extends VerbalNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.LamWithDammatan
        )
      )

  case FormIV18
      extends VerbalNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        ),
        feminine = true
      )

  case FormIV19
      extends VerbalNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.LamWithDammatan
        )
      )

  case FormIV20
      extends VerbalNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithKasra,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.LamWithDammatan
        )
      )

  case FormIV21
      extends VerbalNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithDamma,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.LamWithDammatan
        )
      )

  case FormIV22
      extends VerbalNoun(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithKasra,
          ArabicLetters.AinWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterAlifMaksura
        )
      )

  case FormIV23
      extends VerbalNoun(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterAlifMaksura
        )
      )

  case FormIV24
      extends VerbalNoun(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithDamma,
          ArabicLetters.AinWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterAlifMaksura
        )
      )

  case FormIV25
      extends VerbalNoun(
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

  case FormIV26
      extends VerbalNoun(
        RootWord(
          0,
          1,
          2,
          ArabicLetters.FaWithDamma,
          ArabicLetters.AinWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.NoonWithDammatan
        )
      )

  case FormIV27
      extends VerbalNoun(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.MeemWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        ),
        feminine = true
      )

  case FormIV28
      extends VerbalNoun(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.MeemWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        ),
        feminine = true
      )

  case FormII
      extends VerbalNoun(
        RootWord(
          1,
          2,
          4,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.YaWithSukun,
          ArabicLetters.LamWithDammatan
        )
      )

  case FormIIDefectiveVerb
      extends VerbalNoun(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        ),
        feminine = true
      )

  case FormIIIV1
      extends VerbalNoun(
        RootWord(
          0,
          1,
          3,
          ArabicLetters.FaWithKasra,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.LamWithDammatan
        )
      )

  case FormIIIV2
      extends VerbalNoun(
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
        ),
        feminine = true
      )

  case FormIIIDefectiveVerb
      extends VerbalNoun(
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
        ),
        feminine = true
      )

  case FormIV
      extends VerbalNoun(
        RootWord(
          1,
          2,
          4,
          ArabicLetters.AlifHamzaBelowWithKasra,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.LamWithDammatan
        )
      )

  case FormV
      extends VerbalNoun(
        RootWord(
          1,
          2,
          3,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithSukun,
          ArabicLetters.AinWithShaddaAndDamma,
          ArabicLetters.LamWithDammatan
        )
      )

  case FormVI
      extends VerbalNoun(
        RootWord(
          1,
          3,
          4,
          ArabicLetters.TaWithFatha,
          ArabicLetters.FaWithFatha,
          ArabicLetters.LetterAlif,
          ArabicLetters.AinWithDamma,
          ArabicLetters.LamWithDammatan
        )
      )

  case FormVII
      extends VerbalNoun(
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
        )
      )

  case FormVIII
      extends VerbalNoun(
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
        )
      )

  case FormX
      extends VerbalNoun(
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
        )
      )
  override def code: String = name()
}
