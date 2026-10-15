package com.alphasystem
package arabic
package examples_generator
package ui
package control
package file

import com.alphasystem
import com.alphasystem.arabic.fx.ui.util.createLabel
import de.jensd.fx.glyphs.materialdesignicons.{ MaterialDesignIcon, MaterialDesignIconView }
import ui.model.{ ColumnData, ColumnType, TreeNode }
import javafx.scene.control.TreeCell as JTreeCell
import scalafx.Includes.*
import scalafx.scene.control.{ Button, ContentDisplay, Label, TreeCell }
import scalafx.scene.layout.{ GridPane, HBox, Pane, Priority, VBox }

class ExampleFileTreeCell extends JTreeCell[TreeNode] {

  private val hBox = new HBox()
  private val label = new Label("")
  private val pane = new Pane()
  private val deleteButton = new Button {
    graphic = new MaterialDesignIconView(MaterialDesignIcon.DELETE)
    contentDisplay = ContentDisplay.GraphicOnly
    tooltip = "Remove node"
    onAction = event => {
      val treeItem = getTreeItem
      treeItem.getParent.getChildren.remove(treeItem)
      event.consume()
    }
  }
  hBox.getChildren.addAll(label, pane, deleteButton)
  HBox.setHgrow(pane, Priority.Always)

  override def updateItem(item: TreeNode, empty: Boolean): Unit = {
    super.updateItem(item, empty)
    setText(null)
    setGraphic(null)

    if !empty && Option(item).isDefined then {
      item match {
        case TreeNode.ColumnPropertiesNode(column) =>
          label.text = null
          setGraphic(
            new VBox {
              spacing = 5
              children = Seq(createColumnSettingsGrid(column))
            }
          )

        case _ =>
          label.text = item.label
          setGraphic(hBox)
      }
    }
  }

  private def createColumnSettingsGrid(column: ColumnData): GridPane = {
    val gridPane = new GridPane {
      hgap = 5
      vgap = 2
    }

    var row = 0
    gridPane.add(createLabel("Type:"), 0, row)
    gridPane.add(
      new Label {
        text = column.`type`.toString
      },
      1,
      row
    )

    row += 1
    gridPane.add(createLabel("Text:"), 0, row)
    val label = new Label {
      text = column.text
      font = column.`type` match {
        case ColumnType.Arabic | ColumnType.ArabicBold | ColumnType.ArabicSmall | ColumnType.ArabicTableCaption =>
          preferences.arabicFont(24)
        case ColumnType.Translation | ColumnType.Other =>
          preferences.englishFont(14)
      }
    }
    gridPane.add(label, 1, row)

    column.settings.foreach { s =>
      row += 1
      gridPane.add(createLabel("Col Span:"), 0, row)
      gridPane.add(
        new Label {
          text = s.colSpan.toString
        },
        1,
        row
      )

      row += 1
      gridPane.add(createLabel("Row Span:"), 0, row)
      gridPane.add(
        new Label {
          text = s.rowSpan.toString
        },
        1,
        row
      )

      row += 1
      gridPane.add(createLabel("H Align:"), 0, row)
      gridPane.add(
        new Label {
          text = s.horizontalAlignment.toString
        },
        1,
        row
      )

      row += 1
      gridPane.add(createLabel("V Align:"), 0, row)
      gridPane.add(
        new Label {
          text = s.verticalAlignment.toString
        },
        1,
        row
      )
    }

    gridPane
  }
}

object ExampleFileTreeCell {
  def apply(): TreeCell[TreeNode] = new TreeCell[TreeNode](new ExampleFileTreeCell)
}
