package com.alphasystem
package arabic
package examples_generator
package ui
package utils

import ui.model.*
import munit.FunSuite

import java.util.UUID

class TableBuilderSpec extends FunSuite {

  private val numOfCols = 2

  private def newColumnData(text: String): ColumnData =
    ColumnData(id = UUID.randomUUID(), `type` = ColumnType.Other, text = text)

  private def newRow(texts: String*): Row =
    Row(id = UUID.randomUUID(), columns = texts.map(newColumnData))

  private def newTableBuilder(rows: Row*): TableBuilder = {
    val columns = Seq.fill(numOfCols)(TableColumnInfo(id = UUID.randomUUID(), width = 100 / numOfCols))
    val tableData = TableData(id = UUID.randomUUID(), tableProperties = TableProperties(tag = "test", columns = columns), row = rows)
    new TableBuilder(tableData)
  }

  test("TableBuilder: should return same rows when no row is added") {
    val row1 = newRow("a", "b")
    val row2 = newRow("c", "d")
    val builder = newTableBuilder(row1, row2)

    assertEquals(builder.tableData.row, Seq(row1, row2))
  }

  test("TableBuilder: should insert row before current row") {
    val row1 = newRow("a", "b")
    val row2 = newRow("c", "d")
    val builder = newTableBuilder(row1, row2)

    builder.addRow(row2.id, InsertPosition.Before)

    val rows = builder.tableData.row
    assertEquals(rows.size, 3)
    assertEquals(rows.head, row1)
    assertEquals(rows(2), row2)
    assert(rows(1).id != row1.id)
    assert(rows(1).id != row2.id)
  }

  test("TableBuilder: should insert row after current row") {
    val row1 = newRow("a", "b")
    val row2 = newRow("c", "d")
    val builder = newTableBuilder(row1, row2)

    builder.addRow(row1.id, InsertPosition.After)

    val rows = builder.tableData.row
    assertEquals(rows.size, 3)
    assertEquals(rows.head, row1)
    assertEquals(rows(2), row2)
    assert(rows(1).id != row1.id)
    assert(rows(1).id != row2.id)
  }

  test("TableBuilder: should insert row at the beginning when inserting before first row") {
    val row = newRow("a", "b")
    val builder = newTableBuilder(row)

    builder.addRow(row.id, InsertPosition.Before)

    val rows = builder.tableData.row
    assertEquals(rows.size, 2)
    assertEquals(rows(1), row)
  }

  test("TableBuilder: should insert row at the end when inserting after last row") {
    val row = newRow("a", "b")
    val builder = newTableBuilder(row)

    builder.addRow(row.id, InsertPosition.After)

    val rows = builder.tableData.row
    assertEquals(rows.size, 2)
    assertEquals(rows.head, row)
  }

  test("TableBuilder: new row should have same number of columns as table properties") {
    val row = newRow("a", "b")
    val builder = newTableBuilder(row)

    builder.addRow(row.id, InsertPosition.After)

    val insertedRow = builder.tableData.row(1)
    assertEquals(insertedRow.columns.size, numOfCols)
    insertedRow.columns.foreach { column =>
      assertEquals(column.`type`, ColumnType.Other)
      assertEquals(column.text, "{nbsp}")
    }
  }

  test("TableBuilder: should not insert row when current row id is not found") {
    val row = newRow("a", "b")
    val builder = newTableBuilder(row)

    builder.addRow(UUID.randomUUID(), InsertPosition.After)
    builder.addRow(UUID.randomUUID(), InsertPosition.Before)

    assertEquals(builder.tableData.row, Seq(row))
  }

  test("TableBuilder: new row columns should have unique ids") {
    val row = newRow("a", "b")
    val builder = newTableBuilder(row)

    builder.addRow(row.id, InsertPosition.After)

    val insertedRow = builder.tableData.row(1)
    assertEquals(insertedRow.columns.map(_.id).distinct.size, numOfCols)
  }
}
