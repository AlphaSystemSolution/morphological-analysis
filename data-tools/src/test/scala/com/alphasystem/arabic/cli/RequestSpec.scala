package com.alphasystem
package arabic
package cli

import com.alphasystem.arabic.cli.asciidoc.toDataRequest
import munit.FunSuite

import java.nio.file.Paths
import scala.util.{ Failure, Success, Try }

class RequestSpec extends FunSuite {

  test("Validate verse numbers are in consecutive") {
    Try(SearchRequest(chapterNumber = 1, verses = Seq(VerseSearch(1), VerseSearch(2), VerseSearch(3)))) match
      case Failure(ex) => fail(ex.getMessage, ex)
      case Success(_)  =>
  }

  test("Validate verse numbers for unordered sequence") {
    Try(SearchRequest(chapterNumber = 1, verses = Seq(VerseSearch(4), VerseSearch(5), VerseSearch(3)))) match
      case Failure(ex) => fail(ex.getMessage, ex)
      case Success(_)  =>
  }

  test("Fail when verse numbers are not consecutive") {
    Try(SearchRequest(chapterNumber = 1, verses = Seq(VerseSearch(1), VerseSearch(3), VerseSearch(4)))) match
      case Failure(ex) => assertEquals(ex.getMessage, "requirement failed")
      case Success(_)  => fail("")
  }
}
