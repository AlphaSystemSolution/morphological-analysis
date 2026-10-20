package com.alphasystem
package arabic
package examples_generator
package ui
package utils

import ui.model.ColumnType.Other
import ui.model.{ ColumnData, ColumnSettings, InsertPosition, Row, TableData }

import java.util.UUID
import scala.collection.mutable.ListBuffer

class TableBuilder(_tableData: TableData) {

  private val numsOfCols = _tableData.tableProperties.columns.size
  private val rows = ListBuffer.from[Row](_tableData.row)

  def tableData: TableData = _tableData.copy(row = rows.toSeq)

  /** Adds a new row to the table at the specified position relative to an existing row. When `rowId` is not provided or
    * not found, the row is appended at the end.
    *
    * @param rowId
    *   The unique identifier of the row in the table relative to which the new row will be added.
    * @param insertPosition
    *   The position relative to the current row where the new row should be added (e.g., before or after the current
    *   row).
    */
  def addRow(rowId: Option[UUID], insertPosition: InsertPosition): Unit = {
    val insertIndex = rowId
      .flatMap(id =>
        rows.indexWhere(_.id == id) match {
          case index if index >= 0 => Some(index + insertPosition.offset)
          case _                   => None
        }
      )
      .getOrElse(rows.size)
    rows.insert(insertIndex, createRow(numsOfCols))
  }

  /** Adds or updates a column in the table. If `insertPosition` is provided the column is inserted relative to the
    * reference column, otherwise it is updated in place. When `rowId` is not provided, the row containing the reference
    * (or updated) column is located automatically. When inserting without a reference column, the column is appended at
    * the end of the row.
    *
    * @param rowId
    *   The unique identifier of the row in which the column is being added or updated.
    * @param referenceColumnId
    *   The unique identifier of the column to insert relative to. Defaults to the updated column's own id when not
    *   provided.
    * @param updatedColumnData
    *   The data for the column to add or update, including its text and settings.
    * @param insertPosition
    *   The position relative to the reference column where the column should be added (optional). If not provided, the
    *   column is updated in its current position.
    */
  def addOrUpdateColumn(
    rowId: Option[UUID],
    referenceColumnId: Option[UUID],
    updatedColumnData: ColumnData,
    insertPosition: Option[InsertPosition] = None
  ): Unit = {
    val anchorColumnId = referenceColumnId.getOrElse(updatedColumnData.id)
    val currentRowIndex = rowId match {
      case Some(id) => rows.indexWhere(_.id == id)
      case None     => rows.indexWhere(_.columns.exists(_.id == anchorColumnId))
    }
    if currentRowIndex >= 0 then {
      val row = rows(currentRowIndex)
      val columns = ListBuffer.from(row.columns)
      val columnIndex = columns.indexWhere(_.id == anchorColumnId)
      if columnIndex >= 0 || insertPosition.isDefined then {
        // when the reference column is not found, an insert appends at the end of the row
        val anchorIndex = if columnIndex >= 0 then columnIndex else columns.size
        val currentColumnData = if columnIndex >= 0 then row.columns(columnIndex) else defaultColumn()
        val currentColSpan = currentColumnData.colSpan
        val currentRowSpan = currentColumnData.rowSpan
        val newColSpan = updatedColumnData.colSpan
        val newRowSpan = updatedColumnData.rowSpan
        val targetIndex =
          if columnIndex >= 0 then insertPosition.map(anchorIndex + _.offset).getOrElse(anchorIndex) else anchorIndex

        if newColSpan > numsOfCols then
          throw new IllegalArgumentException("Column span cannot be greater than number of columns")

        // insert the new column first so span adjustments apply relative to it;
        // otherwise the column is updated in place below
        if insertPosition.isDefined then columns.insert(targetIndex, updatedColumnData)

        if newRowSpan > currentRowSpan then {
          // only add the rows missing below current row; existing rows may already
          // be part of another column's span
          val existingRowsBelow = rows.size - currentRowIndex - 1
          val numOfNewRows = newRowSpan - 1 - existingRowsBelow
          if numOfNewRows > 0 then
            rows.insertAll(
              rows.size,
              List.fill(numOfNewRows)(createRow(numsOfCols - newColSpan, Some(updatedColumnData.id)))
            )
        }

        if newRowSpan < currentRowSpan then {
          // only remove rows that are fully covered by this column's span
          var numOfRowsToRemove = currentRowSpan - newRowSpan
          while numOfRowsToRemove > 0
            && currentRowIndex + 1 < rows.size
            && rows(currentRowIndex + 1).columns.forall(_.coveredBy.contains(anchorColumnId))
          do
            rows.remove(currentRowIndex + 1)
            numOfRowsToRemove -= 1
        }

        if newColSpan > currentColSpan then columns.remove(targetIndex + 1, newColSpan - currentColSpan)

        if newColSpan < currentColSpan then
          columns.insertAll(targetIndex + 1, List.fill(currentColSpan - newColSpan)(defaultColumn()))

        if insertPosition.isEmpty then columns.update(anchorIndex, updatedColumnData)

        rows.update(currentRowIndex, row.copy(columns = columns.toSeq))
      }
    }
  }

