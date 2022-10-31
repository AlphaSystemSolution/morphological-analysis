package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import com.alphasystem.arabic.morphologicalanalysis.ui.dependencygraph.control.skin.PhraseNodeSkin
import javafx.scene.control.Skin
import morphology.graph.model.{ FontMetaInfo, PhraseNode }

class PhraseNodeView extends LinkSupportView[PhraseNode] {

  override protected val initial: PhraseNode = defaultPhraseNode

  setSkin(createDefaultSkin())

  override protected def updateText(value: String, src: PhraseNode): PhraseNode = src.copy(text = value)
  override protected def updateX(value: Double, src: PhraseNode): PhraseNode = src.copy(x = value)
  override protected def updateY(value: Double, src: PhraseNode): PhraseNode = src.copy(y = value)
  override protected def updateCx(value: Double, src: PhraseNode): PhraseNode = src.copy(cx = value)
  override protected def updateCy(value: Double, src: PhraseNode): PhraseNode = src.copy(cy = value)
  override protected def updateTranslateX(value: Double, src: PhraseNode): PhraseNode = src.copy(translateX = value)
  override protected def updateTranslateY(value: Double, src: PhraseNode): PhraseNode = src.copy(translateY = value)
  override protected def updateX1(value: Double, src: PhraseNode): PhraseNode = src.copy(x1 = value)
  override protected def updateY1(value: Double, src: PhraseNode): PhraseNode = src.copy(y1 = value)
  override protected def updateX2(value: Double, src: PhraseNode): PhraseNode = src.copy(x2 = value)
  override protected def updateY2(value: Double, src: PhraseNode): PhraseNode = src.copy(y2 = value)
  override protected def updateFont(value: FontMetaInfo, src: PhraseNode): PhraseNode = src.copy(font = value)

  override def createDefaultSkin(): Skin[_] = PhraseNodeSkin(this)
}

object PhraseNodeView {
  def apply(): PhraseNodeView = new PhraseNodeView()
}
