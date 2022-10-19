package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control
package skin

import morphology.model.Location
import javafx.scene.control.{ ContentDisplay, ListCell }
import tokeneditor.*
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
