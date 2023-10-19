package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{ ArabicLetterType, ArabicLetters, ArabicWord, HiddenPronounStatus }
import conjugation.forms.Form
import conjugation.model.internal.RootWord
import conjugation.model.{ MorphologicalTermType, NamedTemplate, OutputFormat }

class DefectiveWordsProcessorSpec extends BaseRuleProcessorSpec {

  test("Third radical Waw with previous letter has Damma") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Dal,
        ArabicLetterType.Ain,
        ArabicLetterType.Waw
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).presentTense.rootWord
    val expected = ArabicWord(
      ArabicLetters.YaWithFatha,
      ArabicLetters.DalWithSukun,
      ArabicLetters.AinWithDamma,
      ArabicLetters.WawWithSukun
    )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Third radical Ya with previous letter has Kasra") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ra,
        ArabicLetterType.Meem,
        ArabicLetterType.Ya
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).presentTense.rootWord
    val expected = ArabicWord(
      ArabicLetters.YaWithFatha,
      ArabicLetters.RaWithSukun,
      ArabicLetters.MeemWithKasra,
      ArabicLetters.YaWithSukun
    )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Third radical Waw with previous letter has Fatha") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupATemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ra,
        ArabicLetterType.Ddad,
        ArabicLetterType.Waw
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).presentTense.rootWord
    val expected = ArabicWord(
      ArabicLetters.YaWithFatha,
      ArabicLetters.RaWithSukun,
      ArabicLetters.DdadWithFatha,
      ArabicLetters.LetterAlifMaksura
    )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Third radical Ya with previous letter has Fatha") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupATemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Kha,
        ArabicLetterType.Sheen,
        ArabicLetterType.Ya
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).presentTense.rootWord
    val expected = ArabicWord(
      ArabicLetters.YaWithFatha,
      ArabicLetters.KhaWithSukun,
      ArabicLetters.SheenWithFatha,
      ArabicLetters.LetterAlifMaksura
    )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Third radical Waw preceded by Damma and followed by another Waw") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Dal,
        ArabicLetterType.Ain,
        ArabicLetterType.Waw
      )

    val baseWord = RootWord(
      `type` = MorphologicalTermType.PresentTense,
      firstRadicalIndex = 1,
      secondRadicalIndex = 2,
      thirdRadicalIndex = 3,
      arabicLetters = Seq(
        ArabicLetters.YaWithFatha,
        ArabicLetters.DalWithSukun,
        ArabicLetters.AinWithDamma,
        ArabicLetters.WawWithDamma,
        ArabicLetters.WawWithSukun,
        ArabicLetters.NoonWithFatha
      )*
    )

    val expected = ArabicWord(
      ArabicLetters.YaWithFatha,
      ArabicLetters.DalWithSukun,
      ArabicLetters.AinWithDamma,
      ArabicLetters.WawWithSukun,
      ArabicLetters.NoonWithFatha
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculinePlural, processingContext)
  }

  test("Third radical Ya preceded by Kasra and followed by another Ya") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ra,
        ArabicLetterType.Meem,
        ArabicLetterType.Ya
      )

    val baseWord = RootWord(
      `type` = MorphologicalTermType.PresentTense,
      firstRadicalIndex = 1,
      secondRadicalIndex = 2,
      thirdRadicalIndex = 3,
      arabicLetters = Seq(
        ArabicLetters.TaWithFatha,
        ArabicLetters.RaWithSukun,
        ArabicLetters.MeemWithKasra,
        ArabicLetters.YaWithKasra,
        ArabicLetters.YaWithSukun,
        ArabicLetters.NoonWithFatha
      )*
    )

    val expected = ArabicWord(
      ArabicLetters.TaWithFatha,
      ArabicLetters.RaWithSukun,
      ArabicLetters.MeemWithKasra,
      ArabicLetters.YaWithSukun,
      ArabicLetters.NoonWithFatha
    )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonFeminineSingular, processingContext)
  }

  test("Third radical Waw preceded by Damma and followed by Ya") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Dal,
        ArabicLetterType.Ain,
        ArabicLetterType.Waw
      )

    val baseWord = RootWord(
      `type` = MorphologicalTermType.PresentTense,
      firstRadicalIndex = 1,
      secondRadicalIndex = 2,
      thirdRadicalIndex = 3,
      arabicLetters = Seq(
        ArabicLetters.TaWithFatha,
        ArabicLetters.DalWithSukun,
        ArabicLetters.AinWithDamma,
        ArabicLetters.WawWithKasra,
        ArabicLetters.YaWithSukun,
        ArabicLetters.NoonWithFatha
      )*
    )

    val expected = ArabicWord(
      ArabicLetters.TaWithFatha,
      ArabicLetters.DalWithSukun,
      ArabicLetters.AinWithKasra,
      ArabicLetters.YaWithSukun,
      ArabicLetters.NoonWithFatha
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculinePlural, processingContext)
  }

  test("Third radical Ya preceded by Kasra and followed by Waw") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ra,
        ArabicLetterType.Meem,
        ArabicLetterType.Ya
      )

    val baseWord = RootWord(
      `type` = MorphologicalTermType.PresentTense,
      firstRadicalIndex = 1,
      secondRadicalIndex = 2,
      thirdRadicalIndex = 3,
      arabicLetters = Seq(
        ArabicLetters.YaWithFatha,
        ArabicLetters.RaWithSukun,
        ArabicLetters.MeemWithKasra,
        ArabicLetters.YaWithDamma,
        ArabicLetters.WawWithSukun,
        ArabicLetters.NoonWithFatha
      )*
    )

    val expected = ArabicWord(
      ArabicLetters.YaWithFatha,
      ArabicLetters.RaWithSukun,
      ArabicLetters.MeemWithDamma,
      ArabicLetters.WawWithSukun,
      ArabicLetters.NoonWithFatha
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculinePlural, processingContext)
  }
}
