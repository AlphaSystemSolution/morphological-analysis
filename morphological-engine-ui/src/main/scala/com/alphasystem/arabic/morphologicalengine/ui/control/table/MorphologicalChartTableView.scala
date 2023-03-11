package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package table

import morphologicalengine.conjugation.model.RootLetters
import morphologicalengine.generator.model.ConjugationTemplate
import scalafx.Includes.*
import scalafx.application.Platform
import scalafx.beans.value.ObservableValue
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control.cell.{ CheckBoxTableCell, TextFieldTableCell }
import scalafx.scene.control.{ SelectionMode, TableCell, TableColumn, TableView }
import scalafx.scene.layout.{ Background, BackgroundFill, CornerRadii }
import scalafx.scene.paint.Color
import scalafx.stage.Screen

class MorphologicalChartTableView(conjugationTemplate: ConjugationTemplate) extends TableView[TableModel] {
  import MorphologicalChartTableView.*

  private val boundsWidth = Screen.primary.visualBounds.width
  private val largeColumnWidth = (boundsWidth * 20) / 100
  private val mediumColumnWidth = (boundsWidth * 8) / 100
  private val smallColumnWidth = (boundsWidth * 4) / 100
  private val tableData = ObservableBuffer.from[TableModel](conjugationTemplate.inputs.map(TableModel(_)))

  background = TableBackground
  selectionModel.value.selectionMode.value = SelectionMode.Single
  editable = true
  columns ++= List(
    new TableColumn[TableModel, java.lang.Boolean]() {
      prefWidth = smallColumnWidth
      editable = true
      cellValueFactory = _.value.checkedProperty.asInstanceOf[ObservableValue[java.lang.Boolean, java.lang.Boolean]]
      cellFactory = CheckBoxTableCell.forTableColumn(this)
    },
    NamedTemplateTableColumn(largeColumnWidth),
    new TableColumn[TableModel, RootLetters]() {
      text = "Root Letters"
      prefWidth = largeColumnWidth
      editable = true
      cellValueFactory = _.value.rootLettersProperty
      cellFactory =
        (_: TableColumn[TableModel, RootLetters]) => new TableCell[TableModel, RootLetters](RootLettersTableCell())
    },
    new TableColumn[TableModel, String]() {
      text = "Translation"
      prefWidth = mediumColumnWidth
      editable = true
      cellValueFactory = _.value.translationProperty
      cellFactory = TextFieldTableCell.forTableColumn()
      onEditCommit = event => {
        event.getTableView.getSelectionModel.getSelectedItem.translation = event.getNewValue
        event.consume()
      }
    },
    new TableColumn[TableModel, java.lang.Boolean]() {
      text = s"Remove${System.lineSeparator()}Passive${System.lineSeparator()}Line"
      prefWidth = smallColumnWidth
      editable = true
      cellValueFactory =
        _.value.removePassiveLineProperty.asInstanceOf[ObservableValue[java.lang.Boolean, java.lang.Boolean]]
      cellFactory = CheckBoxTableCell.forTableColumn(this)
    },
    new TableColumn[TableModel, java.lang.Boolean]() {
      text = s"Skip${System.lineSeparator()}Rule${System.lineSeparator()}Processing"
      prefWidth = smallColumnWidth
      editable = true
      cellValueFactory =
        _.value.skipRuleProcessingProperty.asInstanceOf[ObservableValue[java.lang.Boolean, java.lang.Boolean]]
      cellFactory = CheckBoxTableCell.forTableColumn(this)
    }
  ) // end of columns

  fixedCellSize = RowSize
  prefWidth = boundsWidth
  prefHeight = calculateTableHeight(tableData.size)
  items = tableData

  def addRow(): Unit = {
    Platform.runLater(() -> {
      tableData.addOne(TableModel())
      prefHeight = calculateTableHeight(tableData.size)
      doFocus()
    })
  }

  def duplicateRows(): Unit = {
    Platform.runLater(() => {
      val selectedValues = items.value.filter(_.checked)
      tableData.addAll(selectedValues.map(_.copy))
      prefHeight = calculateTableHeight(tableData.size)
      selectedValues.foreach(_.checked = false)
      doFocus()
    })
  }

  def removeRows(): Unit = {
    Platform.runLater(() => {
      val selectedValues = items.value.filter(_.checked)
      tableData.removeAll(selectedValues)
      prefHeight = calculateTableHeight(tableData.size)
      doFocus()
    })
  }

  private def doFocus(): Unit = {
    requestFocus()
    delegate.getSelectionModel.select(tableData.size - 1)
    focusModel.value.focus(tableData.size - 1)
  }
}

object MorphologicalChartTableView {
  private val TableBackground = new Background(Array(new BackgroundFill(Color.White, CornerRadii.Empty, Insets.Empty)))
  private val RowSize = 40.0
  private val DefaultMinSize = 500.0

  def apply(conjugationTemplate: ConjugationTemplate): MorphologicalChartTableView =
    new MorphologicalChartTableView(conjugationTemplate)

  private def calculateTableHeight(numOfRows: Int) = {
    val height = roundTo100((numOfRows * RowSize) * RowSize)
    math.max(height, DefaultMinSize) + 100
  }
}
