package com.alphasystem
package arabic
package examples_generator
package ui
package control
package file

import arabic.fx.ui.util.createLabel
import de.jensd.fx.glyphs.fontawesome.{ FontAwesomeIcon, FontAwesomeIconView }
import de.jensd.fx.glyphs.materialdesignicons.{ MaterialDesignIcon, MaterialDesignIconView }
import ui.model.{ Action, ColumnData, ColumnType, InsertPosition, Row, TreeNode }
import ui.control.column.ColumnEditorDialog
import javafx.scene.control.TreeCell as JTreeCell
import scalafx.Includes.*
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ Alert, Button, ContentDisplay, ContextMenu, Label, MenuItem, TreeCell }
import scalafx.scene.layout.{ GridPane, HBox, Pane, Priority, VBox }

class ExampleFileTreeCell(control: ExampleFileGeneratorView) extends JTreeCell[TreeNode] {

  private val hBox = new HBox()
  private val label = new Label("")
  private val pane = new Pane()
  private val editButton = new Button {
    graphic = new FontAwesomeIconView(FontAwesomeIcon.EDIT)
    contentDisplay = ContentDisplay.GraphicOnly
    tooltip = "Edit node"
    onAction = event => {
      editNode()
      event.consume()
    }
  }
  private val deleteButton = new Button {
    graphic = new MaterialDesignIconView(MaterialDesignIcon.DELETE)
    contentDisplay = ContentDisplay.GraphicOnly
    tooltip = "Remove node"
    onAction = event => {
      deleteNode()
      event.consume()
    }
  }
  private lazy val columnEditorDialog = ColumnEditorDialog()

  private lazy val rowContextMenu = new ContextMenu(
    new MenuItem("Add Row Before") {
      onAction = _ => addRow(InsertPosition.Before)
    },
    new MenuItem("Add Row After") {
      onAction = _ => addRow(InsertPosition.After)
    }
  )

  private lazy val columnContextMenu = new ContextMenu(
    new MenuItem("Add Column Before") {
      onAction = _ => addColumn(InsertPosition.Before)
    },
    new MenuItem("Add Column After") {
      onAction = _ => addColumn(InsertPosition.After)
    }
  )

  hBox.getChildren.addAll(label, pane, editButton, deleteButton)
  HBox.setHgrow(pane, Priority.Always)

  override def updateItem(item: TreeNode, empty: Boolean): Unit = {
    super.updateItem(item, empty)
    setText(null)
    setGraphic(null)
    setContextMenu(null)
    editButton.visible = true

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

        case TreeNode.RowNode(_, _) =>
          editButton.visible = false
          label.text = item.label
          setGraphic(hBox)
          setContextMenu(rowContextMenu)

        case TreeNode.ColumnNode(_, _) =>
          label.text = item.label
          setGraphic(hBox)
          setContextMenu(columnContextMenu)

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

  private def addRow(position: InsertPosition): Unit = {
    getTreeView.getSelectionModel.select(getTreeItem)
    control.action = Action.AddRow(Row(), position)
  }

  private def addColumn(position: InsertPosition): Unit = {
    getTreeView.getSelectionModel.select(getTreeItem)
    columnEditorDialog.reset()
    columnEditorDialog.showAndWait() match {
      case Some(Some(columnData: ColumnData)) =>
        control.action = Action.AddColumn(columnData, position)
      case _ => // do nothing
    }
  }

  private def editNode(): Unit = {
    val treeItem = getTreeItem
    if Option(treeItem).isDefined then {
      val treeNode = treeItem.value.value
      treeNode match {
        case TreeNode.TableNode(tableData) =>
          control.action = Action.NoAction
          control.action = Action.SelectTable(tableData.tableProperties)
        case TreeNode.ColumnNode(column, _) =>
          control.action = Action.NoAction
          control.action = Action.SelectColumn(column)
        case _ => // do nothing
      }
    }
  }

  private def deleteNode(): Unit = {
    new Alert(AlertType.Confirmation) {
      title = "Delete Node"
      headerText = "Are you sure you want to delete this node?"
      contentText = "This action cannot be undone."
    }.showAndWait() match {
      case Some(buttonType) if buttonType.buttonData == ButtonData.OKDone =>
        val treeItem = getTreeItem
        if Option(treeItem).isDefined then {
          val parent = treeItem.getParent
          if Option(parent).isDefined then parent.getChildren.remove(treeItem)
          treeItem.getValue match {
            case TreeNode.TableNode(tableData)    => // to be implemented
            case TreeNode.RowNode(row, _)         => control.action = Action.RemoveRow(row.id)
            case TreeNode.ColumnNode(column, _)   => control.action = Action.RemoveColumn(column.id)
            case TreeNode.ColumnPropertiesNode(_) =>
            // do nothing, properties will be removed automatically along with the column
          }
        }
      case _ => // do nothing
    }
  }
}

object ExampleFileTreeCell {
  def apply(control: ExampleFileGeneratorView): TreeCell[TreeNode] =
    new TreeCell[TreeNode](new ExampleFileTreeCell(control))
}
