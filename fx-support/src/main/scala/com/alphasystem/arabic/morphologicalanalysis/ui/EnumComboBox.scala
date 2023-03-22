package com.alphasystem
package arabic
package morphologicalanalysis
package ui

import javafx.scene.control.{ ComboBox, ContentDisplay, ListCell, ListView }
import javafx.util.Callback
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer

class EnumComboBox[T <: Enum[T]](values: Array[T]) extends ComboBox[T] {

  import EnumComboBox.*

  setItems(ObservableBuffer.from(values))
  setCellFactory((_: ListView[T]) => new EnumListCell())
  setButtonCell(new EnumListCell[T]())
  getSelectionModel.select(0)
  setMaxWidth(Double.MaxValue)
  setMaxHeight(Double.MaxValue)
}

object EnumComboBox {
  def apply[T <: Enum[T]](values: Array[T]): EnumComboBox[T] = new EnumComboBox[T](values)

  class EnumListCell[T <: Enum[T]] extends ListCell[T] {

    setContentDisplay(ContentDisplay.TEXT_ONLY)

    override def updateItem(item: T, empty: Boolean): Unit = {
      super.updateItem(item, empty)
      setText(if Option(item).isDefined && !empty then item.name() else null)
    }
  }
}
