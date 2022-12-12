package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package utils

import morphologicalanalysis.morphology.graph.model.Point
import scalafx.scene.paint.Color
import scalafx.scene.shape.{ Circle, CubicCurve, Line, LineTo, MoveTo, Path, Polyline }
import scalafx.scene.text.{ Font, Text, TextAlignment }

object DrawingTool {

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
      x = x1
      y = y1
    }

  def drawCircle(circleId: String, cx: Double, cy: Double, r: Double, color: Color = Color.Black): Circle =
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

  def arrowPoints(
    t1: Double,
    t2: Double,
    startX: Double,
    startY: Double,
    controlX1: Double,
    controlY1: Double,
    controlX2: Double,
    controlY2: Double,
    endX: Double,
    endY: Double
  ): Array[Double] = {
    val point1 = calculateCurvePoint(t1, startX, startY, controlX1, controlY1, controlX2, controlY2, endX, endY)
    val point2 = calculateCurvePoint(t2, startX, startY, controlX1, controlY1, controlX2, controlY2, endX, endY)
    Array(point2.x, point2.y, point1.x, point2.y + 5d, point1.x, point1.y - 5d)
  }

  private def calculateA(x1: Double, x2: Double, a: Double, b: Double) = x2 - x1 - b - a

  private def calculateB(x1: Double, x2: Double, a: Double) = (3d * (x2 - x1)) - a

  private def calculateC(x1: Double, x2: Double) = 3d * (x2 - x1)

  private def calculateCoordinate(t: Double, x: Double, a: Double, b: Double, c: Double) =
    (a * math.pow(t, 3d)) + (b * math.pow(t, 2d)) + (c * t) + x

  private def calculateCurvePoint(
    t: Double,
    startX: Double,
    startY: Double,
    controlX1: Double,
    controlY1: Double,
    controlX2: Double,
    controlY2: Double,
    endX: Double,
    endY: Double
  ) = {
    val cx = calculateC(startX, controlX1)
    val bx = calculateB(controlX1, controlX2, cx)
    val ax = calculateA(startX, endX, bx, cx)

    val cy = calculateC(startY, controlY1)
    val by = calculateB(controlY1, controlY2, cy)
    val ay = calculateA(startY, endY, by, cy)

    val x = calculateCoordinate(t, startX, ax, bx, cx)
    val y = calculateCoordinate(t, startY, ay, by, cy)
    Point(x, y)
  }
}
