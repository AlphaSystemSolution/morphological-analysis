package com.alphasystem
package arabic
package examples_generator
package ui
package control
package column
package skin

import control.verse_selector.VerseSelectionDialog
import ui.model.{ ColumnType, VerseSearchResult }
import arabic.fx.ui.util.createLabel
import javafx.scene.control.SkinBase
import javafx.scene.control.TextFormatter.Change
import scalafx.collections.ObservableBuffer
import scalafx.Includes.*
import scalafx.application.Platform
import scalafx.geometry.NodeOrientation.{ LeftToRight, RightToLeft }
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.control.{ Button, ComboBox, RadioButton, TextArea, TextField, TextFormatter, ToggleGroup }
import scalafx.scene.layout.{ BorderPane, GridPane, Pane }

import java.util.function.UnaryOperator

class CreateColumnSkin private (control: CreateColumnView) extends SkinBase[CreateColumnView](control) {

  private val verseSelectionDialog = VerseSelectionDialog()
  private val columnTypeComboBox = new ComboBox[ColumnType](ColumnType.values.toSeq)
  control.columnTypeProperty.bindBidirectional(columnTypeComboBox.valueProperty())

  private val textArea = new TextArea {
    prefRowCount = 5
    prefColumnCount = 10
    editable = false
    wrapText = true
  }
  control.textProperty.bindBidirectional(textArea.textProperty())

  private val group = new ToggleGroup()

  private val rawTextRadioButton = new RadioButton {
    toggleGroup = group
    selected = true
  }
  rawTextRadioButton
    .selectedProperty()
    .onChange((_, _, nv) => {
      textArea.text = ""
      if nv then Platform.runLater(() => textArea.requestFocus())
    })

  private val selectVerseRadioButton = new RadioButton {
    toggleGroup = group
  }
  private val selectVerseButton = new Button {
    text = "Select Verse(s) ..."
    disable = true
    onAction = event => {
      verseSelectionDialog.showAndWait() match {
        case Some(Some(result: VerseSearchResult)) => control.verseSearchResult = Some(result)
        case _                                     => // do nothing
      }
      event.consume()
    }
  }
  selectVerseRadioButton
    .selectedProperty()
    .onChange((_, _, nv) => {
      selectVerseButton.disable = !nv
      textArea.text = ""
    })
  textArea.editableProperty().bind(rawTextRadioButton.selectedProperty())
  columnTypeComboBox
    .valueProperty()
    .onChange((_, _, nv) => {
      nv match {
        case ColumnType.Translation | ColumnType.Other =>
          textArea.nodeOrientation = LeftToRight
          textArea.font = preferences.englishFont
        case _ =>
          textArea.nodeOrientation = RightToLeft
          textArea.font = preferences.arabicFont
      }
    })

  private val spanFieldFilter: UnaryOperator[Change] = (change: Change) => {
    val text = change.getControlNewText
    if text.matches("^$|^[1-9]\\d*$") then change // Accept the change
    else null // Reject the change
  }

  private val colSpanTextField = new TextField {
    textFormatter = new TextFormatter[String](spanFieldFilter)
  }
  control.colSpanProperty.bindBidirectional(colSpanTextField.textProperty())

  private val rowSpanTextField = new TextField {
    textFormatter = new TextFormatter[String](spanFieldFilter)
  }
  control.rowSpanProperty.bindBidirectional(rowSpanTextField.textProperty())

  private val gridPane = {
    val gridPane = new GridPane {
      styleClass = ObservableBuffer("border")
      vgap = 10
      hgap = 10
      alignment = Pos.Center
      padding = Insets(10, 10, 10, 10)
    }

    gridPane.add(createLabel("Column Type:"), 0, 0)
    gridPane.add(columnTypeComboBox, 1, 0)

    gridPane.add(createLabel("Enter text manually"), 0, 1)
    gridPane.add(rawTextRadioButton, 1, 1)
    gridPane.add(createLabel("Search Quranic verse:"), 2, 1)
    gridPane.add(selectVerseRadioButton, 3, 1)

    gridPane.add(createEmptyPanel(), 0, 3)
    gridPane.add(selectVerseButton, 1, 3)

    gridPane.add(createLabel("Text:"), 0, 4)
    gridPane.add(textArea, 1, 4, 3, 1)

    gridPane.add(createLabel("Column span:"), 0, 5)
    gridPane.add(colSpanTextField, 1, 5)
    gridPane.add(createLabel("Row span:"), 2, 5)
    gridPane.add(rowSpanTextField, 3, 5)

    gridPane
  }

  private val mainPane =
    new BorderPane {
      center = gridPane
      BorderPane.setAlignment(gridPane, Pos.Center)
    }

  getChildren.addAll(mainPane)

  private def createEmptyPanel() =
    new Pane() {
      prefWidth = 100
    }
}

object CreateColumnSkin {
  private[column] def apply(control: CreateColumnView): CreateColumnSkin = new CreateColumnSkin(control)
}
