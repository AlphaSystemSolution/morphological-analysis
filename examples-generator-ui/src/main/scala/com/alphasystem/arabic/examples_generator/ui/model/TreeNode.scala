package com.alphasystem
package arabic
package examples_generator
package ui
package model

import scalafx.scene.control.TreeItem

enum TreeNode:
  case TableNode(tableData: TableData)
  case RowNode(row: Row, index: Int)
  case ColumnNode(column: ColumnData, index: Int)
  case ColumnPropertiesNode(column: ColumnData)

  def label: String = this match
    case TableNode(td)           => td.tableProperties.tag
    case RowNode(_, idx)         => s"Row ${idx + 1}"
    case ColumnNode(_, idx)      => s"Column ${idx + 1}"
    case ColumnPropertiesNode(_) => "Properties"

object TreeNode {
  def buildTree(tableData: TableData): TreeItem[TreeNode] = {
    val root = new TreeItem[TreeNode](TreeNode.TableNode(tableData)) {
      expanded = true
    }

    tableData.row.zipWithIndex.foreach { (row, rowIdx) =>
      val rowItem = new TreeItem[TreeNode](TreeNode.RowNode(row, rowIdx)) {
        expanded = false
      }
      row.columns.zipWithIndex.foreach { (col, colIdx) =>
        val columnItem = new TreeItem[TreeNode](TreeNode.ColumnNode(col, colIdx)) {
          expanded = false
        }
        columnItem.children += new TreeItem[TreeNode](TreeNode.ColumnPropertiesNode(col))
        rowItem.children += columnItem
      }
      root.children += rowItem
    }

    root
  }
}
