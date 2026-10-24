package com.alphasystem
package arabic
package examples_generator
package ui
package control
package file

import ui.model.{ Action, ColumnData, ExampleData, InsertPosition, Row, TableData, TableProperties }
import ui.utils.TableBuilder
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
  private var tableBuilder: Option[TableBuilder] = None

  actionProperty.onChange((_, _, nv) => {
    nv match {
      case Action.NoAction                                => // do nothing
      case Action.AddRow(insertPosition)                  => handleAddRow(insertPosition)
      case Action.AddColumn(columnData, insertPosition)   => handleInsertColumn(columnData, insertPosition)
      case Action.UpdateColumn(columnData)                => handleUpdateColumn(columnData)
      case Action.UpdateTable(tableProperties)            => handleUpdateTable(tableProperties)
      case Action.RemoveRow(rowId)                        => handleRemoveRow(rowId)
      case Action.RemoveColumn(columnId)                  => handleRemoveColumn(columnId)
      case Action.SelectColumn(_) | Action.SelectTable(_) => // these will be handled by ExampleFileGeneratorSkin
    }
  })

  private def handleAddRow(position: InsertPosition): Unit = {
    tableBuilder.foreach { builder =>
      val newRow = builder.addRow(Option(selectedRowPropertyWrapper.value).map(_.id), position)
      selectedRowPropertyWrapper.value = newRow
      clearColumnSelection()
      applyTableUpdate()
    }
    action = Action.NoAction
  }

  private def handleInsertColumn(columnData: ColumnData, position: InsertPosition): Unit = {
    tableBuilder.foreach { builder =>
      val rowId = Option(selectedRowPropertyWrapper.value).map(_.id)
      val referenceColumnId = Option(selectedColumnPropertyWrapper.value).map(_.id)
      builder.addOrUpdateColumn(rowId, referenceColumnId, columnData, Some(position))
      rowId.flatMap(id => builder.tableData.row.find(_.id == id)).foreach(selectedRowPropertyWrapper.value = _)
      selectedColumnPropertyWrapper.value = columnData
      applyTableUpdate()
    }
    action = Action.NoAction
  }

  private def handleUpdateColumn(columnData: ColumnData): Unit = {
    tableBuilder.foreach { builder =>
      builder.addOrUpdateColumn(None, Some(columnData.id), columnData)
      builder.tableData.row.find(_.columns.exists(_.id == columnData.id)).foreach { updatedRow =>
        selectedRowPropertyWrapper.value = updatedRow
        selectedColumnPropertyWrapper.value = columnData
      }
      applyTableUpdate()
    }
    action = Action.NoAction
  }

  private def handleUpdateTable(tableProperties: TableProperties): Unit = {
    val currentTable = selectedTablePropertyWrapper.value
    if currentTable != null then {
      tableBuilder match {
        case Some(builder) =>
          val oldColumnIds = currentTable.tableProperties.columns.map(_.id).toSet
          // insert each added column at its final grid index in ascending order;
          // earlier inserts don't disturb later final indices
          tableProperties
            .columns
            .zipWithIndex
            .filterNot((column, _) => oldColumnIds(column.id))
            .sortBy(_._2)
            .foreach((info, index) => builder.addColumn(info, index))
          builder.updateTableProperties(tableProperties)
          applyTableUpdate()
        case None =>
          val updatedTable = currentTable.copy(tableProperties = tableProperties)
          tableBuilder = Some(new TableBuilder(updatedTable))
          updateTableData(updatedTable)
          selectedTablePropertyWrapper.value = updatedTable
          dirtyPropertyWrapper.value = true
      }
    }
    action = Action.NoAction
  }

  private def handleRemoveRow(rowId: UUID): Unit = {
    tableBuilder.foreach { builder =>
      builder.removeRow(rowId)
      clearRowSelection()
      clearColumnSelection()
      applyTableUpdate()
    }
    action = Action.NoAction
  }

  private def handleRemoveColumn(columnId: UUID): Unit = {
    tableBuilder.foreach { builder =>
      builder.removeColumn(None, columnId)
      Option(selectedRowPropertyWrapper.value)
        .flatMap(row => builder.tableData.row.find(_.id == row.id))
        .foreach(selectedRowPropertyWrapper.value = _)
      clearColumnSelection()
      applyTableUpdate()
    }
    action = Action.NoAction
  }

  private def applyTableUpdate(): Unit =
    tableBuilder.foreach { builder =>
      val updatedTable = builder.tableData
      updateTableData(updatedTable)
      selectedTablePropertyWrapper.value = updatedTable
      dirtyPropertyWrapper.value = true
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
      case Some(tableData) =>
        selectedTablePropertyWrapper.value = tableData
        tableBuilder = Some(new TableBuilder(tableData))
      case None => throw new RuntimeException(s"Table ($value) does not exists")
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
    tableBuilder = None
    selectedRowPropertyWrapper.value = null
    selectedColumnPropertyWrapper.value = null
    tableDataProperty.clear()
    tableTagsProperty.clear()
    dirtyPropertyWrapper.value = true
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
