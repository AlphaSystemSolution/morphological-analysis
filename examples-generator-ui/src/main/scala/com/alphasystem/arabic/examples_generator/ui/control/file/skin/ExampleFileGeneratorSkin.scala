package com.alphasystem
package arabic
package examples_generator
package ui
package control
package file
package skin

import ui.model.{ Action, TreeNode }
import examples_generator.model.{ ColumnData, TableProperties }
import arabic.fx.ui.util.*
import control.table.TablePropertiesEditorDialog
import control.column.ColumnEditorDialog
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.geometry.Pos
import scalafx.scene.control.{ ComboBox, Label, TitledPane, TreeView }
import scalafx.scene.layout.{ BorderPane, GridPane, HBox, Priority, VBox }

import java.util.UUID
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
      Option(newTag) match {
        case Some(tag) =>
          control.selectTable(tag)
          updateContent(tag)
        case None =>
          propertiesGridContainer.children = Seq.empty
          treeView.root = null
      }
    }

  control.selectedTableProperty.onChange { (_, _, nv) =>
    Option(nv).foreach { table =>
      val sameTag = tableTagComboBox.getSelectionModel.getSelectedItem == table.tag
      tableTagComboBox.getSelectionModel.select(table.tag)
      if sameTag then updateContent(table.tag)
    }
  }

  control
    .actionProperty
    .onChange((_, _, nv) => {
      nv match {
        case Action.SelectColumn(columnData)     => editColumn(columnData)
        case Action.SelectTable(tableProperties) => editTable(tableProperties)
        case _                                   => // all mutations are rendered via the selectedTableProperty rebuild
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
          control.selectColumn(columnData)
        case Some(TreeNode.ColumnPropertiesNode(columnData)) =>
          selectOwningRow(newItem)
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
        // capture selection before rebuilding the tree, since resetting the root
        // clears the tree selection which clears the control's selection properties
        val selectedRowId = Option(control.selectedRowProperty.value).map(_.id)
        val selectedColumnId = Option(control.selectedColumnProperty.value).map(_.id)
        treeView.root = TreeNode.buildTree(tableData)
        treeView.getSelectionModel.clearSelection()
        restoreSelection(selectedRowId, selectedColumnId)
      case None =>
        propertiesGridContainer.children = Seq.empty
        treeView.root = null
    }
  }

  private def restoreSelection(selectedRowId: Option[UUID], selectedColumnId: Option[UUID]): Unit =
    Option(treeView.getRoot).foreach { root =>
      val selectedItem =
        (selectedRowId, selectedColumnId) match {
          case (Some(rowId), Some(columnId)) =>
            root
              .getChildren
              .asScala
              .find(rowItem => valueId(rowItem).contains(rowId))
              .flatMap(_.getChildren.asScala.find(columnItem => valueId(columnItem).contains(columnId)))
          case (Some(rowId), None) =>
            root.getChildren.asScala.find(rowItem => valueId(rowItem).contains(rowId))
          case _ => None
        }
      selectedItem.foreach(treeView.getSelectionModel.select(_))
    }

  private def valueId(item: javafx.scene.control.TreeItem[TreeNode]): Option[UUID] =
    Option(item).map(_.getValue).flatMap {
      case TreeNode.RowNode(row, _)       => Some(row.id)
      case TreeNode.ColumnNode(column, _) => Some(column.id)
      case _                              => None
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
