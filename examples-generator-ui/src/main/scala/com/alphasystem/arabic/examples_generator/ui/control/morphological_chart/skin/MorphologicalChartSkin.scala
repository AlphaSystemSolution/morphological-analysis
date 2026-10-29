package com.alphasystem
package arabic
package examples_generator
package ui
package control
package morphological_chart
package skin

import javafx.scene.control.SkinBase

class MorphologicalChartSkin(control: MorphologicalChartView) extends SkinBase[MorphologicalChartView](control) {}

object MorphologicalChartSkin {
  def apply(control: MorphologicalChartView): MorphologicalChartSkin = new MorphologicalChartSkin(control)
}
