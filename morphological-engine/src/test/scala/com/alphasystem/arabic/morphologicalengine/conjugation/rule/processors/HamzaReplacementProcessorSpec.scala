package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{ ArabicLetterType, ArabicLetters, ArabicWord, DiacriticType }
import conjugation.forms.{ noun, verb }
import conjugation.model.RootWord
import munit.FunSuite

class HamzaReplacementProcessorSpec extends FunSuite {

  private val processor = HamzaReplacementProcessor()

  test("Fatha as first letter") {
    val rootWord =
      verb.FormIV.PastTense.rootWord.transform(ArabicLetterType.Seen, ArabicLetterType.Lam, ArabicLetterType.Meem)

    val expected = ArabicWord(
      ArabicLetters.AlifHamzaAboveWithFatha,
      ArabicLetters.SeenWithSukun,
      ArabicLetters.LamWithFatha,
      ArabicLetters.MeemWithFatha
    )

    validate(rootWord, expected)
  }

  test("Damma as first letter") {
    val rootWord =
      verb
        .FormIV
        .PastPassiveTense
        .rootWord
        .transform(ArabicLetterType.Seen, ArabicLetterType.Lam, ArabicLetterType.Meem)

    val expected = ArabicWord(
      ArabicLetters.AlifHamzaAboveWithDamma,
      ArabicLetters.SeenWithSukun,
      ArabicLetters.LamWithKasra,
      ArabicLetters.MeemWithFatha
    )

    validate(rootWord, expected)
  }

  test("Kasra as first letter") {
    val rootWord =
      verb
        .FormX
        .PastTense
        .rootWord
        .transform(ArabicLetterType.Ghain, ArabicLetterType.Fa, ArabicLetterType.Ra)

    val expected = ArabicWord(
      ArabicLetters.AlifHamzaBelowWithKasra,
      ArabicLetters.SeenWithSukun,
      ArabicLetters.TaWithFatha,
      ArabicLetters.GhainWithSukun,
      ArabicLetters.FaWithFatha,
      ArabicLetters.RaWithFatha
    )

    validate(rootWord, expected)
  }

  test("Hamzah in the middle") {
    val rootWord =
      verb.FormVI.PresentTense.rootWord.transform(ArabicLetterType.Seen, ArabicLetterType.Hamza, ArabicLetterType.Lam)

    val expected = ArabicWord(
      ArabicLetters.YaWithFatha,
      ArabicLetters.TaWithFatha,
      ArabicLetters.SeenWithFatha,
      ArabicLetters.LetterAlif,
      ArabicLetters.YaHamzaAboveWithFatha,
      ArabicLetters.LamWithDamma
    )

    validate(rootWord, expected)
  }

  test("Two consecutive Hamza's at the beginning (Fatha)") {
    val rootWord =
      verb.FormIV.PastTense.rootWord.transform(ArabicLetterType.Hamza, ArabicLetterType.Meem, ArabicLetterType.Noon)

    val expected = ArabicWord(
      ArabicLetters.LetterAlifMaddah,
      ArabicLetters.MeemWithFatha,
      ArabicLetters.NoonWithFatha
    )

    validate(rootWord, expected)
  }

  test("Two consecutive Hamza's at the beginning (Kasra)") {
    val rootWord =
      noun.VerbalNoun.FormIV.rootWord.transform(ArabicLetterType.Hamza, ArabicLetterType.Meem, ArabicLetterType.Noon)

    val expected = ArabicWord(
      ArabicLetters.AlifHamzaBelowWithKasra,
      ArabicLetters.YaWithSukun,
      ArabicLetters.MeemWithFatha,
      ArabicLetters.LetterAlif,
      ArabicLetters.NoonWithDammatan
    )

    validate(rootWord, expected)
  }

  test("Two consecutive Hamza's at the beginning (Damma)") {
    val rootWord =
      verb
        .FormIV
        .PastPassiveTense
        .rootWord
        .transform(ArabicLetterType.Hamza, ArabicLetterType.Meem, ArabicLetterType.Noon)

    val expected = ArabicWord(
      ArabicLetters.AlifHamzaAboveWithDamma,
      ArabicLetters.WawWithSukun,
      ArabicLetters.MeemWithKasra,
      ArabicLetters.NoonWithFatha
    )

    validate(rootWord, expected)
  }

  private def validate(rootWord: RootWord, expected: ArabicWord): Unit = {
    val word = processor.applyRules(rootWord).derivedWord
    assertEquals(word, expected)
  }
}
