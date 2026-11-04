package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package single

import examples_generator.morphologicalchart.ConjugationRequest
import ui.model.VerbTranslationResult
import arabic.morphologicalanalysis.ui.RootLettersKeyBoardView.DefaultRootLetters
import arabic.morphologicalengine.conjugation.model.{
  ConjugationConfiguration,
  ConjugationInput,
  MorphologicalTermType,
  NamedTemplate
}
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, ReadOnlyObjectProperty, ReadOnlyObjectWrapper }

class SingleConjugationEditorView extends Control {

  private[single] val conjugationRequestProperty = ObjectProperty[ConjugationRequest](this, "conjugationRequest")
  private val conjugationInputPropertyWrapper = new ReadOnlyObjectWrapper[ConjugationInput](this, "conjugationInput")
  private[single] val morphologicalTermTypeProperty =
    ObjectProperty[MorphologicalTermType](this, "morphologicalTermType")
  private val translationsPropertyWrapper = new ReadOnlyObjectWrapper[VerbTranslationResult](this, "translations")

  setSkin(skin.SingleConjugationEditorSkin(this))

  translationResult = VerbTranslationResult()
  morphologicalTermType = MorphologicalTermType.PastTense
  conjugationInput = ConjugationInput(
    namedTemplate = NamedTemplate.FormICategoryAGroupATemplate,
    conjugationConfiguration = ConjugationConfiguration(),
    rootLetters = DefaultRootLetters
  )
  conjugationRequest = null

  morphologicalTermTypeProperty.onChange((_, _, nv) =>
    conjugationRequest = conjugationRequest.copy(morphologicalTermType = nv)
  )
  conjugationRequestProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then {
      conjugationInput = nv.conjugationInput
      translationResult = VerbTranslationResult(nv.translations.getOrElse(Map.empty))
    }
  )
  conjugationInputProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then conjugationRequest = conjugationRequest.copy(conjugationInput = nv)
  )
  translationsProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then {
      val translations = nv.translations
      val filteredMap = translations.filterNot(_._2.isEmpty)
      val mayBeTranslations = if filteredMap.isEmpty then None else Some(filteredMap)
      conjugationRequest = conjugationRequest.copy(translations = mayBeTranslations)
    }
  )

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
  private[single] def conjugationInput_=(value: ConjugationInput): Unit = conjugationInputPropertyWrapper.value = value

  def morphologicalTermType: MorphologicalTermType = morphologicalTermTypeProperty.value
  private def morphologicalTermType_=(value: MorphologicalTermType): Unit =
    morphologicalTermTypeProperty.value = Option(value).getOrElse(MorphologicalTermType.PastTense)

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
