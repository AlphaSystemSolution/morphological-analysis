package com.alphasystem
package arabic
package examples_generator
package ui
package control
package column

import ui.model.Color
import javafx.scene.control.ListCell
import scalafx.scene.shape.Rectangle

class ColorListCell extends ListCell[Color] {

  private val rect = Rectangle(20, 12)

  override def updateItem(item: Color, empty: Boolean): Unit = {
    super.updateItem(item, empty)
    setText(null)
    setGraphic(null)

    if !empty && Option(item).isDefined then {
      setText(item.name())
      if Color.Default == item then {
        setText("None")
        setGraphic(null)
      } else {
        rect.fill = scalafx.scene.paint.Color.web(item.value)
        setGraphic(rect)
      }
    }
  }
}
