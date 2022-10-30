package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import morphology.graph.model.TerminalNode

class TerminalNodeSkin(control: TerminalNodeView)
    extends TerminalNodeSupportSkin[TerminalNode, TerminalNodeView](control) {}

object TerminalNodeSkin {
  def apply(control: TerminalNodeView): TerminalNodeSkin = new TerminalNodeSkin(control)
}
