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

      builder.addRow(Some(targetRow(row1, row2).id), InsertPosition.valueOf(positionName))

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

      builder.addRow(Some(row.id), position)

      val rows = builder.tableData.row
      assertEquals(rows.size, 2)
      assertEquals(rows(1 - expectedIndex), row)
    }
  }

  test("TableBuilder: new row should have same number of columns as table properties") {
    val row = newRow("a", "b")
    val builder = newTableBuilder(row)

    builder.addRow(Some(row.id), InsertPosition.After)

    val insertedRow = builder.tableData.row(1)
    assertEquals(insertedRow.columns.size, numOfCols)
    insertedRow.columns.foreach { column =>
      assertEquals(column.`type`, ColumnType.Other)
      assertEquals(column.text, "{nbsp}")
    }
  }

  Seq(
    ("current row id is not provided", Option.empty[UUID]),
    ("current row id is not found", Some(UUID.randomUUID()))
  ).foreach { case (name, rowId) =>
    test(s"TableBuilder: should append row at the end when $name") {
      val row = newRow("a", "b")
      val builder = newTableBuilder(row)

      builder.addRow(rowId, InsertPosition.After)

      val rows = builder.tableData.row
      assertEquals(rows.size, 2)
      assertEquals(rows.head, row)
      assertEquals(rows(1).columns.size, numOfCols)
    }
  }

  test("TableBuilder: new row columns should have unique ids") {
    val row = newRow("a", "b")
    val builder = newTableBuilder(row)

    builder.addRow(Some(row.id), InsertPosition.After)

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
      builder.addOrUpdateColumn(Some(row.id), Some(column1.id), newColumn, Some(position))

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
    builder.addOrUpdateColumn(Some(row.id), Some(column1.id), newColumn, Some(InsertPosition.After))

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

  test("TableBuilder: updateColumn should locate row by column id when row id is not provided") {
    val column = newColumnData("a")
    val row1 = newRow("x", "y")
    val row2 = Row(id = UUID.randomUUID(), columns = Seq(column, newColumnData("b")))
    val builder = newTableBuilder(row1, row2)

    val updated = column.copy(text = "updated")
    builder.addOrUpdateColumn(None, None, updated)

    val rows = builder.tableData.row
    assertEquals(rows(1).columns.head, updated)
    assertEquals(rows.head, row1)
  }

  test("TableBuilder: updateColumn should append column at the end when reference column is not found") {
    val column = newColumnData("a")
    val row = Row(id = UUID.randomUUID(), columns = Seq(column, newColumnData("b")))
    val builder = newTableBuilder(row)

    val newColumn = newColumnData("new")
    builder.addOrUpdateColumn(Some(row.id), Some(UUID.randomUUID()), newColumn, Some(InsertPosition.After))

    val columns = builder.tableData.row.head.columns
    assertEquals(columns.size, 3)
    assertEquals(columns.last, newColumn)
  }

  test("TableBuilder: updateColumn should update column in place") {
    val column = newColumnData("a")
    val row = Row(id = UUID.randomUUID(), columns = Seq(column, newColumnData("b")))
    val builder = newTableBuilder(row)

    val updated = column.copy(text = "updated")
    builder.addOrUpdateColumn(Some(row.id), Some(column.id), updated)

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
      builder.addOrUpdateColumn(Some(rowId), Some(columnId), column.copy(text = "updated"))

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
        builder.addOrUpdateColumn(Some(row.id), Some(column.id), updated)

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
    builder.addOrUpdateColumn(Some(row.id), Some(column.id), updated)

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
      Some(row.id),
      Some(column1.id),
      column1.copy(settings = Some(ColumnSettings(colSpan = 1, rowSpan = 1)))
    )

    // column2: 1x2 -> adds 1 row below
    builder.addOrUpdateColumn(
      Some(row.id),
      Some(column2.id),
      column2.copy(settings = Some(ColumnSettings(colSpan = 1, rowSpan = 2)))
    )

    // column3: 2x4 -> needs 3 rows below, 1 already exists so adds 2 more,
    // and removes column4 which is covered by the span
    builder.addOrUpdateColumn(
      Some(row.id),
      Some(column3.id),
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
    assertEquals(rows(1).columns.size, 1)
    // newly added rows for column3's span have 4 - 2 = 2 columns
    rows.drop(2).foreach { r =>
      assertEquals(r.columns.size, 2)
      r.columns.foreach { c =>
        assertEquals(c.`type`, ColumnType.Other)
        assertEquals(c.text, "{nbsp}")
      }
    }
  }

  test("TableBuilder: updateColumn should not add extra columns with adjacent row spans of 2 and 5") {
    val column1 = newColumnData("a").copy(settings = Some(ColumnSettings(colSpan = 1, rowSpan = 1)))
    val column2 = newColumnData("b").copy(settings = Some(ColumnSettings(colSpan = 1, rowSpan = 1)))
    val column3 = newColumnData("c")
    val column4 = newColumnData("d")
    val row = Row(id = UUID.randomUUID(), columns = Seq(column1, column2, column3, column4))
    val builder = newTableBuilderWithCols(4, row)
    val updatedColumn3 = column3.copy(settings = Some(ColumnSettings(colSpan = 1, rowSpan = 2)))
    val updatedColumn4 = column4.copy(settings = Some(ColumnSettings(colSpan = 1, rowSpan = 5)))

    builder.addOrUpdateColumn(Some(row.id), Some(column3.id), updatedColumn3)
    builder.addOrUpdateColumn(Some(row.id), Some(column4.id), updatedColumn4)

    val thirdRow = builder.tableData.row(2)
    val thirdRowColumn3 = thirdRow.columns(2).copy(settings = Some(ColumnSettings(colSpan = 1, rowSpan = 3)))
    builder.addOrUpdateColumn(Some(thirdRow.id), Some(thirdRowColumn3.id), thirdRowColumn3)

    val rows = builder.tableData.row
    assertEquals(rows.size, 5)
    assertEquals(rows.head.columns, Seq(column1, column2, updatedColumn3, updatedColumn4))
    assertEquals(rows(2).columns(2), thirdRowColumn3)
    assertEquals(rows.map(_.columns.size), Seq(4, 2, 3, 2, 2))
    rows.tail.foreach { r =>
      r.columns.foreach { column =>
        assertEquals(column.colSpan, 1)
        assertEquals(column.rowSpan, if column.id == thirdRowColumn3.id then 3 else 1)
        assertEquals(column.`type`, ColumnType.Other)
        assertEquals(column.text, "{nbsp}")
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
    val exception =
      intercept[IllegalArgumentException](builder.addOrUpdateColumn(Some(row.id), Some(column.id), updated))

    assertEquals(exception.getMessage, "Column span cannot be greater than number of columns")
  }

  test("TableBuilder: updateColumn should not change rows or columns when spans are unchanged") {
    val column = newColumnData("a").copy(settings = Some(ColumnSettings(colSpan = 1, rowSpan = 1)))
    val row = Row(id = UUID.randomUUID(), columns = Seq(column, newColumnData("b")))
    val builder = newTableBuilder(row)

    val updated = column.copy(text = "updated")
    builder.addOrUpdateColumn(Some(row.id), Some(column.id), updated)

    val rows = builder.tableData.row
    assertEquals(rows.size, 1)
    assertEquals(rows.head.columns.size, numOfCols)
  }

  Seq(
    ("removeRow", (builder: TableBuilder, row: Row) => builder.removeRow(row.id)),
    ("removeRow when row id is not found", (builder: TableBuilder, row: Row) => builder.removeRow(UUID.randomUUID()))
  ).foreach { case (name, remove) =>
    test(s"TableBuilder: $name") {
      val row1 = newRow("a", "b")
      val row2 = newRow("c", "d")
      val builder = newTableBuilder(row1, row2)

      remove(builder, if name.contains("not found") then row1 else row2)

      val expected = if name.contains("not found") then Seq(row1, row2) else Seq(row1)
      assertEquals(builder.tableData.row, expected)
    }
  }

  test("TableBuilder: removeRow should decrement rowSpan of columns covering the removed row") {
    val column = newColumnData("a").copy(settings = Some(ColumnSettings(colSpan = 1, rowSpan = 2)))
    val row1 = Row(id = UUID.randomUUID(), columns = Seq(column, newColumnData("b")))
    val spannedRow = Row(
      id = UUID.randomUUID(),
      columns = Seq(newColumnData("filler").copy(coveredBy = Some(column.id)))
    )
    val builder = newTableBuilder(row1, spannedRow)

    builder.removeRow(spannedRow.id)

    val rows = builder.tableData.row
    assertEquals(rows.size, 1)
    assertEquals(rows.head.columns.head.rowSpan, 1)
  }

  test("TableBuilder: removeRow should remove cells below covered by spans starting in the removed row") {
    val column = newColumnData("a").copy(settings = Some(ColumnSettings(colSpan = 1, rowSpan = 2)))
    val row1 = Row(id = UUID.randomUUID(), columns = Seq(column, newColumnData("b")))
    val realColumn = newColumnData("real")
    val row2 = Row(
      id = UUID.randomUUID(),
      columns = Seq(newColumnData("filler").copy(coveredBy = Some(column.id)), realColumn)
    )
    val builder = newTableBuilder(row1, row2)

    builder.removeRow(row1.id)

    val rows = builder.tableData.row
    assertEquals(rows.size, 1)
    assertEquals(rows.head.columns, Seq(realColumn))
  }

  test("TableBuilder: removeColumn should remove column in place") {
    val column1 = newColumnData("a")
    val column2 = newColumnData("b")
    val row = Row(id = UUID.randomUUID(), columns = Seq(column1, column2))
    val builder = newTableBuilder(row)

    builder.removeColumn(Some(row.id), column1.id)

    assertEquals(builder.tableData.row.head.columns, Seq(column2))
  }

  test("TableBuilder: removeColumn should locate row by column id when row id is not provided") {
    val column1 = newColumnData("a")
    val column2 = newColumnData("b")
    val row1 = newRow("x", "y")
    val row2 = Row(id = UUID.randomUUID(), columns = Seq(column1, column2))
    val builder = newTableBuilder(row1, row2)

    builder.removeColumn(None, column1.id)

    val rows = builder.tableData.row
    assertEquals(rows.head, row1)
    assertEquals(rows(1).columns, Seq(column2))
  }

  Seq(
    ("row id", (rowId: UUID, columnId: UUID) => (UUID.randomUUID(), columnId)),
    ("column id", (rowId: UUID, columnId: UUID) => (rowId, UUID.randomUUID()))
  ).foreach { case (name, ids) =>
    test(s"TableBuilder: removeColumn should do nothing when $name is not found") {
      val column = newColumnData("a")
      val row = Row(id = UUID.randomUUID(), columns = Seq(column, newColumnData("b")))
      val builder = newTableBuilder(row)

      val (rowId, columnId) = ids(row.id, column.id)
      builder.removeColumn(Some(rowId), columnId)

      assertEquals(builder.tableData.row, Seq(row))
    }
  }

  test("TableBuilder: removeColumn should decrement colSpan of owner when removing a covered column") {
    val owner = newColumnData("a").copy(settings = Some(ColumnSettings(colSpan = 2, rowSpan = 1)))
    val covered = newColumnData("filler").copy(coveredBy = Some(owner.id))
    val row = Row(id = UUID.randomUUID(), columns = Seq(owner, covered, newColumnData("b")))
    val builder = newTableBuilderWithCols(3, row)

    builder.removeColumn(Some(row.id), covered.id)

    val columns = builder.tableData.row.head.columns
    assertEquals(columns.size, 2)
    assertEquals(columns.head.colSpan, 1)
    assertEquals(columns(1).text, "b")
  }

  test("TableBuilder: removeColumn should promote next column into freed colSpan") {
    val owner = newColumnData("a").copy(settings = Some(ColumnSettings(colSpan = 2, rowSpan = 1)))
    val next = newColumnData("b")
    val row = Row(id = UUID.randomUUID(), columns = Seq(owner, next))
    val builder = newTableBuilderWithCols(3, row)

    builder.removeColumn(Some(row.id), owner.id)

    val columns = builder.tableData.row.head.columns
    assertEquals(columns.size, 1)
    assertEquals(columns.head.colSpan, 2)
    assertEquals(columns.head.text, "b")
  }

  test("TableBuilder: removeColumn should remove cells below covered by the column's rowSpan") {
    val column = newColumnData("a").copy(settings = Some(ColumnSettings(colSpan = 1, rowSpan = 2)))
    val row1 = Row(id = UUID.randomUUID(), columns = Seq(column, newColumnData("b")))
    val realColumn = newColumnData("real")
    val row2 = Row(
      id = UUID.randomUUID(),
      columns = Seq(newColumnData("filler").copy(coveredBy = Some(column.id)), realColumn)
    )
    val builder = newTableBuilder(row1, row2)

    builder.removeColumn(Some(row1.id), column.id)

    val rows = builder.tableData.row
    assertEquals(rows.size, 2)
    assertEquals(rows.head.columns.size, 1)
    assertEquals(rows(1).columns, Seq(realColumn))
  }
}
