package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import javafx.scene.control.SkinBase
import org.controlsfx.control.{ PopOver, ToggleSwitch }
import org.controlsfx.dialog.FontSelectorDialog
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control.{ Button, ColorPicker, ContentDisplay, Label, Spinner, Tooltip }
import scalafx.scene.layout.{ BorderPane, GridPane }
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

import scala.jdk.OptionConverters.*

class GraphSettingsSkin(control: GraphSettingsView) extends SkinBase[GraphSettingsView](control) {

  private var rowIndex = -1
  private var terminalFontSelectorDialog = new FontSelectorDialog(control.terminalFont.toFont)
  private var partOfSpeechFontSelectorDialog = new FontSelectorDialog(control.partOfSpeechFont.toFont)
  private var translationFontSelectorDialog = new FontSelectorDialog(control.translationFont.toFont)
  private val colorPicker = new ColorPicker(control.backgroundColor)
  private val popOver = new PopOver()
  popOver.setContentNode(colorPicker)

  getChildren.addAll(initializeSkin)

  private def initializeSkin: BorderPane = {
    val gridPane = new GridPane() {
      vgap = DefaultGap
      hgap = DefaultGap
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
    }

    control.terminalFontProperty.onChange((_, _, nv) => terminalFontSelectorDialog = new FontSelectorDialog(nv.toFont))
    control
      .partOfSpeechFontProperty
      .onChange((_, _, nv) => partOfSpeechFontSelectorDialog = new FontSelectorDialog(nv.toFont))
    control
      .translationFontProperty
      .onChange((_, _, nv) => translationFontSelectorDialog = new FontSelectorDialog(nv.toFont))
    control.backgroundColorProperty.onChange((_, _, nv) => colorPicker.setValue(nv))
    colorPicker.onAction = event => {
      control.backgroundColor = colorPicker.getValue
      event.consume()
    }

    initGraphWidthField(gridPane)
    initGraphHeightField(gridPane)
    initTokenWidthField(gridPane)
    initTokenHeightField(gridPane)
    initGapBetweenTokensField(gridPane)
    initTerminalFontField(gridPane)
    initPartOfSpeechFontField(gridPane)
    initTranslationFontField(gridPane)
    initBackgroundColorField(gridPane)
    initShowGridLinesField(gridPane)
    initShowOutLinesField(gridPane)
    initDebugModeField(gridPane)

    new BorderPane() {
      styleClass = ObservableBuffer("border")
      center = gridPane
    }
  }

  private def initGraphWidthField(gridPane: GridPane): Unit = {
    rowIndex += 1
    val label = Label("Graph Width:")
    gridPane.add(label, 0, rowIndex)
    val field = new Spinner[Double](20, 1800, control.graphWidth, 20)
    label.labelFor = field
    control
      .graphWidthProperty
      .onChange((_, _, nv) => {
        val newValue = nv.doubleValue()
        if field.value.value != newValue then field.getValueFactory.setValue(newValue)
      })
    field.valueProperty().onChange((_, _, nv) => control.graphWidth = nv)
    rowIndex += 1
    gridPane.add(field, 0, rowIndex)
  }

  private def initGraphHeightField(gridPane: GridPane): Unit = {
    rowIndex += 1
    val label = Label("Graph Height:")
    gridPane.add(label, 0, rowIndex)
    val field = new Spinner[Double](20, 1200, control.graphHeight, 20)
    label.labelFor = field
    control
      .graphHeightProperty
      .onChange((_, _, nv) => {
        val newValue = nv.doubleValue()
        if field.value.value != newValue then field.getValueFactory.setValue(newValue)
      })
    field.valueProperty().onChange((_, _, nv) => control.graphHeight = nv)
    rowIndex += 1
    gridPane.add(field, 0, rowIndex)
  }

  private def initTokenWidthField(gridPane: GridPane): Unit = {
    rowIndex += 1
    val label = Label("Token Width:")
    gridPane.add(label, 0, rowIndex)
    val field = new Spinner[Double](20, 1200, control.tokenWidth, 2)
    label.labelFor = field
    control
      .tokenWidthProperty
      .onChange((_, _, nv) => {
        val newValue = nv.doubleValue()
        if field.value.value != newValue then field.getValueFactory.setValue(newValue)
      })
    field.valueProperty().onChange((_, _, nv) => control.tokenWidth = nv)
    rowIndex += 1
    gridPane.add(field, 0, rowIndex)
  }

  private def initTokenHeightField(gridPane: GridPane): Unit = {
    rowIndex += 1
    val label = Label("Token Height:")
    gridPane.add(label, 0, rowIndex)
    val field = new Spinner[Double](20, 1200, control.tokenHeight, 2)
    label.labelFor = field
    control
      .tokenHeightProperty
      .onChange((_, _, nv) => {
        val newValue = nv.doubleValue()
        if field.value.value != newValue then field.getValueFactory.setValue(newValue)
      })
    field.valueProperty().onChange((_, _, nv) => control.tokenHeight = nv)
    rowIndex += 1
    gridPane.add(field, 0, rowIndex)
  }

