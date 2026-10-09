package com.alphasystem
package arabic
package examples_generator
package ui
package control
package table

import com.alphasystem.arabic.examples_generator.ui.model.{
  Frame,
  Grid,
  HorizontalAlignment,
  TableColumnInfo,
  VerticalAlignment
}
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.binding.Bindings
import scalafx.beans.property.{ ObjectProperty, ReadOnlyBooleanProperty, ReadOnlyBooleanWrapper, StringProperty }
import scalafx.collections.ObservableBuffer

class TablePropertiesEditorView extends Control {

  private[table] val tagProperty = StringProperty("")
  private[table] val tableWidthProperty = StringProperty("100")
  private[table] val frameProperty = ObjectProperty[Frame](this, "frame")
  private[table] val gridProperty = ObjectProperty[Grid](this, "grid")
  private[table] val roleProperty = StringProperty("")
  private[table] val columnWidthProperty = StringProperty("")
  private[table] val horizontalAlignmentProperty = ObjectProperty[HorizontalAlignment](this, "horizontalAlignment")
  private[table] val verticalAlignmentProperty = ObjectProperty[VerticalAlignment](this, "verticalAlignment")
  private[table] val columnInfosProperty = ObservableBuffer[TableColumnInfo]()
  private val enableWrapperProperty = new ReadOnlyBooleanWrapper(this, "enable")

  setSkin(createDefaultSkin())
  tableWidth = "100"
  frame = Frame.All
  grid = Grid.All
  horizontalAlignment = HorizontalAlignment.Center
  verticalAlignment = VerticalAlignment.Center
  enableWrapperProperty.bind(
    tagProperty.isNotEmpty.and(Bindings.createBooleanBinding(() => columnInfosProperty.nonEmpty))
  )

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

  def horizontalAlignment: HorizontalAlignment = horizontalAlignmentProperty.value
  private[table] def horizontalAlignment_=(value: HorizontalAlignment): Unit = horizontalAlignmentProperty.value = value

  def verticalAlignment: VerticalAlignment = verticalAlignmentProperty.value
  private[table] def verticalAlignment_=(value: VerticalAlignment): Unit = verticalAlignmentProperty.value = value

  def enableProperty: ReadOnlyBooleanProperty = enableWrapperProperty.readOnlyProperty
  def enable: Boolean = enableWrapperProperty.value

  override def createDefaultSkin(): Skin[?] = skin.TablePropertiesEditorSkin(this)
}

object TablePropertiesEditorView {
  def apply(): TablePropertiesEditorView = new TablePropertiesEditorView()
}
