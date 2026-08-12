package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package table

import arabic.morphologicalanalysis.ui.RootLettersKeyBoardView
import morphologicalengine.conjugation.model.RootLetters
import javafx.beans.value.WritableValue
import javafx.scene.control.TableCell
import scalafx.Includes.*
import scalafx.geometry.{ NodeOrientation, Pos }
import scalafx.scene.Group
import scalafx.scene.control.{ ContentDisplay, TableCell as STableCell, TableColumn }
import scalafx.scene.text.TextFlow
import scalafx.stage.Popup

class RootLettersTableCell extends TableCell[TableModel, RootLetters] {

  private val keyBoard = new RootLettersKeyBoardView() {
    font = preferences.arabicFont
    selectedLabelWidth = 48
    selectedLabelHeight = 48
  }

  private val keyboardPopup = new Popup() {
    autoHide = true
    onHiding = event => {
      commitSelection()
      event.consume()
    }
    onAutoHide = event => {
      commitSelection()
      event.consume()
    }
    content.addOne(keyBoard)
  }

  // `commitEdit` is a no-op if the TableView has already taken this cell out of its
  // "editing" state by the time the popup fires its hide/auto-hide callback. To make sure
  // the user's selection is never silently dropped, write it back to the underlying model
  // property directly, independent of the cell's editing state.
  private def commitSelection(): Unit = {
    val newValue = keyBoard.rootLetters
    commitEdit(newValue)
    Option(getTableColumn).foreach { column =>
      column.getCellObservableValue(getIndex) match {
        case writable: WritableValue[?] => writable.asInstanceOf[WritableValue[RootLetters]].setValue(newValue)
        case _                          => ()
      }
    }
  }

  setContentDisplay(ContentDisplay.GraphicOnly)
  setAlignment(Pos.Center)
  setNodeOrientation(NodeOrientation.RightToLeft)

  override def startEdit(): Unit = {
    super.startEdit()
    val bounds = localToScreen(getBoundsInLocal)
    keyboardPopup.show(this, bounds.getMinX, bounds.getMinY + bounds.getHeight)
  }

  override def updateItem(item: RootLetters, empty: Boolean): Unit = {
    super.updateItem(item, empty)

    val label =
      if Option(item).isDefined && !empty then {
        keyBoard.rootLetters = item

        val fourthRadicalLabels =
          item.fourthRadical match
            case Some(value) => Seq(createSpaceLabel(), createLabel(value))
            case None        => Seq(createSpaceLabel())

        val allLabels = Seq(
          createLabel(item.firstRadical),
          createSpaceLabel(),
          createLabel(item.secondRadical),
          createSpaceLabel(),
          createLabel(item.thirdRadical)
        ) ++ fourthRadicalLabels

        val textFlow = new TextFlow() {
          nodeOrientation = NodeOrientation.RightToLeft
          children = allLabels
        }
        new Group(textFlow)
      } else null

    setGraphic(label)
  }
}

object RootLettersTableColumn {
  def apply(columnWidth: Double): TableColumn[TableModel, RootLetters] = new TableColumn[TableModel, RootLetters]() {
    text = "Root Letters"
    prefWidth = columnWidth
    editable = true
    cellValueFactory = _.value.rootLettersProperty
    cellFactory = (_: TableColumn[TableModel, RootLetters]) =>
      new STableCell[TableModel, RootLetters](new RootLettersTableCell())
  }
}
