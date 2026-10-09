package com.alphasystem
package arabic
package examples_generator
package ui
package control
package file

import com.alphasystem.arabic.examples_generator.ui.model.TableData
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer

class ExampleFileGeneratorView extends Control {

  private[file] val fileNameProperty = StringProperty("")
  private[file] val tableDataProperty = ObservableBuffer[TableData]()
  private[file] val tableTagsProperty = new ObservableBuffer[String](tableDataProperty.map(_.id))

  setSkin(createDefaultSkin())

  def fileName: String = fileNameProperty.value
  def fileName_=(value: String): Unit = fileNameProperty.value = value

  def addTableData(value: TableData): Unit = {
    tableDataProperty.add(value)
    tableTagsProperty.add(value.id)
  }

  override def createDefaultSkin(): Skin[?] = skin.ExampleFileGeneratorSkin(this)
}

object ExampleFileGeneratorView {
  def apply(): ExampleFileGeneratorView = new ExampleFileGeneratorView()
}
