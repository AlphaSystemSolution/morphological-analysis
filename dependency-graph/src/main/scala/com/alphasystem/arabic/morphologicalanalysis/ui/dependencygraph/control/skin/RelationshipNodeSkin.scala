package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import morphology.graph.model.RelationshipNode

class RelationshipNodeSkin(control: RelationshipNodeView)
    extends GraphNodeSkin[RelationshipNode, RelationshipNodeView](control) {}

object RelationshipNodeSkin {
  def apply(control: RelationshipNodeView): RelationshipNodeSkin = new RelationshipNodeSkin(control)
}
