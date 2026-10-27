package com.alphasystem
package arabic
package examples_generator
package asciidoc_generator

import examples_generator.model.Color.*
import examples_generator.model.*
import munit.FunSuite

import java.util.UUID

class ColumnGeneratorSpec extends FunSuite {

  private val defaultText = "The quick brown fox jumps over the lazy dog."
  private val id = UUID.randomUUID()

  private val columnData = Seq(
    (
      "ColumnGenerator: should build Arabic column",
      ColumnData(id = id, `type` = ColumnType.Arabic, text = defaultText),
      "|",
      s"|[arabicNormal]#$defaultText#"
    ),
    (
      "ColumnGenerator: should build ArabicBold column",
      ColumnData(id = id, `type` = ColumnType.ArabicBold, text = defaultText),
      "|",
      s"|[arabicBold]#$defaultText#"
    ),
    (
      "ColumnGenerator: should build ArabicSmall column",
      ColumnData(id = id, `type` = ColumnType.ArabicSmall, text = defaultText),
      "|",
      s"|[arabicSmall]#$defaultText#"
    ),
    (
      "ColumnGenerator: should build ArabicTableCaption column",
      ColumnData(id = id, `type` = ColumnType.ArabicTableCaption, text = defaultText),
      "|",
      s"|[arabicTableCaption]#$defaultText#"
    ),
    (
      "ColumnGenerator: should build Translation column",
      ColumnData(id = id, `type` = ColumnType.Translation, text = defaultText),
      "|",
      s"|[translation]#$defaultText#"
    ),
    (
      "ColumnGenerator: should build Other column",
      ColumnData(id = id, `type` = ColumnType.Other, text = defaultText),
      "|",
      s"|$defaultText"
    ),
    (
      "ColumnGenerator: should build Arabic column with nested separator",
      ColumnData(id = id, `type` = ColumnType.Arabic, text = defaultText),
      "!",
      s"![arabicNormal]#$defaultText#"
    ),
    (
      "ColumnGenerator: should build Arabic column with prefix",
      ColumnData(
        id = id,
        `type` = ColumnType.Arabic,
        text = defaultText,
        settings = Some(ColumnSettings(colSpan = 2, rowSpan = 2))
      ),
      "|",
      s"2.2+|[arabicNormal]#$defaultText#"
    )
  )

  columnData.foreach { case (name, data, separator, expected) =>
    test(name) {
      HighLighter.disableEncoding()
      val obtained = ColumnGenerator.buildColumn(separator)(data)
      assertEquals(obtained, expected)
    }
  }

  test("ColumnGenerator: should insert non-breaking space at start when highlighted text begins with markup") {
    HighLighter.disableEncoding()
    val data = ColumnData(
      id = id,
      `type` = ColumnType.Arabic,
      text = defaultText,
      highlights = Seq(HighlightRange(UUID.randomUUID(), TokenHighLight(1), TokenHighLight(1)))
    )
    val obtained = ColumnGenerator.buildColumn("|")(data)
    assertEquals(obtained, "|[arabicNormal]#{nbsp}##The## quick brown fox jumps over the lazy dog.#")
  }

  test("ColumnGenerator: should insert non-breaking space at end when highlighted text ends with markup") {
    HighLighter.disableEncoding()
    val data = ColumnData(
      id = id,
      `type` = ColumnType.Arabic,
      text = defaultText,
      highlights = Seq(HighlightRange(UUID.randomUUID(), TokenHighLight(9), TokenHighLight(9)))
    )
    val obtained = ColumnGenerator.buildColumn("|")(data)
    assertEquals(obtained, "|[arabicNormal]#The quick brown fox jumps over the lazy ##dog.##{nbsp}#")
  }

  test("ColumnGenerator: should insert non-breaking space at start for colored highlight") {
    HighLighter.disableEncoding()
    val data = ColumnData(
      id = id,
      `type` = ColumnType.Arabic,
      text = defaultText,
      highlights = Seq(HighlightRange(UUID.randomUUID(), TokenHighLight(1), TokenHighLight(1), Some(Red)))
    )
    val obtained = ColumnGenerator.buildColumn("|")(data)
    assertEquals(obtained, "|[arabicNormal]#{nbsp}[red]##The## quick brown fox jumps over the lazy dog.#")
  }

  test("ColumnGenerator: should highlight to end of text when end token index is -1") {
    HighLighter.disableEncoding()
    val data = ColumnData(
      id = id,
      `type` = ColumnType.Arabic,
      text = defaultText,
      highlights = Seq(HighlightRange(UUID.randomUUID(), TokenHighLight(7), TokenHighLight(-1), Some(Magenta)))
    )
    val obtained = ColumnGenerator.buildColumn("|")(data)
    assertEquals(obtained, "|[arabicNormal]#The quick brown fox jumps over [magenta]##the lazy dog.##{nbsp}#")
  }

  test("ColumnGenerator: should highlight to end of token when end location index is -1") {
    HighLighter.disableEncoding()
    val data = ColumnData(
      id = id,
      `type` = ColumnType.Arabic,
      text = defaultText,
      highlights =
        Seq(HighlightRange(UUID.randomUUID(), TokenHighLight(2, Some(2)), TokenHighLight(2, Some(-1)), Some(Blue)))
    )
    val obtained = ColumnGenerator.buildColumn("|")(data)
    assertEquals(obtained, "|[arabicNormal]#The q[blue]##uick## brown fox jumps over the lazy dog.#")
  }
}
