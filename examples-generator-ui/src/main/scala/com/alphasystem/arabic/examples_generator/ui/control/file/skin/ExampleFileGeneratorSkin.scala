package com.alphasystem
package arabic
package examples_generator
package ui
package control
package file
package skin

import ui.model.{ Action, ColumnData, Row, TableProperties, TreeNode }
import arabic.fx.ui.util.*
import control.table.TablePropertiesEditorDialog
import control.column.ColumnEditorDialog
import javafx.collections.ListChangeListener
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.application.Platform
import scalafx.geometry.Pos
import scalafx.scene.control.{ ComboBox, Label, TitledPane, TreeItem, TreeView }
import scalafx.scene.layout.{ BorderPane, GridPane, HBox, Priority, VBox }

import scala.jdk.CollectionConverters.*

class ExampleFileGeneratorSkin private (control: ExampleFileGeneratorView)
    extends SkinBase[ExampleFileGeneratorView](control) {

  private val columnEditorDialog = ColumnEditorDialog()
  private val tablePropertiesEditorDialog = TablePropertiesEditorDialog()
  private val tableTagComboBox = ComboBox[String](control.tableTagsProperty)

  tableTagComboBox
    .getSelectionModel
    .selectedItemProperty()
    .onChange { (_, _, newTag) =>
      if Option(newTag).isDefined then {
        control.selectTable(newTag)
        Option(newTag).foreach(updateContent)
      }
    }

  control.selectedTableProperty.onChange { (_, _, nv) =>
    Option(nv).foreach { table =>
      tableTagComboBox.getSelectionModel.select(table.tag)
    }
  }

  control
    .actionProperty
    .onChange((_, _, nv) => {
      nv match {
        case Action.NoAction                     => // do nothing
        case Action.AddRow(row)                  => addRowToTree(row)
        case Action.AddColumn(columnData)        => addColumnToSelectedRow(columnData)
        case Action.UpdateColumn(columnData)     => updateColumnInTree(columnData)
        case Action.UpdateTable(tableProperties) => Platform.runLater(updateContent(tableProperties.tag))
        case Action.RemoveColumn(_)              => reindexColumns()
        case Action.RemoveRow(_)                 => reindexRows()
        case Action.SelectColumn(columnData)     => editColumn(columnData)
        case Action.SelectTable(tableProperties) => editTable(tableProperties)
      }
    })

  treeView
    .getSelectionModel
    .selectedItemProperty()
    .onChange { (_, _, newItem) =>
      Option(newItem).map(_.getValue) match {
        case Some(TreeNode.TableNode(tableData)) =>
          control.clearRowSelection()
          control.clearColumnSelection()
          control.action = Action.SelectTable(tableData.tableProperties)
        case Some(TreeNode.RowNode(row, _)) =>
          selectOwningRow(newItem)
          control.clearColumnSelection()
        case Some(TreeNode.ColumnNode(columnData, _)) =>
          selectOwningRow(newItem)
          control.action = Action.SelectColumn(columnData)
          control.selectColumn(columnData)
        case Some(TreeNode.ColumnPropertiesNode(columnData)) =>
          selectOwningRow(newItem)
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
    prefWidth = 700
    prefHeight = 600
    vgrow = Priority.Always
    cellFactory = (_: TreeView[TreeNode]) => ExampleFileTreeCell(control)
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

  private def reindexColumns(): Unit =
    Option(treeView.getRoot).foreach { root =>
      root.getChildren.asScala.foreach { rowItem =>
        rowItem.getChildren.asScala.zipWithIndex.foreach { (columnItem, idx) =>
          columnItem.getValue match {
            case TreeNode.ColumnNode(column, currentIndex) if currentIndex != idx =>
              columnItem.setValue(TreeNode.ColumnNode(column, idx))
            case _ => // do nothing
          }
        }
      }
    }

  private def reindexRows(): Unit =
    Option(treeView.getRoot).foreach { root =>
      root.getChildren.asScala.zipWithIndex.foreach { (rowItem, idx) =>
        rowItem.getValue match {
          case TreeNode.RowNode(row, currentIndex) if currentIndex != idx =>
            rowItem.setValue(TreeNode.RowNode(row, idx))
          case _ => // do nothing
        }
      }
    }

  private def selectOwningRow(item: javafx.scene.control.TreeItem[TreeNode]): Unit = {
    var current = item
    while current != null && !current.getValue.isInstanceOf[TreeNode.RowNode] do current = current.getParent
    if current != null then {
      val rowId = current.getValue.asInstanceOf[TreeNode.RowNode].row.id
      control.tableDataProperty.flatMap(_.row).find(_.id == rowId).foreach(control.selectRow)
    }
  }

  private def editColumn(columnData: ColumnData): Unit = {
    columnEditorDialog.setColumnData(columnData)
    columnEditorDialog.showAndWait() match {
      case Some(Some(columnData: ColumnData)) =>
        control.action = Action.UpdateColumn(columnData)
      case _ => // do nothing
    }
  }

  private def editTable(tableProperties: TableProperties): Unit = {
    tablePropertiesEditorDialog.setTableProperties(tableProperties)
    tablePropertiesEditorDialog.showAndWait() match {
      case Some(Some(tableProperties: TableProperties)) =>
        control.action = Action.UpdateTable(tableProperties)
      case _ => // do nothing
    }
  }

  private def updateColumnInTree(columnData: ColumnData): Unit = {
    Option(treeView.getSelectionModel.getSelectedItem).foreach { selectedItem =>
      selectedItem.getValue match {
        case TreeNode.ColumnNode(_, columnIndex) =>
          selectedItem.setValue(TreeNode.ColumnNode(columnData, columnIndex))
          selectedItem.getChildren.clear()
          addColumnPropertiesNode(columnData, selectedItem)
        case TreeNode.ColumnPropertiesNode(_) =>
          Option(selectedItem.getParent).foreach { parent =>
            parent.getValue match {
              case TreeNode.ColumnNode(_, columnIndex) =>
                parent.setValue(TreeNode.ColumnNode(columnData, columnIndex))
                parent.getChildren.clear()
                addColumnPropertiesNode(columnData, parent)
                treeView.getSelectionModel.select(parent)
              case _ => // do nothing
            }
          }
        case _ => // do nothing
      }
    }
  }

  private def addColumnPropertiesNode(columnData: ColumnData, selectedItem: TreeItem[TreeNode]) =
    selectedItem.getChildren.add(new TreeItem(TreeNode.ColumnPropertiesNode(columnData)))

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
      TreeNode.addColumns(row, rowItem)
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
    gridPane.add(new Label { text = properties.columnString }, 1, row)

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
}

object ExampleFileGeneratorSkin {
  private[file] def apply(control: ExampleFileGeneratorView) = new ExampleFileGeneratorSkin(control)
}
