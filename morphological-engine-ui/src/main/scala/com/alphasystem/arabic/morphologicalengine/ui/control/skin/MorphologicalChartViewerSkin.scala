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
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Label, ScrollPane}
import scalafx.scene.layout.{BorderPane, HBox}

class MorphologicalChartViewerSkin(control: MorphologicalChartViewerView)
    extends SkinBase[MorphologicalChartViewerView](control) {

  private val errorLabel = new Label {
    wrapText = true
  }

  private val contentBox = new HBox {
    padding = Insets(12)
    spacing = 10
    children = Seq()
  }

  private val scrollPane = new ScrollPane {
    content = contentBox
    fitToWidth = true
    fitToHeight = false
    vbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
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
            contentBox.children.clear()

            chart.detailedConjugation match {
              case Some(detailedConjugation) => addDetailedConjugationPane(detailedConjugation)
              case _                         => // do nothing
            }

            root.center = scrollPane
            BorderPane.setAlignment(scrollPane, Pos.Center)

          case _ =>
            errorLabel.text = "No conjugation found for given root letters and family!"
            root.center = errorLabel
        }
    }

  private def addDetailedConjugationPane(detailedConjugation: DetailedConjugation): Unit = {
    val view = DetailedConjugationView()
    view.setMaxWidth(Double.MaxValue)
    view.setMaxHeight(Double.MaxValue)
    view.detailedConjugation = detailedConjugation
    contentBox.children.add(view)
  }
}

object MorphologicalChartViewerSkin {
  def apply(control: MorphologicalChartViewerView): MorphologicalChartViewerSkin =
    new MorphologicalChartViewerSkin(control)
}
