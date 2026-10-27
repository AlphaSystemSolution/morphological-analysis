package com.alphasystem
package arabic
package examples_generator
package ui
package utils

import examples_generator.model.{
  ColumnData,
  ColumnSettings,
  ColumnType,
  RowData,
  TableColumnInfo,
  TableData,
  TableProperties
}
import ui.model.InsertPosition

import java.util.UUID
import scala.collection.mutable.ListBuffer

class TableBuilder(_tableData: TableData) {

  private var tableProperties = _tableData.tableProperties
  private val rows = ListBuffer.from[RowData](_tableData.row)

  private def numsOfCols: Int = tableProperties.columns.size

  def tableData: TableData = _tableData.copy(tableProperties = tableProperties, row = rows.toSeq)

  /** Replaces the table properties without touching the rows. Used for non-structural updates such as tag, width,
    * frame, grid, or role changes. Structural column changes should go through [[addColumn]].
    *
    * @param props
    *   the new table properties
    */
  def updateTableProperties(props: TableProperties): Unit = tableProperties = props

  /** Adds a new row to the table at the specified position relative to an existing row. When `rowId` is not provided or
    * not found, the row is appended at the end.
    *
    * @param rowId
    *   The unique identifier of the row in the table relative to which the new row will be added.
    * @param insertPosition
    *   The position relative to the current row where the new row should be added (e.g., before or after the current
    *   row).
    * @return
    *   The newly created row.
    */
  def addRow(rowId: Option[UUID], insertPosition: InsertPosition): RowData = {
    val insertIndex = rowId
      .flatMap(id =>
        rows.indexWhere(_.id == id) match {
          case index if index >= 0 => Some(index + insertPosition.offset)
          case _                   => None
        }
      )
      .getOrElse(rows.size)
    val row = createRow(numsOfCols)
    rows.insert(insertIndex, row)
    row
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
          val positions = columnPositions._1
          val startColumn = positions(currentRowIndex)
            .take(targetIndex)
            .lastOption
            .map { case (column, start) => start + column.colSpan }
            .getOrElse(0)
          val spanStart =
            if insertPosition.isEmpty then positions(currentRowIndex)(anchorIndex)._2 else startColumn
          val existingRowsBelow = rows.size - currentRowIndex - 1
          (currentRowSpan until math.min(newRowSpan, existingRowsBelow + 1)).foreach { offset =>
            val rowIndex = currentRowIndex + offset
            val remaining = positions(rowIndex).collect {
              case (column, start) if start + column.colSpan <= spanStart || start >= spanStart + newColSpan => column
            }
            rows.update(rowIndex, rows(rowIndex).copy(columns = remaining))
          }
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

  /** Inserts a new grid column into the table: the `columnInfo` is added to the table properties at `gridIndex` and a
    * default cell is inserted into every row at the corresponding position. When the insertion point falls inside an
    * existing cell's `colSpan` or inside a grid column covered by a `rowSpan` from a row above, that cell's `colSpan`
    * is grown by one instead of inserting a new cell.
    *
    * @param columnInfo
    *   the column metadata to add to the table properties
    * @param gridIndex
    *   the 0-based grid column index where the new column should be inserted; `numsOfCols` appends at the end
    */
  def addColumn(columnInfo: TableColumnInfo, gridIndex: Int): Unit = {
    require(gridIndex >= 0 && gridIndex <= numsOfCols, "Grid index out of bounds")

    val (positions, coverage) = columnPositions
    rows.indices.foreach { rowIndex =>
      if gridIndex == numsOfCols then appendColumn(rowIndex)
      else
        coverage.get((rowIndex, gridIndex)) match {
          case Some(ownerId) =>
            // covered by a rowSpan from above: stretch the covering column over the new grid column
            incrementSpan(ownerId, isRowSpan = false)
          case None =>
            positions(rowIndex).find { case (column, start) =>
              start <= gridIndex && gridIndex < start + column.colSpan
            } match {
              case Some((column, start)) if start == gridIndex => insertColumnBefore(rowIndex, column.id)
              case Some((column, _))                           => incrementSpan(column.id, isRowSpan = false)
              case None                                        => appendColumn(rowIndex)
            }
        }
    }

    tableProperties = tableProperties.copy(columns = tableProperties.columns.patch(gridIndex, Seq(columnInfo), 0))
  }

  private def insertColumnBefore(rowIndex: Int, columnId: UUID): Unit = {
    val row = rows(rowIndex)
    val index = row.columns.indexWhere(_.id == columnId)
    if index >= 0 then rows.update(rowIndex, row.copy(columns = row.columns.patch(index, Seq(defaultColumn()), 0)))
  }

  private def appendColumn(rowIndex: Int): Unit = {
    val row = rows(rowIndex)
    rows.update(rowIndex, row.copy(columns = row.columns :+ defaultColumn()))
  }

  private def incrementSpan(columnId: UUID, isRowSpan: Boolean): Unit =
    rows.indices.foreach { i =>
      val columns = rows(i).columns
      val columnIndex = columns.indexWhere(_.id == columnId)
      if columnIndex >= 0 then {
        val column = columns(columnIndex)
        val settings = column.settings.getOrElse(ColumnSettings())
        val updated =
          if isRowSpan then settings.copy(rowSpan = settings.rowSpan + 1)
          else settings.copy(colSpan = settings.colSpan + 1)
        rows.update(i, rows(i).copy(columns = columns.updated(columnIndex, column.copy(settings = Some(updated)))))
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

  /** Computes the grid start column of every cell in each row along with a coverage map of grid slots that are occupied
    * by `rowSpan`s starting in rows above.
    *
    * @return
    *   a pair where the first element is, per row, each cell paired with its grid start column, and the second element
    *   maps `(rowIndex, gridColumn)` to the id of the column whose span covers that slot
    */
  private def columnPositions: (Seq[Seq[(ColumnData, Int)]], Map[(Int, Int), UUID]) = {
    val occupiedUntil = scala.collection.mutable.Map.empty[Int, Int]
    val coverage = scala.collection.mutable.Map.empty[(Int, Int), UUID]
    val positions = rows
      .zipWithIndex
      .map { case (row, rowIndex) =>
        var gridColumn = 0
        row.columns.map { column =>
          while (gridColumn until gridColumn + column.colSpan).exists(occupiedUntil.getOrElse(_, 0) > rowIndex) do
            gridColumn += 1
          val start = gridColumn
          (start until start + column.colSpan).foreach { index =>
            occupiedUntil.update(index, rowIndex + column.rowSpan)
            ((rowIndex + 1) until (rowIndex + column.rowSpan)).foreach { coveredRow =>
              coverage.update((coveredRow, index), column.id)
            }
          }
          gridColumn += column.colSpan
          (column, start)
        }
      }
      .toSeq

    (positions, coverage.toMap)
  }

  private def createRow(numOfColumns: Int, coveredBy: Option[UUID] = None): RowData =
    RowData(id = UUID.randomUUID(), columns = List.fill(numOfColumns)(defaultColumn(coveredBy)))

  private def defaultColumn(coveredBy: Option[UUID] = None): ColumnData =
    ColumnData(
      id = UUID.randomUUID(),
      text = "{nbsp}",
      `type` = ColumnType.Other,
      settings = Some(ColumnSettings()),
      coveredBy = coveredBy
    )
}
