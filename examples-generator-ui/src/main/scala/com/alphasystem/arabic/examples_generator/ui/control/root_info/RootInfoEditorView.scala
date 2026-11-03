package com.alphasystem
package arabic
package examples_generator
package ui
package control
package root_info

import arabic.morphologicalanalysis.ui.RootLettersKeyBoardView.DefaultRootLetters
import arabic.morphologicalengine.conjugation.forms.{ Form, NounSupport }
import arabic.morphologicalengine.conjugation.model.{
  ConjugationConfiguration,
  ConjugationInput,
  NamedTemplate,
  RootLetters
}
import com.alphasystem.arabic.morphologicalengine.conjugation.forms.noun.VerbalNoun
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ BooleanProperty, ObjectProperty, StringProperty }
import scalafx.collections.ObservableBuffer

class RootInfoEditorView extends Control {

  private[control] val conjugationInputProperty = ObjectProperty[ConjugationInput](this, "conjugationInput")
  private[control] val rootLettersProperty = ObjectProperty[RootLetters](this, "rootLetters", DefaultRootLetters)
  private[root_info] val familyProperty =
    ObjectProperty[NamedTemplate](this, "family", NamedTemplate.FormICategoryAGroupATemplate)
  private[root_info] val translationProperty = new StringProperty(this, "translation")
  private[root_info] val skipRuleProcessingProperty = new BooleanProperty(this, "skipRuleProcessing", false)
  private[root_info] val removePassiveLineProperty = new BooleanProperty(this, "removePassiveLineProperty", false)
  private[root_info] val verbalNounsProperty: ObservableBuffer[NounSupport] = ObservableBuffer.empty[NounSupport]

  setSkin(createDefaultSkin())
  conjugationInputProperty.onChange((_, _, nv) => {
    if Option(nv).isDefined then {
      rootLetters = nv.rootLetters
      family = nv.namedTemplate
      translation = nv.translation.getOrElse("")
      val conjugationConfiguration = nv.conjugationConfiguration
      skipRuleProcessing = conjugationConfiguration.skipRuleProcessing
      removePassiveLine = conjugationConfiguration.removePassiveLine
      updateVerbalNouns(nv.namedTemplate, nv.verbalNounCodes.flatMap(VerbalNoun.getVerbalNouns))
    }
  })
  rootLettersProperty.onChange((_, _, nv) => {
    if Option(nv).isDefined then {
      conjugationInput = conjugationInput.copy(rootLetters = nv)
    }
  })
  familyProperty.onChange((_, _, nv) => {
    if Option(nv).isDefined then {
      conjugationInput = conjugationInput.copy(namedTemplate = nv)
    }
  })
  translationProperty.onChange((_, _, nv) => {
    if Option(nv).isDefined && nv.nonEmpty then {
      conjugationInput = conjugationInput.copy(translation = Some(nv))
    }
  })
  skipRuleProcessingProperty.onChange((_, _, nv) => {
    if Option(nv).isDefined then {
      conjugationInput = conjugationInput.copy(conjugationConfiguration =
        conjugationInput.conjugationConfiguration.copy(skipRuleProcessing = nv)
      )
    }
  })
  removePassiveLineProperty.onChange((_, _, nv) => {
    if Option(nv).isDefined then {
      conjugationInput = conjugationInput.copy(conjugationConfiguration =
        conjugationInput.conjugationConfiguration.copy(removePassiveLine = nv)
      )
    }
  })
  reset()

  def conjugationInput: ConjugationInput = conjugationInputProperty.value
  def conjugationInput_=(value: ConjugationInput): Unit = conjugationInputProperty.value = value

  def rootLetters: RootLetters = rootLettersProperty.value
  def rootLetters_=(value: RootLetters): Unit = rootLettersProperty.value = value

  def family: NamedTemplate = familyProperty.value
  private def family_=(value: NamedTemplate): Unit = familyProperty.value = value

  def translation: String = translationProperty.value
  private def translation_=(value: String): Unit = translationProperty.value = Option(value).map(_.trim).getOrElse("")

  def skipRuleProcessing: Boolean = skipRuleProcessingProperty.value
  private def skipRuleProcessing_=(value: Boolean): Unit = skipRuleProcessingProperty.value = value

  def removePassiveLine: Boolean = removePassiveLineProperty.value
  private def removePassiveLine_=(value: Boolean): Unit = removePassiveLineProperty.value = value

  def verbalNouns: Seq[NounSupport] = verbalNounsProperty.toSeq

  familyProperty.onChange((_, _, nv) => updateVerbalNouns(nv, Seq.empty))

  private def updateVerbalNouns(family: NamedTemplate, verbalNouns: Seq[NounSupport]) = {
    verbalNounsProperty.clear()
    var _verbalNouns = verbalNouns
    if _verbalNouns.isEmpty then _verbalNouns = Form.fromNamedTemplate(family).verbalNouns
    verbalNounsProperty.addAll(_verbalNouns)
  }

  def reset(): Unit =
    conjugationInput = ConjugationInput(
      namedTemplate = NamedTemplate.FormICategoryAGroupATemplate,
      conjugationConfiguration = ConjugationConfiguration(),
      rootLetters = DefaultRootLetters
    )

  override def createDefaultSkin(): Skin[?] = skin.RootInfoEditorSkin(this)
}

object RootInfoEditorView {
  def apply(): RootInfoEditorView = new RootInfoEditorView()
}
