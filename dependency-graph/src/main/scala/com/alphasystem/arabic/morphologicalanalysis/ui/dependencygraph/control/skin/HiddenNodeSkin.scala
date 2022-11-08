package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import morphology.graph.model.HiddenNode

class HiddenNodeSkin(control: HiddenNodeView) extends TerminalNodeSupportSkin[HiddenNode, HiddenNodeView](control) {

  getChildren.addAll(initializeSkin)
}

object HiddenNodeSkin {
  def apply(control: HiddenNodeView): HiddenNodeSkin = new HiddenNodeSkin(control)
}
