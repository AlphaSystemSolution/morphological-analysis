package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import skin.PhraseNodeSkin
import javafx.scene.control.Skin
import morphology.graph.model.{ FontMetaInfo, PhraseNode }

class PhraseNodeView extends LinkSupportView[PhraseNode] {

  override protected val initial: PhraseNode = defaultPhraseNode

  setSkin(createDefaultSkin())

  override protected def updateX(value: Double, src: PhraseNode): PhraseNode =
    src.copy(textPoint = src.textPoint.copy(x = value))

  override protected def updateY(value: Double, src: PhraseNode): PhraseNode =
    src.copy(textPoint = src.textPoint.copy(y = value))

  override protected def updateCx(value: Double, src: PhraseNode): PhraseNode =
    src.copy(circle = src.circle.copy(x = value))

  override protected def updateCy(value: Double, src: PhraseNode): PhraseNode =
    src.copy(circle = src.circle.copy(y = value))

  override protected def updateTranslateX(value: Double, src: PhraseNode): PhraseNode =
    src.copy(translate = src.translate.copy(x = value))

  override protected def updateTranslateY(value: Double, src: PhraseNode): PhraseNode =
    src.copy(translate = src.translate.copy(y = value))

  override protected def updateX1(value: Double, src: PhraseNode): PhraseNode = {
    val line = src.line
    val point = line.p1.copy(x = value)
    src.copy(line = line.copy(p1 = point))
  }

  override protected def updateY1(value: Double, src: PhraseNode): PhraseNode = {
    val line = src.line
    val point = line.p1.copy(y = value)
    src.copy(line = line.copy(p1 = point))
  }

  override protected def updateX2(value: Double, src: PhraseNode): PhraseNode = {
    val line = src.line
    val point = line.p2.copy(x = value)
    src.copy(line = line.copy(p2 = point))
  }

  override protected def updateY2(value: Double, src: PhraseNode): PhraseNode = {
    val line = src.line
    val point = line.p2.copy(y = value)
    src.copy(line = line.copy(p2 = point))
  }

  override protected def updateFont(value: FontMetaInfo, src: PhraseNode): PhraseNode =
    src.copy(font = value)

  override def createDefaultSkin(): Skin[?] = PhraseNodeSkin(this)
}

object PhraseNodeView {
  def apply(): PhraseNodeView = new PhraseNodeView()
}
