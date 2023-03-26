package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package table

import com.alphasystem.arabic.morphologicalengine.conjugation.forms.noun.VerbalNoun
import morphologicalengine.conjugation.forms.NounSupport
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{ TableCell, TableColumn }

class VerbalNounTableCell extends ListTableCell[NounSupport](VerbalNoun.values) {}

object VerbalNounTableColumn {
  def apply(columnWidth: Double): TableColumn[TableModel, Seq[NounSupport]] =
    new TableColumn[TableModel, Seq[NounSupport]]() {
      text = "Verbal Nouns"
      editable = true
      prefWidth = columnWidth
      cellValueFactory = _.value.verbalNounsProperty
      cellFactory = (_: TableColumn[TableModel, Seq[NounSupport]]) =>
        new TableCell[TableModel, Seq[NounSupport]](new VerbalNounTableCell())
    }
}
