package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import morphology.graph.model.LinkSupport

abstract class LinkSupportSkin[N <: LinkSupport, C <: LinkSupportView[N]](control: C)
    extends LineSupportSkin[N, C](control) {

  override protected def addProperties(): Unit = {
    super.addProperties()
    addDoubleProperty(control.cxProperty, "Circle x:")
    addDoubleProperty(control.cyProperty, "Circle y:")
  }
}
