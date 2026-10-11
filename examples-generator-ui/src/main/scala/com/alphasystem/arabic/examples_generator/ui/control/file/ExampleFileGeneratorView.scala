package com.alphasystem
package arabic
package examples_generator
package ui
package control
package file

import ui.model.{Row, TableData}
import javafx.scene.control.{Control, Skin}
import scalafx.beans.property.{ReadOnlyObjectProperty, ReadOnlyObjectWrapper, StringProperty}
import scalafx.collections.ObservableBuffer

import java.util.UUID

class ExampleFileGeneratorView extends Control {

  private[file] val fileNameProperty = StringProperty("")
  private val selectedTablePropertyWrapper = new ReadOnlyObjectWrapper[TableData](this, "selectedTable")
  private[file] val tableDataProperty = ObservableBuffer[TableData]()
  private[file] val tableTagsProperty = new ObservableBuffer[String](tableDataProperty.map(_.id))

  setSkin(createDefaultSkin())

  def fileName: String = fileNameProperty.value
  def fileName_=(value: String): Unit = fileNameProperty.value = value

  def selectedTableProperty: ReadOnlyObjectProperty[TableData] = selectedTablePropertyWrapper.readOnlyProperty
  private def selectedTable: TableData = selectedTablePropertyWrapper.value
  private[file] def selectTable(value: String): Unit = {
    tableDataProperty.find(_.tableProperties.tag == value) match {
      case Some(tableData) => selectedTablePropertyWrapper.value = tableData
      case None            => throw new RuntimeException(s"Table ($value) does not exists")
    }
  }

  def addTableData(value: TableData): Unit = {
    tableDataProperty.add(value)
    tableTagsProperty.add(value.id)
  }

  def addRow(): Unit =
    selectedTablePropertyWrapper.value = selectedTable.copy(row = selectedTable.row :+ Row(UUID.randomUUID()))

  override def createDefaultSkin(): Skin[?] = skin.ExampleFileGeneratorSkin(this)
}

object ExampleFileGeneratorView {
  def apply(): ExampleFileGeneratorView = new ExampleFileGeneratorView()
}
