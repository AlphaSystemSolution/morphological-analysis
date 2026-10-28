package com.alphasystem
package arabic
package examples_generator
package ui
package control
package examples_view

import examples_generator.model.TableData
import ui.model.{ Action, ExampleRequest }
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ BooleanProperty, ObjectProperty, ReadOnlyBooleanProperty, ReadOnlyBooleanWrapper }

class ExamplesView extends Control {

  private[examples_view] val exampleRequestProperty = ObjectProperty[Option[ExampleRequest]](this, "exampleRequest")
  private[examples_view] val tableDataProperty = ObjectProperty[Option[TableData]](this, "tableData")
  private[examples_view] val actionProperty = ObjectProperty[Action](this, "action")
  private[examples_view] val markViewCleanProperty = new BooleanProperty(this, "markViewClean", false)
  private[examples_view] val dirtyPropertyWrapper = new ReadOnlyBooleanWrapper(this, "dirty", false)
  private[examples_view] val hasSelectedTablePropertyWrapper =
    new ReadOnlyBooleanWrapper(this, "hasSelectedTable", false)
  private[examples_view] val hasSelectedColumnPropertyWrapper =
    new ReadOnlyBooleanWrapper(this, "hasSelectedColumn", false)

  setSkin(createDefaultSkin())

  def exampleRequest: Option[ExampleRequest] = exampleRequestProperty.value
  def exampleRequest_=(value: ExampleRequest): Unit = exampleRequestProperty.value = Option(value)

  def tableData: Option[TableData] = tableDataProperty.value
  def tableData_=(value: TableData): Unit = tableDataProperty.value = Option(value)

  def action: Action = actionProperty.value
  def action_=(value: Action): Unit = actionProperty.value = value

  def markViewClean: Boolean = markViewCleanProperty.value
  def markViewClean_=(value: Boolean): Unit = markViewCleanProperty.value = value

  def dirtyProperty: ReadOnlyBooleanProperty = dirtyPropertyWrapper.readOnlyProperty
  def dirty: Boolean = dirtyPropertyWrapper.value
  private[examples_view] def dirty_=(value: Boolean): Unit = dirtyPropertyWrapper.value = value

  def hasSelectedTableProperty: ReadOnlyBooleanProperty = hasSelectedTablePropertyWrapper.readOnlyProperty
  def hasSelectedTable: Boolean = hasSelectedTablePropertyWrapper.value
  private[examples_view] def hasSelectedTable_=(value: Boolean): Unit = hasSelectedTablePropertyWrapper.value = value

  def hasSelectedColumnProperty: ReadOnlyBooleanProperty = hasSelectedColumnPropertyWrapper.readOnlyProperty
  def hasSelectedColumn: Boolean = hasSelectedColumnPropertyWrapper.value
  private[examples_view] def hasSelectedColumn_=(value: Boolean): Unit = hasSelectedColumnPropertyWrapper.value = value

  override def createDefaultSkin(): Skin[?] = skin.ExamplesSkin(this)
}

object ExamplesView {
  def apply(): ExamplesView = new ExamplesView()
}
