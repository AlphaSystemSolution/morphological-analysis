package com.alphasystem
package morphologicalanalysis
package ui

import arabic.model.ArabicSupportEnum
import fx.ui.util.UIUserPreferences
import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import scalafx.collections.ObservableBuffer

class ArabicSupportEnumComboBox[T <: ArabicSupportEnum](
  values: Array[T],
  listType: ListType
)(implicit preferences: UIUserPreferences)
    extends ComboBox[T] {

  setItems(FXCollections.observableArrayList[T](values*))
  setCellFactory(ArabicSupportEnumCellFactory(listType))
  setButtonCell(ArabicSupportEnumListCell(listType))
  getSelectionModel.select(0)
  setMaxWidth(Double.MaxValue)
  setMaxHeight(Double.MaxValue)
}

object ArabicSupportEnumComboBox {

  def apply[T <: ArabicSupportEnum](
    values: Array[T],
    listType: ListType
  )(implicit preferences: UIUserPreferences
  ): ArabicSupportEnumComboBox[T] =
    new ArabicSupportEnumComboBox[T](values, listType)
}
