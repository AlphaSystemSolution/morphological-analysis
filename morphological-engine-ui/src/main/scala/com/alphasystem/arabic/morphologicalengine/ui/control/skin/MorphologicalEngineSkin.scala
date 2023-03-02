package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import scalafx.Includes.*
import javafx.scene.control.SkinBase
import scalafx.scene.control.TabPane
import scalafx.scene.layout.BorderPane

class MorphologicalEngineSkin(control: MorphologicalEngineView) extends SkinBase[MorphologicalEngineView](control) {

  private val tabPane: TabPane = new TabPane()

  getChildren.addAll(initializeSkin)

  private def initializeSkin = {

    new BorderPane()
  }
}

object MorphologicalEngineSkin {
  def apply(control: MorphologicalEngineView) = new MorphologicalEngineSkin(control)
}
