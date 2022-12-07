package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import morphology.graph.model.LineSupport
import scalafx.beans.property.DoubleProperty

abstract class LineSupportView[N <: LineSupport] extends GraphNodeView[N] {

  lazy val x1Property: DoubleProperty = DoubleProperty(0.0)
  lazy val y1Property: DoubleProperty = DoubleProperty(0.0)
  lazy val x2Property: DoubleProperty = DoubleProperty(0.0)
  lazy val y2Property: DoubleProperty = DoubleProperty(0.0)

  // initializations & bindings
  x1Property.onChange((_, _, nv) => update(nv.doubleValue(), updateX1))
  y1Property.onChange((_, _, nv) => update(nv.doubleValue(), updateY1))
  x2Property.onChange((_, _, nv) => update(nv.doubleValue(), updateX2))
  y2Property.onChange((_, _, nv) => update(nv.doubleValue(), updateY2))

  // getters & setters
  def x1: Double = x1Property.value
  def x1_=(value: Double): Unit = x1Property.value = value

  def y1: Double = y1Property.value
  def y1_=(value: Double): Unit = y1Property.value = value

  def x2: Double = x2Property.value
  def x2_=(value: Double): Unit = x2Property.value = value

  def y2: Double = y2Property.value
  def y2_=(value: Double): Unit = y2Property.value = value

  override protected def initValues(src: N): Unit = {
    super.initValues(src)
    x1 = src.line.p1.x
    y1 = src.line.p1.y
    x2 = src.line.p2.x
    y2 = src.line.p2.y
  }

  protected def updateX1(value: Double, src: N): N
  protected def updateY1(value: Double, src: N): N
  protected def updateX2(value: Double, src: N): N
  protected def updateY2(value: Double, src: N): N
}
