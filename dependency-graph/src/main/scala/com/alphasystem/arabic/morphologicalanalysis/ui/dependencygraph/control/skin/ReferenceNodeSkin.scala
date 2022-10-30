package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import morphology.graph.model.ReferenceNode

class ReferenceNodeSkin(control: ReferenceNodeView)
    extends TerminalNodeSupportSkin[ReferenceNode, ReferenceNodeView](control) {}

object ReferenceNodeSkin {
  def apply(control: ReferenceNodeView): ReferenceNodeView = new ReferenceNodeView()
}
