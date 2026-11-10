package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package full

import arabic.morphologicalengine.generator.model.ConjugationTemplate
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, StringProperty }

class ConjugationTemplateConfigurationEditorView extends Control {

  private[full] val conjugationTemplateProperty = ObjectProperty[ConjugationTemplate](this, "conjugationTemplate")
  private[full] val tagProperty = new StringProperty(this, "tag")

  setSkin(createDefaultSkin())
  tag = ""
  conjugationTemplate = DefaultConjugationTemplate

  tagProperty.onChange { (_, _, nv) =>
    if conjugationTemplate.tag != nv then conjugationTemplate = conjugationTemplate.copy(tag = nv)
  }

  conjugationTemplateProperty.onChange { (_, _, nv) =>
    if Option(nv).isDefined && tag != nv.tag then tag = nv.tag
  }

  def conjugationTemplate: ConjugationTemplate =
    Option(conjugationTemplateProperty.value).getOrElse(DefaultConjugationTemplate)
  def conjugationTemplate_=(value: ConjugationTemplate): Unit = conjugationTemplateProperty.value = value

  def tag: String = tagProperty.value
  def tag_=(value: String): Unit = tagProperty.value = value

  override def createDefaultSkin(): Skin[?] = skin.ConjugationTemplateConfigurationEditorSkin(this)
}

object ConjugationTemplateConfigurationEditorView {
  def apply(): ConjugationTemplateConfigurationEditorView = new ConjugationTemplateConfigurationEditorView()
}
