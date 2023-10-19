package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{ ArabicLetterType, ArabicLetters, ArabicWord, HiddenPronounStatus }
import conjugation.model.internal.RootWord
import conjugation.model.{ MorphologicalTermType, NamedTemplate, OutputFormat }

class Rule20ProcessorSpec extends BaseRuleProcessorSpec {

  test("Waw at 4th index") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Dal,
        ArabicLetterType.Ain,
        ArabicLetterType.Waw
      )

    val baseWord = RootWord(
      `type` = MorphologicalTermType.PresentPassiveTense,
      firstRadicalIndex = 1,
      secondRadicalIndex = 2,
      thirdRadicalIndex = 3,
      arabicLetters = Seq(
        ArabicLetters.YaWithDamma,
        ArabicLetters.DalWithSukun,
        ArabicLetters.AinWithFatha,
        ArabicLetters.WawWithFatha,
        ArabicLetters.LetterAlif,
        ArabicLetters.NoonWithKasra
      )*
    )
    val expected = ArabicWord(
      ArabicLetters.YaWithDamma,
      ArabicLetters.DalWithSukun,
      ArabicLetters.AinWithFatha,
      ArabicLetters.YaWithFatha,
      ArabicLetters.LetterAlif,
      ArabicLetters.NoonWithKasra
    )
    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
  }

  test("Waw at 6th index") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormXTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ain,
        ArabicLetterType.Lam,
        ArabicLetterType.Waw
      )

    val baseWord = RootWord(
      `type` = MorphologicalTermType.PastTense,
      firstRadicalIndex = 3,
      secondRadicalIndex = 4,
      thirdRadicalIndex = 5,
      arabicLetters = Seq(
        ArabicLetters.AlifHamzaBelowWithKasra,
        ArabicLetters.SeenWithSukun,
        ArabicLetters.TaWithFatha,
        ArabicLetters.AinWithSukun,
        ArabicLetters.LamWithFatha,
        ArabicLetters.WawWithSukun,
        ArabicLetters.TaWithDamma
      )*
    )
    val expected = ArabicWord(
      ArabicLetters.AlifHamzaBelowWithKasra,
      ArabicLetters.SeenWithSukun,
      ArabicLetters.TaWithFatha,
      ArabicLetters.AinWithSukun,
      ArabicLetters.LamWithFatha,
      ArabicLetters.YaWithSukun,
      ArabicLetters.TaWithDamma
    )
    validate(baseWord, expected, HiddenPronounStatus.SecondPersonMasculineSingular, processingContext)
  }
}
