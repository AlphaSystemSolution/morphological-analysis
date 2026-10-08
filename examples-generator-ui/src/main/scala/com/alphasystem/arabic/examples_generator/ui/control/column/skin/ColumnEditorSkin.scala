package com.alphasystem
package arabic
package examples_generator
package ui
package control
package column
package skin

import control.verse_selector.VerseSelectionDialog
import ui.model.{Color, ColumnType, HorizontalAlignment, VerseSearchResult, VerticalAlignment}
import arabic.fx.ui.util.createLabel
import javafx.scene.control.{ListView, SkinBase}
import javafx.scene.control.TextFormatter.Change
import javafx.util.Callback
import scalafx.collections.ObservableBuffer
import scalafx.Includes.*
import scalafx.application.Platform
import scalafx.geometry.NodeOrientation.{LeftToRight, RightToLeft}
import scalafx.geometry.{Insets, Orientation, Pos}
import scalafx.scene.control.{Button, ComboBox, RadioButton, Separator, TextArea, TextField, TextFormatter, ToggleGroup}
import scalafx.scene.layout.{BorderPane, GridPane, Pane}

import java.util.function.UnaryOperator

class ColumnEditorSkin private (control: ColumnEditorView) extends SkinBase[ColumnEditorView](control) {

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

  private val horizontalAlignmentComboBox = new ComboBox[HorizontalAlignment](HorizontalAlignment.values.toSeq)
  control.horizontalAlignmentProperty.bindBidirectional(horizontalAlignmentComboBox.valueProperty())

  private val verticalAlignmentComboBox = new ComboBox[VerticalAlignment](VerticalAlignment.values.toSeq)
  control.verticalAlignmentProperty.bindBidirectional(verticalAlignmentComboBox.valueProperty())

  private val tokenFieldFilter: UnaryOperator[Change] = (change: Change) => {
    val text = change.getControlNewText
    if text.matches("^$|^-?\\d+$") then change // Accept the change
    else null // Reject the change
  }

  private val tokenStartTextField = new TextField {
    textFormatter = new TextFormatter[String](tokenFieldFilter)
  }

  private val locationStartTextField = new TextField {
    textFormatter = new TextFormatter[String](tokenFieldFilter)
  }

  private val tokenEndTextField = new TextField {
    textFormatter = new TextFormatter[String](tokenFieldFilter)
  }

  private val locationEndTextField = new TextField {
    textFormatter = new TextFormatter[String](tokenFieldFilter)
  }

  private val addHighlightButton = new Button {
    text = "Add Highlight"
    disable = true
  }
  addHighlightButton.disableProperty().bind(control.textProperty.isEmpty)

  private val colorComboBox = new ComboBox[Color](Color.values.toSeq)
  colorComboBox.setCellFactory((_: ListView[Color]) => new ColorListCell())
  colorComboBox.setButtonCell(new ColorListCell)
  colorComboBox.getSelectionModel.selectFirst()

  private val gridPane = {
    val gridPane = new GridPane {
      styleClass = ObservableBuffer("border")
      vgap = 10
      hgap = 10
      alignment = Pos.Center
      padding = Insets(10, 10, 10, 10)
    }

    var row = 0

    gridPane.add(createLabel("Column Type:"), 0, row)
    gridPane.add(columnTypeComboBox, 1, row)

    row += 1
    gridPane.add(createLabel("Enter text manually"), 0, row)
    gridPane.add(rawTextRadioButton, 1, row)
    gridPane.add(createLabel("Search Quranic verse:"), 2, row)
    gridPane.add(selectVerseRadioButton, 3, row)

    row += 1
    gridPane.add(createEmptyPanel(), 0, row)
    gridPane.add(selectVerseButton, 1, row)

    row += 1
    gridPane.add(createLabel("Text:"), 0, row)
    gridPane.add(textArea, 1, row, 3, 1)

    row += 1
    gridPane.add(createLabel("Column Settings:"), 0, row)

    row += 1
    gridPane.add(Separator(Orientation.Horizontal), 0, row, 4, 1)

    row += 1
    gridPane.add(createLabel("Column span:"), 0, row)
    gridPane.add(colSpanTextField, 1, row)
    gridPane.add(createLabel("Row span:"), 2, row)
    gridPane.add(rowSpanTextField, 3, row)

    row += 1
    gridPane.add(createLabel("Horizontal Alignment:"), 0, row)
    gridPane.add(horizontalAlignmentComboBox, 1, row)
    gridPane.add(createLabel("Vertical Alignment:"), 2, row)
    gridPane.add(verticalAlignmentComboBox, 3, row)

    row += 1
    gridPane.add(Separator(Orientation.Horizontal), 0, row, 4, 1)

    row += 1
    gridPane.add(createLabel("Text Highlights:"), 0, row)

    row += 1
    gridPane.add(Separator(Orientation.Horizontal), 0, row, 4, 1)

    row += 1
    gridPane.add(createLabel("Token Start Index:"), 0, row)
    gridPane.add(tokenStartTextField, 1, row)
    gridPane.add(createLabel("Location Start Index:"), 2, row)
    gridPane.add(locationStartTextField, 3, row)

    row += 1
    gridPane.add(createLabel("Token End Index:"), 0, row)
    gridPane.add(tokenEndTextField, 1, row)
    gridPane.add(createLabel("Location End Index:"), 2, row)
    gridPane.add(locationEndTextField, 3, row)

    row += 1
    gridPane.add(createLabel("Highlight Color:"), 0, row)
    gridPane.add(colorComboBox, 1, row)

    row += 1
    gridPane.add(createEmptyPanel(), 0, row)
    gridPane.add(addHighlightButton, 1, row)

    row += 1
    gridPane.add(Separator(Orientation.Horizontal), 0, row, 4, 1)

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

object ColumnEditorSkin {
  private[column] def apply(control: ColumnEditorView): ColumnEditorSkin = new ColumnEditorSkin(control)
}
