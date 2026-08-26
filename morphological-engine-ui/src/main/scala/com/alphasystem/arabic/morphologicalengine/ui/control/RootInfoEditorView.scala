package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import com.alphasystem.arabic.model.ArabicLetterType
import com.alphasystem.arabic.morphologicalengine.asciidoc_generator.RootInfo
import com.alphasystem.arabic.morphologicalengine.conjugation.forms.{ Form, NounSupport }
import com.alphasystem.arabic.morphologicalengine.conjugation.forms.noun.VerbalNoun
import com.alphasystem.arabic.morphologicalengine.conjugation.model.{
  ConjugationConfiguration,
  NamedTemplate,
  RootLetters
}
import com.alphasystem.arabic.morphologicalengine.ui.control.skin.RootInfoEditorSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, StringProperty }
import scalafx.collections.{ ObservableBuffer, ObservableSet }

class RootInfoEditorView extends Control {

  import RootInfoEditorView.*

  private[control] val rootLettersProperty = ObjectProperty[RootLetters](this, "rootLetters", DefaultRootLetters)
  private[control] val familyProperty =
    ObjectProperty[NamedTemplate](this, "family", NamedTemplate.FormICategoryAGroupATemplate)
  private[control] val baseTranslationProperty = new StringProperty(this, "baseTranslation")
  private[control] val translationsProperty = ObservableSet.empty[String]
  private[control] val verbalNounsProperty: ObservableBuffer[NounSupport] = ObservableBuffer.empty[NounSupport]

  setSkin(createDefaultSkin())

  def rootLetters: RootLetters = rootLettersProperty.value
  def rootLetters_=(value: RootLetters): Unit = rootLettersProperty.value = value

  def family: NamedTemplate = familyProperty.value
  private[control] def family_=(value: NamedTemplate): Unit = familyProperty.value = value

  def baseTranslation: String = baseTranslationProperty.value
  private def baseTranslation_=(value: String): Unit = baseTranslationProperty.value = value

  def verbalNouns: Seq[NounSupport] = verbalNounsProperty.toSeq

  familyProperty.onChange((_, _, nv) => {
    updateVerbalNouns(nv, Seq.empty)
  })

  def update(rootInfo: RootInfo): Unit = {
//    rootLetters = rootInfo.rootLetters
//    family = rootInfo.family
    baseTranslation = rootInfo.baseTranslation
    translationsProperty.clear()
    translationsProperty.addAll(rootInfo.translations)
    updateVerbalNouns(family, rootInfo.verbalNounCodes.flatMap(VerbalNoun.getVerbalNouns))
  }

  private def updateVerbalNouns(family: NamedTemplate, verbalNouns: Seq[NounSupport]) = {
    verbalNounsProperty.clear()
    var _verbalNouns = verbalNouns
    if _verbalNouns.isEmpty then _verbalNouns = Form.fromNamedTemplate(family).verbalNouns
    verbalNounsProperty.addAll(_verbalNouns)
  }

  def toRootInfo: RootInfo = RootInfo(
    rootLetters = rootLetters,
    family = family,
    baseTranslation = baseTranslation,
    conjugationConfiguration = ConjugationConfiguration(),
    verbalNounCodes = verbalNouns.map(_.code),
    translations = Seq.empty
  )

  override def createDefaultSkin(): Skin[?] = RootInfoEditorSkin(this)
}

object RootInfoEditorView {

  def apply(): RootInfoEditorView = new RootInfoEditorView()

  private[control] val DefaultRootLetters = RootLetters(ArabicLetterType.Fa, ArabicLetterType.Ain, ArabicLetterType.Lam)
}
