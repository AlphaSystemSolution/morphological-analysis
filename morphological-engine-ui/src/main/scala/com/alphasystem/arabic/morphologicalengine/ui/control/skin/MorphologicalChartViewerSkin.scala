package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import com.alphasystem.arabic.morphologicalengine.conjugation.model.DetailedConjugation
import com.alphasystem.arabic.morphologicalengine.ui.control.MorphologicalChartViewerView
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.geometry.Pos
import scalafx.scene.control.Label
import scalafx.scene.layout.BorderPane

class MorphologicalChartViewerSkin(control: MorphologicalChartViewerView)
    extends SkinBase[MorphologicalChartViewerView](control) {

  private val root = new BorderPane()

  control.morphologicalChartProperty.onChange((_, _, _) => refresh())
  control.errorProperty.onChange((_, _, _) => refresh())

  getChildren.add(root)
  refresh()

  private def refresh(): Unit =
    control.error match {
      case Some(message) =>
        root.center = new Label(message) { wrapText = true }

      case None =>
        control.morphologicalChart match {
          case None =>
            root.center = new Label("Select a row to preview its morphological chart.")

          case Some(chart) =>
            chart.detailedConjugation match {
              case Some(detailed) => addDetailedConjugationPane(detailed)
              case None           => root.center = null
            }
        }
    }

  private def addDetailedConjugationPane(detailedConjugation: DetailedConjugation) = {
    val view = DetailedConjugationView()
    view.setMaxWidth(Double.MaxValue)
    view.setMaxHeight(Double.MaxValue)
    view.detailedConjugation = detailedConjugation
    root.center = view
    BorderPane.setAlignment(view, Pos.Center)
  }
}

object MorphologicalChartViewerSkin {
  def apply(control: MorphologicalChartViewerView): MorphologicalChartViewerSkin =
    new MorphologicalChartViewerSkin(control)
}
