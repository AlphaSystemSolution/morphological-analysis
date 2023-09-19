package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package table

import morphologicalengine.conjugation.model.{ ConjugationConfiguration, ConjugationInput, RootLetters }
import morphologicalengine.generator.model.ConjugationTemplate
import scalafx.Includes.*
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control.{ SelectionMode, TableView }
import scalafx.scene.layout.{ Background, BackgroundFill, CornerRadii }
import scalafx.scene.paint.Color
import scalafx.stage.Screen

import java.util.UUID

class MorphologicalChartTableView(control: MorphologicalChartView) extends TableView[TableModel] {
  import MorphologicalChartTableView.*

  private val conjugationTemplate = control.conjugationTemplate
  private val extraLargeColumnWidth = (BoundsWidth * 20) / 150
  private val largeColumnWidth = (BoundsWidth * 20) / 100
  private val mediumColumnWidth = (BoundsWidth * 8) / 100
  private val smallColumnWidth = (BoundsWidth * 4) / 100
  private val tableData = ObservableBuffer.from[TableModel](conjugationTemplate.inputs.map(TableModel(_)))

  background = TableBackground
  selectionModel.value.selectionMode.value = SelectionMode.Single
  editable = true
  columns ++= List(
    CheckedTableColumn(smallColumnWidth),
    NamedTemplateTableColumn(largeColumnWidth),
    RootLettersTableColumn(largeColumnWidth),
    TranslationTableColumn(mediumColumnWidth),
    VerbalNounTableColumn(extraLargeColumnWidth),
    RemovePassiveLineColumn(smallColumnWidth),
    SkipRuleProcessingColumn(smallColumnWidth),
    InfoTableColumn(smallColumnWidth, control)
  ) // end of columns

  fixedCellSize = RowSize
  prefWidth = BoundsWidth * 0.99
  prefHeight = calculateTableHeight(tableData.size)
  items = tableData

  def updateView(conjugationTemplate: ConjugationTemplate): Unit = {
    Platform.runLater(() -> {
      tableData.clear()
      tableData.addAll(conjugationTemplate.inputs.map(TableModel(_)))
      prefHeight = calculateTableHeight(tableData.size)
      doFocus(first = true)
    })
  }

  def addRow(): Unit = {
    Platform.runLater(() -> {
      tableData.addOne(TableModel())
      prefHeight = calculateTableHeight(tableData.size)
      doFocus()
    })
  }

  def duplicateRows(): Unit = duplicateRows(items.value.filter(_.checked))

  def duplicateRow(input: ConjugationInput): Unit =
    duplicateRows(ObservableBuffer.from[TableModel](Seq(TableModel(input))))

  private def duplicateRows(values: ObservableBuffer[TableModel]): Unit = {
    Platform.runLater(() => {
      tableData.addAll(values.map(_.copy))
      prefHeight = calculateTableHeight(tableData.size)
      values.foreach(_.checked = false)
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

  def getData: ConjugationTemplate = {
    val inputs =
      items.value.toList.map { tm =>
        ConjugationInput(
          namedTemplate = tm.template,
          conjugationConfiguration = ConjugationConfiguration(
            skipRuleProcessing = tm.skipRuleProcessing,
            removePassiveLine = tm.removePassiveLine
          ),
          rootLetters = tm.rootLetters,
          translation = Option(tm.translation),
          verbalNounCodes = Seq.empty
        )
      }

    ConjugationTemplate(
      id = UUID.randomUUID().toString,
      chartConfiguration = conjugationTemplate.chartConfiguration,
      inputs = inputs
    )
  }

  private def doFocus(first: Boolean = false): Unit = {
    requestFocus()
    val lastIndex = tableData.size - 1
    val index = if first then 0 else lastIndex
    delegate.getSelectionModel.select(index)
    focusModel.value.focus(index)
  }
}

object MorphologicalChartTableView {
  private lazy val VisualBounds = Screen.primary.visualBounds
  private lazy val BoundsWidth = VisualBounds.width
  private lazy val BoundsHeight = VisualBounds.height
  private val TableBackground = new Background(Array(new BackgroundFill(Color.White, CornerRadii.Empty, Insets.Empty)))
  private val RowSize = 40.0
  private lazy val DefaultMinSize = BoundsHeight * 0.80

  def apply(control: MorphologicalChartView): MorphologicalChartTableView =
    new MorphologicalChartTableView(control)

  private def calculateTableHeight(numOfRows: Int) = {
    val height = roundTo100((numOfRows * RowSize) * RowSize)
    math.max(height, DefaultMinSize) + 100
  }
}
