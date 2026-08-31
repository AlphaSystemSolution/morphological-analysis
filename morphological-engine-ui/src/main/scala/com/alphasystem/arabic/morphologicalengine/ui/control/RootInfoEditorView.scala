package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import arabic.model.ArabicLetterType
import morphologicalengine.ui.utils.GetRootInfoService
import morphologicalengine.asciidoc_generator.RootInfo
import morphologicalengine.conjugation.forms.{ Form, NounSupport }
import morphologicalengine.conjugation.forms.noun.VerbalNoun
import morphologicalengine.conjugation.model.{ NamedTemplate, RootLetters }
import morphologicalengine.ui.control.skin.RootInfoEditorSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, StringProperty }
import scalafx.collections.ObservableBuffer
import scalafx.Includes.*

class RootInfoEditorView extends Control {

  import RootInfoEditorView.*

  private val getRootInfoService = GetRootInfoService(this)
  private[control] val rootLettersProperty = ObjectProperty[RootLetters](this, "rootLetters", DefaultRootLetters)
  private[control] val familyProperty =
    ObjectProperty[NamedTemplate](this, "family", NamedTemplate.FormICategoryAGroupATemplate)
  private[control] val baseTranslationProperty = new StringProperty(this, "baseTranslation")
  private[control] val translationsProperty = new StringProperty(this, "otherTranslation")
  private[control] val verbalNounsProperty: ObservableBuffer[NounSupport] = ObservableBuffer.empty[NounSupport]

  setSkin(createDefaultSkin())

  def rootLetters: RootLetters = rootLettersProperty.value
  def rootLetters_=(value: RootLetters): Unit = rootLettersProperty.value = value

  def family: NamedTemplate = familyProperty.value
  private[control] def family_=(value: NamedTemplate): Unit = familyProperty.value = value

  def baseTranslation: String = baseTranslationProperty.value
  private def baseTranslation_=(value: String): Unit = baseTranslationProperty.value = value

  def translations: String = translationsProperty.value
  private def translations_=(value: String): Unit = translationsProperty.value = value

  def verbalNouns: Seq[NounSupport] = verbalNounsProperty.toSeq

  familyProperty.onChange((_, _, nv) => {
    updateVerbalNouns(nv, Seq.empty)
  })

  def update(rootInfo: RootInfo): Unit = {
    rootLetters = rootInfo.rootLetters
    family = rootInfo.family
    baseTranslation = rootInfo.baseTranslation
    translations = rootInfo.translations.getOrElse("")
    updateVerbalNouns(family, rootInfo.verbalNounCodes.flatMap(VerbalNoun.getVerbalNouns))
  }

  private def updateVerbalNouns(family: NamedTemplate, verbalNouns: Seq[NounSupport]) = {
    verbalNounsProperty.clear()
    var _verbalNouns = verbalNouns
    if _verbalNouns.isEmpty then _verbalNouns = Form.fromNamedTemplate(family).verbalNouns
    verbalNounsProperty.addAll(_verbalNouns)
  }

  def updateStatusLabel(newLabel: String): Unit =
    this.skin.value.asInstanceOf[RootInfoEditorSkin].updateStatusLabel(newLabel)

  /*
   * Loads root info from the rootLetters and family. Called when the rootLetters or family changes.
   */
  private[control] def loadRootInfo(rootLetters: RootLetters, family: NamedTemplate): Unit = {
    val service = getRootInfoService.service(rootLetters, family)
    getRootInfoService.handleResponse(service)
    getRootInfoService.start(service)
  }

  override def createDefaultSkin(): Skin[?] = RootInfoEditorSkin(this)
}

object RootInfoEditorView {

  def apply(): RootInfoEditorView = new RootInfoEditorView()

  private[control] val DefaultRootLetters = RootLetters(ArabicLetterType.Fa, ArabicLetterType.Ain, ArabicLetterType.Lam)
}
