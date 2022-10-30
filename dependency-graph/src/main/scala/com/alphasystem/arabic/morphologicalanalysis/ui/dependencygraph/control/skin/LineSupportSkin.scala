package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import morphology.graph.model.LineSupport

abstract class LineSupportSkin[N <: LineSupport, C <: LineSupportView[N]](control: C)
    extends GraphNodeSkin[N, C](control) {

  override protected def addProperties(): Unit = {
    super.addProperties()
    addDoubleProperty(control.x1Property, "Line x1:")
    addDoubleProperty(control.y1Property, "Line y1:")
    addDoubleProperty(control.x2Property, "Line x2:")
    addDoubleProperty(control.y2Property, "Line y2:")
  }
}
