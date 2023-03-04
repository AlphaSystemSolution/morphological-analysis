package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import morphologicalengine.generator.model.ConjugationTemplate
import skin.MorphologicalChartSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.ObjectProperty

import java.nio.file.Paths

class MorphologicalChartView extends Control {

  private val defaultTemplate = toConjugationTemplate(Paths.get("sample.json"))

  private val conjugationTemplateProperty =
    ObjectProperty[ConjugationTemplate](this, "conjugationTemplate", defaultTemplate)

  setSkin(createDefaultSkin())

  def conjugationTemplate: ConjugationTemplate = conjugationTemplateProperty.value
  def conjugationTemplate_=(value: ConjugationTemplate): Unit =
    conjugationTemplateProperty.value = if Option(value).isEmpty then defaultTemplate else value

  override def createDefaultSkin(): Skin[_] = MorphologicalChartSkin(this)
}

object MorphologicalChartView {
  def apply(): MorphologicalChartView = new MorphologicalChartView()
}
