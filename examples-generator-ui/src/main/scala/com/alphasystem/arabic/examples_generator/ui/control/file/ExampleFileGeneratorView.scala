package com.alphasystem
package arabic
package examples_generator
package ui
package control
package file

import ui.model.{ Action, ColumnData, ExampleData, InsertPosition, Row, TableData, TableProperties }
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{
  ObjectProperty,
  ReadOnlyBooleanProperty,
  ReadOnlyBooleanWrapper,
  ReadOnlyObjectProperty,
  ReadOnlyObjectWrapper
}
import scalafx.collections.ObservableBuffer

import java.nio.file.Path
import java.util.UUID

class ExampleFileGeneratorView extends Control {

  private[file] val filePathProperty = ObjectProperty[Option[Path]](this, "filePath")
  private val selectedTablePropertyWrapper = new ReadOnlyObjectWrapper[TableData](this, "selectedTable")
  private val selectedRowPropertyWrapper = new ReadOnlyObjectWrapper[Row](this, "selectedRow")
  private val selectedColumnPropertyWrapper = new ReadOnlyObjectWrapper[ColumnData](this, "selectedColumn")
  private val dirtyPropertyWrapper = new ReadOnlyBooleanWrapper(this, "dirty", false)
  private[file] val actionProperty = ObjectProperty[Action](this, "action")
  private[file] val tableDataProperty = ObservableBuffer[TableData]()
  private[file] val tableTagsProperty = new ObservableBuffer[String](tableDataProperty.map(_.tag))

  actionProperty.onChange((_, _, nv) => {
    nv match {
      case Action.NoAction                                => // do nothing
      case Action.AddRow(row, insertPosition)             => handleAddRow(row, insertPosition)
      case Action.AddColumn(columnData, insertPosition)   => handleInsertColumn(columnData, insertPosition)
      case Action.UpdateColumn(columnData)                => handleUpdateColumn(columnData)
      case Action.UpdateTable(tableProperties)            => handleUpdateTable(tableProperties)
      case Action.RemoveRow(rowId)                        => handleRemoveRow(rowId)
      case Action.RemoveColumn(columnId)                  => handleRemoveColumn(columnId)
      case Action.SelectColumn(_) | Action.SelectTable(_) => // these will be handled by ExampleFileGeneratorSkin
    }
  })

  private def handleAddRow(row: Row, position: InsertPosition): Unit = {
    val referenceRowId = Option(selectedRowPropertyWrapper.value).map(_.id)
    insertRowAt(row, referenceRowId, position)
    dirtyPropertyWrapper.value = true
    action = Action.NoAction
  }

  private def insertRowAt(
    row: Row,
    referenceRowId: Option[UUID],
    position: InsertPosition
  ): Unit = {
    val rows = selectedTable.row
    val insertIndex = referenceRowId match {
      case Some(id) =>
        val idx = rows.indexWhere(_.id == id)
        if idx >= 0 then idx + position.offset else rows.size
      case None => rows.size
    }
    val updatedRows = rows.patch(insertIndex, Seq(row), 0)
    selectedTablePropertyWrapper.value = selectedTable.copy(row = updatedRows)
    updateTableData(selectedTable)
    selectedRowPropertyWrapper.value = row
  }

  private def handleInsertColumn(columnData: ColumnData, position: InsertPosition): Unit = {
    val referenceColumnId = Option(selectedColumnPropertyWrapper.value).map(_.id)
    insertColumnAt(columnData, referenceColumnId, position)
    dirtyPropertyWrapper.value = true
    action = Action.NoAction
  }

  private def insertColumnAt(
    columnData: ColumnData,
    referenceColumnId: Option[UUID],
    position: InsertPosition
  ): Unit = {
    val currentRow = selectedRowPropertyWrapper.value
    if currentRow != null then {
      val rowIndex = selectedTable.row.indexWhere(_.id == currentRow.id)
      if rowIndex > -1 then {
        val columns = selectedTable.row(rowIndex).columns
        val insertIndex = referenceColumnId match {
          case Some(id) =>
            val idx = columns.indexWhere(_.id == id)
            if idx >= 0 then idx + position.offset else columns.size
          case None => columns.size
        }
        val updatedColumns = columns.patch(insertIndex, Seq(columnData), 0)
        val updatedRow = selectedTable.row(rowIndex).copy(columns = updatedColumns)
        val updatedRows = selectedTable.row.updated(rowIndex, updatedRow)
        selectedTablePropertyWrapper.value = selectedTable.copy(row = updatedRows)
        updateTableData(selectedTable)
        selectedRowPropertyWrapper.value = updatedRow
      }
    }
  }

