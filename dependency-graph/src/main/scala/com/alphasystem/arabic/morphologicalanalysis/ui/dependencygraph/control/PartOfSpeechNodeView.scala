package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import skin.PartOfSpeechNodeSkin
import javafx.scene.control.Skin
import morphology.graph.model.{ FontMetaInfo, PartOfSpeechNode, TerminalNode }
import scalafx.beans.property.ObjectProperty

class PartOfSpeechNodeView extends LinkSupportView[PartOfSpeechNode] {

  override protected val initial: PartOfSpeechNode = defaultPartOfSpeechNode

  private val terminalNodeProperty = ObjectProperty[TerminalNode](this, "terminalNodeId")

  setSkin(createDefaultSkin())

  def terminalNode: TerminalNode = terminalNodeProperty.value
  def terminalNode_=(value: TerminalNode): Unit = terminalNodeProperty.value = value

  override protected def updateX(value: Double, src: PartOfSpeechNode): PartOfSpeechNode =
    src.copy(textPoint = src.textPoint.copy(x = value))

  override protected def updateY(value: Double, src: PartOfSpeechNode): PartOfSpeechNode =
    src.copy(textPoint = src.textPoint.copy(y = value))

  override protected def updateCx(value: Double, src: PartOfSpeechNode): PartOfSpeechNode =
    src.copy(circle = src.circle.copy(x = value))

  override protected def updateCy(value: Double, src: PartOfSpeechNode): PartOfSpeechNode =
    src.copy(circle = src.circle.copy(y = value))

  override protected def updateTranslateX(value: Double, src: PartOfSpeechNode): PartOfSpeechNode =
    src.copy(translate = src.translate.copy(x = value))

  override protected def updateTranslateY(value: Double, src: PartOfSpeechNode): PartOfSpeechNode =
    src.copy(translate = src.translate.copy(y = value))

  override protected def updateX1(value: Double, src: PartOfSpeechNode): PartOfSpeechNode = src
  override protected def updateY1(value: Double, src: PartOfSpeechNode): PartOfSpeechNode = src
  override protected def updateX2(value: Double, src: PartOfSpeechNode): PartOfSpeechNode = src
  override protected def updateY2(value: Double, src: PartOfSpeechNode): PartOfSpeechNode = src
  override protected def updateFont(value: FontMetaInfo, src: PartOfSpeechNode): PartOfSpeechNode =
    src.copy(font = value)

  override def createDefaultSkin(): Skin[?] = PartOfSpeechNodeSkin(this)
}

object PartOfSpeechNodeMetaInfoView {
  def apply(): PartOfSpeechNodeView = new PartOfSpeechNodeView()
}
