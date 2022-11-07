package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import morphology.graph.model.LineSupport
import scalafx.geometry.Insets
import scalafx.scene.control.TitledPane
import scalafx.scene.layout.GridPane

abstract class LineSupportSkin[N <: LineSupport, C <: LineSupportView[N]](control: C)
    extends GraphNodeSkin[N, C](control) {

  private def createLinesProperties = {
    rowIndex = 0
    val gridPane: GridPane =
      new GridPane() {
        vgap = DefaultGap
        hgap = DefaultGap
        padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
      }

    addDoubleProperty(control.x1Property, screenWidth, "Line x1:", gridPane)
    addDoubleProperty(control.y1Property, screenHeight, "Line y1:", gridPane)
    addDoubleProperty(control.x2Property, screenWidth, "Line x2:", gridPane)
    addDoubleProperty(control.y2Property, screenHeight, "Line y2:", gridPane)
    createTitledPane("Line Properties:", gridPane)
  }

  override protected def addProperties(): Seq[TitledPane] = super.addProperties() :+ createLinesProperties
}
