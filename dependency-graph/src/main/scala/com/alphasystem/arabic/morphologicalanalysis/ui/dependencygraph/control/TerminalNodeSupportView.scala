package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import morphology.graph.model.{ FontMetaInfo, TerminalNodeSupport }
import scalafx.beans.property.{ DoubleProperty, ObjectProperty }

abstract class TerminalNodeSupportView[N <: TerminalNodeSupport] extends GraphNodeView[N] {

  lazy val translationXProperty: DoubleProperty = DoubleProperty(0.0)
  lazy val translationYProperty: DoubleProperty = DoubleProperty(0.0)
  lazy val translationFontProperty: ObjectProperty[FontMetaInfo] = ObjectProperty[FontMetaInfo](this, "font")

  // initializations & bindings
  translationXProperty.onChange((_, _, nv) => update(nv.doubleValue(), updateTranslationX))
  translationYProperty.onChange((_, _, nv) => update(nv.doubleValue(), updateTranslationY))
  translationFontProperty.onChange((_, _, nv) => update(nv, updateTranslationFont))

  // getters & setters
  def translationX: Double = translationXProperty.value
  def translationX_=(value: Double): Unit = translationXProperty.value = value

  def translationY: Double = translationYProperty.value
  def translationY_=(value: Double): Unit = translationYProperty.value = value

  def translationFont: FontMetaInfo = translationFontProperty.value
  def translationFont_=(value: FontMetaInfo): Unit = translationFontProperty.value = value

  override protected def initValues(src: N): Unit = {
    super.initValues(src)
    translationX = src.translationX
    translationY = src.translationY
    translationFont = src.translationFont
  }

  protected def updateTranslationX(value: Double, src: N): N
  protected def updateTranslationY(value: Double, src: N): N
  protected def updateTranslationFont(value: FontMetaInfo, src: N): N
}
