package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package table

import arabic.morphologicalanalysis.ui.{ ArabicSupportEnumComboBoxTableCell, ListType }
import morphologicalengine.conjugation.model.NamedTemplate
import scalafx.Includes.*
import scalafx.scene.control.cell.ComboBoxTableCell
import scalafx.scene.control.TableColumn

class NamedTemplateTableColumn(width: Double) extends TableColumn[TableModel, NamedTemplate] {

  text = "Family"
  editable = true
  prefWidth = width
  cellValueFactory = _.value.templateProperty

  cellFactory = (_: TableColumn[TableModel, NamedTemplate]) =>
    new ComboBoxTableCell[TableModel, NamedTemplate](
      ArabicSupportEnumComboBoxTableCell[TableModel, NamedTemplate](
        ListType.LABEL_AND_CODE,
        NamedTemplate.values.toArray
      )
    )

  onEditCommit = event => {
    val selectedItem = event.getTableView.getSelectionModel.getSelectedItem
    selectedItem.template = event.getNewValue
    event.consume()
  }

}

object NamedTemplateTableColumn {
  def apply(width: Double): NamedTemplateTableColumn = new NamedTemplateTableColumn(width)
}
