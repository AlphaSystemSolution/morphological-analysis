package com.alphasystem
package arabic
package examples_generator
package ui
package control
package file

import ui.model.{ Action, Row, TableData }
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, ReadOnlyObjectProperty, ReadOnlyObjectWrapper, StringProperty }
import scalafx.collections.ObservableBuffer

class ExampleFileGeneratorView extends Control {

  private[file] val fileNameProperty = StringProperty("")
  private val selectedTablePropertyWrapper = new ReadOnlyObjectWrapper[TableData](this, "selectedTable")
  private val selectedRowPropertyWrapper = new ReadOnlyObjectWrapper[Row](this, "selectedRow")
  private[file] val actionProperty = ObjectProperty[Action](this, "action")
  private[file] val tableDataProperty = ObservableBuffer[TableData]()
  private[file] val tableTagsProperty = new ObservableBuffer[String](tableDataProperty.map(_.id))

  setSkin(createDefaultSkin())
  actionProperty.onChange((_, _, nv) => {
    nv match {
      case Action.NoAction => // do nothing
      case Action.AddRow(row) =>
        selectedTablePropertyWrapper.value = selectedTable.copy(row = selectedTable.row :+ row)
        updateTableData(selectedTable)
        selectedRowPropertyWrapper.value = row
    }
  })

  def fileName: String = fileNameProperty.value
  def fileName_=(value: String): Unit = fileNameProperty.value = value

  def selectedTableProperty: ReadOnlyObjectProperty[TableData] = selectedTablePropertyWrapper.readOnlyProperty
  def selectedRowProperty: ReadOnlyObjectProperty[Row] = selectedRowPropertyWrapper.readOnlyProperty
  private def selectedTable: TableData = selectedTablePropertyWrapper.value
  private[file] def selectTable(value: String): Unit = {
    tableDataProperty.find(_.tableProperties.tag == value) match {
      case Some(tableData) => selectedTablePropertyWrapper.value = tableData
      case None            => throw new RuntimeException(s"Table ($value) does not exists")
    }
  }

  private[file] def selectRow(value: Row): Unit = selectedRowPropertyWrapper.value = value
  private[file] def clearRowSelection(): Unit = selectedRowPropertyWrapper.value = null

  def action: Action = actionProperty.value
  def action_=(value: Action): Unit = actionProperty.value = value

  def addTableData(value: TableData): Unit = {
    tableDataProperty.add(value)
    tableTagsProperty.add(value.id)
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
