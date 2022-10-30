package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import morphology.graph.model.PartOfSpeechNode

class PartOfSpeechNodeSkin(control: PartOfSpeechNodeView)
    extends LinkSupportSkin[PartOfSpeechNode, PartOfSpeechNodeView](control) {}

object PartOfSpeechNodeSkin {
  def apply(control: PartOfSpeechNodeView): PartOfSpeechNodeSkin = new PartOfSpeechNodeSkin(control)
}
