package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import morphology.graph.model.LinkSupport
import scalafx.beans.property.DoubleProperty

abstract class LinkSupportView[N <: LinkSupport] extends LineSupportView[N] {

  lazy val cxProperty: DoubleProperty = DoubleProperty(0.0)
  lazy val cyProperty: DoubleProperty = DoubleProperty(0.0)

  // initializations & bindings
  cxProperty.onChange((_, _, nv) => update(nv.doubleValue(), updateCx))
  cyProperty.onChange((_, _, nv) => update(nv.doubleValue(), updateCy))

  // getters & setters
  def cx: Double = cxProperty.value
  def cx_=(value: Double): Unit = cxProperty.value = value

  def cy: Double = cyProperty.value
  def cy_=(value: Double): Unit = cyProperty.value = value

  override protected def initValues(src: N): Unit = {
    super.initValues(src)
    cx = src.cx
    cy = src.cy
  }

  protected def updateCx(value: Double, src: N): N
  protected def updateCy(value: Double, src: N): N
}
