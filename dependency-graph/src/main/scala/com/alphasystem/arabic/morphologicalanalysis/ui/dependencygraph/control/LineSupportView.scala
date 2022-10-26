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
  def x1: Double = xProperty.value
  def x1_=(value: Double): Unit = xProperty.value = value

  def y1: Double = yProperty.value
  def y1_=(value: Double): Unit = yProperty.value = value

  def x2: Double = xProperty.value
  def x2_=(value: Double): Unit = xProperty.value = value

  def y2: Double = yProperty.value
  def y2_=(value: Double): Unit = yProperty.value = value

  override protected def initValues(src: N): Unit = {
    super.initValues(src)
    x1 = src.y1
    y1 = src.y1
    x2 = src.x2
    y2 = src.y2
  }

  protected def updateX1(value: Double, src: N): N
  protected def updateY1(value: Double, src: N): N
  protected def updateX2(value: Double, src: N): N
  protected def updateY2(value: Double, src: N): N
}