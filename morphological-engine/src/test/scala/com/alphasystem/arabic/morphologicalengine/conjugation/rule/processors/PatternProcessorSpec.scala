package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import com.alphasystem.arabic.model.{ ArabicLetters, ArabicWord }
import munit.FunSuite

class PatternProcessorSpec extends FunSuite {

  test("Remove consecutive sukun") {
    val text = ArabicWord(
      ArabicLetters.MeemWithDamma,
      ArabicLetters.WawWithSukun,
      ArabicLetters.SeenWithSukun,
      ArabicLetters.LamWithKasra,
      ArabicLetters.YaWithSukun,
      ArabicLetters.MeemWithSukun
    )

    val expected = ArabicWord(
      ArabicLetters.MeemWithDamma,
      ArabicLetters.SeenWithSukun,
      ArabicLetters.LamWithKasra,
      ArabicLetters.MeemWithSukun
    )

    val obtained = ArabicWord(PatternProcessor.removeConsecutiveSukun(text.code), false)
    assertEquals(obtained, expected)
  }
}
