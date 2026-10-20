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

  private def newTableBuilder(rows: Row*): TableBuilder = newTableBuilderWithCols(numOfCols, rows*)

  private def newTableBuilderWithCols(numOfColumns: Int, rows: Row*): TableBuilder = {
    val columns = Seq.fill(numOfColumns)(TableColumnInfo(id = UUID.randomUUID(), width = 100 / numOfColumns))
    val tableData =
      TableData(id = UUID.randomUUID(), tableProperties = TableProperties(tag = "test", columns = columns), row = rows)
    new TableBuilder(tableData)
  }

  test("TableBuilder: should return same rows when no row is added") {
    val row1 = newRow("a", "b")
    val row2 = newRow("c", "d")
    val builder = newTableBuilder(row1, row2)

    assertEquals(builder.tableData.row, Seq(row1, row2))
  }

  Seq(
    ("Before", (row1: Row, row2: Row) => row2),
    ("After", (row1: Row, row2: Row) => row1)
  ).foreach { case (positionName, targetRow) =>
    test(s"TableBuilder: should insert row $positionName current row") {
      val row1 = newRow("a", "b")
      val row2 = newRow("c", "d")
      val builder = newTableBuilder(row1, row2)

      builder.addRow(targetRow(row1, row2).id, InsertPosition.valueOf(positionName))

      val rows = builder.tableData.row
      assertEquals(rows.size, 3)
      assertEquals(rows.head, row1)
      assertEquals(rows(2), row2)
      assert(rows(1).id != row1.id)
      assert(rows(1).id != row2.id)
    }
  }

  Seq(
    ("at the beginning when inserting before first row", InsertPosition.Before, 0),
    ("at the end when inserting after last row", InsertPosition.After, 1)
  ).foreach { case (name, position, expectedIndex) =>
    test(s"TableBuilder: should insert row $name") {
      val row = newRow("a", "b")
      val builder = newTableBuilder(row)

      builder.addRow(row.id, position)

      val rows = builder.tableData.row
      assertEquals(rows.size, 2)
      assertEquals(rows(1 - expectedIndex), row)
    }
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

  Seq(
    (InsertPosition.Before, 0),
    (InsertPosition.After, 1)
  ).foreach { case (position, expectedIndexOffset) =>
    test(
      s"TableBuilder: updateColumn should insert new column $position current column when insertPosition is provided"
    ) {
      val column1 = newColumnData("a")
      val column2 = newColumnData("b")
      val row = Row(id = UUID.randomUUID(), columns = Seq(column1, column2))
      val builder = newTableBuilder(row)

      val newColumn = newColumnData("new")
      builder.addOrUpdateColumn(row.id, column1.id, newColumn, Some(position))

      val rows = builder.tableData.row
      assertEquals(rows.size, 1)
      assertEquals(rows.head.columns.size, 3)
      assertEquals(rows.head.columns(expectedIndexOffset), newColumn)
      // original column stays in place, shifted when inserting before
      assertEquals(rows.head.columns(1 - expectedIndexOffset), column1)
      assertEquals(rows.head.columns(2), column2)
    }
  }

  test("TableBuilder: updateColumn should honor spans on inserted column when insertPosition is provided") {
    val column1 = newColumnData("a")
    val column2 = newColumnData("b")
    val column3 = newColumnData("c")
    val row = Row(id = UUID.randomUUID(), columns = Seq(column1, column2, column3))
    val builder = newTableBuilderWithCols(4, row)

    // insert a 2x2 column after column1: covers the column to its right (column2)
    // and adds 1 row below with 4 - 2 = 2 columns
    val newColumn = newColumnData("new").copy(settings = Some(ColumnSettings(colSpan = 2, rowSpan = 2)))
    builder.addOrUpdateColumn(row.id, column1.id, newColumn, Some(InsertPosition.After))

    val rows = builder.tableData.row
    assertEquals(rows.size, 2)
    // [column1(1x1), newColumn(2x2), column3] -> 3 entries spanning 4 grid columns
    assertEquals(rows.head.columns.size, 3)
    assertEquals(rows.head.columns(0), column1)
    assertEquals(rows.head.columns(1), newColumn)
    // column2 was covered by the span and removed
    assertEquals(rows.head.columns(2), column3)
    assertEquals(rows(1).columns.size, 2)
    rows(1).columns.foreach { c =>
      assertEquals(c.`type`, ColumnType.Other)
      assertEquals(c.text, "{nbsp}")
    }
  }

  test("TableBuilder: updateColumn should update column in place") {
    val column = newColumnData("a")
    val row = Row(id = UUID.randomUUID(), columns = Seq(column, newColumnData("b")))
    val builder = newTableBuilder(row)

    val updated = column.copy(text = "updated")
    builder.addOrUpdateColumn(row.id, column.id, updated)

    val rows = builder.tableData.row
    assertEquals(rows.size, 1)
    assertEquals(rows.head.columns.size, numOfCols)
    assertEquals(rows.head.columns.head, updated)
  }

  Seq(
    ("row id", (rowId: UUID, columnId: UUID) => (UUID.randomUUID(), columnId)),
    ("column id", (rowId: UUID, columnId: UUID) => (rowId, UUID.randomUUID()))
  ).foreach { case (name, ids) =>
    test(s"TableBuilder: updateColumn should do nothing when $name is not found") {
      val column = newColumnData("a")
      val row = Row(id = UUID.randomUUID(), columns = Seq(column, newColumnData("b")))
      val builder = newTableBuilder(row)

      val (rowId, columnId) = ids(row.id, column.id)
      builder.addOrUpdateColumn(rowId, columnId, column.copy(text = "updated"))

      assertEquals(builder.tableData.row, Seq(row))
    }
  }

  // (name, numTableCols, currentSettings, newSettings, extraRowsBelow, expectedTotalRows, expectedHeadCols, expectedAddedRowCols)
  private val spanData = Seq(
    (
      "add newRowSpan - 1 rows when newColSpan is 1 and newRowSpan is greater than 1",
      2,
      ColumnSettings(colSpan = 1, rowSpan = 1),
      ColumnSettings(colSpan = 1, rowSpan = 3),
      0,
      3,
      2,
      Some(1)
    ),
    (
      "add newRowSpan - 1 rows with fewer columns when newColSpan is greater than 1",
      3,
      ColumnSettings(colSpan = 1, rowSpan = 1),
      ColumnSettings(colSpan = 2, rowSpan = 2),
      0,
      2,
      2,
      Some(1)
    ),
    (
      "add rows when both newColSpan and newRowSpan grow",
      3,
      ColumnSettings(colSpan = 1, rowSpan = 1),
      ColumnSettings(colSpan = 2, rowSpan = 3),
      0,
      3,
      2,
      Some(1)
    ),
    (
      "remove covered columns when only newColSpan grows",
      3,
      ColumnSettings(colSpan = 1, rowSpan = 1),
      ColumnSettings(colSpan = 2, rowSpan = 1),
      0,
      1,
      2,
      None
    ),
    (
      "insert back columns when newColSpan shrinks",
      3,
      ColumnSettings(colSpan = 2, rowSpan = 1),
      ColumnSettings(colSpan = 1, rowSpan = 1),
      0,
      1,
      3,
      None
    ),
    (
      "remove rows when newRowSpan shrinks",
      2,
      ColumnSettings(colSpan = 1, rowSpan = 3),
      ColumnSettings(colSpan = 1, rowSpan = 1),
      2,
      1,
      2,
      None
    )
  )

  spanData.foreach {
    case (
          name,
          numTableCols,
          currentSettings,
          newSettings,
          extraRowsBelow,
          expectedTotalRows,
          expectedHeadCols,
          expectedAddedRowCols
        ) =>
      test(s"TableBuilder: updateColumn should $name") {
        val column = newColumnData("a").copy(settings = Some(currentSettings))
        val row = Row(
          id = UUID.randomUUID(),
          columns = column +: Seq.fill(numTableCols - currentSettings.colSpan)(newColumnData("x"))
        )
        val spannedRows = Seq.fill(extraRowsBelow)(
          Row(
            id = UUID.randomUUID(),
            columns = Seq.fill(numTableCols - currentSettings.colSpan)(
              newColumnData("filler").copy(coveredBy = Some(column.id))
            )
          )
        )
        val builder = newTableBuilderWithCols(numTableCols, row +: spannedRows*)

        val updated = column.copy(settings = Some(newSettings))
        builder.addOrUpdateColumn(row.id, column.id, updated)

        val rows = builder.tableData.row
        assertEquals(rows.size, expectedTotalRows)
        assertEquals(rows.head.columns.size, expectedHeadCols)
        assertEquals(rows.head.columns.head, updated)
        expectedAddedRowCols.foreach { numColumns =>
          rows.tail.foreach { r =>
            assertEquals(r.columns.size, numColumns)
            r.columns.foreach { c =>
              assertEquals(c.`type`, ColumnType.Other)
              assertEquals(c.text, "{nbsp}")
              assertEquals(c.coveredBy, Some(column.id))
            }
          }
        }
      }
  }

  test("TableBuilder: updateColumn should not remove rows that are not covered by the column when newRowSpan shrinks") {
    val column = newColumnData("a").copy(settings = Some(ColumnSettings(colSpan = 1, rowSpan = 3)))
    val row = Row(id = UUID.randomUUID(), columns = Seq(column, newColumnData("b")))
    // a real row below, not covered by column's span
    val realRow = newRow("c", "d")
    val builder = newTableBuilder(row, realRow)

    val updated = column.copy(settings = Some(ColumnSettings(colSpan = 1, rowSpan = 1)))
    builder.addOrUpdateColumn(row.id, column.id, updated)

    val rows = builder.tableData.row
    assertEquals(rows.size, 2)
    assertEquals(rows(1), realRow)
  }

  test("TableBuilder: updateColumn should build table with mixed colSpan and rowSpan") {
    val column1 = newColumnData("a")
    val column2 = newColumnData("b")
    val column3 = newColumnData("c")
    val column4 = newColumnData("d")
    val row = Row(id = UUID.randomUUID(), columns = Seq(column1, column2, column3, column4))
    val builder = newTableBuilderWithCols(4, row)

    // column1: stays 1x1
    builder.addOrUpdateColumn(
      row.id,
      column1.id,
      column1.copy(settings = Some(ColumnSettings(colSpan = 1, rowSpan = 1)))
    )

    // column2: 1x2 -> adds 1 row below
    builder.addOrUpdateColumn(
      row.id,
      column2.id,
      column2.copy(settings = Some(ColumnSettings(colSpan = 1, rowSpan = 2)))
    )

    // column3: 2x4 -> needs 3 rows below, 1 already exists so adds 2 more,
    // and removes column4 which is covered by the span
    builder.addOrUpdateColumn(
      row.id,
      column3.id,
      column3.copy(settings = Some(ColumnSettings(colSpan = 2, rowSpan = 4)))
    )

    val rows = builder.tableData.row
    assertEquals(rows.size, 4)

    assertEquals(rows.head.columns.size, 3)
    assertEquals(rows.head.columns.head.colSpan, 1)
    assertEquals(rows.head.columns.head.rowSpan, 1)
    assertEquals(rows.head.columns(1).colSpan, 1)
    assertEquals(rows.head.columns(1).rowSpan, 2)
    assertEquals(rows.head.columns(2).colSpan, 2)
    assertEquals(rows.head.columns(2).rowSpan, 4)

    // existing row below (from column2's span) keeps its 3 columns
    assertEquals(rows(1).columns.size, 3)
    // newly added rows for column3's span have 4 - 2 = 2 columns
    rows.drop(2).foreach { r =>
      assertEquals(r.columns.size, 2)
      r.columns.foreach { c =>
        assertEquals(c.`type`, ColumnType.Other)
        assertEquals(c.text, "{nbsp}")
      }
    }
  }

  test("TableBuilder: should return table with mixed colSpan and rowSpan unchanged") {
    val column1 = newColumnData("a").copy(settings = Some(ColumnSettings(colSpan = 1, rowSpan = 1)))
    val column2 = newColumnData("b").copy(settings = Some(ColumnSettings(colSpan = 1, rowSpan = 2)))
    val column3 = newColumnData("c").copy(settings = Some(ColumnSettings(colSpan = 2, rowSpan = 4)))
    val row1 = Row(id = UUID.randomUUID(), columns = Seq(column1, column2, column3))
    // rows covered by column2's 1x2 and column3's 2x4 spans
    val row2 = newRow("x", "y", "z")
    val row3 = newRow("x", "y")
    val row4 = newRow("x", "y")
    val builder = newTableBuilderWithCols(4, row1, row2, row3, row4)

    val rows = builder.tableData.row
    assertEquals(rows.size, 4)
    assertEquals(rows.head.columns.size, 3)
    assertEquals(rows.head.columns.head.colSpan, 1)
    assertEquals(rows.head.columns.head.rowSpan, 1)
    assertEquals(rows.head.columns(1).colSpan, 1)
    assertEquals(rows.head.columns(1).rowSpan, 2)
    assertEquals(rows.head.columns(2).colSpan, 2)
    assertEquals(rows.head.columns(2).rowSpan, 4)
    assertEquals(rows.tail, Seq(row2, row3, row4))
  }

  test("TableBuilder: updateColumn should throw when newColSpan is greater than number of columns") {
    val column = newColumnData("a")
    val row = Row(id = UUID.randomUUID(), columns = Seq(column, newColumnData("b")))
    val builder = newTableBuilder(row)

    val updated = column.copy(settings = Some(ColumnSettings(colSpan = numOfCols + 1, rowSpan = 1)))
    val exception = intercept[IllegalArgumentException](builder.addOrUpdateColumn(row.id, column.id, updated))

    assertEquals(exception.getMessage, "Column span cannot be greater than number of columns")
  }

  test("TableBuilder: updateColumn should not change rows or columns when spans are unchanged") {
    val column = newColumnData("a").copy(settings = Some(ColumnSettings(colSpan = 1, rowSpan = 1)))
    val row = Row(id = UUID.randomUUID(), columns = Seq(column, newColumnData("b")))
    val builder = newTableBuilder(row)

    val updated = column.copy(text = "updated")
    builder.addOrUpdateColumn(row.id, column.id, updated)

    val rows = builder.tableData.row
    assertEquals(rows.size, 1)
    assertEquals(rows.head.columns.size, numOfCols)
  }
}
