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

  def addRow(currentRowId: UUID, insertPosition: InsertPosition): Unit = {
    val newRow = createRow(numsOfCols)
    val currentRowIndex = rows.indexWhere(_.id == currentRowId)
    if currentRowIndex >= 0 then rows.insert(currentRowIndex + insertPosition.offset, newRow)
  }

  def updateColumn(
    currentRowId: UUID,
    currentColumnId: UUID,
    updatedColumnData: ColumnData
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

        if newColSpan > numsOfCols then
          throw new IllegalArgumentException("Column span cannot be greater than number of columns")

        if newRowSpan > currentRowSpan then {
          // only add the rows missing below current row; existing rows may already
          // be part of another column's span
          val existingRowsBelow = rows.size - currentRowIndex - 1
          val numOfNewRows = newRowSpan - 1 - existingRowsBelow
          if numOfNewRows > 0 then
            rows.insertAll(rows.size, List.fill(numOfNewRows)(createRow(numsOfCols - newColSpan)))
        }

        if newRowSpan < currentRowSpan then rows.remove(currentRowIndex + 1, currentRowSpan - newRowSpan)

        if newColSpan > currentColSpan then columns.remove(columnIndex + 1, newColSpan - currentColSpan)

        if newColSpan < currentColSpan then
          columns.insertAll(columnIndex + 1, List.fill(currentColSpan - newColSpan)(defaultColumn()))

        // update
        columns.update(columnIndex, updatedColumnData)
        rows.update(currentRowIndex, row.copy(columns = columns.toSeq))
      }
    }
  }

  private def createRow(numOfColumns: Int): Row =
    Row(id = UUID.randomUUID(), columns = List.fill(numOfColumns)(defaultColumn()))

  private def defaultColumn(): ColumnData =
    ColumnData(id = UUID.randomUUID(), text = "{nbsp}", `type` = Other, settings = Some(ColumnSettings()))
}
