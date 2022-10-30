package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import morphology.graph.model.{ FontMetaInfo, TerminalNode }

class TerminalNodeView extends TerminalNodeSupportView[TerminalNode] {

  override protected val initial: TerminalNode = defaultTerminalNode

  override protected def updateTranslationText(value: String, src: TerminalNode): TerminalNode =
    src.copy(translationText = value)
  override protected def updateText(value: String, src: TerminalNode): TerminalNode = src.copy(text = value)
  override protected def updateX(value: Double, src: TerminalNode): TerminalNode = src.copy(x = value)
  override protected def updateY(value: Double, src: TerminalNode): TerminalNode = src.copy(y = value)
  override protected def updateTranslateX(value: Double, src: TerminalNode): TerminalNode = src.copy(translateX = value)
  override protected def updateTranslateY(value: Double, src: TerminalNode): TerminalNode = src.copy(translateY = value)
  override protected def updateTranslationX(value: Double, src: TerminalNode): TerminalNode =
    src.copy(translationX = value)
  override protected def updateTranslationY(value: Double, src: TerminalNode): TerminalNode =
    src.copy(translationY = value)
  override protected def updateX1(value: Double, src: TerminalNode): TerminalNode = src.copy(x1 = value)
  override protected def updateY1(value: Double, src: TerminalNode): TerminalNode = src.copy(y1 = value)
  override protected def updateX2(value: Double, src: TerminalNode): TerminalNode = src.copy(x2 = value)
  override protected def updateY2(value: Double, src: TerminalNode): TerminalNode = src.copy(y2 = value)
  override protected def updateTranslationFont(value: FontMetaInfo, src: TerminalNode): TerminalNode =
    src.copy(translationFont = value)
  override protected def updateFont(value: FontMetaInfo, src: TerminalNode): TerminalNode = src.copy(font = value)
}

object TerminalNodeView {
  def apply(): TerminalNodeView = new TerminalNodeView()
}
