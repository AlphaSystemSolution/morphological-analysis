package com.alphasystem
package arabic
package examples_generator
package ui
package control
package file

import ui.model.{ Action, ColumnData, ExampleData, Row, TableData }
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, ReadOnlyObjectProperty, ReadOnlyObjectWrapper }
import scalafx.collections.ObservableBuffer

import java.nio.file.Path

class ExampleFileGeneratorView extends Control {

  private[file] val filePathProperty = ObjectProperty[Option[Path]](this, "filePath")
  private val selectedTablePropertyWrapper = new ReadOnlyObjectWrapper[TableData](this, "selectedTable")
  private val selectedRowPropertyWrapper = new ReadOnlyObjectWrapper[Row](this, "selectedRow")
  private val selectedColumnPropertyWrapper = new ReadOnlyObjectWrapper[ColumnData](this, "selectedColumn")
  private[file] val actionProperty = ObjectProperty[Action](this, "action")
  private[file] val tableDataProperty = ObservableBuffer[TableData]()
  private[file] val tableTagsProperty = new ObservableBuffer[String](tableDataProperty.map(_.tag))

  actionProperty.onChange((_, _, nv) => {
    nv match {
      case Action.NoAction => // do nothing
      case Action.AddRow(row) =>
        selectedTablePropertyWrapper.value = selectedTable.copy(row = selectedTable.row :+ row)
        updateTableData(selectedTable)
        selectedRowPropertyWrapper.value = row
        action = Action.NoAction
      case Action.AddColumn(columnData) =>
        val currentRow = selectedRowPropertyWrapper.value
        if currentRow != null then {
          val rowIndex = selectedTable.row.indexWhere(_.id == currentRow.id)
          if rowIndex > -1 then {
            val updatedRow =
              selectedTable.row(rowIndex).copy(columns = selectedTable.row(rowIndex).columns :+ columnData)
            val updatedRows = selectedTable.row.updated(rowIndex, updatedRow)
            selectedTablePropertyWrapper.value = selectedTable.copy(row = updatedRows)
            updateTableData(selectedTable)
            selectedRowPropertyWrapper.value = updatedRow
          }
        }
        action = Action.NoAction
      case Action.UpdateColumn(columnData) =>
        val rowIndex = selectedTable.row.indexWhere(_.columns.exists(_.id == columnData.id))
        if rowIndex > -1 then {
          val currentRow = selectedTable.row(rowIndex)
          val updatedColumns = currentRow.columns.map(c => if c.id == columnData.id then columnData else c)
          val updatedRow = currentRow.copy(columns = updatedColumns)
          selectedTablePropertyWrapper.value = selectedTable.copy(row = selectedTable.row.updated(rowIndex, updatedRow))
          updateTableData(selectedTable)
          selectedRowPropertyWrapper.value = updatedRow
          selectedColumnPropertyWrapper.value = columnData
        }
        action = Action.NoAction
      case Action.UpdateTable(tableProperties) =>
        val currentTable = selectedTablePropertyWrapper.value
        if currentTable != null then {
          val updatedTable = currentTable.copy(tableProperties = tableProperties)
          selectedTablePropertyWrapper.value = updatedTable
          updateTableData(updatedTable)
        }
        action = Action.NoAction
      case Action.RemoveColumn(columnId) =>
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
        }
        action = Action.NoAction
      case Action.SelectColumn(_) | Action.SelectTable(_) => // these will be handled by ExamplesGeneratorApp
    }
  })

  setSkin(createDefaultSkin())

  def filePath: Option[Path] = filePathProperty.value
  def filePath_=(value: Path): Unit = filePathProperty.value = Option(value)

  def selectedTableProperty: ReadOnlyObjectProperty[TableData] = selectedTablePropertyWrapper.readOnlyProperty
  def selectedRowProperty: ReadOnlyObjectProperty[Row] = selectedRowPropertyWrapper.readOnlyProperty
  def selectedColumnProperty: ReadOnlyObjectProperty[ColumnData] = selectedColumnPropertyWrapper.readOnlyProperty
  private def selectedTable: TableData = selectedTablePropertyWrapper.value
  private[file] def selectTable(value: String): Unit = {
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

  def toExampleData: ExampleData = ExampleData(tableDataProperty.toSeq)

  def addExampleData(exampleData: ExampleData): Unit = {
    selectedRowPropertyWrapper.value = null
    selectedColumnPropertyWrapper.value = null
    tableDataProperty.clear()
    tableTagsProperty.clear()
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
