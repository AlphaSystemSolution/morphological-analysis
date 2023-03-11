package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package table

import arabic.model.ArabicLetterType
import morphologicalengine.conjugation.model.{ ConjugationConfiguration, ConjugationInput, NamedTemplate, RootLetters }
import scalafx.Includes.*
import scalafx.beans.property.{ BooleanProperty, ObjectProperty, StringProperty }

import java.util.UUID

class TableModel(src: ConjugationInput) {

  import TableModel.*

  private[table] val idProperty: ObjectProperty[UUID] = ObjectProperty[UUID](this, "id", UUID.randomUUID())
  private[table] val checkedProperty: BooleanProperty = new BooleanProperty(this, "checked", false)
  private[table] val templateProperty = ObjectProperty[NamedTemplate](this, "template")
  private[table] val rootLettersProperty = ObjectProperty[RootLetters](this, "rootLetters")
  private[table] val translationProperty = new StringProperty(this, "translation", "")
  private[table] val removePassiveLineProperty = new BooleanProperty(this, "removePassiveLine", false)
  private[table] val skipRuleProcessingProperty = new BooleanProperty(this, "skipRuleProcessing", false)
  private[table] val conjugationInputProperty =
    new ObjectProperty[ConjugationInput](this, "conjugationInput", defaultInput)

  conjugationInputProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then {
      id = nv.id
      template = nv.namedTemplate
      rootLetters = nv.rootLetters
      translation = nv.translation.getOrElse("")
      val cc = nv.conjugationConfiguration
      skipRuleProcessing = cc.skipRuleProcessing
      removePassiveLine = cc.removePassiveLine
    }
  )

  templateProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then conjugationInput = conjugationInput.copy(namedTemplate = nv)
  )

  rootLettersProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then conjugationInput = conjugationInput.copy(rootLetters = nv)
  )

  translationProperty.onChange((_, _, nv) =>
    conjugationInput =
      conjugationInput.copy(translation = if Option(nv).isDefined && !nv.trim.isBlank then Some(nv) else None)
  )

  skipRuleProcessingProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then {
      val cc = conjugationInput.conjugationConfiguration.copy(skipRuleProcessing = nv)
      conjugationInput = conjugationInput.copy(conjugationConfiguration = cc)
    }
  )

  removePassiveLineProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then {
      val cc = conjugationInput.conjugationConfiguration.copy(removePassiveLine = nv)
      conjugationInput = conjugationInput.copy(conjugationConfiguration = cc)
    }
  )

  conjugationInput = src

  def checked: Boolean = checkedProperty.value
  private[control] def checked_=(value: Boolean): Unit = checkedProperty.value = value

  def id: UUID = idProperty.value
  def id_=(value: UUID): Unit = idProperty.value = value

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
}

object TableModel {

  private val defaultInput = ConjugationInput(
    namedTemplate = NamedTemplate.FormICategoryAGroupUTemplate,
    conjugationConfiguration = ConjugationConfiguration(),
    rootLetters = RootLetters(ArabicLetterType.Fa, ArabicLetterType.Ain, ArabicLetterType.Lam)
  )

  def apply(src: ConjugationInput = defaultInput): TableModel = new TableModel(src)
}
