package com.alphasystem
package arabic
package cli
package examples

import com.alphasystem.arabic.model.ArabicLetterType.{Five, Four, Nine, One, Seven, Three, Two, Zero}
import com.alphasystem.arabic.model.ArabicWord
import munit.FunSuite

class ExampleGeneratorSpec extends FunSuite {

  private val defaultText = "The quick brown fox jumps over the lazy dog."

  test("RowGenerator: getTokensWithinBound: Creates tokens within bound") {
    val tokens = (0 until 5).map(_.toString)
    // text is 01234

    def testGetTokensWithinBound(start: Int, end: Int, expected: String): Unit = {
      assertEquals(RowGenerator.getTokensWithinBound(start, end, tokens), expected)
    }

    testGetTokensWithinBound(1, 2, "01")
    testGetTokensWithinBound(3, 3, "2")
    testGetTokensWithinBound(1, 5, "01234")
    testGetTokensWithinBound(1, -1, "01234")

    // start location is 3
    testGetTokensWithinBound(1, 2, "01") // token before
    testGetTokensWithinBound(3, -1, "234") // tokens from 3 (start location) to end

    // end location is 3
    testGetTokensWithinBound(1, 3, "012")
    testGetTokensWithinBound(4, -1, "34")
  }

  // (highlight, test description, expected text)
  private val encodingTestData = Seq(
    (Nil, "No highlights, returns text without any change", defaultText),
    (
      List(
        Highlight(Token(1), Token(1), Some("red")),
        Highlight(Token(2), Token(3), Some("green")),
        Highlight(Token(5, Some(2)), Token(6, Some(2)), Some("blue")),
        Highlight(Token(6, Some(3)), Token(8, Some(1)), Some("magenta"))
      ),
      "Mixed highlights, returns text with highlights encoded",
      "[red]##The## [green]##quick brown## fox j[blue]##umps ov##[magenta]##er the l##azy dog."
    ),
    (
      List(
        Highlight(Token(1), Token(3)),
        Highlight(Token(5, Some(2)), Token(8, Some(1)))
      ),
      "Mixed highlights with default markup, returns text with highlights encoded",
      "##The quick brown## fox j##umps over the l##azy dog."
    ),
    (
      List(
        Highlight(Token(1), Token(1), Some("teal")),
        Highlight(Token(9), Token(9), Some("cyan"))
      ),
      "Highlights at the beginning and the end (end index is provided), returns text with highlights encoded",
      "[teal]##The## quick brown fox jumps over the lazy [cyan]##dog.##"
    ),
    (
      List(
        Highlight(Token(7), Token(-1), Some("magenta"))
      ),
      "Highlights last few at the end where end index is -1, should create markup accordingly",
      "The quick brown fox jumps over [magenta]##the lazy dog.##"
    ),
    (
      List(
        Highlight(Token(2), Token(3), Some("green")),
        Highlight(Token(5), Token(6), Some("fuchsia"))
      ),
      "No highlights at the beginning and the end, returns text with highlights encoded",
      "The [green]##quick brown## fox [fuchsia]##jumps over## the lazy dog."
    ),
    (
      List(Highlight(Token(2, Some(2)), Token(2, Some(4)), Some("red"))),
      "Partial highlight within a single token, returns only selected characters highlighted",
      "The q[red]##uic##k brown fox jumps over the lazy dog."
    ),
    (
      List(Highlight(Token(2, Some(3)), Token(2), Some("blue"))),
      "Partial highlight with omitted end location, highlights through the end of the token",
      "The qu[blue]##ick## brown fox jumps over the lazy dog."
    ),
    (
      List(Highlight(Token(5, Some(2)), Token(6, Some(2)))),
      "Partial multi-token highlight with default markup, returns the selected range highlighted",
      "The quick brown fox j##umps ov##er the lazy dog."
    ),
    (
      List(
        Highlight(Token(2), Token(2), Some("green")),
        Highlight(Token(3), Token(3), Some("blue"))
      ),
      "Adjacent token highlights, keeps both highlighted ranges separate",
      "The [green]##quick## [blue]##brown## fox jumps over the lazy dog."
    )
  )

  encodingTestData.foreach { case (highlights, description, expected) =>
    test(s"Process Highlights: $description") {
      RowGenerator.disableEncoding()
      assertEquals(RowGenerator.processText(defaultText, highlights), expected)
    }
  }

  test("Process Highlights: Blank text, returns blank text") {
    RowGenerator.disableEncoding()
    assertEquals(RowGenerator.processText("   ", Nil), "")
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
      assertEquals(RowGenerator.toArabicNumber(number), expected)
    }
  }

}
