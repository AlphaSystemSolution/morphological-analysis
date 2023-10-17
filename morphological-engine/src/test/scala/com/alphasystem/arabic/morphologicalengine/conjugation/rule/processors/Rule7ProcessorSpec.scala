package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import com.alphasystem.arabic.model.{ ArabicLetterType, ArabicLetters, ArabicWord, HiddenPronounStatus }
import com.alphasystem.arabic.morphologicalengine.conjugation.forms.Form
import com.alphasystem.arabic.morphologicalengine.conjugation.model.internal.RootWord
import com.alphasystem.arabic.morphologicalengine.conjugation.model.{
  MorphologicalTermType,
  NamedTemplate,
  OutputFormat
}

class Rule7ProcessorSpec extends BaseRuleProcessorSpec {

  test("Second radical Waw with Fatha") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Qaf,
        ArabicLetterType.Waw,
        ArabicLetterType.Lam
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastTense.rootWord
    val expected = ArabicWord(ArabicLetters.QafWithFatha, ArabicLetters.LetterAlif, ArabicLetters.LamWithFatha)
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Second radical Ya with Fatha") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ba,
        ArabicLetterType.Ya,
        ArabicLetterType.Ain
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastTense.rootWord
    val expected = ArabicWord(ArabicLetters.BaWithFatha, ArabicLetters.LetterAlif, ArabicLetters.AinWithFatha)
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Third radical Waw with Fatha") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Dal,
        ArabicLetterType.Ain,
        ArabicLetterType.Waw
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastTense.rootWord
    val expected = ArabicWord(ArabicLetters.DalWithFatha, ArabicLetters.AinWithFatha, ArabicLetters.LetterAlif)
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Third radical Ya with Fatha") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ra,
        ArabicLetterType.Meem,
        ArabicLetterType.Ya
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastTense.rootWord
    val expected = ArabicWord(ArabicLetters.RaWithFatha, ArabicLetters.MeemWithFatha, ArabicLetters.LetterAlifMaksura)
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Does not apply rule if first radical is weak") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Waw,
        ArabicLetterType.Ain,
        ArabicLetterType.Dal
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastTense.rootWord
    val expected = ArabicWord(ArabicLetters.WawWithFatha, ArabicLetters.AinWithFatha, ArabicLetters.DalWithFatha)
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Does not apply rule if the word is doubly weak with Waw as second radical") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Tta,
        ArabicLetterType.Waw,
        ArabicLetterType.Ya
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastTense.rootWord
    val expected = ArabicWord(ArabicLetters.TtaWithFatha, ArabicLetters.WawWithFatha, ArabicLetters.YaWithFatha)
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Does not apply rule if the word is doubly weak with Ya as second radical") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ha,
        ArabicLetterType.Ya,
        ArabicLetterType.Ya
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastTense.rootWord
    val expected = ArabicWord(ArabicLetters.HaWithFatha, ArabicLetters.YaWithFatha, ArabicLetters.YaWithFatha)
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Does not apply rule when first radical weak letter in present tense") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormVTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ya,
        ArabicLetterType.Seen,
        ArabicLetterType.Ra
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastTense.rootWord
    val expected =
      ArabicWord(
        ArabicLetters.TaWithFatha,
        ArabicLetters.YaWithFatha,
        ArabicLetters.SeenWithShaddaAndFatha,
        ArabicLetters.RaWithFatha
      )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Does not apply rule when third radical is Waw in dual form") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Dal,
        ArabicLetterType.Ain,
        ArabicLetterType.Waw
      )

    val expected = ArabicWord(
      ArabicLetters.DalWithFatha,
      ArabicLetters.AinWithFatha,
      ArabicLetters.WawWithFatha,
      ArabicLetters.LetterAlif
    )

    val baseWord = RootWord(
      `type` = MorphologicalTermType.PastTense,
      firstRadicalIndex = 0,
      secondRadicalIndex = 1,
      thirdRadicalIndex = 2,
      fourthRadicalIndex = None,
      arabicLetters = expected.letters*
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineDual, processingContext)
  }

  test("Does not apply rule when third radical is Ya in dual form") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ra,
        ArabicLetterType.Meem,
        ArabicLetterType.Ya
      )

    val expected = ArabicWord(
      ArabicLetters.RaWithFatha,
      ArabicLetters.MeemWithFatha,
      ArabicLetters.YaWithFatha,
      ArabicLetters.LetterAlif
    )

    val baseWord = RootWord(
      `type` = MorphologicalTermType.PastTense,
      firstRadicalIndex = 0,
      secondRadicalIndex = 1,
      thirdRadicalIndex = 2,
      fourthRadicalIndex = None,
      arabicLetters = expected.letters*
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineDual, processingContext)
  }

  test("Remove third radical Waw due to two sukuns comes together") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Dal,
        ArabicLetterType.Ain,
        ArabicLetterType.Waw
      )

    val expected = ArabicWord(
      ArabicLetters.DalWithFatha,
      ArabicLetters.AinWithFatha,
      ArabicLetters.WawWithSukun,
      ArabicLetters.LetterAlif
    )

    val baseWord = RootWord(
      `type` = MorphologicalTermType.PastTense,
      firstRadicalIndex = 0,
      secondRadicalIndex = 1,
      thirdRadicalIndex = 2,
      fourthRadicalIndex = None,
      arabicLetters = Seq(
        ArabicLetters.DalWithFatha,
        ArabicLetters.AinWithFatha,
        ArabicLetters.WawWithDamma,
        ArabicLetters.WawWithSukun,
        ArabicLetters.LetterAlif
      )*
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculinePlural, processingContext)
  }

  test("Remove third radical Ya due to two sukuns comes together") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ra,
        ArabicLetterType.Ddad,
        ArabicLetterType.Ya
      )

    val expected = ArabicWord(
      ArabicLetters.TaWithFatha,
      ArabicLetters.RaWithSukun,
      ArabicLetters.DdadWithFatha,
      ArabicLetters.YaWithSukun,
      ArabicLetters.NoonWithFatha
    )

    val baseWord = RootWord(
      `type` = MorphologicalTermType.PresentTense,
      firstRadicalIndex = 1,
      secondRadicalIndex = 2,
      thirdRadicalIndex = 3,
      fourthRadicalIndex = None,
      arabicLetters = Seq(
        ArabicLetters.TaWithFatha,
        ArabicLetters.RaWithSukun,
        ArabicLetters.DdadWithFatha,
        ArabicLetters.YaWithKasra,
        ArabicLetters.YaWithSukun,
        ArabicLetters.NoonWithFatha
      )*
    )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonFemininePlural, processingContext)
  }

  test("Remove third radical Waw in third person feminine singular of past tense") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Dal,
        ArabicLetterType.Ain,
        ArabicLetterType.Waw
      )

    val expected = ArabicWord(
      ArabicLetters.DalWithFatha,
      ArabicLetters.AinWithFatha,
      ArabicLetters.TaWithSukun
    )

    val baseWord = RootWord(
      `type` = MorphologicalTermType.PastTense,
      firstRadicalIndex = 0,
      secondRadicalIndex = 1,
      thirdRadicalIndex = 2,
      fourthRadicalIndex = None,
      arabicLetters = Seq(
        ArabicLetters.DalWithFatha,
        ArabicLetters.AinWithFatha,
        ArabicLetters.WawWithFatha,
        ArabicLetters.TaWithSukun
      )*
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonFeminineSingular, processingContext)
  }

  test("Remove third radical Waw in third person feminine dual of past tense") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Dal,
        ArabicLetterType.Ain,
        ArabicLetterType.Waw
      )

    val expected = ArabicWord(
      ArabicLetters.DalWithFatha,
      ArabicLetters.AinWithFatha,
      ArabicLetters.TaWithFatha,
      ArabicLetters.LetterAlif
    )

    val baseWord = RootWord(
      `type` = MorphologicalTermType.PastTense,
      firstRadicalIndex = 0,
      secondRadicalIndex = 1,
      thirdRadicalIndex = 2,
      fourthRadicalIndex = None,
      arabicLetters = Seq(
        ArabicLetters.DalWithFatha,
        ArabicLetters.AinWithFatha,
        ArabicLetters.WawWithFatha,
        ArabicLetters.TaWithFatha,
        ArabicLetters.LetterAlif
      )*
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonFeminineDual, processingContext)
  }

  test("First radical gets Damma when second radical Waw has Fatha") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Qaf,
        ArabicLetterType.Waw,
        ArabicLetterType.Lam
      )

    val expected = ArabicWord(
      ArabicLetters.QafWithDamma,
      ArabicLetters.LamWithSukun,
      ArabicLetters.NoonWithFatha
    )

    val baseWord = RootWord(
      `type` = MorphologicalTermType.PastTense,
      firstRadicalIndex = 0,
      secondRadicalIndex = 1,
      thirdRadicalIndex = 2,
      fourthRadicalIndex = None,
      arabicLetters = Seq(
        ArabicLetters.QafWithFatha,
        ArabicLetters.WawWithFatha,
        ArabicLetters.LamWithSukun,
        ArabicLetters.NoonWithFatha
      )*
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonFemininePlural, processingContext)
  }

  test("First radical gets Kasra when second radical Waw has Kasra") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Kha,
        ArabicLetterType.Waw,
        ArabicLetterType.Fa
      )

    val expected = ArabicWord(
      ArabicLetters.KhaWithKasra,
      ArabicLetters.FaWithSukun,
      ArabicLetters.NoonWithFatha
    )

    val baseWord = RootWord(
      `type` = MorphologicalTermType.PastTense,
      firstRadicalIndex = 0,
      secondRadicalIndex = 1,
      thirdRadicalIndex = 2,
      fourthRadicalIndex = None,
      arabicLetters = Seq(
        ArabicLetters.KhaWithFatha,
        ArabicLetters.WawWithKasra,
        ArabicLetters.FaWithSukun,
        ArabicLetters.NoonWithFatha
      )*
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonFemininePlural, processingContext)
  }
}
