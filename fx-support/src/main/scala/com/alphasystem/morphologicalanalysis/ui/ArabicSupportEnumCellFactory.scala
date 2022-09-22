package com.alphasystem.morphologicalanalysis.ui

import com.alphasystem.arabic.model.ArabicSupportEnum
import com.alphasystem.fx.ui.util.UIUserPreferences
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback

import java.lang.Enum

class ArabicSupportEnumCellFactory[T <: Enum[T] & ArabicSupportEnum](
  listType: ListType
)(implicit preferences: UIUserPreferences)
    extends Callback[ListView[T], ListCell[T]] {

  override def call(p: ListView[T]): ListCell[T] =
    ArabicSupportEnumListCell(listType)
}

object ArabicSupportEnumCellFactory {

  def apply[T <: Enum[T] & ArabicSupportEnum](
    listType: ListType
  )(implicit preferences: UIUserPreferences
  ): ArabicSupportEnumCellFactory[T] =
    new ArabicSupportEnumCellFactory[T](listType)
}
