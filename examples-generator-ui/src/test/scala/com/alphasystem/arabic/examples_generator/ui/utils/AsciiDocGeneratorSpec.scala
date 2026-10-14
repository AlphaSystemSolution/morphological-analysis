package com.alphasystem
package arabic
package examples_generator
package ui
package utils

import ui.model.*
import munit.FunSuite

import java.util.UUID

class AsciiDocGeneratorSpec extends FunSuite {

  private val separatorValue = (separator: String) => if separator == "!" then s""", separator="$separator"""" else ""

  private val emptyTableData = {
    val exampleData = ExampleData(
      Seq(
        TableData(
          id = UUID.randomUUID(),
          tableProperties = TableProperties(
            tag = "test",
            columns = Seq(
              TableColumnInfo(
                id = UUID.randomUUID(),
                width = 25,
                horizontalAlignment = HorizontalAlignment.Left,
                verticalAlignment = VerticalAlignment.Center
              ),
              TableColumnInfo(
                id = UUID.randomUUID(),
                width = 20,
                horizontalAlignment = HorizontalAlignment.Right,
                verticalAlignment = VerticalAlignment.Top
              ),
              TableColumnInfo(
                id = UUID.randomUUID(),
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
        "",
        "// tag::test[]",
        s"""[cols="<.^25,>.<20,^.>55", align="center", halign="center", valign="center", frame="all", grid="all"${separatorValue(
          separator
        )}]""",
        s"$separator===",
        "",
        "",
        s"$separator===",
        "// end::test[]",
        ""
      )

    Seq(
      ("DocumentGenerator: should generate table, no nested table", false, exampleData, expected("|")),
      ("DocumentGenerator: should generate table, nested table", true, exampleData, expected("!"))
    )
  }

  emptyTableData.foreach { case (name, nested, exampleData, expected) =>
    test(name) {
      val obtained = DocumentGenerator.buildTable(exampleData, nested)
      assertEquals(obtained, expected)
    }
  }
}
