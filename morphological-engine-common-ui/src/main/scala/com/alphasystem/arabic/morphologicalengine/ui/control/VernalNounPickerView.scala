package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import com.alphasystem.arabic.fx.ui.util.UIUserPreferences
import com.alphasystem.arabic.morphologicalengine.conjugation.forms.{Form, NounSupport}
import com.alphasystem.arabic.morphologicalengine.conjugation.model.NamedTemplate
import javafx.scene.control.{Control, Skin}
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.text.Font

class VernalNounPickerView(using preferences: UIUserPreferences) extends Control {

  private[control] val verbalNounsProperty: ObservableBuffer[NounSupport] = ObservableBuffer.empty[NounSupport]

  private[control] val namedTemplateProperty: ObjectProperty[NamedTemplate] = ObjectProperty(this, "namedTemplate")

  private[control] val fontProperty: ObjectProperty[Font] = ObjectProperty[Font](this, "font", preferences.arabicFont)

  namedTemplateProperty.onChange((_, _, nv) => updateVerbalNouns(nv))

  setSkin(createDefaultSkin())

  def verbalNouns: Seq[NounSupport] = verbalNounsProperty.toSeq

  def namedTemplate: NamedTemplate = namedTemplateProperty.value
  def namedTemplate_=(value: NamedTemplate): Unit = namedTemplateProperty.value = value

  def font: Font = fontProperty.value
  def font_=(value: Font): Unit = fontProperty.value = value

  private def updateVerbalNouns(namedTemplate: NamedTemplate) = {
    verbalNounsProperty.clear()
    verbalNounsProperty.addAll(Form.fromNamedTemplate(namedTemplate).verbalNouns)
  }

  override def createDefaultSkin(): Skin[?] = skin.VernalNounPickerSkin(this)
}

object VernalNounPickerView {
  def apply()(using preferences: UIUserPreferences): VernalNounPickerView = new VernalNounPickerView()
}