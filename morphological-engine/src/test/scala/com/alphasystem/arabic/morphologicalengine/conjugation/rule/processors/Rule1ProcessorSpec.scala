package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{ ArabicLetterType, ArabicLetters, ArabicWord, HiddenPronounStatus }
import conjugation.forms.Form
import conjugation.model.{ NamedTemplate, OutputFormat }

class Rule1ProcessorSpec extends BaseRuleProcessorSpec {

  test("Root word with second radical has Kasra") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Waw,
        ArabicLetterType.Ain,
        ArabicLetterType.Dal
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).presentTense.rootWord
    val expected = ArabicWord(ArabicLetters.YaWithFatha, ArabicLetters.AinWithKasra, ArabicLetters.DalWithDamma)
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Root word with second radical has Fatha with heavy letters") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupATemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Waw,
        ArabicLetterType.Hha,
        ArabicLetterType.Ba
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).presentTense.rootWord
    val expected = ArabicWord(ArabicLetters.YaWithFatha, ArabicLetters.HhaWithFatha, ArabicLetters.BaWithDamma)
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Root word with second radical has Fatha with non-heavy letters") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupATemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Waw,
        ArabicLetterType.Seen,
        ArabicLetterType.Ba
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).presentTense.rootWord
    val expected = ArabicWord(
      ArabicLetters.YaWithFatha,
      ArabicLetters.WawWithSukun,
      ArabicLetters.SeenWithFatha,
      ArabicLetters.BaWithDamma
    )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Accept only present active or passive tense") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Waw,
        ArabicLetterType.Ain,
        ArabicLetterType.Dal
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastTense.rootWord
    val expected = ArabicWord(ArabicLetters.WawWithFatha, ArabicLetters.AinWithFatha, ArabicLetters.DalWithFatha)
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }
}
