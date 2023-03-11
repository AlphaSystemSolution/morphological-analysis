package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import com.alphasystem.arabic.morphologicalengine.generator.model.ConjugationTemplate
import com.alphasystem.arabic.morphologicalengine.ui.control.table.MorphologicalChartTableView
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.beans.value.ObservableValue
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control.ScrollPane
import scalafx.scene.control.cell.CheckBoxTableCell
import scalafx.scene.layout.BorderPane
import scalafx.scene.paint.Color
import scalafx.stage.Screen

class MorphologicalChartSkin(control: MorphologicalChartView) extends SkinBase[MorphologicalChartView](control) {

  import MorphologicalChartSkin.*

  private lazy val tableView = MorphologicalChartTableView(control.conjugationTemplate)

  getChildren.addAll(initializeSkin)

  private def initializeSkin =
    new BorderPane() {
      center = new ScrollPane() {
        content = tableView
        vbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
        hbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
      }
    }

}

object MorphologicalChartSkin {

  def apply(control: MorphologicalChartView): MorphologicalChartSkin = new MorphologicalChartSkin(control)

}
