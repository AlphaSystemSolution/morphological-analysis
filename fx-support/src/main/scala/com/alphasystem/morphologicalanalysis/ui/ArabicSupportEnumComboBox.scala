package com.alphasystem.morphologicalanalysis.ui

import com.alphasystem.arabic.model.ArabicSupportEnum
import com.alphasystem.fx.ui.util.UIUserPreferences
import javafx.beans.NamedArg
import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import scalafx.collections.ObservableBuffer

import java.lang.Enum

class ArabicSupportEnumComboBox[T <: Enum[T] & ArabicSupportEnum](
  @NamedArg("klass") klass: Class[T],
  @NamedArg("required") required: Boolean,
  @NamedArg("listType") listType: ListType
)(implicit preferences: UIUserPreferences)
    extends ComboBox[T] {

  setItems(FXCollections.observableArrayList[T](klass.getEnumConstants*))
  setCellFactory(ArabicSupportEnumCellFactory(listType))
  setButtonCell(ArabicSupportEnumListCell(listType))
  getSelectionModel.select(0)
  setMaxWidth(Double.MaxValue)
  setMaxHeight(Double.MaxValue)
}

object ArabicSupportEnumComboBox {

  def apply[T <: Enum[T] & ArabicSupportEnum](
    klass: Class[T],
    required: Boolean,
    listType: ListType
  )(implicit preferences: UIUserPreferences
  ): ArabicSupportEnumComboBox[T] =
    new ArabicSupportEnumComboBox[T](klass, required, listType)
}
