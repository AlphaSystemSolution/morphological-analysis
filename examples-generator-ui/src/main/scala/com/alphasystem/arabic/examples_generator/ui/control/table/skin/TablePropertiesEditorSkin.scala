package com.alphasystem
package arabic
package examples_generator
package ui
package control
package table
package skin

import com.alphasystem.arabic.examples_generator.ui.model.{
  Frame,
  Grid,
  HorizontalAlignment,
  TableColumnInfo,
  VerticalAlignment
}
import com.alphasystem.arabic.fx.ui.util.{ createEmptyPanel, createLabel, createPositiveIntegerTextField }
import javafx.scene.control.{ ListView, SkinBase }
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{ Insets, Orientation, Pos }
import scalafx.scene.control.SelectionMode.Single
import scalafx.scene.control.{ Button, ComboBox, Separator, TextField }
import scalafx.scene.layout.{ BorderPane, GridPane }

import java.util.UUID

class TablePropertiesEditorSkin private (control: TablePropertiesEditorView)
    extends SkinBase[TablePropertiesEditorView](control) {

  private val tagTextField = new TextField() {
    prefColumnCount = 25
  }
  private val tableWidthField = createPositiveIntegerTextField
  private val frameComboBox = ComboBox[Frame](Frame.values.toSeq)
  private val gridComboBox = ComboBox[Grid](Grid.values.toSeq)
  private val roleTextField = new TextField()
  private val columnWidthField = createPositiveIntegerTextField
  private val columnInfosList = new ListView[TableColumnInfo](control.columnInfosProperty)
  private val horizontalAlignmentComboBox = ComboBox[HorizontalAlignment](HorizontalAlignment.values.toSeq)
  private val verticalAlignmentComboBox = ComboBox[VerticalAlignment](VerticalAlignment.values.toSeq)
  private val saveColumnInfoButton = new Button {
    text = "Save Column"
    disable = true
    onAction = event => {
      event.consume()
    }
  }
  columnInfosList.setCellFactory((_: ListView[TableColumnInfo]) => new TableColumnInfoListCell())
  columnInfosList.getSelectionModel.selectionMode = Single
  columnInfosList.setMaxHeight(5 * 24 + 2)

  private val columnId = UUID.randomUUID()

  control.tagProperty.bindBidirectional(tagTextField.textProperty())
  control.tableWidthProperty.bindBidirectional(tableWidthField.textProperty())
  control.frameProperty.bindBidirectional(frameComboBox.valueProperty())
  control.gridProperty.bindBidirectional(gridComboBox.valueProperty())
  control.roleProperty.bindBidirectional(roleTextField.textProperty())
  control.columnWidthProperty.bindBidirectional(columnWidthField.textProperty())
  control.horizontalAlignmentProperty.bindBidirectional(horizontalAlignmentComboBox.valueProperty())
  control.verticalAlignmentProperty.bindBidirectional(verticalAlignmentComboBox.valueProperty())

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

    row += 1
    gridPane.add(createLabel("Table Column Properties:"), 0, row)

    row += 1
    gridPane.add(Separator(Orientation.Horizontal), 0, row, 2, 1)

    row += 1
    gridPane.add(createLabel("Column Width:"), 0, row)
    gridPane.add(columnWidthField, 1, row)

    row += 1
    gridPane.add(createLabel("Horizontal Alignment:"), 0, row)
    gridPane.add(horizontalAlignmentComboBox, 1, row)

    row += 1
    gridPane.add(createLabel("Vertical Alignment:"), 0, row)
    gridPane.add(verticalAlignmentComboBox, 1, row)

    row += 1
    gridPane.add(createEmptyPanel(100), 0, row)
    gridPane.add(saveColumnInfoButton, 1, row)

    row += 1
    gridPane.add(createLabel("Table Columns:"), 0, row)
    gridPane.add(columnInfosList, 1, row)

    row += 1
    gridPane.add(Separator(Orientation.Horizontal), 0, row, 2, 1)

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
