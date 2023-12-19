package com.alphasystem
package arabic
package model

import munit.FunSuite

class ArabicWordSpec extends FunSuite {

  test("Create empty ArabicWord") {
    assertEquals(ArabicWord().code, "")
  }

  test("ArabicWord should be created") {
    assertEquals(
      ArabicWord(
        ArabicLetters.LetterAlif,
        ArabicLetter(ArabicLetterType.Seen),
        ArabicLetter(ArabicLetterType.Meem)
      ).code,
      "Asm"
    )
  }

  test("Concatenate two ArabicWords with space") {
    val word1 = ArabicWord(
      ArabicLetters.LetterAlif,
      ArabicLetter(ArabicLetterType.Seen),
      ArabicLetter(ArabicLetterType.Meem)
    )

    val word2 =
      ArabicWord(
        ArabicLetter(ArabicLetterType.Fa),
        ArabicLetters.LetterAlif,
        ArabicLetter(ArabicLetterType.Ain)
      )

    assertEquals(word1.concatWithSpace(word2).code, "Asm fAE")
  }

  test("Concatenate two ArabicWords with waw") {
    val word1 = ArabicWord(
      ArabicLetters.LetterAlif,
      ArabicLetter(ArabicLetterType.Seen),
      ArabicLetter(ArabicLetterType.Meem)
    )

    val word2 =
      ArabicWord(
        ArabicLetter(ArabicLetterType.Fa),
        ArabicLetters.LetterAlif,
        ArabicLetter(ArabicLetterType.Ain)
      )

    assertEquals(word1.concatenateWithAnd(word2).code, "Asm w fAE")
  }

  test("Convert string into ArabicWord without diacritics using unicode") {
    val source = "العريبية"
    val arabicWord = ArabicWord(source)
    val expected = ArabicWord(
      ArabicLetterType.Alif,
      ArabicLetterType.Lam,
      ArabicLetterType.Ain,
      ArabicLetterType.Ra,
      ArabicLetterType.Ya,
      ArabicLetterType.Ba,
      ArabicLetterType.Ya,
      ArabicLetterType.TaMarbuta
    )
    assertEquals(arabicWord, expected)
    assertEquals(arabicWord.unicode, source)
  }

  test("Convert string into ArabicWord with diacritics using unicode") {
    val source = "أَلْشَّمْسُ"
    val arabicWord = ArabicWord(source)
    val expected = ArabicWord(
      Seq(
        ArabicLetter(
          ArabicLetterType.AlifHamzaAbove,
          Seq(DiacriticType.Fatha)*
        ),
        ArabicLetter(ArabicLetterType.Lam, Seq(DiacriticType.Sukun)*),
        ArabicLetter(
          ArabicLetterType.Sheen,
          Seq(DiacriticType.Shadda, DiacriticType.Fatha)*
        ),
        ArabicLetter(ArabicLetterType.Meem, Seq(DiacriticType.Sukun)*),
        ArabicLetter(ArabicLetterType.Seen, Seq(DiacriticType.Damma)*)
      )*
    )
    assertEquals(arabicWord, expected)
    assertEquals(arabicWord.unicode, source)
  }

  test(
    "Convert string into ArabicWord with diacritics using unicode starting with diacritic"
  ) {
    val source = "ْسمْ"
    val arabicWord = ArabicWord(source)
    val expected = ArabicWord(
      Seq(
        ArabicLetter(ArabicLetterType.Seen, Seq(DiacriticType.Sukun)*),
        ArabicLetter(ArabicLetterType.Meem, Seq(DiacriticType.Sukun)*)
      )*
    )
    assertEquals(arabicWord, expected)
  }

  test("Convert integer to ArabicWord") {
    assertEquals(ArabicWord(273), ArabicWord(ArabicLetterType.Two, ArabicLetterType.Seven, ArabicLetterType.Three))
    assertEquals(ArabicWord(27), ArabicWord(ArabicLetterType.Two, ArabicLetterType.Seven))
    assertEquals(ArabicWord(2), ArabicWord(ArabicLetterType.Two))
  }

}
