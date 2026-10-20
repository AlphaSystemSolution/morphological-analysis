package com.alphasystem
package arabic
package model

import model.ArabicLetterType.*
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

  // (number, test description, expected result)
  private val numbersData = Seq(
    (1, "Single digit", ArabicWord(One)),
    (20, "Two digit with unit number is zero", ArabicWord(Two, Zero)),
    (35, "Two digit number", ArabicWord(Three, Five)),
    (479, "Three digit number", ArabicWord(Four, Seven, Nine))
  )

  numbersData.foreach { case (number, description, expected) =>
    test(s"Process Numbers: $description") {
      assertEquals(ArabicWord(number), expected)
    }
  }

}
