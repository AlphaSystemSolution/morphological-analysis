package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import com.alphasystem.arabic.morphologicalanalysis.morphology.graph.model.GraphMetaInfo
import com.alphasystem.arabic.morphologicalanalysis.ui.dependencygraph.utils.GraphTool
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.geometry.Pos
import scalafx.scene.Node
import scalafx.scene.layout.{ BorderPane, Pane, Region }
import scalafx.scene.paint.Color

class CanvasSkin(control: CanvasView) extends SkinBase[CanvasView](control) {

  private val styleText = (hex: String) => s"-fx-background-color: $hex"

  private val canvasPane = new Pane() {
    minWidth = Region.USE_PREF_SIZE
    minHeight = Region.USE_PREF_SIZE
    maxWidth = Region.USE_PREF_SIZE
    maxHeight = Region.USE_PREF_SIZE
    prefWidth = control.graphMetaInfo.width
    prefHeight = control.graphMetaInfo.height
    style = styleText(control.graphMetaInfo.toColor.toHex)
  }
  private var gridLines: Option[Node] = None

  getChildren.addAll(initializeSkin)

  private def initializeSkin = {
    control
      .graphMetaInfoProperty
      .onChange((_, _, nv) => {
        canvasPane.prefWidth = nv.width
        canvasPane.prefHeight = nv.height
        canvasPane.style = styleText(nv.toColor.toHex)
        toggleGridLines(nv)
      })

    BorderPane.setAlignment(canvasPane, Pos.TopLeft)
    new BorderPane() {
      center = canvasPane
      minHeight = screenHeight * 0.90
    }
  }

  private def toggleGridLines(graphMetaInfo: GraphMetaInfo): Unit = {
    gridLines.foreach(canvasPane.children.remove(_))
    gridLines = None
    val showGridLines = graphMetaInfo.showGridLines
    if graphMetaInfo.showOutLines || showGridLines then {
      gridLines = Some(
        GraphTool.drawGridLines(
          showGridLines,
          graphMetaInfo.width.intValue(),
          graphMetaInfo.height.intValue()
        )
      )
      canvasPane.children.addOne(gridLines.get)
    }
    canvasPane.requestLayout()
  }
}

object CanvasSkin {
  def apply(control: CanvasView): CanvasSkin = new CanvasSkin(control)
}
