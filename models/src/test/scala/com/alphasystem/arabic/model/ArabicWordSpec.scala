package com.alphasystem.arabic.model

import munit.FunSuite

class ArabicWordSpec extends FunSuite {

  test("Create empty ArabicWord") {
    assertEquals(ArabicWord().code, "")
  }

  test("ArabicWord should be created") {
    assertEquals(
      ArabicWord(
        ArabicLetters.LETTER_ALIF,
        ArabicLetter(ArabicLetterType.SEEN),
        ArabicLetter(ArabicLetterType.MEEM)
      ).code,
      "Asm"
    )
  }

  test("Concatenate two ArabicWords with space") {
    val word1 = ArabicWord(
      ArabicLetters.LETTER_ALIF,
      ArabicLetter(ArabicLetterType.SEEN),
      ArabicLetter(ArabicLetterType.MEEM)
    )

    val word2 =
      ArabicWord(
        ArabicLetter(ArabicLetterType.FA),
        ArabicLetters.LETTER_ALIF,
        ArabicLetter(ArabicLetterType.AIN)
      )

    assertEquals(word1.concatWithSpace(word2).code, "Asm fAE")
  }

  test("Concatenate two ArabicWords with waw") {
    val word1 = ArabicWord(
      ArabicLetters.LETTER_ALIF,
      ArabicLetter(ArabicLetterType.SEEN),
      ArabicLetter(ArabicLetterType.MEEM)
    )

    val word2 =
      ArabicWord(
        ArabicLetter(ArabicLetterType.FA),
        ArabicLetters.LETTER_ALIF,
        ArabicLetter(ArabicLetterType.AIN)
      )

    assertEquals(word1.concatenateWithAnd(word2).code, "Asm w fAE")
  }

  test("Convert string into ArabicWord without diacritics using unicode") {
    val source = "العريبية"
    val arabicWord = ArabicWord(source)
    val expected = ArabicWord(
      ArabicLetterType.ALIF,
      ArabicLetterType.LAM,
      ArabicLetterType.AIN,
      ArabicLetterType.RA,
      ArabicLetterType.YA,
      ArabicLetterType.BA,
      ArabicLetterType.YA,
      ArabicLetterType.TA_MARBUTA
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
          ArabicLetterType.ALIF_HAMZA_ABOVE,
          Seq(DiacriticType.FATHA)*
        ),
        ArabicLetter(ArabicLetterType.LAM, Seq(DiacriticType.SUKUN)*),
        ArabicLetter(
          ArabicLetterType.SHEEN,
          Seq(DiacriticType.SHADDA, DiacriticType.FATHA)*
        ),
        ArabicLetter(ArabicLetterType.MEEM, Seq(DiacriticType.SUKUN)*),
        ArabicLetter(ArabicLetterType.SEEN, Seq(DiacriticType.DAMMA)*)
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
        ArabicLetter(ArabicLetterType.SEEN, Seq(DiacriticType.SUKUN)*),
        ArabicLetter(ArabicLetterType.MEEM, Seq(DiacriticType.SUKUN)*)
      )*
    )
    assertEquals(arabicWord, expected)
  }

}
