package com.alphasystem
package arabic
package morphologicalanalysis
package ui

import com.alphasystem.arabic.fx.ui.util.UIUserPreferences
import com.alphasystem.arabic.model.ArabicSupportEnum
import javafx.scene.control.cell.ComboBoxTableCell
import scalafx.Includes.*
import scalafx.geometry.{ NodeOrientation, Pos }
import scalafx.scene.Group
import scalafx.scene.control.ContentDisplay
import scalafx.scene.text.{ Text, TextAlignment, TextFlow }

class ArabicSupportEnumComboBoxTableCell[M, T <: ArabicSupportEnum](
  listType: ListType,
  values: Array[T]
)(using preferences: UIUserPreferences)
    extends ComboBoxTableCell[M, T] {

  private val arabicText = new Text("") {
    font = preferences.arabicFont
    nodeOrientation = NodeOrientation.RightToLeft
    textAlignment = TextAlignment.Center
  }
  private val codeText = new Text("") {
    font = preferences.englishFont
    textAlignment = TextAlignment.Center
  }
  private val comboBox = ArabicSupportEnumComboBox[T](values, listType)
  comboBox.setVisibleRowCount(15)
  comboBox.getSelectionModel.selectedItemProperty().onChange((_, _, nv) => commitEdit(nv))

  setEditable(true)
  setContentDisplay(ContentDisplay.GraphicOnly)
  setNodeOrientation(NodeOrientation.RightToLeft)
  setAlignment(Pos.Center)

  override def startEdit(): Unit = {
    if isEditable && getTableView.isEditable && getTableColumn.isEditable then {
      comboBox.getSelectionModel.select(getItem)
      super.startEdit()
      setText(null)
      setGraphic(comboBox)
    }
  }

  override def updateItem(item: T, empty: Boolean): Unit = {
    super.updateItem(item, empty)

    arabicText.text = ""
    codeText.text = ""
    val graphic =
      if Option(item).isDefined && !empty then {
        arabicText.text = item.label
        codeText.text = s"(${item.code})"
        val textFlow = new TextFlow() {
          nodeOrientation = NodeOrientation.RightToLeft
          children = Seq(arabicText, new Text(" "), codeText)
        }
        new Group(textFlow)
      } else null

    setGraphic(graphic)
  }
}

object ArabicSupportEnumComboBoxTableCell {
  def apply[M, T <: ArabicSupportEnum](
    listType: ListType,
    values: Array[T]
  )(using preferences: UIUserPreferences
  ): ArabicSupportEnumComboBoxTableCell[M, T] = new ArabicSupportEnumComboBoxTableCell[M, T](listType, values)
}
