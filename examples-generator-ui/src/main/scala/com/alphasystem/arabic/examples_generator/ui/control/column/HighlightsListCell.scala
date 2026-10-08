package com.alphasystem
package arabic
package examples_generator
package ui
package control
package column

import ui.model.HighlightRange
import de.jensd.fx.glyphs.materialdesignicons.{ MaterialDesignIcon, MaterialDesignIconView }
import javafx.scene.control.ListCell
import scalafx.Includes.*
import scalafx.scene.control.{ Button, ContentDisplay, Label }
import scalafx.scene.layout.{ HBox, Pane, Priority }

class HighlightsListCell extends ListCell[HighlightRange] {

  private val hBox = new HBox()
  private val label = new Label("")
  private val pane = new Pane()
  private val button = new Button {
    graphic = new MaterialDesignIconView(MaterialDesignIcon.DELETE)
    contentDisplay = ContentDisplay.GraphicOnly
    tooltip = "Delete highlight"
    onAction = event => {
      getListView.getItems.remove(getItem)
      event.consume()
    }
  }
  hBox.getChildren.addAll(label, pane, button)
  HBox.setHgrow(pane, Priority.Always)

  override def updateItem(item: HighlightRange, empty: Boolean): Unit = {
    super.updateItem(item, empty)
    setText(null)
    setGraphic(null)

    if !empty && Option(item).isDefined then {
      label.setText(item.stringValue)
      setGraphic(hBox)
    }
  }
}
