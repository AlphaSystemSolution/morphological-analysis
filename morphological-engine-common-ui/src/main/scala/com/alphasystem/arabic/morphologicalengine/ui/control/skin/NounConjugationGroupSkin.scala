package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import arabic.morphologicalanalysis.ui.ArabicLabelView
import arabic.model.ArabicLetters.WordSpace
import fx.ui.util.UIUserPreferences
import morphologicalengine.conjugation.model.{ ConjugationTuple, NounConjugationGroup }
import control.NounConjugationGroupView

class NounConjugationGroupSkin(control: NounConjugationGroupView)(using preferences: UIUserPreferences)
    extends ConjugationGroupSkin[NounConjugationGroup, NounConjugationGroupView](control) {

  import ConjugationGroupSkin.NumOfColumns

  private val cells = Array.tabulate(NumOfColumns, NumOfColumns)((_, _) => arabicLabel(WordSpace))

  initialize()

  override protected def buildRows(): Unit = {
    println(s">>>> ${cells.length}")
    cells.zipWithIndex.foreach { case (row, rowIndex) =>
      row.zipWithIndex.foreach { case (cell, columnIndex) =>
        gridPane.add(cell, columnIndex, rowIndex + 1)
      }
    }
  }

  override protected def refreshRows(): Unit =
    Option(control.group) match {
      case Some(group) =>
        updateRow(0, group.nominative)
        updateRow(1, group.accusative)
        updateRow(2, group.genitive)
      case None =>
        updateRow(0, null)
        updateRow(1, null)
        updateRow(2, null)
    }

  private def updateRow(rowIndex: Int, tuple: ConjugationTuple): Unit = {
    val (singular, dual, plural) = tupleWords(Option(tuple))
    cells(rowIndex)(0).label = singular
    cells(rowIndex)(1).label = dual
    cells(rowIndex)(2).label = plural
  }
}

object NounConjugationGroupSkin {
  def apply(control: NounConjugationGroupView)(using preferences: UIUserPreferences): NounConjugationGroupSkin =
    new NounConjugationGroupSkin(control)
}
