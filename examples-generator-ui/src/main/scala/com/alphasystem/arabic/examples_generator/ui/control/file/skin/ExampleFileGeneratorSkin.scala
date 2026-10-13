package com.alphasystem
package arabic
package examples_generator
package ui
package control
package file
package skin

import ui.model.{ Action, ColumnData, Row, TableProperties, TreeNode }
import arabic.fx.ui.util.*
import javafx.collections.ListChangeListener
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.geometry.Pos
import scalafx.scene.control.{ ComboBox, Label, TitledPane, TreeItem, TreeView }
import scalafx.scene.layout.{ BorderPane, GridPane, HBox, Priority, VBox }

import scala.jdk.CollectionConverters.*

class ExampleFileGeneratorSkin private (control: ExampleFileGeneratorView)
    extends SkinBase[ExampleFileGeneratorView](control) {

  private val tableTagComboBox = ComboBox[String](control.tableTagsProperty)

  tableTagComboBox
    .getItems
    .addListener(new ListChangeListener[String] {
      override def onChanged(change: ListChangeListener.Change[? <: String]): Unit = {
        while change.next do {
          if change.wasAdded then {
            tableTagComboBox.getSelectionModel.select(change.getList.asScala.head)
          }
        }
      }
    })

  tableTagComboBox
    .getSelectionModel
    .selectedItemProperty()
    .onChange { (_, _, newTag) =>
      control.selectTable(newTag)
      Option(newTag).foreach(updateContent)
    }

  control
    .actionProperty
    .onChange((_, _, nv) => {
      nv match {
        case Action.NoAction                 => // do nothing
        case Action.AddRow(row)              => addRowToTree(row)
        case Action.AddColumn(columnData)    => addColumnToSelectedRow(columnData)
        case Action.SelectColumn(_)          => // this will be handled by ExamplesGeneratorApp
        case Action.UpdateColumn(columnData) => updateColumnInTree(columnData)
      }
    })

  treeView
    .getSelectionModel
    .selectedItemProperty()
    .onChange { (_, _, newItem) =>
      Option(newItem).map(_.getValue) match {
        case Some(TreeNode.RowNode(row, _)) =>
          control.selectRow(row)
          control.clearColumnSelection()
        case Some(TreeNode.ColumnNode(columnData, _)) =>
          control.action = Action.SelectColumn(columnData)
          control.selectColumn(columnData)
        case Some(TreeNode.ColumnPropertiesNode(columnData)) =>
          control.action = Action.SelectColumn(columnData)
          control.selectColumn(columnData)
        case _ =>
          control.clearRowSelection()
          control.clearColumnSelection()
      }
    }

  private val tableTagContainer =
    new HBox() {
      spacing = DefaultGap
      alignment = Pos.CenterLeft
      children = Seq(createLabel("Tags:"), tableTagComboBox)
    }

  private lazy val propertiesGridContainer = new VBox {
    spacing = DefaultGap
  }

  private lazy val treeView = new TreeView[TreeNode] {
    showRoot = true
    prefWidth = 500
    prefHeight = 600
    vgrow = Priority.Always
    cellFactory = (cell, item) => {
      if item != null then {
        item match {
          case TreeNode.ColumnNode(_, _) =>
            cell.text = item.label
            cell.graphic = null
          case TreeNode.ColumnPropertiesNode(column) =>
            cell.text = null
            cell.graphic = new VBox {
              spacing = 5
              children = Seq(createColumnSettingsGrid(column))
            }
          case _ =>
            cell.text = item.label
            cell.graphic = null
        }
      } else {
        cell.text = null
        cell.graphic = null
      }
    }
  }

  private val mainPanel = new BorderPane {
    center = new VBox {
      spacing = DefaultGap
      padding = DefaultInsets
      children = Seq(
        tableTagContainer,
        new TitledPane {
          text = "Table Properties"
          content = propertiesGridContainer
          expanded = false
        },
        treeView
      )
    }
  }

  getChildren.addAll(mainPanel)

  Option(tableTagComboBox.getSelectionModel.getSelectedItem).foreach(updateContent)

  private def updateContent(tag: String): Unit = {
    control.tableDataProperty.find(_.tag == tag) match {
      case Some(tableData) =>
        propertiesGridContainer.children = Seq(createPropertiesGrid(tableData.tableProperties))
        treeView.root = TreeNode.buildTree(tableData)
        treeView.getSelectionModel.clearSelection()
      case None =>
        propertiesGridContainer.children = Seq.empty
        treeView.root = null
    }
  }

  private def updateColumnInTree(columnData: ColumnData): Unit = {
    Option(treeView.getSelectionModel.getSelectedItem).foreach { selectedItem =>
      selectedItem.getValue match {
        case TreeNode.ColumnNode(_, columnIndex) =>
          selectedItem.setValue(TreeNode.ColumnNode(columnData, columnIndex))
          selectedItem.getChildren.clear()
          selectedItem.getChildren.add(new TreeItem(TreeNode.ColumnPropertiesNode(columnData)))
        case TreeNode.ColumnPropertiesNode(_) =>
          Option(selectedItem.getParent).foreach { parent =>
            parent.getValue match {
              case TreeNode.ColumnNode(_, columnIndex) =>
                parent.setValue(TreeNode.ColumnNode(columnData, columnIndex))
                parent.getChildren.clear()
                parent.getChildren.add(new TreeItem(TreeNode.ColumnPropertiesNode(columnData)))
                treeView.getSelectionModel.select(parent)
              case _ => // do nothing
            }
          }
        case _ => // do nothing
      }
    }
  }

  private def addColumnToSelectedRow(columnData: ColumnData): Unit = {
    Option(control.selectedRowProperty.value).foreach { updatedRow =>
      Option(treeView.getSelectionModel.getSelectedItem).foreach { selectedItem =>
        val rowIndex = selectedItem.getValue match {
          case TreeNode.RowNode(_, idx) => idx
          case _                        => 0
        }
        selectedItem.setValue(TreeNode.RowNode(updatedRow, rowIndex))
        val columnIndex = selectedItem.getChildren.size
        val columnItem = new javafx.scene.control.TreeItem(TreeNode.ColumnNode(columnData, columnIndex))
        columnItem.getChildren.add(new javafx.scene.control.TreeItem(TreeNode.ColumnPropertiesNode(columnData)))
        selectedItem.getChildren.add(columnItem)
      }
    }
  }

  private def addRowToTree(row: Row): Unit = {
    Option(treeView.root.value).foreach { root =>
      val rowIndex = root.children.size
      val rowItem = new TreeItem[TreeNode](TreeNode.RowNode(row, rowIndex)) {
        expanded = true
      }
      row.columns.zipWithIndex.foreach { (col, colIdx) =>
        val columnItem = new TreeItem[TreeNode](TreeNode.ColumnNode(col, colIdx)) {
          expanded = false
        }
        columnItem.children += new TreeItem[TreeNode](TreeNode.ColumnPropertiesNode(col))
        rowItem.children += columnItem
      }
      root.children += rowItem
      treeView.getSelectionModel.select(rowItem)
    }
  }

  private def createPropertiesGrid(properties: TableProperties): GridPane = {
    val gridPane = new GridPane {
      hgap = DefaultGap
      vgap = DefaultGap
      alignment = Pos.CenterLeft
    }

    var row = 0
    gridPane.add(createLabel("Tag:"), 0, row)
    gridPane.add(new Label { text = properties.tag }, 1, row)

    row += 1
    gridPane.add(createLabel("Columns:"), 0, row)
    gridPane.add(new Label { text = properties.columns.size.toString }, 1, row)

    row += 1
    gridPane.add(createLabel("Table Width:"), 0, row)
    gridPane.add(new Label { text = properties.tableWidth.toString }, 1, row)

    row += 1
    gridPane.add(createLabel("Frame:"), 0, row)
    gridPane.add(new Label { text = properties.frame.toString }, 1, row)

    row += 1
    gridPane.add(createLabel("Grid:"), 0, row)
    gridPane.add(new Label { text = properties.grid.toString }, 1, row)

    row += 1
    gridPane.add(createLabel("Role:"), 0, row)
    gridPane.add(new Label { text = properties.role.getOrElse("None") }, 1, row)

    gridPane
  }

  private def createColumnSettingsGrid(column: ColumnData): GridPane = {
    val gridPane = new GridPane {
      hgap = 5
      vgap = 2
    }

    var row = 0
    gridPane.add(createLabel("Type:"), 0, row)
    gridPane.add(new Label { text = column.`type`.toString }, 1, row)

    row += 1
    gridPane.add(createLabel("Text:"), 0, row)
    gridPane.add(new Label { text = column.text }, 1, row)

    column.settings.foreach { s =>
      row += 1
      gridPane.add(createLabel("Col Span:"), 0, row)
      gridPane.add(new Label { text = s.colSpan.toString }, 1, row)

      row += 1
      gridPane.add(createLabel("Row Span:"), 0, row)
      gridPane.add(new Label { text = s.rowSpan.toString }, 1, row)

      row += 1
      gridPane.add(createLabel("H Align:"), 0, row)
      gridPane.add(new Label { text = s.horizontalAlignment.toString }, 1, row)

      row += 1
      gridPane.add(createLabel("V Align:"), 0, row)
      gridPane.add(new Label { text = s.verticalAlignment.toString }, 1, row)
    }

    gridPane
  }
}

object ExampleFileGeneratorSkin {
  private[file] def apply(control: ExampleFileGeneratorView) = new ExampleFileGeneratorSkin(control)
}
