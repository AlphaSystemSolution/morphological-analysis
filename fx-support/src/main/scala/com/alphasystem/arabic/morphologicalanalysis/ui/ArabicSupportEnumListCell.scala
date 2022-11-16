package com.alphasystem
package arabic
package morphologicalanalysis
package ui

import fx.ui.util.UIUserPreferences
import model.{ ArabicLetterType, ArabicSupportEnum }
import morphologicalanalysis.ui.ListType.{ CODE_ONLY, LABEL_AND_CODE, LABEL_ONLY }
import javafx.scene.Group
import javafx.scene.control.{ ContentDisplay, ListCell }
import javafx.scene.text.{ Text, TextAlignment, TextFlow }

class ArabicSupportEnumListCell[T <: ArabicSupportEnum](
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

    codeText.setText("")
    arabicText.setText("")
    val textFlow = new TextFlow()
    textFlow.setTextAlignment(TextAlignment.CENTER)
    if item != null && !empty then {
      if item.code != "None" then codeText.setText(s"${item.code}")
      arabicText.setText(item.label)
      val children =
        listType match
          case LABEL_ONLY => Seq(arabicText)
          case CODE_ONLY  => Seq(codeText)
          case LABEL_AND_CODE =>
            Seq(
              codeText,
              new Text(s" "),
              arabicText
            )
      textFlow.getChildren.addAll(children*)
      setGraphic(new Group(textFlow))
    }

  }
}

object ArabicSupportEnumListCell {

  def apply[T <: ArabicSupportEnum](
    listType: ListType
  )(implicit preferences: UIUserPreferences
  ): ArabicSupportEnumListCell[T] = new ArabicSupportEnumListCell[T](listType)
}
