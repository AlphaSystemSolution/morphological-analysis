package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import com.alphasystem.arabic.model.ArabicLetterType
import com.alphasystem.arabic.morphologicalengine.asciidoc_generator.Word
import com.alphasystem.arabic.morphologicalengine.conjugation.model.{NamedTemplate, RootLetters}
import com.alphasystem.arabic.morphologicalengine.ui.control.skin.WordEditorSkin
import javafx.scene.control.{Control, Skin}
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.collections.ObservableSet

class WordEditorView extends Control {

  import WordEditorView.*

  private[control] val rootLettersProperty = ObjectProperty[RootLetters](this, "rootLetters", DefaultRootLetters)
  private[control] val familyProperty = ObjectProperty[NamedTemplate](this, "family", NamedTemplate.FormICategoryAGroupUTemplate)
  private[control] val baseTranslationProperty = new StringProperty(this, "baseTranslation")
  private[control] val translationsProperty = ObservableSet.empty[String]

  setSkin(createDefaultSkin())

  def rootLetters: RootLetters = rootLettersProperty.value
  def rootLetters_=(value: RootLetters): Unit = rootLettersProperty.value = value

  def family: NamedTemplate = familyProperty.value
  private def family_=(value: NamedTemplate): Unit = familyProperty.value = value

  def baseTranslation: String = baseTranslationProperty.value
  private def baseTranslation_=(value: String): Unit = baseTranslationProperty.value = value

  def update(word: Word): Unit = {
    rootLetters = word.rootLetters
    family = word.family
    baseTranslation = word.baseTranslation
    translationsProperty.clear()
    translationsProperty.addAll(word.translations)
  }

  def toWord: Word = Word(rootLetters, family, baseTranslation, translationsProperty.toSet)

  override def createDefaultSkin(): Skin[?] = WordEditorSkin(this)
}

object WordEditorView {

  def apply(): WordEditorView = new WordEditorView()

  private val DefaultRootLetters = RootLetters(ArabicLetterType.Fa, ArabicLetterType.Ain, ArabicLetterType.Lam)
}