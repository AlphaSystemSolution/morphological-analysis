package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import morphology.graph.model.PhraseNode

class PhraseNodeSkin(control: PhraseNodeView) extends LinkSupportSkin[PhraseNode, PhraseNodeView](control) {}

object PhraseNodeSkin {
  def apply(control: PhraseNodeView) = new PhraseNodeSkin(control)
}
