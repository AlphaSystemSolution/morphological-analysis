package com.alphasystem
package arabic
package examples_generator
package ui
package control
package table

import com.alphasystem.arabic.examples_generator.ui.model.{ Frame, Grid }
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, StringProperty }

class TablePropertiesEditorView extends Control {

  private[table] val tagProperty = StringProperty("")
  private[table] val tableWidthProperty = StringProperty("100")
  private[table] val frameProperty = ObjectProperty[Frame](this, "frame")
  private[table] val gridProperty = ObjectProperty[Grid](this, "grid")
  private[table] val roleProperty = StringProperty("")

  setSkin(createDefaultSkin())
  tableWidth = "100"
  frame = Frame.All
  grid = Grid.All

  def tag: String = tagProperty.value
  private[table] def tag_=(value: String): Unit = tagProperty.value = value

  def tableWidth: String = tableWidthProperty.value
  private[table] def tableWidth_=(value: String): Unit = tableWidthProperty.value = value

  def frame: Frame = frameProperty.value
  private[table] def frame_=(value: Frame): Unit = frameProperty.value = value

  def grid: Grid = gridProperty.value
  private[table] def grid_=(value: Grid): Unit = gridProperty.value = value

  def role: String = roleProperty.value
  private[table] def role_=(value: String): Unit = roleProperty.value = value

  override def createDefaultSkin(): Skin[?] = skin.TablePropertiesEditorSkin(this)
}

object TablePropertiesEditorView {
  def apply(): TablePropertiesEditorView = new TablePropertiesEditorView()
}
