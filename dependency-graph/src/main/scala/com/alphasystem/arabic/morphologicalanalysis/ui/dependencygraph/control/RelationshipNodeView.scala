package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import com.alphasystem.arabic.morphologicalanalysis.morphology.graph.model.{ FontMetaInfo, RelationshipNode }

class RelationshipNodeView extends GraphNodeView[RelationshipNode] {

  override protected def updateX(value: Double, src: RelationshipNode): RelationshipNode = src.copy(x = value)
  override protected def updateY(value: Double, src: RelationshipNode): RelationshipNode = src.copy(y = value)
  override protected def updateTranslateX(value: Double, src: RelationshipNode): RelationshipNode =
    src.copy(translateX = value)
  override protected def updateTranslateY(value: Double, src: RelationshipNode): RelationshipNode =
    src.copy(translateY = value)
  override protected def updateFont(value: FontMetaInfo, src: RelationshipNode): RelationshipNode =
    src.copy(font = value)
}
