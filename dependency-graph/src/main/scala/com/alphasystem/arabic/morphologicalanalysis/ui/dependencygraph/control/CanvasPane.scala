package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import fx.ui.util.*
import scalafx.Includes.*
import scalafx.scene.layout.{ Pane, Region }
import scalafx.scene.paint.Color
import scalafx.stage.Screen

class CanvasPane extends Pane {

  private val bounds = Screen.primary.visualBounds

  private val canvasPane = new Pane() {
    minWidth = Region.USE_PREF_SIZE
    minHeight = Region.USE_PREF_SIZE
    maxWidth = Region.USE_PREF_SIZE
    maxHeight = Region.USE_PREF_SIZE
    prefWidth = bounds.width
    prefHeight = 600
    style = s"-fx-background-color: ${Color.Beige.toHex}"
  }

  prefWidth = bounds.width
  prefHeight = bounds.height
  style = s"-fx-background-color: ${Color.White.toHex}"
  children = Seq(canvasPane)

}

object CanvasPane {

  def apply(): CanvasPane = new CanvasPane()
}
