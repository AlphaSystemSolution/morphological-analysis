package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import arabic.model.ArabicWord
import arabic.morphologicalanalysis.ui.ArabicLabelView
import fx.ui.util.UIUserPreferences
import morphologicalengine.conjugation.model.{ ConjugationTuple, VerbConjugationGroup }
import control.VerbConjugationGroupView

class VerbConjugationGroupViewSkin(control: VerbConjugationGroupView)(using preferences: UIUserPreferences)
    extends ConjugationGroupViewSkin[VerbConjugationGroup, VerbConjugationGroupView](control) {

  import ConjugationGroupViewSkin.{ CellHeight, NumOfColumns }
  import VerbConjugationGroupViewSkin.{ DoubleWidth, NumOfTupleRows }

  // masculineThirdPerson, feminineThirdPerson, masculineSecondPerson, feminineSecondPerson
  private val cells: Array[Array[ArabicLabelView]] =
    Array.tabulate(NumOfTupleRows, NumOfColumns)((_, _) => arabicLabel(ArabicWord()))

  // firstPerson has no dual form: singular is a normal-width cell, plural spans the dual+plural columns.
  private val firstPersonSingular: ArabicLabelView = arabicLabel(ArabicWord())
  private val firstPersonPlural: ArabicLabelView = arabicLabel(ArabicWord(), DoubleWidth, CellHeight)

  initialize()

  override protected def buildRows(): Unit = {
    cells.zipWithIndex.foreach { case (row, rowIndex) =>
      row.zipWithIndex.foreach { case (cell, columnIndex) =>
        gridPane.add(cell, columnIndex, rowIndex + 1)
      }
    }
    val firstPersonRow = NumOfTupleRows + 1
    gridPane.add(firstPersonSingular, 0, firstPersonRow)
    gridPane.add(firstPersonPlural, 1, firstPersonRow, 2, 1)
  }

  override protected def refreshRows(): Unit =
    Option(control.group) match {
      case Some(group) =>
        updateRow(0, group.masculineThirdPerson)
        updateRow(1, group.feminineThirdPerson)
        updateRow(2, Some(group.masculineSecondPerson))
        updateRow(3, Some(group.feminineSecondPerson))
        updateFirstPersonRow(group.firstPerson)
      case None =>
        (0 until NumOfTupleRows).foreach(rowIndex => updateRow(rowIndex, None))
        updateFirstPersonRow(None)
    }

  private def updateRow(rowIndex: Int, tuple: Option[ConjugationTuple]): Unit = {
    val (singular, dual, plural) = tupleWords(tuple)
    cells(rowIndex)(0).label = singular
    cells(rowIndex)(1).label = dual
    cells(rowIndex)(2).label = plural
  }

  private def updateFirstPersonRow(tuple: Option[ConjugationTuple]): Unit = {
    val (singular, _, plural) = tupleWords(tuple)
    firstPersonSingular.label = singular
    firstPersonPlural.label = plural
  }
}

object VerbConjugationGroupViewSkin {

  private val NumOfTupleRows = 4
  private val DoubleWidth = 256.0

  def apply(control: VerbConjugationGroupView)(using preferences: UIUserPreferences): VerbConjugationGroupViewSkin =
    new VerbConjugationGroupViewSkin(control)
}
