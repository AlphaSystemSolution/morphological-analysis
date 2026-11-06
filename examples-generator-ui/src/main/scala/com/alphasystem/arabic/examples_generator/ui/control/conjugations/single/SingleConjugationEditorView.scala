package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package single

import examples_generator.morphologicalchart.{ ConjugationRequest, Settings, SingleConjugation }
import ui.model.VerbTranslationResult
import arabic.morphologicalanalysis.ui.RootLettersKeyBoardView.DefaultRootLetters
import arabic.morphologicalengine.conjugation.model.{
  ConjugationConfiguration,
  ConjugationInput,
  MorphologicalTermType,
  NamedTemplate
}
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{
  BooleanProperty,
  ObjectProperty,
  ReadOnlyObjectProperty,
  ReadOnlyObjectWrapper,
  StringProperty
}

class SingleConjugationEditorView extends Control {

  import SingleConjugationEditorView.*

  private[single] val tagProperty = new StringProperty(this, "tag")
  private[single] val showTagProperty = new BooleanProperty(this, "showTag")
  private[single] val singleConjugationProperty = ObjectProperty[SingleConjugation](this, "singleConjugation")
  private[single] val conjugationRequestProperty = ObjectProperty[ConjugationRequest](this, "conjugationRequest")
  private val conjugationInputPropertyWrapper = new ReadOnlyObjectWrapper[ConjugationInput](this, "conjugationInput")
  private val settingsPropertyWrapper = new ReadOnlyObjectWrapper[Settings](this, "settings")
  private[single] val morphologicalTermTypeProperty =
    ObjectProperty[MorphologicalTermType](this, "morphologicalTermType")
  private val translationsPropertyWrapper = new ReadOnlyObjectWrapper[VerbTranslationResult](this, "translations")

  setSkin(createDefaultSkin())

  tag = ""
  showTag = true
  translationResult = VerbTranslationResult()
  morphologicalTermType = MorphologicalTermType.PastTense
  conjugationInput = DefaultConjugationInput
  conjugationRequest = DefaultConjugationRequest
  settings = DefaultSettings
  singleConjugation = DefaultSingleConjugation

  tagProperty.onChange((_, _, nv) => singleConjugation = singleConjugation.copy(tag = nv))
  morphologicalTermTypeProperty.onChange((_, _, nv) =>
    conjugationRequest = conjugationRequest.copy(morphologicalTermType = nv)
  )
  conjugationRequestProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then {
      conjugationInput = nv.conjugationInput
      morphologicalTermType = nv.morphologicalTermType
      translationResult = VerbTranslationResult(nv.translations.getOrElse(Map.empty))
      singleConjugation = singleConjugation.copy(request = nv)
    }
  )
  conjugationInputProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then conjugationRequest = conjugationRequest.copy(conjugationInput = nv)
  )
  settingsPropertyWrapper.onChange((_, _, nv) =>
    if Option(nv).isDefined then singleConjugation = singleConjugation.copy(settings = nv)
  )
  translationsProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then {
      val translations = nv.translations
      val filteredMap = translations.filterNot(_._2.isEmpty)
      val mayBeTranslations = if filteredMap.isEmpty then None else Some(filteredMap)
      conjugationRequest = conjugationRequest.copy(translations = mayBeTranslations)
    }
  )
  singleConjugationProperty.onChange((_, _, nv) => {
    if Option(nv).isDefined then {
      tag = nv.tag
      conjugationRequest = nv.request
      settings = nv.settings
      morphologicalTermType = nv.request.morphologicalTermType
      translationResult = VerbTranslationResult(nv.request.translations.getOrElse(Map.empty))
    } else {
      tag = ""
      translationResult = VerbTranslationResult()
      morphologicalTermType = MorphologicalTermType.PastTense
      conjugationInput = null
      conjugationRequest = null
      settings = null
    }
  })

  def tag: String = tagProperty.value
  private[single] def tag_=(value: String): Unit = tagProperty.value = value

  def showTag: Boolean = showTagProperty.value
  private[single] def showTag_=(value: Boolean): Unit = showTagProperty.value = value

  def singleConjugation: SingleConjugation = singleConjugationProperty.value
  private[single] def singleConjugation_=(value: SingleConjugation): Unit =
    singleConjugationProperty.value = Option(value).getOrElse(DefaultSingleConjugation)

  def conjugationRequest: ConjugationRequest = conjugationRequestProperty.value
  def conjugationRequest_=(value: ConjugationRequest): Unit =
    conjugationRequestProperty.value = Option(value).getOrElse(DefaultConjugationRequest)

  def conjugationInputProperty: ReadOnlyObjectProperty[ConjugationInput] =
    conjugationInputPropertyWrapper.readOnlyProperty
  def conjugationInput: ConjugationInput = conjugationInputPropertyWrapper.value
  private[single] def conjugationInput_=(value: ConjugationInput): Unit =
    conjugationInputPropertyWrapper.value = Option(value).getOrElse(DefaultConjugationInput)

  def morphologicalTermType: MorphologicalTermType = morphologicalTermTypeProperty.value
  private def morphologicalTermType_=(value: MorphologicalTermType): Unit =
    morphologicalTermTypeProperty.value = Option(value).getOrElse(MorphologicalTermType.PastTense)

  def settings: Settings = settingsPropertyWrapper.value
  def settings_=(value: Settings): Unit = settingsPropertyWrapper.value = Option(value).getOrElse(Settings())

  private def translationsProperty: ReadOnlyObjectProperty[VerbTranslationResult] =
    translationsPropertyWrapper.readOnlyProperty
  def translationResult: VerbTranslationResult = translationsPropertyWrapper.value
  private[single] def translationResult_=(value: VerbTranslationResult): Unit = translationsPropertyWrapper.value =
    value

  override def createDefaultSkin(): Skin[?] = skin.SingleConjugationEditorSkin(this)
}

object SingleConjugationEditorView {
  def apply(): SingleConjugationEditorView = new SingleConjugationEditorView()

  private val DefaultConjugationInput = ConjugationInput(
    namedTemplate = NamedTemplate.FormICategoryAGroupATemplate,
    conjugationConfiguration = ConjugationConfiguration(),
    rootLetters = DefaultRootLetters
  )

  val DefaultConjugationRequest = ConjugationRequest(
    morphologicalTermType = MorphologicalTermType.PastTense,
    conjugationInput = DefaultConjugationInput
  )

  private val DefaultSettings = Settings()

  private def DefaultSingleConjugation =
    SingleConjugation(tag = "", settings = DefaultSettings, request = DefaultConjugationRequest)
}
