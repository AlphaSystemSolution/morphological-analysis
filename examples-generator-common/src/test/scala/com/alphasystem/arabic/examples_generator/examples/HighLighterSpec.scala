package com.alphasystem
package arabic
package examples_generator
package examples

import examples_generator.model.Color.*
import examples_generator.model.{ HighlightRange, TokenHighLight }
import munit.FunSuite

import java.util.UUID

class HighLighterSpec extends FunSuite {

  private val defaultText = "The quick brown fox jumps over the lazy dog."

  test("getTokensWithinBound: Creates tokens within bound") {
    val tokens = (0 until 5).map(_.toString)
    // text is 01234

    def testGetTokensWithinBound(start: Int, end: Int, expected: String): Unit = {
      assertEquals(HighLighter.getTokensWithinBound(start, end, tokens), expected)
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

  private val id: UUID = UUID.randomUUID()
  // (highlight, test description, expected text)
  private val encodingTestData = Seq(
    (Nil, "No highlights, returns text without any change", defaultText),
    (
      List(
        HighlightRange(id, TokenHighLight(1), TokenHighLight(1), Some(Red)),
        HighlightRange(id, TokenHighLight(2), TokenHighLight(3), Some(Green)),
        HighlightRange(id, TokenHighLight(5, Some(2)), TokenHighLight(6, Some(2)), Some(Blue)),
        HighlightRange(id, TokenHighLight(6, Some(3)), TokenHighLight(8, Some(1)), Some(Magenta))
      ),
      "Mixed highlights, returns text with highlights encoded",
      "[red]##The## [green]##quick brown## fox j[blue]##umps ov##[magenta]##er the l##azy dog."
    ),
    (
      List(
        HighlightRange(id, TokenHighLight(1), TokenHighLight(3)),
        HighlightRange(id, TokenHighLight(5, Some(2)), TokenHighLight(8, Some(1)))
      ),
      "Mixed highlights with default markup, returns text with highlights encoded",
      "##The quick brown## fox j##umps over the l##azy dog."
    ),
    (
      List(
        HighlightRange(id, TokenHighLight(1), TokenHighLight(1), Some(Teal)),
        HighlightRange(id, TokenHighLight(9), TokenHighLight(9), Some(Cyan))
      ),
      "Highlights at the beginning and the end (end index is provided), returns text with highlights encoded",
      "[teal]##The## quick brown fox jumps over the lazy [cyan]##dog.##"
    ),
    (
      List(
        HighlightRange(id, TokenHighLight(7), TokenHighLight(-1), Some(Magenta))
      ),
      "Highlights last few at the end where end index is -1, should create markup accordingly",
      "The quick brown fox jumps over [magenta]##the lazy dog.##"
    ),
    (
      List(
        HighlightRange(id, TokenHighLight(2), TokenHighLight(3), Some(Green)),
        HighlightRange(id, TokenHighLight(5), TokenHighLight(6), Some(Fuchsia))
      ),
      "No highlights at the beginning and the end, returns text with highlights encoded",
      "The [green]##quick brown## fox [fuchsia]##jumps over## the lazy dog."
    ),
    (
      List(HighlightRange(id, TokenHighLight(2, Some(2)), TokenHighLight(2, Some(4)), Some(Red))),
      "Partial highlight within a single token, returns only selected characters highlighted",
      "The q[red]##uic##k brown fox jumps over the lazy dog."
    ),
    (
      List(HighlightRange(id, TokenHighLight(2, Some(3)), TokenHighLight(2), Some(Blue))),
      "Partial highlight with omitted end location, highlights through the end of the token",
      "The qu[blue]##ick## brown fox jumps over the lazy dog."
    ),
    (
      List(HighlightRange(id, TokenHighLight(2, Some(2)), TokenHighLight(2, Some(-1)), Some(Blue))),
      "Partial highlight with end location -1, highlights through the end of the token",
      "The q[blue]##uick## brown fox jumps over the lazy dog."
    ),
    (
      List(HighlightRange(id, TokenHighLight(5, Some(2)), TokenHighLight(6, Some(2)))),
      "Partial multi-token highlight with default markup, returns the selected range highlighted",
      "The quick brown fox j##umps ov##er the lazy dog."
    ),
    (
      List(
        HighlightRange(id, TokenHighLight(2), TokenHighLight(2), Some(Green)),
        HighlightRange(id, TokenHighLight(3), TokenHighLight(3), Some(Blue))
      ),
      "Adjacent token highlights, keeps both highlighted ranges separate",
      "The [green]##quick## [blue]##brown## fox jumps over the lazy dog."
    )
  )

  encodingTestData.foreach { case (highlights, description, expected) =>
    test(s"Process Highlights: $description") {
      HighLighter.disableEncoding()
      assertEquals(HighLighter.processHighlights(defaultText, highlights), expected)
    }
  }

  test("Process Highlights: Blank text, returns blank text") {
    HighLighter.disableEncoding()
    assertEquals(HighLighter.processHighlights("   ", Nil), "")
  }
}
