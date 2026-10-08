package com.alphasystem
package arabic
package examples_generator
package ui
package control
package column
package skin

import control.verse_selector.VerseSelectionDialog
import ui.model.{
  Color,
  ColumnType,
  HighlightRange,
  HorizontalAlignment,
  TokenHighLight,
  VerseSearchResult,
  VerticalAlignment
}
import arabic.fx.ui.util.{ createEmptyPanel, createLabel }
import com.alphasystem.arabic.examples_generator.ui.model.Color.Default
import javafx.scene.control.{ ListView, SkinBase }
import javafx.scene.control.TextFormatter.Change
import javafx.util.Callback
import scalafx.collections.ObservableBuffer
import scalafx.Includes.*
import scalafx.application.{ JFXApp3, Platform }
import scalafx.beans.binding.{ Bindings, BooleanBinding }
import scalafx.geometry.NodeOrientation.{ LeftToRight, RightToLeft }
import scalafx.geometry.{ Insets, Orientation, Pos }
import scalafx.scene.control.Alert.AlertType.Warning
import scalafx.scene.control.SelectionMode.Single
import scalafx.scene.control.{
  Alert,
  Button,
  ComboBox,
  RadioButton,
  Separator,
  TextArea,
  TextField,
  TextFormatter,
  ToggleGroup
}
import scalafx.scene.layout.{ BorderPane, GridPane, Pane }

