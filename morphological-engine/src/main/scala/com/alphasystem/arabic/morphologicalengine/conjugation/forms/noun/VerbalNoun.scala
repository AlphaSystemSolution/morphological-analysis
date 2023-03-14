package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package noun

import arabic.model.ArabicLetters
import arabic.morphologicalanalysis.morphology.model.Flexibility
import conjugation.model.{ MorphologicalTermType, NamedTemplate }
import conjugation.model.internal.RootWord
import conjugation.transformer.noun.AbstractNounTransformer.PluralType

object VerbalNoun {

  case object FormIV1
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
          0,
          1,
          2,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithSukun,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  case object FormIV2
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
          0,
          1,
          2,
          ArabicLetters.FaWithDamma,
          ArabicLetters.AinWithDamma,
          ArabicLetters.LamWithDammatan
        )
      )

  case object FormIV3
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
          0,
          1,
          2,
          ArabicLetters.FaWithDamma,
          ArabicLetters.AinWithSukun,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  case object FormIV4
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
          0,
          1,
          2,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  case object FormIV5
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
          0,
          1,
          2,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  case object FormIV6
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
          0,
          1,
          2,
          ArabicLetters.FaWithDamma,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  case object FormIV7
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
          0,
          1,
          2,
          ArabicLetters.FaWithKasra,
          ArabicLetters.AinWithSukun,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  case object FormIV8
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
          0,
          1,
          2,
          ArabicLetters.FaWithKasra,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithDammatan
        ),
        pluralType = PluralType.Feminine
      )

  case object FormIV9
      extends FeminineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
          0,
          1,
          2,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  case object FormIV10
      extends FeminineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
          0,
          1,
          2,
          ArabicLetters.FaWithKasra,
          ArabicLetters.AinWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  case object FormIV11
      extends FeminineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
          0,
          1,
          2,
          ArabicLetters.FaWithDamma,
          ArabicLetters.AinWithSukun,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  case object FormIV12
      extends FeminineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
          0,
          1,
          2,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithFatha,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  case object FormIV13
      extends FeminineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
          0,
          1,
          2,
          ArabicLetters.FaWithFatha,
          ArabicLetters.AinWithKasra,
          ArabicLetters.LamWithFatha,
          ArabicLetters.TaMarbutaWithDammatan
        )
      )

  case object FormIV14
      extends FeminineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormIV15
      extends FeminineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormIV16
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormIV17
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormIV18
      extends FeminineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormIV19
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormIV20
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormIV21
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormIV22
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormIV23
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormIV24
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormIV25
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormIV26
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormIV27
      extends FeminineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormIV28
      extends FeminineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormII
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormIIDefectiveVerb
      extends FeminineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormIIIV1
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormIIIV2
      extends FeminineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormIIIDefectiveVerb
      extends FeminineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormIV
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormV
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormVI
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormVII
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormVIII
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  case object FormX
      extends MasculineBasedVerbalNoun(
        RootWord(
          MorphologicalTermType.VerbalNoun,
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

  private lazy val byFormICodes = Seq(
    FormIV1.code -> FormIV1,
    FormIV2.code -> FormIV2,
    FormIV3.code -> FormIV3,
    FormIV4.code -> FormIV4,
    FormIV5.code -> FormIV5,
    FormIV6.code -> FormIV6,
    FormIV7.code -> FormIV7,
    FormIV8.code -> FormIV8,
    FormIV9.code -> FormIV9,
    FormIV10.code -> FormIV10,
    FormIV11.code -> FormIV11,
    FormIV12.code -> FormIV12,
    FormIV13.code -> FormIV13,
    FormIV14.code -> FormIV14,
    FormIV15.code -> FormIV15,
    FormIV16.code -> FormIV16,
    FormIV17.code -> FormIV17,
    FormIV18.code -> FormIV18,
    FormIV19.code -> FormIV19,
    FormIV20.code -> FormIV20,
    FormIV21.code -> FormIV21,
    FormIV22.code -> FormIV22,
    FormIV23.code -> FormIV23,
    FormIV24.code -> FormIV24,
    FormIV25.code -> FormIV25,
    FormIV26.code -> FormIV26,
    FormIV27.code -> FormIV27,
    FormIV28.code -> FormIV28
  )

  private lazy val allValues = byFormICodes ++ Seq(
    FormII.code -> FormII,
    FormIIDefectiveVerb.code -> FormIIDefectiveVerb,
    FormIIIV1.code -> FormIIIV1,
    FormIIIV2.code -> FormIIIV2,
    FormIIIDefectiveVerb.code -> FormIIIDefectiveVerb,
    FormIV.code -> FormIV,
    FormV.code -> FormV,
    FormVI.code -> FormVI,
    FormVII.code -> FormVII,
    FormVIII.code -> FormVIII,
    FormX.code -> FormX
  )

  lazy val formIDefaultValues: Seq[NounSupportBase] = byFormICodes.map(_._2)

  lazy val byNamedTemplate: Map[NamedTemplate, Seq[NounSupportBase]] = Map(
    NamedTemplate.FormIITemplate -> Seq(FormII),
    NamedTemplate.FormIIITemplate -> Seq(FormIIIV1, FormIIIV2),
    NamedTemplate.FormIVTemplate -> Seq(FormIV),
    NamedTemplate.FormVTemplate -> Seq(FormV),
    NamedTemplate.FormVITemplate -> Seq(FormVI),
    NamedTemplate.FormVIITemplate -> Seq(FormVII),
    NamedTemplate.FormVIIITemplate -> Seq(FormVIII),
    NamedTemplate.FormXTemplate -> Seq(FormX)
  )

  lazy val values: Seq[NounSupport] = allValues.map(_._2)

  lazy val byCode: Map[String, NounSupport] = allValues.toMap
}
