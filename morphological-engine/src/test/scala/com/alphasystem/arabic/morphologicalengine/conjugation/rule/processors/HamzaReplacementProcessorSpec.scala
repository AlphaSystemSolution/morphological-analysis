package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{ ArabicLetterType, ArabicLetters, ArabicWord, HiddenNounStatus, HiddenPronounStatus }
import conjugation.forms.Form
import conjugation.model.{ NamedTemplate, OutputFormat }

class HamzaReplacementProcessorSpec extends BaseRuleProcessorSpec {

  private val processor = HamzaReplacementProcessor()

  test("Fatha as first letter") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormIVTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Seen,
        ArabicLetterType.Lam,
        ArabicLetterType.Meem
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastTense.rootWord

    val expected = ArabicWord(
      ArabicLetters.AlifHamzaAboveWithFatha,
      ArabicLetters.SeenWithSukun,
      ArabicLetters.LamWithFatha,
      ArabicLetters.MeemWithFatha
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processor, processingContext)
  }

  test("Damma as first letter") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormIVTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Seen,
        ArabicLetterType.Lam,
        ArabicLetterType.Meem
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastPassiveTense.get.rootWord

    val expected = ArabicWord(
      ArabicLetters.AlifHamzaAboveWithDamma,
      ArabicLetters.SeenWithSukun,
      ArabicLetters.LamWithKasra,
      ArabicLetters.MeemWithFatha
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processor, processingContext)
  }

  test("Kasra as first letter") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormXTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ghain,
        ArabicLetterType.Fa,
        ArabicLetterType.Ra
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastTense.rootWord

    val expected = ArabicWord(
      ArabicLetters.AlifHamzaBelowWithKasra,
      ArabicLetters.SeenWithSukun,
      ArabicLetters.TaWithFatha,
      ArabicLetters.GhainWithSukun,
      ArabicLetters.FaWithFatha,
      ArabicLetters.RaWithFatha
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processor, processingContext)
  }

  test("Hamzah in the middle") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormVITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Seen,
        ArabicLetterType.Hamza,
        ArabicLetterType.Lam
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).presentTense.rootWord

    val expected = ArabicWord(
      ArabicLetters.YaWithFatha,
      ArabicLetters.TaWithFatha,
      ArabicLetters.SeenWithFatha,
      ArabicLetters.LetterAlif,
      ArabicLetters.YaHamzaAboveWithFatha,
      ArabicLetters.LamWithDamma
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processor, processingContext)
  }

  test("Two consecutive Hamza's at the beginning (Fatha)") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormIVTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Hamza,
        ArabicLetterType.Meem,
        ArabicLetterType.Noon
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastTense.rootWord

    val expected = ArabicWord(
      ArabicLetters.LetterAlifMaddah,
      ArabicLetters.MeemWithFatha,
      ArabicLetters.NoonWithFatha
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processor, processingContext)
  }

  test("Two consecutive Hamza's at the beginning (Kasra)") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormIVTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Hamza,
        ArabicLetterType.Meem,
        ArabicLetterType.Noon
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).verbalNouns.head.rootWord

    val expected = ArabicWord(
      ArabicLetters.AlifHamzaBelowWithKasra,
      ArabicLetters.YaWithSukun,
      ArabicLetters.MeemWithFatha,
      ArabicLetters.LetterAlif,
      ArabicLetters.NoonWithDammatan
    )

    validate(baseWord, expected, HiddenNounStatus.NominativeSingular, processor, processingContext)
  }

  test("Two consecutive Hamza's at the beginning (Damma)") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormIVTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Hamza,
        ArabicLetterType.Meem,
        ArabicLetterType.Noon
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastPassiveTense.get.rootWord

    val expected = ArabicWord(
      ArabicLetters.AlifHamzaAboveWithDamma,
      ArabicLetters.WawWithSukun,
      ArabicLetters.MeemWithKasra,
      ArabicLetters.NoonWithFatha
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processor, processingContext)
  }
}
