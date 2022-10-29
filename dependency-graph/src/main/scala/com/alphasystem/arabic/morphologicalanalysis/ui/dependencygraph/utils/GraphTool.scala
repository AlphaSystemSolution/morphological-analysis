package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package utils

import scalafx.scene.paint.Color
import scalafx.scene.shape.{ Circle, CubicCurve, Line, LineTo, MoveTo, Path, Polyline }
import scalafx.scene.text.{ Font, Text, TextAlignment }

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

  def drawLine(
    lineId: String,
    x1: Double,
    y1: Double,
    x2: Double,
    y2: Double,
    strokeColor: Color,
    width: Double
  ): Line =
    new Line() {
      id = lineId
      startX = x1
      startY = y1
      endX = x2
      endY = y2
      stroke = strokeColor
      strokeWidth = width
    }

  def drawDashedLine(
    lineId: String,
    x1: Double,
    y1: Double,
    x2: Double,
    y2: Double,
    strokeColor: Color,
    width: Double
  ): Line =
    new Line() {
      id = lineId
      startX = x1
      startY = y1
      endX = x2
      endY = y2
      stroke = strokeColor
      strokeWidth = width
      fill = strokeColor
      strokeDashArray = Seq(2.0)
    }

  def drawText(
    textId: String,
    value: String,
    x1: Double,
    y1: Double,
    alignment: TextAlignment,
    fillColor: Color,
    textFont: Font
  ): Text =
    new Text() {
      id = textId
      text = value
      textAlignment = alignment
      fill = fillColor
      font = textFont
      if x1 > 0 then x = x1
      if y1 > 0 then y = y1
    }

  def drawCircle(circleId: String, cx: Double, cy: Double, r: Double, color: Color): Circle =
    new Circle() {
      id = circleId
      centerX = cx
      centerY = cy
      radius = r
      stroke = color
      fill = color
    }

  def drawCubicCurve(
    curveId: String,
    x: Double,
    y: Double,
    x1: Double,
    y1: Double,
    x2: Double,
    y2: Double,
    ex: Double,
    ey: Double,
    color: Color
  ): CubicCurve =
    new CubicCurve() {
      id = curveId
      startX = x
      startY = y
      controlX1 = x1
      controlY1 = y1
      controlX2 = x2
      controlY2 = y2
      endX = ex
      endY = ey
      stroke = color
      strokeWidth = 1.0
      fill = Color.Transparent
    }

  def drawPolyline(color: Color, points: Double*): Polyline = {
    val polyline = Polyline(points*)
    polyline.stroke = color
    polyline.fill = color
    polyline.strokeWidth = 1.0
    polyline
  }

  def drawCubicCurveBounds(
    startX: Double,
    startY: Double,
    controlX1: Double,
    controlY1: Double,
    controlX2: Double,
    controlY2: Double,
    endX: Double,
    endY: Double
  ): Path =
    new Path() {
      elements =
        Seq(MoveTo(startX, startY), LineTo(controlX1, controlY1), LineTo(controlX2, controlY2), LineTo(endX, endY))
      stroke = Color.Red
      strokeWidth = 0.5
      strokeDashArray = Seq(2.0)
    }

}
