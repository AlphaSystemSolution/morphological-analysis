package com.alphasystem
package arabic
package cli
package examples

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

  private val encodingTestData = Seq(
    (Nil, "No highlights, returns text without any change", defaultText.replace(" ", "{nbsp}")),
    (
      List(
        Highlight(Token(1), Token(1), Some("red")),
        Highlight(Token(2), Token(3), Some("green")),
        Highlight(Token(5, Some(2)), Token(6, Some(2)), Some("blue")),
        Highlight(Token(6, Some(3)), Token(8, Some(1)), Some("magenta"))
      ),
      "Mixed highlights, returns text with highlights encoded",
      "[red]##The##{nbsp}[green]##quick{nbsp}brown##{nbsp}fox{nbsp}j[blue]##umps{nbsp}ov##[magenta]##er{nbsp}the{nbsp}l##azy{nbsp}dog."
    ),
    (
      List(
        Highlight(Token(1), Token(3)),
        Highlight(Token(5, Some(2)), Token(8, Some(1)))
      ),
      "Mixed highlights with default markup, returns text with highlights encoded",
      "##The{nbsp}quick{nbsp}brown##{nbsp}fox{nbsp}j##umps{nbsp}over{nbsp}the{nbsp}l##azy{nbsp}dog."
    ),
    (
      List(
        Highlight(Token(1), Token(1), Some("teal")),
        Highlight(Token(9), Token(9), Some("cyan"))
      ),
      "Highlights at the beginning and the end, returns text with highlights encoded",
      "[teal]##The##{nbsp}quick{nbsp}brown{nbsp}fox{nbsp}jumps{nbsp}over{nbsp}the{nbsp}lazy{nbsp}[cyan]##dog.##"
    )
  )

  encodingTestData.foreach { case (highlights, description, expected) =>
    test(s"Encoding: $description") {
      println()
      println("=" * 100)
      // RowGenerator.disableEncoding()
      val obtained = RowGenerator.encode(defaultText, highlights)
      println(s"Obtained: $obtained")
      assertEquals(obtained, expected)
      println("=" * 100)
      println()
    }
  }

  private def encode(token: String) = arabic.model.toHtmlCodeString(token)
}
