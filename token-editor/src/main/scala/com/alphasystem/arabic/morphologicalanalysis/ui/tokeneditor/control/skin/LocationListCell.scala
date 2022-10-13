package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Location
import javafx.scene.control.{ ContentDisplay, ListCell }
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.*
import scalafx.Includes.*
import scalafx.scene.text.Text
import scalafx.scene.text.{ Font, FontPosture, FontWeight }

class LocationListCell extends ListCell[Location] {

  private val label: Text = new Text("")

  setContentDisplay(ContentDisplay.GRAPHIC_ONLY)
  label.setFont(
    Font(
      tokenEditorPreferences.arabicFontName,
      FontWeight.Normal,
      FontPosture.Regular,
      14.0
    )
  )

  override def updateItem(item: Location, empty: Boolean): Unit = {
    super.updateItem(item, empty)

    label.setText("")
    if Option(item).isDefined && !empty then label.setText(item.displayName)

    setGraphic(label)
  }

}
