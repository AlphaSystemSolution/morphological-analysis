package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import morphologicalengine.generator.model.ConjugationTemplate
import control.table.MorphologicalChartTableView
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

  private lazy val tableView = MorphologicalChartTableView(control.conjugationTemplate)

  control
    .actionProperty
    .onChange((_, _, nv) => {
      nv match
        case TableAction.None      => // do nothing
        case TableAction.Add       => tableView.addRow()
        case TableAction.Remove    => tableView.removeRows()
        case TableAction.Duplicate => tableView.duplicateRows()
    })

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
