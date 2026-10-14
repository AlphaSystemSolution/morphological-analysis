package com.alphasystem
package arabic

import io.circe.generic.auto.*
import munit.FunSuite

class MultilineYamlSpec extends FunSuite {

  private case class Sample(text: String, items: List[String])

  test("multiline text is written as literal block with empty lines") {
    val text = "[S]#{nbsp}#\\n\n   \n[S]#Connection#  \nz"
    val yaml = toMultilineYaml(Sample(text, Nil))
    assert(yaml.contains("text: |-"), yaml)
    assert(!yaml.contains("\"") && !yaml.contains("\\\\n\\n"), yaml)
    assertEquals(yaml.linesIterator.toList.slice(1, 5), List("  [S]#{nbsp}#\\n", "", "  [S]#Connection#", "  z"))
  }

  test("multiline text round trips with trailing whitespace and CRLF normalized before line breaks") {
    val sample = Sample("a\r\n  \r\nb  ", List("x\n\ny"))
    val result = fromString[Sample](toMultilineYaml(sample))
    assertEquals(result, Sample("a\n\nb  ", List("x\n\ny")))
  }

  test("tabs do not force escaped string") {
    val yaml = toMultilineYaml(Sample("x\n\t\ny\n\tz", Nil))
    assert(yaml.contains("text: |-"), yaml)
    assertEquals(fromString[Sample](yaml), Sample("x\n\ny\n    z", Nil))
  }

  test("single line text is unchanged") {
    assertEquals(fromString[Sample](toMultilineYaml(Sample("abc", List("d")))), Sample("abc", List("d")))
  }
}
