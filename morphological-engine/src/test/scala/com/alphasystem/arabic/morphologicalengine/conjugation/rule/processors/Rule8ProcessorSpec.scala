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

class Rule8ProcessorSpec extends BaseRuleProcessorSpec {

  test("Second radical Waw with Damma") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Qaf,
        ArabicLetterType.Waw,
        ArabicLetterType.Lam
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).presentTense.rootWord
    val expected = ArabicWord(
      ArabicLetters.YaWithFatha,
      ArabicLetters.QafWithDamma,
      ArabicLetters.WawWithSukun,
      ArabicLetters.LamWithDamma
    )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Second radical Ya with Kasra") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ba,
        ArabicLetterType.Ya,
        ArabicLetterType.Ain
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).presentTense.rootWord
    val expected = ArabicWord(
      ArabicLetters.YaWithFatha,
      ArabicLetters.BaWithKasra,
      ArabicLetters.YaWithSukun,
      ArabicLetters.AinWithDamma
    )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Second radical Waw with Fatha") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Qaf,
        ArabicLetterType.Waw,
        ArabicLetterType.Lam
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).presentPassiveTense.get.rootWord
    val expected = ArabicWord(
      ArabicLetters.YaWithDamma,
      ArabicLetters.QafWithFatha,
      ArabicLetters.LetterAlif,
      ArabicLetters.LamWithDamma
    )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Second radical Ya with Fatha") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ba,
        ArabicLetterType.Ya,
        ArabicLetterType.Ain
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).presentPassiveTense.get.rootWord
    val expected = ArabicWord(
      ArabicLetters.YaWithDamma,
      ArabicLetters.BaWithFatha,
      ArabicLetters.LetterAlif,
      ArabicLetters.AinWithDamma
    )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Second radical Waw with Damma in Forbidden") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Qaf,
        ArabicLetterType.Waw,
        ArabicLetterType.Lam
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).forbidden.rootWord
    val expected = ArabicWord(
      ArabicLetters.LetterLam,
      ArabicLetters.LetterAlif,
      ArabicLetters.LetterSpace,
      ArabicLetters.TaWithFatha,
      ArabicLetters.QafWithDamma,
      ArabicLetters.LamWithSukun
    )
    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
  }

  test("Second radical Ya with Fatha in jussive passive mode") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ba,
        ArabicLetterType.Ya,
        ArabicLetterType.Ain
      )

    val baseWord = RootWord(
      `type` = MorphologicalTermType.PresentPassiveTense,
      firstRadicalIndex = 1,
      secondRadicalIndex = 2,
      thirdRadicalIndex = 3,
      arabicLetters = Seq(
        ArabicLetters.YaWithDamma,
        ArabicLetters.BaWithSukun,
        ArabicLetters.YaWithFatha,
        ArabicLetters.AinWithSukun
      )*
    )
    val expected = ArabicWord(
      ArabicLetters.YaWithDamma,
      ArabicLetters.BaWithFatha,
      ArabicLetters.AinWithSukun
    )
    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
  }

  test("Does not apply rule with extra madd") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Qaf,
        ArabicLetterType.Waw,
        ArabicLetterType.Lam
      )

    val expected = ArabicWord(
      ArabicLetters.MeemWithKasra,
      ArabicLetters.QafWithSukun,
      ArabicLetters.WawWithFatha,
      ArabicLetters.LetterAlif,
      ArabicLetters.LamWithDammatan
    )
    val baseWord = RootWord(
      `type` = MorphologicalTermType.ActiveParticipleMasculine,
      firstRadicalIndex = 1,
      secondRadicalIndex = 2,
      thirdRadicalIndex = 4,
      arabicLetters = expected.letters*
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Does not apply rule with extra madd with second radical Waw") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Qaf,
        ArabicLetterType.Waw,
        ArabicLetterType.Lam
      )

    val expected = ArabicWord(
      ArabicLetters.MeemWithFatha,
      ArabicLetters.QafWithDamma,
      ArabicLetters.WawWithSukun,
      ArabicLetters.LamWithDammatan
    )
    val baseWord = RootWord(
      `type` = MorphologicalTermType.PassiveParticipleMasculine,
      firstRadicalIndex = 1,
      secondRadicalIndex = 2,
      thirdRadicalIndex = 4,
      arabicLetters = Seq(
        ArabicLetters.MeemWithFatha,
        ArabicLetters.QafWithSukun,
        ArabicLetters.WawWithDamma,
        ArabicLetters.WawWithSukun,
        ArabicLetters.LamWithDammatan
      )*
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Does not apply rule with extra madd with second radical Ya") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ba,
        ArabicLetterType.Ya,
        ArabicLetterType.Ain
      )

    val expected = ArabicWord(
      ArabicLetters.MeemWithFatha,
      ArabicLetters.BaWithKasra,
      ArabicLetters.YaWithSukun,
      ArabicLetters.AinWithDammatan
    )
    val baseWord = RootWord(
      `type` = MorphologicalTermType.PassiveParticipleMasculine,
      firstRadicalIndex = 1,
      secondRadicalIndex = 2,
      thirdRadicalIndex = 4,
      arabicLetters = Seq(
        ArabicLetters.MeemWithFatha,
        ArabicLetters.BaWithSukun,
        ArabicLetters.YaWithDamma,
        ArabicLetters.WawWithSukun,
        ArabicLetters.AinWithDammatan
      )*
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }
}
