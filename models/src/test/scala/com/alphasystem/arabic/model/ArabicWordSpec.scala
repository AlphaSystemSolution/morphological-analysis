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

}
