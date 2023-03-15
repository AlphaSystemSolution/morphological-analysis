package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package table

import morphologicalengine.conjugation.model.ConjugationInput
import de.jensd.fx.glyphs.fontawesome.{ FontAwesomeIcon, FontAwesomeIconView }
import javafx.scene.control.TableCell
import scalafx.Includes.*
import scalafx.geometry.Pos
import scalafx.scene.control.{ Button, ContentDisplay, TableColumn, Tooltip, TableCell as STableCell }
import scalafx.scene.layout.FlowPane

class InfoTableCell(control: MorphologicalChartView) extends TableCell[TableModel, ConjugationInput] {

  setContentDisplay(ContentDisplay.GraphicOnly)
  setAlignment(Pos.Center)

  override def updateItem(item: ConjugationInput, empty: Boolean): Unit = {
    super.updateItem(item, empty)

    val flowPane =
      if Option(item).isDefined && !empty then {
        val duplicateRowButton =
          new Button() {
            graphic = new FontAwesomeIconView(FontAwesomeIcon.CLONE, "1em")
            contentDisplay = ContentDisplay.GraphicOnly
            tooltip = Tooltip("Duplicate row")
            onAction = event => {
              control.duplicateRow = null
              control.duplicateRow = item
              event.consume()
            }
          }

        val dictionaryButton =
          new Button() {
            graphic = new FontAwesomeIconView(FontAwesomeIcon.INFO, "1em")
            contentDisplay = ContentDisplay.GraphicOnly
            tooltip = Tooltip("View Dictionary")
            onAction = event => {
              control.viewDictionary = item.rootLetters
              event.consume()
            }
          }

        new FlowPane() {
          hgap = 3
          children.addAll(duplicateRowButton, dictionaryButton)
        }
      } else null

    setGraphic(flowPane)
  }
}

object InfoTableColumn {
  def apply(
    columnWidth: Double,
    control: MorphologicalChartView
  ): TableColumn[TableModel, ConjugationInput] =
    new TableColumn[TableModel, ConjugationInput]() {
      prefWidth = columnWidth
      editable = false
      cellValueFactory = _.value.conjugationInputProperty
      cellFactory = (_: TableColumn[TableModel, ConjugationInput]) =>
        new STableCell[TableModel, ConjugationInput](new InfoTableCell(control))
    }
}
