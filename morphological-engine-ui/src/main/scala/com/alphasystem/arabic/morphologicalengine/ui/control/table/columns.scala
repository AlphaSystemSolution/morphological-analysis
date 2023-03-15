package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package table

import scalafx.beans.value.ObservableValue
import scalafx.scene.control.TableColumn
import scalafx.scene.control.cell.{ CheckBoxTableCell, TextFieldTableCell }

import java.lang

object CheckedTableColumn {
  def apply(columnWidth: Double): TableColumn[TableModel, lang.Boolean] =
    new TableColumn[TableModel, java.lang.Boolean]() {
      prefWidth = columnWidth
      editable = true
      cellValueFactory = _.value.checkedProperty.asInstanceOf[ObservableValue[java.lang.Boolean, java.lang.Boolean]]
      cellFactory = CheckBoxTableCell.forTableColumn(this)
    }
}

object TranslationTableColumn {
  def apply(columnWidth: Double): TableColumn[TableModel, String] =
    new TableColumn[TableModel, String]() {
      text = "Translation"
      prefWidth = columnWidth
      editable = true
      cellValueFactory = _.value.translationProperty
      cellFactory = TextFieldTableCell.forTableColumn()
      onEditCommit = event => {
        event.getTableView.getSelectionModel.getSelectedItem.translation = event.getNewValue
        event.consume()
      }
    }
}

object RemovePassiveLineColumn {
  def apply(columnWidth: Double): TableColumn[TableModel, lang.Boolean] =
    new TableColumn[TableModel, java.lang.Boolean]() {
      text = s"Remove${System.lineSeparator()}Passive${System.lineSeparator()}Line"
      prefWidth = columnWidth
      editable = true
      cellValueFactory =
        _.value.removePassiveLineProperty.asInstanceOf[ObservableValue[java.lang.Boolean, java.lang.Boolean]]
      cellFactory = CheckBoxTableCell.forTableColumn(this)
    }
}

object SkipRuleProcessingColumn {
  def apply(columnWidth: Double): TableColumn[TableModel, lang.Boolean] =
    new TableColumn[TableModel, java.lang.Boolean]() {
      text = s"Skip${System.lineSeparator()}Rule${System.lineSeparator()}Processing"
      prefWidth = columnWidth
      editable = true
      cellValueFactory =
        _.value.skipRuleProcessingProperty.asInstanceOf[ObservableValue[java.lang.Boolean, java.lang.Boolean]]
      cellFactory = CheckBoxTableCell.forTableColumn(this)
    }
}
