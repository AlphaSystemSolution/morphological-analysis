package com.alphasystem
package arabic
package examples_generator
package ui
package control
package column

import ui.model.ColumnType
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, StringProperty }

class CreateColumnView extends Control {

  private[column] val columnTypeProperty = ObjectProperty[ColumnType](this, "columnType", ColumnType.Arabic)
  private[column] val textProperty = StringProperty("")

  setSkin(createDefaultSkin())
  columnType = ColumnType.Arabic

  def columnType: ColumnType = columnTypeProperty.value
  def columnType_=(value: ColumnType): Unit = columnTypeProperty.value = value

  def text: String = textProperty.value
  def text_=(value: String): Unit = textProperty.value = value

  override def createDefaultSkin(): Skin[?] = skin.CreateColumnSkin(this)
}

object CreateColumnView {
  def apply(): CreateColumnView = new CreateColumnView()
}