  private def handleUpdateColumn(columnData: ColumnData): Unit = {
    val rowIndex = selectedTable.row.indexWhere(_.columns.exists(_.id == columnData.id))
    if rowIndex > -1 then {
      val currentRow = selectedTable.row(rowIndex)
      val updatedColumns = currentRow.columns.map(c => if c.id == columnData.id then columnData else c)
      val updatedRow = currentRow.copy(columns = updatedColumns)
      selectedTablePropertyWrapper.value = selectedTable.copy(row = selectedTable.row.updated(rowIndex, updatedRow))
      updateTableData(selectedTable)
      selectedRowPropertyWrapper.value = updatedRow
      selectedColumnPropertyWrapper.value = columnData
      dirtyPropertyWrapper.value = true
    }
    action = Action.NoAction
  }

  private def handleUpdateTable(tableProperties: TableProperties): Unit = {
    val currentTable = selectedTablePropertyWrapper.value
    if currentTable != null then {
      val updatedTable = currentTable.copy(tableProperties = tableProperties)
      selectedTablePropertyWrapper.value = updatedTable
      updateTableData(updatedTable)
      dirtyPropertyWrapper.value = true
    }
    action = Action.NoAction
  }

  private def handleRemoveRow(rowId: UUID): Unit = {
    val currentTable = selectedTablePropertyWrapper.value
    if currentTable != null then {
      val updatedTable = currentTable.copy(row = currentTable.row.filterNot(_.id == rowId))
      selectedTablePropertyWrapper.value = updatedTable
      updateTableData(updatedTable)
      clearRowSelection()
      clearColumnSelection()
      dirtyPropertyWrapper.value = true
    }
    action = Action.NoAction
  }

  private def handleRemoveColumn(columnId: UUID): Unit = {
    val rowIndex = selectedTable.row.indexWhere(_.columns.exists(_.id == columnId))
    if rowIndex > -1 then {
      val currentRow = selectedTable.row(rowIndex)
      val updatedRow = currentRow.copy(columns = currentRow.columns.filterNot(_.id == columnId))
      selectedTablePropertyWrapper.value = selectedTable.copy(row = selectedTable.row.updated(rowIndex, updatedRow))
      updateTableData(selectedTable)
      selectedRowPropertyWrapper.value = updatedRow
      selectedColumnPropertyWrapper.value = null
      clearRowSelection()
      clearColumnSelection()
      dirtyPropertyWrapper.value = true
    }
    action = Action.NoAction
  }

  setSkin(createDefaultSkin())

  def filePath: Option[Path] = filePathProperty.value
  def filePath_=(value: Path): Unit = filePathProperty.value = Option(value)

  def selectedTableProperty: ReadOnlyObjectProperty[TableData] = selectedTablePropertyWrapper.readOnlyProperty
  def selectedRowProperty: ReadOnlyObjectProperty[Row] = selectedRowPropertyWrapper.readOnlyProperty
  def selectedColumnProperty: ReadOnlyObjectProperty[ColumnData] = selectedColumnPropertyWrapper.readOnlyProperty
  def dirtyProperty: ReadOnlyBooleanProperty = dirtyPropertyWrapper.readOnlyProperty
  private def selectedTable: TableData = selectedTablePropertyWrapper.value
  def selectTable(value: String): Unit = {
    tableDataProperty.find(_.tableProperties.tag == value) match {
      case Some(tableData) => selectedTablePropertyWrapper.value = tableData
      case None            => throw new RuntimeException(s"Table ($value) does not exists")
    }
  }

  private[file] def selectRow(value: Row): Unit = selectedRowPropertyWrapper.value = value
  private[file] def clearRowSelection(): Unit = selectedRowPropertyWrapper.value = null

  private[file] def selectColumn(value: ColumnData): Unit = selectedColumnPropertyWrapper.value = value
  private[file] def clearColumnSelection(): Unit = selectedColumnPropertyWrapper.value = null

  def action: Action = actionProperty.value
  def action_=(value: Action): Unit = actionProperty.value = value

  def markClean(): Unit = dirtyPropertyWrapper.value = false

  def toExampleData: ExampleData = ExampleData(tableDataProperty.toSeq)

  def addExampleData(exampleData: ExampleData): Unit = {
    selectedRowPropertyWrapper.value = null
    selectedColumnPropertyWrapper.value = null
    tableDataProperty.clear()
    tableTagsProperty.clear()
    dirtyPropertyWrapper.value = false
    val examples = exampleData.examples
    tableDataProperty.addAll(examples)
    tableTagsProperty.addAll(examples.map(_.tag))
    if examples.nonEmpty then {
      selectTable(examples.head.tag)
    }
  }

  def addTableData(value: TableData): Unit = {
    tableDataProperty.add(value)
    tableTagsProperty.add(value.tag)
    dirtyPropertyWrapper.value = true
  }

  private def updateTableData(tableData: TableData): Unit = {
    val index = tableDataProperty.indexWhere(_.id == tableData.id, 0)
    if index > -1 then tableDataProperty.update(index, tableData)
  }

  override def createDefaultSkin(): Skin[?] = skin.ExampleFileGeneratorSkin(this)
}

object ExampleFileGeneratorView {
  def apply(): ExampleFileGeneratorView = new ExampleFileGeneratorView()
}
