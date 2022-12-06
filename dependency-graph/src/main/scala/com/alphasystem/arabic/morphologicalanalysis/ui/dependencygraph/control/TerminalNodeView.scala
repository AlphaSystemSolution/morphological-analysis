package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import skin.TerminalNodeSkin
import javafx.scene.control.Skin
import morphology.graph.model.{ FontMetaInfo, TerminalNodeMetaInfo }
import scalafx.beans.property.{ DoubleProperty, ObjectProperty, StringProperty }

class TerminalNodeView extends LineSupportView[TerminalNodeMetaInfo] {

  override protected val initial: TerminalNodeMetaInfo = defaultTerminalNodeMetaInfo

  lazy val translationTextProperty: StringProperty = StringProperty("")
  lazy val translationXProperty: DoubleProperty = DoubleProperty(0.0)
  lazy val translationYProperty: DoubleProperty = DoubleProperty(0.0)
  lazy val translationFontProperty: ObjectProperty[FontMetaInfo] =
    ObjectProperty[FontMetaInfo](this, "font", defaultEnglishFont)

  // initializations & bindings
  translationXProperty.onChange((_, _, nv) => update(nv.doubleValue(), updateTranslationX))
  translationYProperty.onChange((_, _, nv) => update(nv.doubleValue(), updateTranslationY))
  translationFontProperty.onChange((_, _, nv) => update(nv, updateTranslationFont))

  setSkin(createDefaultSkin())

  // getters & setters
  def translationText: String = translationTextProperty.value
  def translationText_=(value: String): Unit = translationTextProperty.value = value

  def translationX: Double = translationXProperty.value
  def translationX_=(value: Double): Unit = translationXProperty.value = value

  def translationY: Double = translationYProperty.value
  def translationY_=(value: Double): Unit = translationYProperty.value = value

  def translationFont: FontMetaInfo = translationFontProperty.value
  def translationFont_=(value: FontMetaInfo): Unit = translationFontProperty.value = value

  override protected def updateX(value: Double, src: TerminalNodeMetaInfo): TerminalNodeMetaInfo = {
    src.copy(textPoint = src.textPoint.copy(x = value))
  }
  override protected def updateY(value: Double, src: TerminalNodeMetaInfo): TerminalNodeMetaInfo =
    src.copy(textPoint = src.textPoint.copy(y = value))

  override protected def updateTranslateX(value: Double, src: TerminalNodeMetaInfo): TerminalNodeMetaInfo =
    src.copy(translate = src.translate.copy(x = value))

  override protected def updateTranslateY(value: Double, src: TerminalNodeMetaInfo): TerminalNodeMetaInfo =
    src.copy(translate = src.translate.copy(y = value))

  private def updateTranslationX(value: Double, src: TerminalNodeMetaInfo): TerminalNodeMetaInfo =
    src.copy(translationPoint = src.translationPoint.copy(x = value))

  private def updateTranslationY(value: Double, src: TerminalNodeMetaInfo): TerminalNodeMetaInfo =
    src.copy(translationPoint = src.translationPoint.copy(y = value))

  override protected def updateX1(value: Double, src: TerminalNodeMetaInfo): TerminalNodeMetaInfo = {
    val line = src.line
    val point = line.p1.copy(x = value)
    src.copy(line = line.copy(p1 = point))
  }

  override protected def updateY1(value: Double, src: TerminalNodeMetaInfo): TerminalNodeMetaInfo = {
    val line = src.line
    val point = line.p1.copy(y = value)
    src.copy(line = line.copy(p1 = point))
  }

  override protected def updateX2(value: Double, src: TerminalNodeMetaInfo): TerminalNodeMetaInfo = {
    val line = src.line
    val point = line.p2.copy(x = value)
    src.copy(line = line.copy(p2 = point))
  }

  override protected def updateY2(value: Double, src: TerminalNodeMetaInfo): TerminalNodeMetaInfo = {
    val line = src.line
    val point = line.p2.copy(y = value)
    src.copy(line = line.copy(p2 = point))
  }

  private def updateTranslationFont(value: FontMetaInfo, src: TerminalNodeMetaInfo): TerminalNodeMetaInfo =
    src.copy(translationFont = value)

  override protected def updateFont(value: FontMetaInfo, src: TerminalNodeMetaInfo): TerminalNodeMetaInfo =
    src.copy(font = value)

  override def createDefaultSkin(): Skin[_] = TerminalNodeSkin(this)

  override protected def initValues(src: TerminalNodeMetaInfo): Unit = {
    super.initValues(src)
    translationText = src.translationText
    translationX = src.translationPoint.y
    translationY = src.translationPoint.y
    translationFont = src.translationFont
  }
}

object TerminalNodeView {
  def apply(): TerminalNodeView = new TerminalNodeView()
}
