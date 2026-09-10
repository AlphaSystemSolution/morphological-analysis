package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{ ArabicLetterType, ArabicLetters, ArabicWord, HiddenPronounStatus, JussiveParticle }
import conjugation.forms.Form
import conjugation.model.{ NamedTemplate, OutputFormat }

class ImperativePrefixProcessorSpec extends BaseRuleProcessorSpec {

  test("Form I Category A Group U - second radical Damma produces Hamza with Damma") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Noon,
        ArabicLetterType.Sad,
        ArabicLetterType.Ra
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).imperative.rootWord
    val expected = ArabicWord(
      ArabicLetters.AlifHamzaAboveWithDamma,
      ArabicLetters.NoonWithSukun,
      ArabicLetters.SadWithDamma,
      ArabicLetters.RaWithSukun
    )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), true)
  }

  test("Form I Category A Group I - second radical Kasra produces Hamza with Kasra") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ddad,
        ArabicLetterType.Ra,
        ArabicLetterType.Ba
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).imperative.rootWord
    val expected = ArabicWord(
      ArabicLetters.AlifHamzaBelowWithKasra,
      ArabicLetters.DdadWithSukun,
      ArabicLetters.RaWithKasra,
      ArabicLetters.BaWithSukun
    )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), true)
  }

  test("Form I Category A Group A - second radical Fatha produces Hamza with Kasra") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupATemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Fa,
        ArabicLetterType.Ta,
        ArabicLetterType.Hha
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).imperative.rootWord
    val expected = ArabicWord(
      ArabicLetters.AlifHamzaBelowWithKasra,
      ArabicLetters.FaWithSukun,
      ArabicLetters.TaWithFatha,
      ArabicLetters.HhaWithSukun
    )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), true)
  }

  test("Form I Category I Group A - second radical Fatha produces Hamza with Kasra") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryIGroupATemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Seen,
        ArabicLetterType.Meem,
        ArabicLetterType.Ain
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).imperative.rootWord
    val expected = ArabicWord(
      ArabicLetters.AlifHamzaBelowWithKasra,
      ArabicLetters.SeenWithSukun,
      ArabicLetters.MeemWithFatha,
      ArabicLetters.AinWithSukun
    )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), true)
  }

  test("Form I Category I Group I - second radical Kasra produces Hamza with Kasra") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryIGroupITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Hha,
        ArabicLetterType.Seen,
        ArabicLetterType.Ba
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).imperative.rootWord
    val expected = ArabicWord(
      ArabicLetters.AlifHamzaBelowWithKasra,
      ArabicLetters.HhaWithSukun,
      ArabicLetters.SeenWithKasra,
      ArabicLetters.BaWithSukun
    )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), true)
  }

  test("Form I Category U - second radical Damma produces Hamza with Damma") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Kaf,
        ArabicLetterType.Ra,
        ArabicLetterType.Meem
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).imperative.rootWord
    val expected = ArabicWord(
      ArabicLetters.AlifHamzaAboveWithDamma,
      ArabicLetters.KafWithSukun,
      ArabicLetters.RaWithDamma,
      ArabicLetters.MeemWithSukun
    )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), true)
  }

  test("Form I Category A Group U with first radical Hamzah drops Hamzah (أخذ -> خُذْ)") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Hamza,
        ArabicLetterType.Kha,
        ArabicLetterType.Thal
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).imperative.rootWord
    val expected = ArabicWord(
      ArabicLetters.KhaWithDamma,
      ArabicLetters.ThalWithSukun
    )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), true)
  }

  test("Form I Category A Group U with first radical Hamzah drops Hamzah (أكل -> كُلْ)") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Hamza,
        ArabicLetterType.Kaf,
        ArabicLetterType.Lam
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).imperative.rootWord
    val expected = ArabicWord(
      ArabicLetters.KafWithDamma,
      ArabicLetters.LamWithSukun
    )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), true)
  }

  test("Form IV produces Hamza with Fatha (أسلم -> أَسْلِمْ)") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormIVTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Seen,
        ArabicLetterType.Lam,
        ArabicLetterType.Meem
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).imperative.rootWord
    val expected = ArabicWord(
      ArabicLetters.AlifHamzaAboveWithFatha,
      ArabicLetters.SeenWithSukun,
      ArabicLetters.LamWithKasra,
      ArabicLetters.MeemWithSukun
    )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), true)
  }

  test("Form II with voweled first radical does not prefix Hamzah (علّم -> عَلِّمْ)") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ain,
        ArabicLetterType.Lam,
        ArabicLetterType.Meem
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).imperative.rootWord
    val expected = ArabicWord(
      ArabicLetters.AinWithFatha,
      ArabicLetters.LamWithShaddaAndKasra,
      ArabicLetters.MeemWithSukun
    )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), true)
  }

  test("Form III with voweled first radical does not prefix Hamzah (جاهد -> جَاهِدْ)") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormIIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Jeem,
        ArabicLetterType.Hha,
        ArabicLetterType.Dal
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).imperative.rootWord
    val expected = ArabicWord(
      ArabicLetters.JeemWithFatha,
      ArabicLetters.LetterAlif,
      ArabicLetters.HhaWithKasra,
      ArabicLetters.DalWithSukun
    )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), true)
  }

  test("Form V with voweled first radical does not prefix Hamzah (تقبّل -> تَقَبَّلْ)") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormVTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Qaf,
        ArabicLetterType.Ba,
        ArabicLetterType.Lam
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).imperative.rootWord
    val expected = ArabicWord(
      ArabicLetters.TaWithFatha,
      ArabicLetters.QafWithFatha,
      ArabicLetters.BaWithShaddaAndFatha,
      ArabicLetters.LamWithSukun
    )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), true)
  }

  test("Form VI with voweled first radical does not prefix Hamzah (تقاتل -> تَقَاتَلْ)") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormVITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Qaf,
        ArabicLetterType.Ta,
        ArabicLetterType.Lam
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).imperative.rootWord
    val expected = ArabicWord(
      ArabicLetters.TaWithFatha,
      ArabicLetters.QafWithFatha,
      ArabicLetters.LetterAlif,
      ArabicLetters.TaWithFatha,
      ArabicLetters.LamWithSukun
    )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), true)
  }

  test("Form VII prefixes Hamza with Kasra (انقلب -> اِنْقَلِبْ)") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormVIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Qaf,
        ArabicLetterType.Lam,
        ArabicLetterType.Ba
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).imperative.rootWord
    val expected = ArabicWord(
      ArabicLetters.AlifHamzaBelowWithKasra,
      ArabicLetters.NoonWithSukun,
      ArabicLetters.QafWithFatha,
      ArabicLetters.LamWithKasra,
      ArabicLetters.BaWithSukun
    )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), true)
  }

  test("Form VIII prefixes Hamza with Kasra (اجتمع -> اِجْتَمِعْ)") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormVIIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Jeem,
        ArabicLetterType.Meem,
        ArabicLetterType.Ain
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).imperative.rootWord
    val expected = ArabicWord(
      ArabicLetters.AlifHamzaBelowWithKasra,
      ArabicLetters.JeemWithSukun,
      ArabicLetters.TaWithFatha,
      ArabicLetters.MeemWithKasra,
      ArabicLetters.AinWithSukun
    )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), true)
  }

  test("Form X prefixes Hamza with Kasra (استغفر -> اِسْتَغْفِرْ)") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormXTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ghain,
        ArabicLetterType.Fa,
        ArabicLetterType.Ra
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).imperative.rootWord
    val expected = ArabicWord(
      ArabicLetters.AlifHamzaBelowWithKasra,
      ArabicLetters.SeenWithSukun,
      ArabicLetters.TaWithFatha,
      ArabicLetters.GhainWithSukun,
      ArabicLetters.FaWithKasra,
      ArabicLetters.RaWithSukun
    )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), true)
  }

  test("Does not apply rule to past tense") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Noon,
        ArabicLetterType.Sad,
        ArabicLetterType.Ra
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastTense.rootWord
    val expected = ArabicWord(
      ArabicLetters.NoonWithFatha,
      ArabicLetters.SadWithFatha,
      ArabicLetters.RaWithFatha
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), false)
  }

  test("Does not apply rule to present tense") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Noon,
        ArabicLetterType.Sad,
        ArabicLetterType.Ra
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).presentTense.rootWord
    val expected = ArabicWord(
      ArabicLetters.YaWithFatha,
      ArabicLetters.NoonWithSukun,
      ArabicLetters.SadWithDamma,
      ArabicLetters.RaWithDamma
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), false)
  }

  test("Does not apply rule to forbidden tense") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Noon,
        ArabicLetterType.Sad,
        ArabicLetterType.Ra
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).forbidden.rootWord
    val expected = JussiveParticle
      .LamOfProhibition
      .word
      .concat(
        ArabicWord(
          ArabicLetters.TaWithFatha,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.SadWithDamma,
          ArabicLetters.RaWithSukun
        )
      )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), false)
  }

  test("Present tense jussive with LamOfCommand transforms second person to imperative form") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupATemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Fa,
        ArabicLetterType.Ta,
        ArabicLetterType.Hha,
        jussiveParticle = Some(JussiveParticle.LamOfCommand)
      )

    val baseWord = conjugation.forms.verb.FormI.PresentTenseJussiveModeV3.rootWord

    val expected = ArabicWord(
      ArabicLetters.AlifHamzaBelowWithKasra,
      ArabicLetters.FaWithSukun,
      ArabicLetters.TaWithFatha,
      ArabicLetters.HhaWithSukun
    )

    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), true)
  }

  test("Present tense jussive with LamOfCommand does not transform third person to imperative") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupATemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Fa,
        ArabicLetterType.Ta,
        ArabicLetterType.Hha,
        jussiveParticle = Some(JussiveParticle.LamOfCommand)
      )

    val baseWord = conjugation.forms.verb.FormI.PresentTenseJussiveModeV3.rootWord

    val expected = ArabicWord(
      ArabicLetters.LamWithKasra,
      ArabicLetters.YaWithFatha,
      ArabicLetters.FaWithSukun,
      ArabicLetters.TaWithFatha,
      ArabicLetters.HhaWithSukun
    )

    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
    assertEquals(processingContext.appliedRules.contains("ImperativePrefixProcessor"), false)
  }
}
