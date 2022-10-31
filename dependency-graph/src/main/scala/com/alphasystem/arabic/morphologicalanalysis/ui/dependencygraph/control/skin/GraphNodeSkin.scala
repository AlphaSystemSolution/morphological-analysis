package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import morphology.graph.model.GraphNode
import javafx.scene.control.SkinBase
import org.controlsfx.dialog.FontSelectorDialog
import scalafx.Includes.*
import scalafx.beans.property.DoubleProperty
import scalafx.geometry.Insets
import scalafx.scene.Node
import scalafx.scene.control.*
import scalafx.scene.layout.{ BorderPane, GridPane }

import scala.jdk.OptionConverters.*

abstract class GraphNodeSkin[N <: GraphNode, C <: GraphNodeView[N]](control: C) extends SkinBase[C](control) {

  protected var rowIndex: Int = 0
  private var fontSelectorDialog: FontSelectorDialog = _
  control.fontProperty.onChange((_, _, nv) => fontSelectorDialog = new FontSelectorDialog(nv.toFont))

  protected val gridPane: GridPane =
    new GridPane() {
      vgap = DefaultGap
      hgap = DefaultGap
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
    }

  protected def initializeSkin: BorderPane = {
    addProperties()
    new BorderPane() {
      center = gridPane
    }
  }

  protected def addProperties(): Unit = {
    fontSelectorDialog = new FontSelectorDialog(control.font.toFont)
    addTextProperty()
    addDoubleProperty(control.xProperty, "Text x:")
    addDoubleProperty(control.yProperty, "Text y:")
    addDoubleProperty(control.txProperty, "Translate x:")
    addDoubleProperty(control.tyProperty, "Translate y:")
  }

  private def addTextProperty(): Unit = {
    addNode(Label("Text Font:"))

    val arabicText = control.text
    val labelText = if Option(arabicText).isDefined && !arabicText.isBlank then arabicText else "Sample"
    val field = new Label(labelText) {
      font = control.font.toFont
      prefWidth = 100
      tooltip = Tooltip(control.font.toFont.toDisplayText)
    }
    gridPane.add(field, 0, rowIndex)
    control
      .fontProperty
      .onChange((_, _, nv) => {
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

  protected def addDoubleProperty(property: DoubleProperty, labelText: String): Unit = {
    addNode(Label(labelText))

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

    addNode(spinnerField)
    addNode(sliderField)
  }

  protected def addNode(node: Node): Unit = {
    gridPane.add(node, 0, rowIndex)
    rowIndex += 1
  }
}
