package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import morphology.graph.model.{ FontMetaInfo, HiddenNode }

class HiddenNodeView extends TerminalNodeSupportView[HiddenNode] {

  override protected def updateText(value: String, src: HiddenNode): HiddenNode = src.copy(text = value)
  override protected def updateX(value: Double, src: HiddenNode): HiddenNode = src.copy(x = value)
  override protected def updateY(value: Double, src: HiddenNode): HiddenNode = src.copy(y = value)
  override protected def updateTranslateX(value: Double, src: HiddenNode): HiddenNode = src.copy(translateX = value)
  override protected def updateTranslateY(value: Double, src: HiddenNode): HiddenNode = src.copy(translateY = value)
  override protected def updateTranslationX(value: Double, src: HiddenNode): HiddenNode = src.copy(translationX = value)
  override protected def updateTranslationY(value: Double, src: HiddenNode): HiddenNode = src.copy(translationY = value)
  override protected def updateTranslationFont(value: FontMetaInfo, src: HiddenNode): HiddenNode =
    src.copy(translationFont = value)
  override protected def updateFont(value: FontMetaInfo, src: HiddenNode): HiddenNode = src.copy(font = value)
}
