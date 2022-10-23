package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import scalafx.Includes.*
import scalafx.scene.layout.{ Pane, Region }

class CanvasPane extends Pane {

  private val canvasPane = new Pane() {
    minWidth = Region.USE_PREF_SIZE
    minHeight = Region.USE_PREF_SIZE
    maxWidth = Region.USE_PREF_SIZE
    maxHeight = Region.USE_PREF_SIZE
    prefWidth = 1100
    prefHeight = 800
  }

  children = Seq(canvasPane)

}

object CanvasPane {

  def apply(): CanvasPane = new CanvasPane()
}
