package com.alphasystem
package morphologicalanalysis
package ui

import arabic.model.ArabicSupportEnum
import fx.ui.util.UIUserPreferences
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback

class ArabicSupportEnumCellFactory[T <: ArabicSupportEnum](
  listType: ListType
)(implicit preferences: UIUserPreferences)
    extends Callback[ListView[T], ListCell[T]] {

  override def call(p: ListView[T]): ListCell[T] =
    ArabicSupportEnumListCell(listType)
}

object ArabicSupportEnumCellFactory {

  def apply[T <: ArabicSupportEnum](
    listType: ListType
  )(implicit preferences: UIUserPreferences
  ): ArabicSupportEnumCellFactory[T] =
    new ArabicSupportEnumCellFactory[T](listType)
}
