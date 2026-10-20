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

  /** Adds a new row to the table at the specified position relative to an existing row.
    *
    * @param currentRowId
    *   The unique identifier of the current row in the table relative to which the new row will be added.
    * @param insertPosition
    *   The position relative to the current row where the new row should be added (e.g., before or after the current
    *   row).
    */
  def addRow(currentRowId: UUID, insertPosition: InsertPosition): Unit = {
    val newRow = createRow(numsOfCols)
    val currentRowIndex = rows.indexWhere(_.id == currentRowId)
    if currentRowIndex >= 0 then rows.insert(currentRowIndex + insertPosition.offset, newRow)
  }

  /** Adds or updates a column in the specified row of the table. If the column already exists, its data is updated. If
    * the column does not exist, it is added at the specified position or replaces the column at the same ID.
    *
    * @param currentRowId
    *   The unique identifier of the current row in which the column is being added or updated.
    * @param currentColumnId
    *   The unique identifier of the column to be added or updated.
    * @param updatedColumnData
    *   The updated data for the column, including its text and settings.
    * @param insertPosition
    *   The position relative to the specified column where the updated column should be added (optional). If not
    *   provided, the column is updated in its current position.
    */
  def addOrUpdateColumn(
    currentRowId: UUID,
    currentColumnId: UUID,
    updatedColumnData: ColumnData,
    insertPosition: Option[InsertPosition] = None
  ): Unit = {
    val currentRowIndex = rows.indexWhere(_.id == currentRowId)
    if currentRowIndex >= 0 then {
      val row = rows(currentRowIndex)
      val columns = ListBuffer.from(row.columns)
      val columnIndex = columns.indexWhere(_.id == currentColumnId)
      if columnIndex >= 0 then {
        val currentColumnData = row.columns(columnIndex)
        val currentColSpan = currentColumnData.colSpan
        val currentRowSpan = currentColumnData.rowSpan
        val newColSpan = updatedColumnData.colSpan
        val newRowSpan = updatedColumnData.rowSpan
        val targetIndex = insertPosition.map(columnIndex + _.offset).getOrElse(columnIndex)

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
              List.fill(numOfNewRows)(createRow(numsOfCols - newColSpan, Some(currentColumnId)))
            )
        }

        if newRowSpan < currentRowSpan then {
          // only remove rows that are fully covered by this column's span
          var numOfRowsToRemove = currentRowSpan - newRowSpan
          while numOfRowsToRemove > 0
            && currentRowIndex + 1 < rows.size
            && rows(currentRowIndex + 1).columns.forall(_.coveredBy.contains(currentColumnId))
          do
            rows.remove(currentRowIndex + 1)
            numOfRowsToRemove -= 1
        }

        if newColSpan > currentColSpan then columns.remove(targetIndex + 1, newColSpan - currentColSpan)

        if newColSpan < currentColSpan then
          columns.insertAll(targetIndex + 1, List.fill(currentColSpan - newColSpan)(defaultColumn()))

        if insertPosition.isEmpty then columns.update(columnIndex, updatedColumnData)

        rows.update(currentRowIndex, row.copy(columns = columns.toSeq))
      }
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
