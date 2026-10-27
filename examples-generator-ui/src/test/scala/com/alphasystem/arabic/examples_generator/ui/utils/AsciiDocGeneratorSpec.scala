package com.alphasystem
package arabic
package examples_generator
package ui
package utils

import examples_generator.model.*
import munit.FunSuite

import java.nio.file.Files
import java.util.UUID

class AsciiDocGeneratorSpec extends FunSuite {

  private val id = UUID.randomUUID()
  private val separatorValue = (separator: String) => if separator == "!" then s""", separator="$separator"""" else ""

  private val emptyTableData = {
    val exampleData = ExampleData(
      Seq(
        TableData(
          id = id,
          tableProperties = TableProperties(
            tag = "test",
            columns = Seq(
              TableColumnInfo(
                id = id,
                width = 25,
                horizontalAlignment = HorizontalAlignment.Left,
                verticalAlignment = VerticalAlignment.Center
              ),
              TableColumnInfo(
                id = id,
                width = 20,
                horizontalAlignment = HorizontalAlignment.Right,
                verticalAlignment = VerticalAlignment.Top
              ),
              TableColumnInfo(
                id = id,
                width = 55,
                horizontalAlignment = HorizontalAlignment.Center,
                verticalAlignment = VerticalAlignment.Bottom
              )
            ),
            frame = Frame.All,
            grid = Grid.All
          )
        )
      )
    )

    val expected = (separator: String) =>
      Seq(
        DocumentGenerator.DefaultAttributes,
        "// tag::test[]",
        s"""[cols="<.^25%,>.<20%,^.>55%", align="center", halign="center", valign="center", frame="all", grid="all"${separatorValue(
          separator
        )}]""",
        s"$separator===",
        "",
        s"$separator===",
        "// end::test[]",
        ""
      )

    Seq(
      ("DocumentGenerator: should generate table, default table", false, exampleData, expected("|")),
      ("DocumentGenerator: should generate table, nested table", true, exampleData, expected("!"))
    )
  }

  emptyTableData.foreach { case (name, nested, exampleData, expected) =>
    test(name) {
      val obtained = DocumentGenerator.buildTable(exampleData, nested)
      assertEquals(obtained, expected)
    }
  }

  private val buildRowData = {
    val arabicColumn = ColumnData(id, ColumnType.Arabic, text = "First")
    val translationColumn = ColumnData(id, ColumnType.Translation, text = "Second")
    val otherColumn = ColumnData(id, ColumnType.Other, text = "Third")
    val highlightedColumn = ColumnData(
      id,
      ColumnType.Arabic,
      text = "First second",
      highlights = Seq(HighlightRange(id, TokenHighLight(1), TokenHighLight(1)))
    )

    val multiColumnRow = RowData(id = id, columns = Seq(arabicColumn, translationColumn, otherColumn))
    val emptyRow = RowData(id = id)
    val highlightedRow = RowData(id = id, columns = Seq(highlightedColumn))

    Seq(
      (
        "DocumentGenerator: should build row, default separator",
        "|",
        multiColumnRow,
        Seq("// Row 1", "|[arabicNormal]#First#", "|[translation]#Second#", "|Third", "")
      ),
      (
        "DocumentGenerator: should build row, nested separator",
        "!",
        multiColumnRow,
        Seq("// Row 1", "![arabicNormal]#First#", "![translation]#Second#", "!Third", "")
      ),
      ("DocumentGenerator: should build empty row", "|", emptyRow, Seq("// Row 1", "")),
      (
        "DocumentGenerator: should build row with highlighted column",
        "|",
        highlightedRow,
        Seq("// Row 1", "|[arabicNormal]#{nbsp}##First## second#", "")
      )
    )
  }

  buildRowData.foreach { case (name, separator, row, expected) =>
    test(name) {
      HighLighter.disableEncoding()
      val obtained = DocumentGenerator.buildRow(separator)(row, 0)
      assertEquals(obtained, expected)
    }
  }

  private val generateDocumentData = {
    val exampleData = ExampleData(
      Seq(
        TableData(
          id = id,
          tableProperties = TableProperties(
            tag = "test",
            columns = Seq(
              TableColumnInfo(
                id = id,
                width = 25,
                horizontalAlignment = HorizontalAlignment.Left,
                verticalAlignment = VerticalAlignment.Center
              ),
              TableColumnInfo(
                id = id,
                width = 20,
                horizontalAlignment = HorizontalAlignment.Right,
                verticalAlignment = VerticalAlignment.Top
              ),
              TableColumnInfo(
                id = id,
                width = 55,
                horizontalAlignment = HorizontalAlignment.Center,
                verticalAlignment = VerticalAlignment.Bottom
              )
            ),
            frame = Frame.All,
            grid = Grid.All
          )
        )
      )
    )

    val expected = (separator: String) =>
      Seq(
        DocumentGenerator.DefaultAttributes,
        "// tag::test[]",
        s"""[cols="<.^25%,>.<20%,^.>55%", align="center", halign="center", valign="center", frame="all", grid="all"${separatorValue(
          separator
        )}]""",
        s"$separator===",
        "",
        s"$separator===",
        "// end::test[]",
        ""
      ).mkString("", System.lineSeparator(), System.lineSeparator())

    Seq(
      ("DocumentGenerator: should generate document, default table", false, exampleData, expected("|")),
      ("DocumentGenerator: should generate document, nested table", true, exampleData, expected("!"))
    )
  }

  generateDocumentData.foreach { case (name, nested, exampleData, expected) =>
    test(name) {
      val destPath = Files.createTempFile("generate-document", ".adoc")
      try {
        val obtainedPath = DocumentGenerator.generateDocument(exampleData, destPath, nested)
        assertEquals(obtainedPath, destPath)
        val obtained = Files.readString(destPath)
        assertEquals(obtained, expected)
      } finally {
        Files.deleteIfExists(destPath)
      }
    }
  }
}
