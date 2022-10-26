package com.alphasystem
package arabic
package morphologicalanalysis
package ui

import fx.ui.util.UIUserPreferences
import javafx.beans.property.BooleanProperty
import model.ArabicSupportEnum
import javafx.beans.value.ObservableValue
import javafx.scene.Group
import javafx.scene.control.cell.CheckBoxListCell
import javafx.util.{ Callback, StringConverter }
import scalafx.Includes.*
import scalafx.scene.control.{ CheckBox, ContentDisplay }
import scalafx.scene.layout.FlowPane
import scalafx.scene.text.{ Text, TextAlignment, TextFlow }

class ArabicSupportEnumCheckBoxListCell[T <: ArabicSupportEnum](
  listType: ListType,
  selectedStateCallback: Callback[T, ObservableValue[java.lang.Boolean]]
)(implicit preferences: UIUserPreferences)
    extends CheckBoxListCell[T](selectedStateCallback) {

  private var booleanProperty: ObservableValue[java.lang.Boolean] = _
  private val pane = new FlowPane() {
    hgap = 5
  }
  private val checkBox = new CheckBox()
  private val codeText: Text = Text("")
  private val arabicText: Text = new Text("")

  codeText.setFont(preferences.englishFont)
  arabicText.setFont(preferences.arabicFont)
  setContentDisplay(ContentDisplay.GraphicOnly)
  focusedProperty().onChange((_, _, nv) =>
    if nv then {
      val p = getParent
      if Option(p).isDefined then p.requestFocus()
    }
  )

  override def updateItem(item: T, empty: Boolean): Unit = {
    super.updateItem(item, empty)

    if Option(booleanProperty).isDefined then
      checkBox.selectedProperty().unbindBidirectional(booleanProperty.asInstanceOf[BooleanProperty])

    pane.children.clear()
    codeText.setText("")
    val textFlow = new TextFlow()
    textFlow.setTextAlignment(TextAlignment.Right)
    if item != null && !empty then {
      if item.code != "None" then codeText.setText(s"${item.code}")
      arabicText.setText(item.label)
      val children =
        listType match
          case ListType.LABEL_ONLY => Seq(arabicText)
          case ListType.CODE_ONLY  => Seq(codeText)
          case ListType.LABEL_AND_CODE =>
            Seq(
              codeText,
              new Text(s" "),
              arabicText
            )

      textFlow.children = children
      pane.getChildren.addAll(checkBox, new Group(textFlow))
      setGraphic(pane)

      booleanProperty = getSelectedStateCallback.call(item)
      if Option(booleanProperty).isDefined then
        checkBox.selectedProperty().bindBidirectional(booleanProperty.asInstanceOf[BooleanProperty])
    }
  }
}

object ArabicSupportEnumCheckBoxListCell {

  def apply[T <: ArabicSupportEnum](
    listType: ListType,
    callback: Callback[T, ObservableValue[java.lang.Boolean]]
  )(implicit preferences: UIUserPreferences
  ): ArabicSupportEnumCheckBoxListCell[T] = new ArabicSupportEnumCheckBoxListCell[T](listType, callback)
}
