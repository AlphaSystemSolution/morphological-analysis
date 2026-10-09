package com.alphasystem
package arabic
package examples_generator
package ui
package control
package table
package skin

import ui.model.{ Frame, Grid }
import arabic.fx.ui.util.{ createLabel, createPositiveIntegerTextField }
import javafx.scene.control.SkinBase
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.control.{ ComboBox, TextField }
import scalafx.scene.layout.{ BorderPane, GridPane }

class TablePropertiesEditorSkin private (control: TablePropertiesEditorView)
    extends SkinBase[TablePropertiesEditorView](control) {

  private val tagTextField = new TextField() {
    prefColumnCount = 25
  }
  private val tableWidthField = createPositiveIntegerTextField
  private val frameComboBox = ComboBox[Frame](Frame.values.toSeq)
  private val gridComboBox = ComboBox[Grid](Grid.values.toSeq)
  private val roleTextField = new TextField()

  control.tagProperty.bindBidirectional(tagTextField.textProperty())
  control.tableWidthProperty.bindBidirectional(tableWidthField.textProperty())
  control.frameProperty.bindBidirectional(frameComboBox.valueProperty())
  control.gridProperty.bindBidirectional(gridComboBox.valueProperty())
  control.roleProperty.bindBidirectional(roleTextField.textProperty())

  private val gridPane = {
    val gridPane = new GridPane {
      styleClass = ObservableBuffer("border")
      vgap = 10
      hgap = 10
      alignment = Pos.Center
      padding = Insets(10, 10, 10, 10)
    }

    var row = 0
    gridPane.add(createLabel("Tag:"), 0, row)
    gridPane.add(tagTextField, 1, row)

    row += 1
    gridPane.add(createLabel("Table Width:"), 0, row)
    gridPane.add(tableWidthField, 1, row)

    row += 1
    gridPane.add(createLabel("Frame:"), 0, row)
    gridPane.add(frameComboBox, 1, row)

    row += 1
    gridPane.add(createLabel("Grid:"), 0, row)
    gridPane.add(gridComboBox, 1, row)

    row += 1
    gridPane.add(createLabel("Role:"), 0, row)
    gridPane.add(roleTextField, 1, row)

    gridPane
  }

  private val mainPanel = new BorderPane {
    center = gridPane
    BorderPane.setAlignment(gridPane, Pos.Center)
  }

  getChildren.addAll(mainPanel)
}

object TablePropertiesEditorSkin {
  private[table] def apply(control: TablePropertiesEditorView) = new TablePropertiesEditorSkin(control)
}
