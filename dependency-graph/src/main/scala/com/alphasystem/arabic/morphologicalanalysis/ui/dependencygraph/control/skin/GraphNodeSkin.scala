package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import morphology.graph.model.{ FontMetaInfo, GraphNode }
import javafx.scene.control.SkinBase
import org.controlsfx.dialog.FontSelectorDialog
import scalafx.Includes.*
import scalafx.beans.property.{ DoubleProperty, ObjectProperty }
import scalafx.geometry.Insets
import scalafx.scene.Node
import scalafx.scene.control.*
import scalafx.scene.layout.{ BorderPane, GridPane }

import scala.jdk.OptionConverters.*

abstract class GraphNodeSkin[N <: GraphNode, C <: GraphNodeView[N]](control: C) extends SkinBase[C](control) {

  protected var rowIndex: Int = 0
  private var fontSelectorDialog: FontSelectorDialog = _
  control.fontProperty.onChange((_, _, nv) => fontSelectorDialog = new FontSelectorDialog(nv.toFont))

  protected def initializeSkin: BorderPane = {
    val alPanes = addProperties()
    val accordion: Accordion = new Accordion() {
      panes = alPanes
      expandedPane = alPanes.head
    }

    new BorderPane() {
      center = accordion
    }
  }

  private def createCommonProperties = {
    fontSelectorDialog = new FontSelectorDialog(control.font.toFont)
    val gridPane: GridPane =
      new GridPane() {
        vgap = DefaultGap
        hgap = DefaultGap
        padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
      }

    addTextAndFontSelectionProperty(
      "Arabic Text Font:",
      control.text,
      control.font,
      control.fontProperty,
      fontSelectorDialog,
      gridPane
    )
    addDoubleProperty(control.xProperty, "Text x:", gridPane)
    addDoubleProperty(control.yProperty, "Text y:", gridPane)
    addDoubleProperty(control.txProperty, "Translate x:", gridPane)
    addDoubleProperty(control.tyProperty, "Translate y:", gridPane)
    createTitledPane("Common Properties:", gridPane)
  }
  protected def addProperties(): Seq[TitledPane] = Seq(createCommonProperties)

  protected def addTextAndFontSelectionProperty(
    label: String,
    initialText: String,
    initialFont: FontMetaInfo,
    property: ObjectProperty[FontMetaInfo],
    fontSelectorDialog: FontSelectorDialog,
    gridPane: GridPane
  ): Unit = {
    addNode(Label(label), gridPane)

    val labelText = if Option(initialText).isDefined && !initialText.isBlank then initialText else "Sample"
    val field = new Label(labelText) {
      font = initialFont.toFont
      prefWidth = 100
      tooltip = Tooltip(initialFont.toFont.toDisplayText)
    }
    gridPane.add(field, 0, rowIndex)

    property.onChange((_, _, nv) => {
      val font = nv.toFont
      field.setFont(font)
      field.setTooltip(Tooltip(font.toDisplayText))
    })

    val button = new Button() {
      text = " ... "
      onAction = event => {
        fontSelectorDialog.showAndWait().toScala match
          case Some(updateFont) =>
            control.font = updateFont.toFontMetaInfo
            field.setFont(updateFont)
            field.setTooltip(Tooltip(updateFont.toDisplayText))

          case None => ()
        event.consume()
      }
    }
    gridPane.add(button, 1, rowIndex)
    rowIndex += 1
  }

  protected def addDoubleProperty(property: DoubleProperty, labelText: String, gridPane: GridPane): Unit = {
    addNode(Label(labelText), gridPane)

    val spinnerField = new Spinner[Double](-100, 100, property.value, 0.5)
    val sliderField = new Slider(-100, 100, property.value)
    sliderField.valueProperty().bindBidirectional(property)
    sliderField
      .valueProperty()
      .onChange((_, _, nv) => {
        val value = spinnerField.getValue
        val newValue = nv.doubleValue()
        if newValue != value then spinnerField.getValueFactory.setValue(newValue)
      })

    addNode(spinnerField, gridPane)
    addNode(sliderField, gridPane)
  }

  protected def addNode(node: Node, gridPane: GridPane): Unit = {
    gridPane.add(node, 0, rowIndex)
    rowIndex += 1
  }
}
