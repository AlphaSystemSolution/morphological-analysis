package com.alphasystem
package arabic
package examples_generator
package ui
package control
package morphological_chart

import javafx.scene.control.{ Control, Skin }

class MorphologicalChartView extends Control {

  setSkin(skin.MorphologicalChartSkin(this))

  override def createDefaultSkin(): Skin[?] = skin.MorphologicalChartSkin(this)
}

object MorphologicalChartView {
  def apply(): MorphologicalChartView = new MorphologicalChartView()
}
