package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import control.MorphologicalChartViewerView
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.control.{ Label, ScrollPane }
import scalafx.scene.layout.{ BorderPane, VBox }

class MorphologicalChartViewerSkin(control: MorphologicalChartViewerView)
    extends SkinBase[MorphologicalChartViewerView](control) {

  private val abbreviatedConjugationView = AbbreviatedConjugationView()
  private val detailedConjugationView = DetailedConjugationView()
  private val errorLabel = new Label {
    wrapText = true
  }

  private val contentBox = new VBox {
    padding = Insets(12)
    spacing = 12
    children.addAll(abbreviatedConjugationView, detailedConjugationView)
  }

  private val scrollPane = new ScrollPane {
    content = contentBox
    fitToWidth = true
    fitToHeight = false
    vbarPolicy = ScrollPane.ScrollBarPolicy.Always
    hbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
  }

  private val root = new BorderPane {
    center = scrollPane
  }

  control.morphologicalChartProperty.onChange((_, _, _) => refresh())
  control.errorProperty.onChange((_, _, _) => refresh())

  getChildren.add(root)
  refresh()

  private def refresh(): Unit =
    control.error match {
      case Some(message) =>
        errorLabel.text = message
        root.center = errorLabel

      case None =>
        control.morphologicalChart match {
          case Some(chart) =>
            chart.abbreviatedConjugation match {
              case Some(abbreviatedConjugation) =>
                abbreviatedConjugationView.conjugationHeader = chart.conjugationHeader
                abbreviatedConjugationView.abbreviatedConjugation = abbreviatedConjugation
              case None => // do nothing
            }

            chart.detailedConjugation match {
              case Some(detailedConjugation) => detailedConjugationView.detailedConjugation = detailedConjugation
              case _                         => // do nothing
            }

            root.center = scrollPane
            BorderPane.setAlignment(scrollPane, Pos.Center)

          case _ =>
            errorLabel.text = "No conjugation found for given root letters and family!"
            root.center = errorLabel
        }
    }
}

object MorphologicalChartViewerSkin {
  def apply(control: MorphologicalChartViewerView): MorphologicalChartViewerSkin =
    new MorphologicalChartViewerSkin(control)
}
