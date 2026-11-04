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

  private[single] val tagProperty = new StringProperty(this, "tag")
  private[single] val showTagProperty = new BooleanProperty(this, "showTag")
  private[single] val singleConjugationProperty = ObjectProperty[SingleConjugation](this, "singleConjugation")
  private[single] val conjugationRequestProperty = ObjectProperty[ConjugationRequest](this, "conjugationRequest")
  private val conjugationInputPropertyWrapper = new ReadOnlyObjectWrapper[ConjugationInput](this, "conjugationInput")
  private val settingsPropertyWrapper = new ReadOnlyObjectWrapper[Settings](this, "settings")
  private[single] val morphologicalTermTypeProperty =
    ObjectProperty[MorphologicalTermType](this, "morphologicalTermType")
  private val translationsPropertyWrapper = new ReadOnlyObjectWrapper[VerbTranslationResult](this, "translations")

  setSkin(skin.SingleConjugationEditorSkin(this))

  tag = ""
  showTag = true
  translationResult = VerbTranslationResult()
  morphologicalTermType = MorphologicalTermType.PastTense
  conjugationInput = null
  conjugationRequest = null
  settings = null
  singleConjugation = null

  tagProperty.onChange((_, _, nv) => singleConjugation = singleConjugation.copy(tag = nv))
  morphologicalTermTypeProperty.onChange((_, _, nv) =>
    conjugationRequest = conjugationRequest.copy(morphologicalTermType = nv)
  )
  conjugationRequestProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then {
      conjugationInput = nv.conjugationInput
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

  def tag: String = tagProperty.value
  private[single] def tag_=(value: String): Unit = tagProperty.value = value

  def showTag: Boolean = showTagProperty.value
  private[single] def showTag_=(value: Boolean): Unit = showTagProperty.value = value

  def singleConjugation: SingleConjugation = singleConjugationProperty.value
  private[single] def singleConjugation_=(value: SingleConjugation): Unit =
    singleConjugationProperty.value =
      Option(value).getOrElse(SingleConjugation(tag = tag, settings = Settings(), request = conjugationRequest))

  def conjugationRequest: ConjugationRequest = conjugationRequestProperty.value
  def conjugationRequest_=(value: ConjugationRequest): Unit =
    conjugationRequestProperty.value = Option(value).getOrElse(
      ConjugationRequest(
        morphologicalTermType = morphologicalTermType,
        conjugationInput = conjugationInput,
        translations = None
      )
    )

  def conjugationInputProperty: ReadOnlyObjectProperty[ConjugationInput] =
    conjugationInputPropertyWrapper.readOnlyProperty
  def conjugationInput: ConjugationInput = conjugationInputPropertyWrapper.value
  private[single] def conjugationInput_=(value: ConjugationInput): Unit =
    conjugationInputPropertyWrapper.value = Option(value).getOrElse(
      ConjugationInput(
        namedTemplate = NamedTemplate.FormICategoryAGroupATemplate,
        conjugationConfiguration = ConjugationConfiguration(),
        rootLetters = DefaultRootLetters
      )
    )

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
}
