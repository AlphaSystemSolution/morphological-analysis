package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package table

import arabic.model.ArabicLetterType
import morphologicalengine.conjugation.forms.noun.VerbalNoun
import morphologicalengine.conjugation.*
import morphologicalengine.conjugation.forms.NounSupport
import morphologicalengine.conjugation.model.{ ConjugationConfiguration, ConjugationInput, NamedTemplate, RootLetters }
import scalafx.Includes.*
import scalafx.beans.property.{ BooleanProperty, ObjectProperty, StringProperty }
import scalafx.collections.ObservableBuffer

import java.util.UUID

class TableModel(src: ConjugationInput) {

  import TableModel.*

  private[table] val idProperty: ObjectProperty[UUID] = ObjectProperty[UUID](this, "id", UUID.randomUUID())
  private[table] val checkedProperty: BooleanProperty = new BooleanProperty(this, "checked", false)
  private[table] val templateProperty = ObjectProperty[NamedTemplate](this, "template")
  private[table] val rootLettersProperty = ObjectProperty[RootLetters](this, "rootLetters")
  private[table] val translationProperty = new StringProperty(this, "translation", "")
  private[table] val verbalNounsProperty = ObjectProperty[Seq[NounSupport]](this, "verbalNouns")
  private[table] val removePassiveLineProperty = new BooleanProperty(this, "removePassiveLine", false)
  private[table] val skipRuleProcessingProperty = new BooleanProperty(this, "skipRuleProcessing", false)
  private[table] val conjugationInputProperty = ObjectProperty[ConjugationInput](this, "conjugationInput", defaultInput)

  conjugationInputProperty.onChange((_, _, nv) => init(nv))

  templateProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then {
      conjugationInput = conjugationInput.copy(namedTemplate = nv, verbalNounCodes = Seq.empty)
      VerbalNoun.byNamedTemplate.get(nv).foreach(values => verbalNouns = values)
    }
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

  verbalNounsProperty.onChange((_, _, nv) => {
    if Option(nv).isDefined && nv.nonEmpty then
      conjugationInput = conjugationInput.copy(verbalNounCodes = nv.map(_.code))
  })

  conjugationInput = src
  init(conjugationInput)

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

  def verbalNouns: Seq[NounSupport] = verbalNounsProperty.value
  private[control] def verbalNouns_=(values: Seq[NounSupport]): Unit = {
    verbalNounsProperty.value = Seq.empty
    verbalNounsProperty.value = values
  }

  def removePassiveLine: Boolean = removePassiveLineProperty.value
  private[control] def removePassiveLine_=(value: Boolean): Unit = removePassiveLineProperty.value = value

  def skipRuleProcessing: Boolean = skipRuleProcessingProperty.value
  private[control] def skipRuleProcessing_=(value: Boolean): Unit = skipRuleProcessingProperty.value = value

  def conjugationInput: ConjugationInput = conjugationInputProperty.value
  private[control] def conjugationInput_=(value: ConjugationInput): Unit =
    conjugationInputProperty.value = if Option(value).isEmpty then defaultInput else value

  def copy: TableModel = {
    val model = TableModel(conjugationInput.copy())
    model.checked = false
    model
  }

  private def init(src: ConjugationInput): Unit = {
    if Option(src).isDefined then {
      id = src.id
      template = src.namedTemplate
      rootLetters = src.rootLetters
      translation = src.translation.getOrElse("")
      verbalNouns = src.verbalNouns
      val cc = src.conjugationConfiguration
      skipRuleProcessing = cc.skipRuleProcessing
      removePassiveLine = cc.removePassiveLine
    }
  }
}

object TableModel {

  private val defaultInput = ConjugationInput(
    namedTemplate = NamedTemplate.FormICategoryAGroupUTemplate,
    conjugationConfiguration = ConjugationConfiguration(),
    rootLetters = RootLetters(ArabicLetterType.Fa, ArabicLetterType.Ain, ArabicLetterType.Lam)
  )

  def apply(src: ConjugationInput = defaultInput): TableModel = new TableModel(src)
}
