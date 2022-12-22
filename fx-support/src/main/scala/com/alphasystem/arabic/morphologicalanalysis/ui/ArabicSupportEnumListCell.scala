package com.alphasystem
package arabic
package morphologicalanalysis
package ui

import fx.ui.util.UIUserPreferences
import model.{ ArabicLetterType, ArabicSupportEnum }
import morphologicalanalysis.ui.ListType.{ CODE_ONLY, LABEL_AND_CODE, LABEL_ONLY }
import scalafx.Includes.*
import scalafx.scene.Group
import javafx.scene.control.{ ContentDisplay, ListCell }
import scalafx.geometry.NodeOrientation
import scalafx.scene.text.{ Text, TextAlignment, TextFlow }

class ArabicSupportEnumListCell[T <: ArabicSupportEnum](
  listType: ListType
)(implicit preferences: UIUserPreferences)
    extends ListCell[T] {

  private val codeText: Text = new Text("") {
    font = preferences.englishFont
  }
  private val arabicText: Text = new Text("") {
    font = preferences.arabicFont
  }
  private val textFlow = new TextFlow() {
    textAlignment = TextAlignment.Center
    nodeOrientation = NodeOrientation.RightToLeft
  }

  setContentDisplay(ContentDisplay.GRAPHIC_ONLY)

  override def updateItem(item: T, empty: Boolean): Unit = {
    super.updateItem(item, empty)

    codeText.text = ""
    arabicText.text = ""
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
              new Text(" "),
              arabicText
            )

      textFlow.children.clear()
      textFlow.children = children
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
