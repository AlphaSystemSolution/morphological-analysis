package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package table

import arabic.morphologicalanalysis.ui.RootLettersKeyBoardView
import morphologicalengine.conjugation.model.RootLetters
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
      commitEdit(keyBoard.rootLetters)
      event.consume()
    }
    onAutoHide = event => {
      commitEdit(keyBoard.rootLetters)
      event.consume()
    }
    content.addOne(keyBoard)
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

object RootLettersTableCell {
  def apply(columnWidth: Double): TableColumn[TableModel, RootLetters] = new TableColumn[TableModel, RootLetters]() {
    text = "Root Letters"
    prefWidth = columnWidth
    editable = true
    cellValueFactory = _.value.rootLettersProperty
    cellFactory = (_: TableColumn[TableModel, RootLetters]) =>
      new STableCell[TableModel, RootLetters](new RootLettersTableCell())
  }
}
