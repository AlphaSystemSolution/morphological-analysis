package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import morphology.graph.model.{ FontMetaInfo, PartOfSpeechNode }

class PartOfSpeechNodeView extends LinkSupportView[PartOfSpeechNode] {

  override protected def updateX(value: Double, src: PartOfSpeechNode): PartOfSpeechNode = src.copy(x = value)
  override protected def updateY(value: Double, src: PartOfSpeechNode): PartOfSpeechNode = src.copy(y = value)
  override protected def updateCx(value: Double, src: PartOfSpeechNode): PartOfSpeechNode = src.copy(cx = value)
  override protected def updateCy(value: Double, src: PartOfSpeechNode): PartOfSpeechNode = src.copy(cy = value)
  override protected def updateTranslateX(value: Double, src: PartOfSpeechNode): PartOfSpeechNode =
    src.copy(translateX = value)
  override protected def updateTranslateY(value: Double, src: PartOfSpeechNode): PartOfSpeechNode =
    src.copy(translateY = value)
  override protected def updateFont(value: FontMetaInfo, src: PartOfSpeechNode): PartOfSpeechNode =
    src.copy(font = value)
}
