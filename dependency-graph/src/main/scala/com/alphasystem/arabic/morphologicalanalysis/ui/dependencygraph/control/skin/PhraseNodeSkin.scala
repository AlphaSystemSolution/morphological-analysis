package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import morphology.graph.model.PhraseNodeMetaInfo

class PhraseNodeSkin(control: PhraseNodeView) extends LinkSupportSkin[PhraseNodeMetaInfo, PhraseNodeView](control) {

  getChildren.addAll(initializeSkin)
}

object PhraseNodeSkin {
  def apply(control: PhraseNodeView) = new PhraseNodeSkin(control)
}
