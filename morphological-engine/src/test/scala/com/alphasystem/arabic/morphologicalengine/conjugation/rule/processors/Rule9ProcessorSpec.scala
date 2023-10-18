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

class Rule9ProcessorSpec extends BaseRuleProcessorSpec {

  test("Second radical Waw") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Qaf,
        ArabicLetterType.Waw,
        ArabicLetterType.Lam
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastPassiveTense.get.rootWord
    val expected = ArabicWord(
      ArabicLetters.QafWithKasra,
      ArabicLetters.YaWithSukun,
      ArabicLetters.LamWithFatha
    )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("Second radical Ya") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ba,
        ArabicLetterType.Ya,
        ArabicLetterType.Ain
      )

    val baseWord = Form.fromNamedTemplate(processingContext.namedTemplate).pastPassiveTense.get.rootWord
    val expected = ArabicWord(
      ArabicLetters.BaWithKasra,
      ArabicLetters.YaWithSukun,
      ArabicLetters.AinWithFatha
    )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)
  }

  test("First radical gets Damma when second radical Waw") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Qaf,
        ArabicLetterType.Waw,
        ArabicLetterType.Lam
      )

    val pastTenseRoot = Form.fromNamedTemplate(processingContext.namedTemplate).pastTense.rootWord
    val pastTenseExpected = ArabicWord(ArabicLetters.QafWithFatha, ArabicLetters.LetterAlif, ArabicLetters.LamWithFatha)
    validate(pastTenseRoot, pastTenseExpected, HiddenPronounStatus.ThirdPersonMasculineSingular, processingContext)

    val baseWord = RootWord(
      `type` = MorphologicalTermType.PastPassiveTense,
      firstRadicalIndex = 0,
      secondRadicalIndex = 1,
      thirdRadicalIndex = 2,
      arabicLetters = Seq(
        ArabicLetters.QafWithDamma,
        ArabicLetters.WawWithKasra,
        ArabicLetters.LamWithSukun,
        ArabicLetters.TaWithDamma
      )*
    )
    val expected = ArabicWord(
      ArabicLetters.QafWithDamma,
      ArabicLetters.LamWithSukun,
      ArabicLetters.TaWithDamma
    )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonFemininePlural, processingContext)
  }

  test("First radical gets Kasra when second radical Ya") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ba,
        ArabicLetterType.Ya,
        ArabicLetterType.Ain
      )

    val baseWord = RootWord(
      `type` = MorphologicalTermType.PastPassiveTense,
      firstRadicalIndex = 0,
      secondRadicalIndex = 1,
      thirdRadicalIndex = 2,
      arabicLetters = Seq(
        ArabicLetters.BaWithDamma,
        ArabicLetters.YaWithKasra,
        ArabicLetters.AinWithSukun,
        ArabicLetters.TaWithDamma
      )*
    )
    val expected = ArabicWord(
      ArabicLetters.BaWithKasra,
      ArabicLetters.AinWithSukun,
      ArabicLetters.TaWithDamma
    )
    validate(baseWord, expected, HiddenPronounStatus.ThirdPersonFemininePlural, processingContext)
  }
}