import java.util.UUID
import java.util.function.UnaryOperator
import scala.util.Try

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
      control.verseSearchResult.foreach(verseSelectionDialog.setDialogContent)
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

  private val positiveIntegerFieldFilter: UnaryOperator[Change] = (change: Change) => {
    val text = change.getControlNewText
    if text.matches("^$|^[1-9]\\d*$") then change // Accept the change
    else null // Reject the change
  }

  private val colSpanTextField = createFormattedTextField(positiveIntegerFieldFilter)
  control.colSpanProperty.bindBidirectional(colSpanTextField.textProperty())

  private val rowSpanTextField = createFormattedTextField(positiveIntegerFieldFilter)
  control.rowSpanProperty.bindBidirectional(rowSpanTextField.textProperty())

  private val horizontalAlignmentComboBox = new ComboBox[HorizontalAlignment](HorizontalAlignment.values.toSeq)
  control.horizontalAlignmentProperty.bindBidirectional(horizontalAlignmentComboBox.valueProperty())

  private val verticalAlignmentComboBox = new ComboBox[VerticalAlignment](VerticalAlignment.values.toSeq)
  control.verticalAlignmentProperty.bindBidirectional(verticalAlignmentComboBox.valueProperty())

  private val integerFieldFilter: UnaryOperator[Change] = (change: Change) => {
    val text = change.getControlNewText
    if text.matches("^$|^-?\\d+$") then change // Accept the change
    else null // Reject the change
  }

  private val tokenStartTextField = createFormattedTextField(positiveIntegerFieldFilter)
  private val locationStartTextField = createFormattedTextField(positiveIntegerFieldFilter)
  private val tokenEndTextField = createFormattedTextField(integerFieldFilter)
  private val locationEndTextField = createFormattedTextField(integerFieldFilter)

  private val saveHighlightButton = new Button {
    text = "Save Highlight"
    disable = true
    onAction = event => {
      saveHighlight()
      event.consume()
    }
  }
  saveHighlightButton
    .disableProperty()
    .bind(
      control.textProperty.isEmpty.or(textFieldBinding(tokenStartTextField)).or(textFieldBinding(tokenEndTextField))
    )

  private val colorComboBox = new ComboBox[Color](Color.values.toSeq)
  colorComboBox.setCellFactory((_: ListView[Color]) => new ColorListCell())
  colorComboBox.setButtonCell(new ColorListCell)
  colorComboBox.getSelectionModel.selectFirst()

  private var highlightId = UUID.randomUUID()
  private val highlightsList = new ListView[HighlightRange](control.highlightsProperty)
  highlightsList.setCellFactory((_: ListView[HighlightRange]) => new HighlightsListCell())
  highlightsList.getSelectionModel.selectionMode = Single
  highlightsList.setMaxHeight(5 * 24 + 2)
  highlightsList
    .getSelectionModel
    .selectedItemProperty()
    .onChange((_, _, nv) => {
      if Option(nv).isDefined then {
        highlightId = nv.id
        tokenStartTextField.text = nv.tokenStart.index.toString
        tokenEndTextField.text = nv.tokenEnd.index.toString
        locationStartTextField.text = nv.tokenStart.locationIndex.map(_.toString).getOrElse("")
        locationEndTextField.text = nv.tokenEnd.locationIndex.map(_.toString).getOrElse("")
        colorComboBox.value = nv.color.getOrElse(Default)
      }
    })

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
    gridPane.add(createEmptyPanel(100), 0, row)
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
    gridPane.add(createEmptyPanel(100), 0, row)
    gridPane.add(saveHighlightButton, 1, row)

    row += 1
    gridPane.add(createLabel("Highlights:"), 0, row)
    gridPane.add(highlightsList, 1, row, 4, 1)

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

  private def createFormattedTextField(filter: UnaryOperator[Change]) =
    new TextField {
      textFormatter = new TextFormatter[String](filter)
    }

  private def textFieldBinding(textField: TextField) =
    Bindings.createBooleanBinding(
      () => {
        val value = textField.text.value
        Option(value).isEmpty || value.isBlank
      },
      textField.textProperty()
    )

  private def saveHighlight(): Unit = {
    val tokenStartIndex = Try(tokenStartTextField.text.value.toInt).getOrElse(0)
    val tokenEndIndex = Try(tokenEndTextField.text.value.toInt).getOrElse(-1)

    if tokenStartIndex <= 0 then {
      new Alert(Warning) {
        initOwner(JFXApp3.Stage)
        title = "Error!"
        headerText = "Invalid token start index"
        contentText = "Token start index must be a positive integer."
      }.showAndWait()
      return
    }

    if tokenEndIndex > -1 && tokenStartIndex > tokenEndIndex then {
      new Alert(Warning) {
        initOwner(JFXApp3.Stage)
        title = "Error!"
        headerText = "Invalid token start index"
        contentText = "Token start index must be equal or less than token end index."
      }.showAndWait()
      return
    }

    val locationStartIndex = Try(locationStartTextField.text.value.toInt).getOrElse(0)
    val locationEndIndex = Try(locationEndTextField.text.value.toInt).getOrElse(-1)

    val tokenStart = TokenHighLight(
      index = tokenStartIndex,
      locationIndex = if locationStartIndex == 0 then None else Some(locationStartIndex)
    )
    val tokenEnd = TokenHighLight(
      index = tokenEndIndex,
      locationIndex = if locationStartIndex == 0 then None else Some(locationEndIndex)
    )
    val color = colorComboBox.value.value

    val highlightRange = HighlightRange(
      id = highlightId,
      tokenStart = tokenStart,
      tokenEnd = tokenEnd,
      color = if Color.Default == color then None else Some(color)
    )
    val index = control.highlightsProperty.indexWhere(_.id == highlightId, 0)
    if index == -1 then {
      println("New item")
      control.highlightsProperty.add(highlightRange)
    } else {
      // update existing
      control.highlightsProperty.update(index, highlightRange)
    }

    highlightId = UUID.randomUUID()
    tokenStartTextField.text = ""
    tokenEndTextField.text = ""
    locationStartTextField.text = ""
    locationEndTextField.text = ""
    colorComboBox.getSelectionModel.selectFirst()
  }
}

object ColumnEditorSkin {
  private[column] def apply(control: ColumnEditorView): ColumnEditorSkin = new ColumnEditorSkin(control)
}
