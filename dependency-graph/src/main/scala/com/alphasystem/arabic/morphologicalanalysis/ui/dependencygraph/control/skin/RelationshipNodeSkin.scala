package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import morphology.graph.model.RelationshipNodeMetaInfo

class RelationshipNodeSkin(control: RelationshipNodeView)
    extends GraphNodeSkin[RelationshipNodeMetaInfo, RelationshipNodeView](control) {

  getChildren.addAll(initializeSkin)
}

object RelationshipNodeSkin {
  def apply(control: RelationshipNodeView): RelationshipNodeSkin = new RelationshipNodeSkin(control)
}