  private def initGapBetweenTokensField(gridPane: GridPane): Unit = {
    rowIndex += 1
    val label = Label("Token Height:")
    gridPane.add(label, 0, rowIndex)
    val field = new Spinner[Double](20, 1200, control.gapBetweenTokens, 2)
    label.labelFor = field
    control
      .gapBetweenTokensProperty
      .onChange((_, _, nv) => {
        val newValue = nv.doubleValue()
        if field.value.value != newValue then field.getValueFactory.setValue(newValue)
      })
    field.valueProperty().onChange((_, _, nv) => control.gapBetweenTokens = nv)
    rowIndex += 1
    gridPane.add(field, 0, rowIndex)
  }

  private def initShowGridLinesField(gridPane: GridPane): Unit = {
    val field = new ToggleSwitch("Show GridLines:")
    field.selectedProperty().bindBidirectional(control.showGridLinesWrapperProperty)
    rowIndex += 1
    gridPane.add(field, 0, rowIndex)
  }

  private def initShowOutLinesField(gridPane: GridPane): Unit = {
    val field = new ToggleSwitch("Show OutLines:")
    field.selectedProperty().bindBidirectional(control.showOutLinesWrapperProperty)
    rowIndex += 1
    gridPane.add(field, 0, rowIndex)
  }

  private def initDebugModeField(gridPane: GridPane): Unit = {
    val field = new ToggleSwitch("Debug Mode:")
    field.selectedProperty().bindBidirectional(control.debugModeWrapperProperty)
    rowIndex += 1
    gridPane.add(field, 0, rowIndex)
  }

  private def initTerminalFontField(gridPane: GridPane): Unit = {
    rowIndex += 1
    val label = Label("Terminal Font:")
    gridPane.add(label, 0, rowIndex)
    val field = new Label("Sample") {
      font = control.terminalFont.toFont
      prefWidth = 50
      tooltip = Tooltip(control.terminalFont.toFont.toDisplayText)
    }
    control
      .terminalFontProperty
      .onChange((_, _, nv) => {
        val font = nv.toFont
        field.setFont(font)
        field.setTooltip(Tooltip(font.toDisplayText))
      })
    rowIndex += 1
    gridPane.add(field, 0, rowIndex)

    val button = new Button() {
      text = " ... "
      onAction = event => {
        terminalFontSelectorDialog.showAndWait().toScala match
          case Some(updateFont) =>
            control.terminalFont = updateFont.toFontMetaInfo
            field.setFont(updateFont)
            field.setTooltip(Tooltip(updateFont.toDisplayText))

          case None => ()
        event.consume()
      }
    }
    gridPane.add(button, 1, rowIndex)
  }

  private def initPartOfSpeechFontField(gridPane: GridPane): Unit = {
    rowIndex += 1
    val label = Label("Part of Speech Font:")
    gridPane.add(label, 0, rowIndex)
    val field = new Label("Sample") {
      font = control.partOfSpeechFont.toFont
      prefWidth = 50
      tooltip = Tooltip(control.partOfSpeechFont.toFont.toDisplayText)
    }
    control
      .partOfSpeechFontProperty
      .onChange((_, _, nv) => {
        val font = nv.toFont
        field.setFont(font)
        field.setTooltip(Tooltip(font.toDisplayText))
      })
    rowIndex += 1
    gridPane.add(field, 0, rowIndex)

    val button = new Button() {
      text = " ... "
      onAction = event => {
        partOfSpeechFontSelectorDialog.showAndWait().toScala match
          case Some(updateFont) =>
            control.partOfSpeechFont = updateFont.toFontMetaInfo
            field.setFont(updateFont)
            field.setTooltip(Tooltip(updateFont.toDisplayText))

          case None => ()
        event.consume()
      }
    }
    gridPane.add(button, 1, rowIndex)
  }

  private def initTranslationFontField(gridPane: GridPane): Unit = {
    rowIndex += 1
    val label = Label("Translation Font:")
    gridPane.add(label, 0, rowIndex)
    val field = new Label("Sample") {
      font = control.translationFont.toFont
      prefWidth = 50
      tooltip = Tooltip(control.translationFont.toFont.toDisplayText)
    }
    control
      .translationFontProperty
      .onChange((_, _, nv) => {
        val font = nv.toFont
        field.setFont(font)
        field.setTooltip(Tooltip(font.toDisplayText))
      })
    rowIndex += 1
    gridPane.add(field, 0, rowIndex)

    val button = new Button() {
      text = " ... "
      onAction = event => {
        translationFontSelectorDialog.showAndWait().toScala match
          case Some(updateFont) =>
            control.translationFont = updateFont.toFontMetaInfo
            field.setFont(updateFont)
            field.setTooltip(Tooltip(updateFont.toDisplayText))

          case None => ()
        event.consume()
      }
    }
    gridPane.add(button, 1, rowIndex)
  }

  private def initBackgroundColorField(gridPane: GridPane): Unit = {
    rowIndex += 1
    val label = Label("Background Color:")
    gridPane.add(label, 0, rowIndex)

    val field = Rectangle(100, 30, control.backgroundColor)
    control.backgroundColorProperty.onChange((_, _, nv) => field.fill = nv)
    rowIndex += 1
    gridPane.add(field, 0, rowIndex)

    val button = new Button() {
      text = " ... "
      onAction = event => {
        popOver.show(this)
        event.consume()
      }
    }
    gridPane.add(button, 1, rowIndex)
  }
}

object GraphSettingsSkin {
  def apply(control: GraphSettingsView): GraphSettingsSkin = new GraphSettingsSkin(control)
}
