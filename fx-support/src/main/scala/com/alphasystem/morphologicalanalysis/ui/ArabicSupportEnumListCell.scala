package com.alphasystem.morphologicalanalysis.ui

import com.alphasystem.arabic.model.ArabicSupportEnum
import com.alphasystem.fx.ui.util.UIUserPreferences
import com.alphasystem.morphologicalanalysis.ui.ListType.{
  CODE_ONLY,
  LABEL_AND_CODE,
  LABEL_ONLY
}
import javafx.scene.Group
import javafx.scene.control.{ ContentDisplay, ListCell }
import javafx.scene.text.{ Text, TextFlow }

import java.lang.Enum

class ArabicSupportEnumListCell[T <: Enum[T] & ArabicSupportEnum](
  listType: ListType
)(implicit preferences: UIUserPreferences)
    extends ListCell[T] {

  private val codeText: Text = Text("")
  private val arabicText: Text = new Text("")

  codeText.setFont(preferences.englishFont)
  arabicText.setFont(preferences.arabicFont)
  setContentDisplay(ContentDisplay.GRAPHIC_ONLY)

  override def updateItem(item: T, empty: Boolean): Unit = {
    super.updateItem(item, empty)

    val textFlow = new TextFlow()
    if item != null && !empty then {
      codeText.setText(s"(${item.code})")
      arabicText.setText(item.label.unicode)
      val children =
        listType match
          case LABEL_ONLY     => Seq(arabicText)
          case CODE_ONLY      => Seq(codeText)
          case LABEL_AND_CODE => Seq(codeText, arabicText)
      textFlow.getChildren.addAll(children*)
      setGraphic(new Group(textFlow))
    }

  }
}

object ArabicSupportEnumListCell {

  def apply[T <: Enum[T] & ArabicSupportEnum](
    listType: ListType
  )(implicit preferences: UIUserPreferences
  ): ArabicSupportEnumListCell[T] = new ArabicSupportEnumListCell[T](listType)
}
