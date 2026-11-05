package com.alphasystem
package arabic
package examples_generator
package ui
package control
package examples_view

import ui.model.{ Action, ExampleRequest, FileType }
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, ReadOnlyBooleanProperty, ReadOnlyBooleanWrapper, ReadOnlyObjectWrapper }

class ExamplesView extends Control {

  private[examples_view] val exampleRequestProperty =
    new ReadOnlyObjectWrapper[Option[ExampleRequest]](this, "exampleRequest")
  private val fileTypeProperty = ObjectProperty[FileType](this, "fileType")
  private[examples_view] val actionProperty = ObjectProperty[Action](this, "action")
  private[examples_view] val dirtyPropertyWrapper = new ReadOnlyBooleanWrapper(this, "dirty", false)
  private[examples_view] val multipleDirtyPropertyWrapper = new ReadOnlyBooleanWrapper(this, "multipleDirty", false)
  private[examples_view] val hasSelectedTablePropertyWrapper =
    new ReadOnlyBooleanWrapper(this, "hasSelectedTable", false)
  private[examples_view] val hasSelectedColumnPropertyWrapper =
    new ReadOnlyBooleanWrapper(this, "hasSelectedColumn", false)
  private val hasConjugationsFilePropertyWrapper = new ReadOnlyBooleanWrapper(this, "hasConjugationsFile", false)

  fileType = FileType.Example
  exampleRequest = null
  setSkin(createDefaultSkin())

  def exampleRequest: Option[ExampleRequest] = exampleRequestProperty.value
  private[examples_view] def exampleRequest_=(value: ExampleRequest): Unit =
    exampleRequestProperty.value = Option(value)

  def fileType: FileType = fileTypeProperty.value
  def fileType_=(value: FileType): Unit = fileTypeProperty.value = value

  def action: Action = actionProperty.value
  def action_=(value: Action): Unit = actionProperty.value = value

  def dirtyProperty: ReadOnlyBooleanProperty = dirtyPropertyWrapper.readOnlyProperty
  def dirty: Boolean = dirtyPropertyWrapper.value
  private[examples_view] def dirty_=(value: Boolean): Unit = dirtyPropertyWrapper.value = value

  def multipleDirtyProperty: ReadOnlyBooleanProperty = multipleDirtyPropertyWrapper.readOnlyProperty
  def multipleDirty: Boolean = multipleDirtyPropertyWrapper.value
  private[examples_view] def multipleDirty_=(value: Boolean): Unit = multipleDirtyPropertyWrapper.value = value

  def hasSelectedTableProperty: ReadOnlyBooleanProperty = hasSelectedTablePropertyWrapper.readOnlyProperty
  def tableIsSelected: Boolean = hasSelectedTablePropertyWrapper.value
  private[examples_view] def tableIsSelected_=(value: Boolean): Unit = hasSelectedTablePropertyWrapper.value = value

  def hasSelectedColumnProperty: ReadOnlyBooleanProperty = hasSelectedColumnPropertyWrapper.readOnlyProperty
  def columnIsSelected: Boolean = hasSelectedColumnPropertyWrapper.value
  private[examples_view] def columnIsSelected_=(value: Boolean): Unit = hasSelectedColumnPropertyWrapper.value = value

  def hasConjugationsFileProperty: ReadOnlyBooleanProperty = hasConjugationsFilePropertyWrapper.readOnlyProperty
  def conjugationsFile: Boolean = hasConjugationsFilePropertyWrapper.value
  private[examples_view] def conjugationsFile_=(value: Boolean): Unit = hasConjugationsFilePropertyWrapper.value = value

  override def createDefaultSkin(): Skin[?] = skin.ExamplesSkin(this)
}

object ExamplesView {
  def apply(): ExamplesView = new ExamplesView()
}
