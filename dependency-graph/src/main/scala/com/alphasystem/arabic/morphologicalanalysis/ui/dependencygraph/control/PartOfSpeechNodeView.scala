package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import skin.PartOfSpeechNodeSkin
import javafx.scene.control.Skin
import morphology.graph.model.{ FontMetaInfo, PartOfSpeechNodeMetaInfo }

class PartOfSpeechNodeView extends LinkSupportView[PartOfSpeechNodeMetaInfo] {

  override protected val initial: PartOfSpeechNodeMetaInfo = defaultPartOfSpeechNodeMetaInfo

  setSkin(createDefaultSkin())

  override protected def updateX(value: Double, src: PartOfSpeechNodeMetaInfo): PartOfSpeechNodeMetaInfo =
    src.copy(textPoint = src.textPoint.copy(x = value))

  override protected def updateY(value: Double, src: PartOfSpeechNodeMetaInfo): PartOfSpeechNodeMetaInfo =
    src.copy(textPoint = src.textPoint.copy(y = value))

  override protected def updateCx(value: Double, src: PartOfSpeechNodeMetaInfo): PartOfSpeechNodeMetaInfo =
    src.copy(textPoint = src.circle.copy(x = value))

  override protected def updateCy(value: Double, src: PartOfSpeechNodeMetaInfo): PartOfSpeechNodeMetaInfo =
    src.copy(textPoint = src.circle.copy(y = value))

  override protected def updateTranslateX(value: Double, src: PartOfSpeechNodeMetaInfo): PartOfSpeechNodeMetaInfo =
    src.copy(textPoint = src.translate.copy(x = value))

  override protected def updateTranslateY(value: Double, src: PartOfSpeechNodeMetaInfo): PartOfSpeechNodeMetaInfo =
    src.copy(textPoint = src.translate.copy(y = value))

  override protected def updateX1(value: Double, src: PartOfSpeechNodeMetaInfo): PartOfSpeechNodeMetaInfo = src
  override protected def updateY1(value: Double, src: PartOfSpeechNodeMetaInfo): PartOfSpeechNodeMetaInfo = src
  override protected def updateX2(value: Double, src: PartOfSpeechNodeMetaInfo): PartOfSpeechNodeMetaInfo = src
  override protected def updateY2(value: Double, src: PartOfSpeechNodeMetaInfo): PartOfSpeechNodeMetaInfo = src
  override protected def updateFont(value: FontMetaInfo, src: PartOfSpeechNodeMetaInfo): PartOfSpeechNodeMetaInfo =
    src.copy(font = value)

  override def createDefaultSkin(): Skin[_] = PartOfSpeechNodeSkin(this)
}

object PartOfSpeechNodeMetaInfoView {
  def apply(): PartOfSpeechNodeView = new PartOfSpeechNodeView()
}
