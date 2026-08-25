package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import com.alphasystem.arabic.model.ArabicLetterType
import com.alphasystem.arabic.morphologicalengine.asciidoc_generator.{RootInfo, Word}
import com.alphasystem.arabic.morphologicalengine.conjugation.model.{NamedTemplate, RootLetters}
import com.alphasystem.arabic.morphologicalengine.ui.control.skin.RootInfoEditorSkin
import javafx.scene.control.{Control, Skin}
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.collections.ObservableSet

class RootInfoEditorView extends Control {

  import RootInfoEditorView.*

  private[control] val rootLettersProperty = ObjectProperty[RootLetters](this, "rootLetters", DefaultRootLetters)
  private[control] val familyProperty = ObjectProperty[NamedTemplate](this, "family", NamedTemplate.FormICategoryAGroupATemplate)
  private[control] val baseTranslationProperty = new StringProperty(this, "baseTranslation")
  private[control] val translationsProperty = ObservableSet.empty[String]

  setSkin(createDefaultSkin())

  def rootLetters: RootLetters = rootLettersProperty.value
  def rootLetters_=(value: RootLetters): Unit = rootLettersProperty.value = value

  def family: NamedTemplate = familyProperty.value
  private def family_=(value: NamedTemplate): Unit = familyProperty.value = value

  def baseTranslation: String = baseTranslationProperty.value
  private def baseTranslation_=(value: String): Unit = baseTranslationProperty.value = value

  def update(rootInfo: RootInfo): Unit = {
    rootLetters = rootInfo.rootLetters
    family = rootInfo.family
    baseTranslation = rootInfo.baseTranslation
    translationsProperty.clear()
    translationsProperty.addAll(rootInfo.translations)
  }

  def toWord: Word = Word(rootLetters, family, baseTranslation, translationsProperty.toSet)

  override def createDefaultSkin(): Skin[?] = RootInfoEditorSkin(this)
}

object RootInfoEditorView {

  def apply(): RootInfoEditorView = new RootInfoEditorView()

  private val DefaultRootLetters = RootLetters(ArabicLetterType.Fa, ArabicLetterType.Ain, ArabicLetterType.Lam)
}