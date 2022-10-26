package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package utils

import scalafx.scene.paint.Color
import scalafx.scene.shape.{ LineTo, MoveTo, Path }

object GraphTool {

  def drawGridLines(showGridLines: Boolean, width: Int, height: Int, step: Int = 20): Path = {
    val gridLines = new Path() {
      id = "gridLines"
      stroke = Color.web("#B6B6B4")
      strokeWidth = 1.0
    }

    val elements = gridLines.elements

    // start drawing outlines first
    var x1 = 0
    var y1 = 0
    var x2 = 0
    var y2 = height
    elements.addAll(MoveTo(x1, y1), LineTo(x2, y2))

    x2 = width
    y2 = 0
    elements.addAll(MoveTo(x1, y1), LineTo(x2, y2))

    y1 = height
    y2 = height
    elements.addAll(MoveTo(x1, y1), LineTo(x2, y2))

    x1 = width
    y1 = 0
    elements.addAll(MoveTo(x1, y1), LineTo(x2, y2))

    if showGridLines then {
      // horizontal lines
      x1 = 0
      x2 = width
      (step until height by step).foreach { value =>
        y1 = value
        y2 = value
        elements.addAll(MoveTo(x1, y1), LineTo(x2, y2))
      }

      // vertical lines
      y1 = 0
      y2 = height
      (step until width by step).foreach { value =>
        x1 = value
        x2 = value
        elements.addAll(MoveTo(x1, y1), LineTo(x2, y2))
      }
    }

    gridLines
  }
}
