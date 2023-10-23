package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import com.alphasystem.arabic.model.{ ArabicLetterType, ArabicLetters, ArabicWord, HiddenPronounStatus }
import com.alphasystem.arabic.morphologicalengine.conjugation.forms.Form
import com.alphasystem.arabic.morphologicalengine.conjugation.model.{ NamedTemplate, OutputFormat }

class FormVIIIProcessorSpec extends BaseRuleProcessorSpec {

  test("First radical has Dal") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormVIIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Dal,
        ArabicLetterType.Ain,
        ArabicLetterType.Waw
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastTense.rootWord
    val expected =
      ArabicWord(
        ArabicLetters.AlifHamzaBelowWithKasra,
        ArabicLetters.DalWithShaddaAndFatha,
        ArabicLetters.AinWithFatha,
        ArabicLetters.LetterAlif
      )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("First radical has Thal") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormVIIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Thal,
        ArabicLetterType.Kaf,
        ArabicLetterType.Ra
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastTense.rootWord
    val expected =
      ArabicWord(
        ArabicLetters.AlifHamzaBelowWithKasra,
        ArabicLetters.DalWithShaddaAndFatha,
        ArabicLetters.KafWithFatha,
        ArabicLetters.RaWithFatha
      )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("First radical has Tta") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormVIIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Tta,
        ArabicLetterType.Lam,
        ArabicLetterType.Ba
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastTense.rootWord
    val expected =
      ArabicWord(
        ArabicLetters.AlifHamzaBelowWithKasra,
        ArabicLetters.TtaWithShaddaAndFatha,
        ArabicLetters.LamWithFatha,
        ArabicLetters.BaWithFatha
      )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("First radical has Dtha") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormVIIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Dtha,
        ArabicLetterType.Lam,
        ArabicLetterType.Meem
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastTense.rootWord
    val expected =
      ArabicWord(
        ArabicLetters.AlifHamzaBelowWithKasra,
        ArabicLetters.TtaWithShaddaAndFatha,
        ArabicLetters.LamWithFatha,
        ArabicLetters.MeemWithFatha
      )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Second radical has Sad in past tense") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormVIIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Kha,
        ArabicLetterType.Sad,
        ArabicLetterType.Meem
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastTense.rootWord
    val expected =
      ArabicWord(
        ArabicLetters.KhaWithFatha,
        ArabicLetters.SadWithShaddaAndFatha,
        ArabicLetters.MeemWithFatha
      )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Second radical has Sad in present tense") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormVIIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Kha,
        ArabicLetterType.Sad,
        ArabicLetterType.Meem
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).presentTense.rootWord
    val expected =
      ArabicWord(
        ArabicLetters.YaWithFatha,
        ArabicLetters.KhaWithFatha,
        ArabicLetters.SadWithShaddaAndKasra,
        ArabicLetters.MeemWithDamma
      )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }
}
