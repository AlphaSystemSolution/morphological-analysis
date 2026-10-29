package com.alphasystem
package arabic
package examples_generator
package ui
package control
package morphological_chart
package skin

import javafx.scene.control.SkinBase

class MorphologicalChartFileGeneratorSkin(control: MorphologicalChartFileGeneratorView)
    extends SkinBase[MorphologicalChartFileGeneratorView](control) {}

object MorphologicalChartFileGeneratorSkin {
  def apply(control: MorphologicalChartFileGeneratorView): MorphologicalChartFileGeneratorSkin =
    new MorphologicalChartFileGeneratorSkin(control)
}
