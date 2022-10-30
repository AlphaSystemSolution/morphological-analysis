package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import morphology.graph.model.{ FontMetaInfo, ReferenceNode }

class ReferenceNodeView extends TerminalNodeSupportView[ReferenceNode] {

  override protected def updateTranslationText(value: String, src: ReferenceNode): ReferenceNode =
    src.copy(translationText = value)
  override protected def updateText(value: String, src: ReferenceNode): ReferenceNode = src.copy(text = value)
  override protected def updateX(value: Double, src: ReferenceNode): ReferenceNode = src.copy(x = value)
  override protected def updateY(value: Double, src: ReferenceNode): ReferenceNode = src.copy(y = value)
  override protected def updateTranslateX(value: Double, src: ReferenceNode): ReferenceNode =
    src.copy(translateX = value)
  override protected def updateTranslateY(value: Double, src: ReferenceNode): ReferenceNode =
    src.copy(translateY = value)
  override protected def updateTranslationX(value: Double, src: ReferenceNode): ReferenceNode =
    src.copy(translationX = value)
  override protected def updateTranslationY(value: Double, src: ReferenceNode): ReferenceNode =
    src.copy(translationY = value)
  override protected def updateX1(value: Double, src: ReferenceNode): ReferenceNode = src.copy(x1 = value)
  override protected def updateY1(value: Double, src: ReferenceNode): ReferenceNode = src.copy(y1 = value)
  override protected def updateX2(value: Double, src: ReferenceNode): ReferenceNode = src.copy(x2 = value)
  override protected def updateY2(value: Double, src: ReferenceNode): ReferenceNode = src.copy(y2 = value)

  override protected def updateTranslationFont(value: FontMetaInfo, src: ReferenceNode): ReferenceNode =
    src.copy(translationFont = value)
  override protected def updateFont(value: FontMetaInfo, src: ReferenceNode): ReferenceNode = src.copy(font = value)
}

object ReferenceNodeView {
  def apply(): ReferenceNodeView = new ReferenceNodeView()
}
