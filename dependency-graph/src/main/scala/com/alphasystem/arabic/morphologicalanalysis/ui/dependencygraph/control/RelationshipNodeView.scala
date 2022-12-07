package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import skin.RelationshipNodeSkin
import javafx.scene.control.Skin
import morphology.graph.model.{ FontMetaInfo, RelationshipNode }

class RelationshipNodeView extends GraphNodeView[RelationshipNode] {

  override protected val initial: RelationshipNode = defaultRelationshipNode

  setSkin(createDefaultSkin())

  override protected def updateX(value: Double, src: RelationshipNode): RelationshipNode =
    src.copy(textPoint = src.textPoint.copy(x = value))

  override protected def updateY(value: Double, src: RelationshipNode): RelationshipNode =
    src.copy(textPoint = src.textPoint.copy(y = value))

  override protected def updateTranslateX(value: Double, src: RelationshipNode): RelationshipNode =
    src.copy(textPoint = src.translate.copy(x = value))

  override protected def updateTranslateY(value: Double, src: RelationshipNode): RelationshipNode =
    src.copy(textPoint = src.translate.copy(y = value))

  override protected def updateFont(value: FontMetaInfo, src: RelationshipNode): RelationshipNode =
    src.copy(font = value)

  override def createDefaultSkin(): Skin[_] = RelationshipNodeSkin(this)
}

object RelationshipNodeView {
  def apply(): RelationshipNodeView = new RelationshipNodeView()
}
