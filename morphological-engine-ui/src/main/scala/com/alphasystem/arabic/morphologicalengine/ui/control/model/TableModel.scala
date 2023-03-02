package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package model

import arabic.model.ArabicLetterType
import morphologicalengine.conjugation.model.{ ConjugationConfiguration, NamedTemplate }
import morphologicalengine.generator.model.ConjugationInput
import scalafx.Includes.*
import scalafx.beans.property.{ BooleanProperty, ObjectProperty, StringProperty }

class TableModel {

  private val defaultInput = ConjugationInput(
    namedTemplate = NamedTemplate.FormICategoryAGroupUTemplate,
    firstRadical = ArabicLetterType.Fa,
    secondRadical = ArabicLetterType.Ain,
    thirdRadical = ArabicLetterType.Lam
  )

  private val defaultConfiguration = ConjugationConfiguration()

  private val checkedProperty: BooleanProperty = new BooleanProperty(this, "checked", false)
  private val templateProperty = ObjectProperty[NamedTemplate](this, "template")
  private val rootLettersProperty = ObjectProperty[RootLetters](this, "rootLetters")
  private val translationProperty = new StringProperty(this, "translation", "")
  private val removePassiveLineProperty = new BooleanProperty(this, "removePassiveLine", false)
  private val skipRuleProcessingProperty = new BooleanProperty(this, "skipRuleProcessing", false)
  private val conjugationInputProperty = new ObjectProperty[ConjugationInput](this, "conjugationInput", defaultInput)
  private val conjugationConfigurationProperty =
    new ObjectProperty[ConjugationConfiguration](this, "conjugationConfiguration", defaultConfiguration)

  conjugationInputProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then {
      template = nv.namedTemplate
      rootLetters = RootLetters(nv.firstRadical, nv.secondRadical, nv.thirdRadical, nv.fourthRadical)
      translation = nv.translation.getOrElse("")
    }
  )

  conjugationConfigurationProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then {
      skipRuleProcessing = nv.skipRuleProcessing
      removePassiveLine = nv.removePassiveLine
    }
  )

  templateProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then conjugationInput = conjugationInput.copy(namedTemplate = nv)
  )

  rootLettersProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then
      conjugationInput = conjugationInput.copy(
        firstRadical = nv.firstRadical,
        secondRadical = nv.secondRadical,
        thirdRadical = nv.thirdRadical,
        fourthRadical = nv.fourthRadical
      )
  )

  translationProperty.onChange((_, _, nv) =>
    conjugationInput =
      conjugationInput.copy(translation = if Option(nv).isDefined && !nv.trim.isBlank then Some(nv) else None)
  )

  skipRuleProcessingProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then conjugationConfiguration = conjugationConfiguration.copy(skipRuleProcessing = nv)
  )

  removePassiveLineProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then conjugationConfiguration = conjugationConfiguration.copy(removePassiveLine = nv)
  )

  def checked: Boolean = checkedProperty.value
  private[control] def checked_=(value: Boolean): Unit = checkedProperty.value = value

  def template: NamedTemplate = templateProperty.value
  def template_=(value: NamedTemplate): Unit = templateProperty.value = value

  def rootLetters: RootLetters = rootLettersProperty.value
  private[control] def rootLetters_=(value: RootLetters): Unit = rootLettersProperty.value = value

  def translation: String = translationProperty.value
  private[control] def translation_=(value: String): Unit = translationProperty.value = value

  def removePassiveLine: Boolean = removePassiveLineProperty.value
  private[control] def removePassiveLine_=(value: Boolean): Unit = removePassiveLineProperty.value = value

  def skipRuleProcessing: Boolean = skipRuleProcessingProperty.value
  private[control] def skipRuleProcessing_=(value: Boolean): Unit = skipRuleProcessingProperty.value = value

  def conjugationInput: ConjugationInput = conjugationInputProperty.value
  def conjugationInput_=(value: ConjugationInput): Unit =
    conjugationInputProperty.value = if Option(value).isEmpty then defaultInput else value

  def conjugationConfiguration: ConjugationConfiguration = conjugationConfigurationProperty.value
  def conjugationConfiguration_=(value: ConjugationConfiguration): Unit =
    conjugationConfigurationProperty.value = if Option(value).isEmpty then defaultConfiguration else value
}

case class RootLetters(
  firstRadical: ArabicLetterType,
  secondRadical: ArabicLetterType,
  thirdRadical: ArabicLetterType,
  fourthRadical: Option[ArabicLetterType] = None)