  /** Removes a row from the table, identified by its unique ID. Updates column spans and cleans up any cells that are
    * covered or impacted by the removed row to maintain the integrity of the table structure.
    *
    * @param rowId
    *   The unique identifier of the row to be removed from the table.
    */
  def removeRow(rowId: UUID): Unit = {
    val rowIndex = rows.indexWhere(_.id == rowId)
    if rowIndex >= 0 then {
      // remove cells below covered by spans starting in this row
      rows(rowIndex).columns.foreach { column =>
        val rowsBelow = math.min(column.rowSpan - 1, rows.size - rowIndex - 1)
        (1 to rowsBelow).foreach { offset =>
          val r = rows(rowIndex + offset)
          rows.update(rowIndex + offset, r.copy(columns = r.columns.filterNot(_.coveredBy.contains(column.id))))
        }
      }
      // decrement rowSpan of columns whose span covers cells in this row
      rows(rowIndex).columns.flatMap(_.coveredBy).distinct.foreach(decrementSpan(_, isRowSpan = true))
      rows.remove(rowIndex)
    }
  }

  /** Removes a specified column from the table. When `rowId` is provided the column is removed from that row; otherwise
    * the row containing the column is located automatically. Updates column spans and row spans as necessary to
    * maintain table structure and ensures any dependent or covered cells are handled correctly.
    *
    * @param rowId
    *   The unique identifier of the row containing the column to be removed.
    * @param columnId
    *   The unique identifier of the column to be removed.
    */
  def removeColumn(rowId: Option[UUID], columnId: UUID): Unit = {
    val rowIndex = rowId match {
      case Some(id) => rows.indexWhere(_.id == id)
      case None     => rows.indexWhere(_.columns.exists(_.id == columnId))
    }
    if rowIndex >= 0 then {
      val row = rows(rowIndex)
      val columns = ListBuffer.from(row.columns)
      val columnIndex = columns.indexWhere(_.id == columnId)
      if columnIndex >= 0 then {
        val column = columns(columnIndex)
        if column.coveredBy.isEmpty then {
          // promote the next column into the freed span
          if column.colSpan > 1 && columnIndex + 1 < columns.size then {
            val next = columns(columnIndex + 1)
            val nextSettings = next
              .settings
              .getOrElse(ColumnSettings())
              .copy(colSpan = next.colSpan + column.colSpan - 1)
            columns.update(columnIndex + 1, next.copy(settings = Some(nextSettings)))
          }
          // remove cells below covered by this column's rowSpan
          val rowsBelow = math.min(column.rowSpan - 1, rows.size - rowIndex - 1)
          (1 to rowsBelow).foreach { offset =>
            val r = rows(rowIndex + offset)
            rows.update(rowIndex + offset, r.copy(columns = r.columns.filterNot(_.coveredBy.contains(columnId))))
          }
        }
        columns.remove(columnIndex)
        rows.update(rowIndex, row.copy(columns = columns.toSeq))
        // decrement colSpan of the covering column, if any (after the row write so it isn't overwritten)
        column.coveredBy.foreach(decrementSpan(_, isRowSpan = false))
      }
    }
  }

  private def decrementSpan(columnId: UUID, isRowSpan: Boolean): Unit =
    rows.indices.foreach { i =>
      val columns = rows(i).columns
      val columnIndex = columns.indexWhere(_.id == columnId)
      if columnIndex >= 0 then {
        val column = columns(columnIndex)
        val settings = column.settings.getOrElse(ColumnSettings())
        val updated =
          if isRowSpan then settings.copy(rowSpan = math.max(1, settings.rowSpan - 1))
          else settings.copy(colSpan = math.max(1, settings.colSpan - 1))
        rows.update(i, rows(i).copy(columns = columns.updated(columnIndex, column.copy(settings = Some(updated)))))
      }
    }

  private def createRow(numOfColumns: Int, coveredBy: Option[UUID] = None): Row =
    Row(id = UUID.randomUUID(), columns = List.fill(numOfColumns)(defaultColumn(coveredBy)))

  private def defaultColumn(coveredBy: Option[UUID] = None): ColumnData =
    ColumnData(
      id = UUID.randomUUID(),
      text = "{nbsp}",
      `type` = Other,
      settings = Some(ColumnSettings()),
      coveredBy = coveredBy
    )
}
