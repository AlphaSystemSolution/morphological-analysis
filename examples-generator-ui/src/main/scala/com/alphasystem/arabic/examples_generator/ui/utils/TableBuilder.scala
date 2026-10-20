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

  def addRow(currentRowId: UUID, insertionPoint: InsertPosition): Unit = {
    val newRow = Row(id = UUID.randomUUID(), columns = List.fill(numsOfCols)(defaultColumn()))
    val currentRowIndex = rows.indexWhere(_.id == currentRowId)
    if currentRowIndex >= 0 then rows.insert(currentRowIndex + insertionPoint.offset, newRow)
  }

  def updateColumn(
    currentRowId: UUID,
    currentColumnId: UUID,
    updatedColumnData: ColumnData,
    insertionPoint: InsertPosition
  ): Unit = {
    val currentRowIndex = rows.indexWhere(_.id == currentRowId)
    if currentRowIndex >= 0 then {
      val row = rows(currentRowIndex)
      val columnIndex = row.columns.indexWhere(_.id == currentColumnId)
      if columnIndex >= 0 then {

      }
    }
  }

  private def defaultColumn(): ColumnData =
    ColumnData(id = UUID.randomUUID(), text = "{nbsp}", `type` = Other, settings = Some(ColumnSettings()))
}
