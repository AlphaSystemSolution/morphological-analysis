package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugation_selector

import examples_generator.morphologicalchart.{ ConjugationRequest, SingleConjugation }
import ui.model.VerbTranslationResult
import arabic.morphologicalengine.conjugation.model.{ ConjugationInput, MorphologicalTermType }
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, ReadOnlyObjectProperty, ReadOnlyObjectWrapper }

class ConjugationTermSelectorView extends Control {

  private[conjugation_selector] val morphologicalTermTypeProperty =
    ObjectProperty[MorphologicalTermType](this, "morphologicalTermType")
  private[conjugation_selector] val singleConjugationProperty =
    ObjectProperty[SingleConjugation](this, "singleConjugation")
  private[conjugation_selector] val conjugationRequestProperty =
    ObjectProperty[ConjugationRequest](this, "conjugationRequest")
  private val conjugationInputPropertyWrapper = new ReadOnlyObjectWrapper[ConjugationInput](this, "conjugationInput")
  private[conjugation_selector] val otherMorphologicalTermTypeProperty =
    ObjectProperty[Option[MorphologicalTermType]](this, "otherMorphologicalTermType")
  private val translationsPropertyWrapper = new ReadOnlyObjectWrapper[VerbTranslationResult](this, "translations")

  setSkin(createDefaultSkin())
  translationResult = VerbTranslationResult()
  morphologicalTermType = MorphologicalTermType.PastTense
  conjugationInput = DefaultConjugationInput
  conjugationRequest = DefaultConjugationRequest
  singleConjugation = DefaultSingleConjugation

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
  conjugationInputProperty.onChange((_, _, _) =>
    if Option(conjugationInput).isDefined then
      conjugationRequest = conjugationRequest.copy(conjugationInput = conjugationInput)
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
      conjugationRequest = nv.request
      morphologicalTermType = nv.request.morphologicalTermType
      translationResult = VerbTranslationResult(nv.request.translations.getOrElse(Map.empty))
    } else {
      translationResult = VerbTranslationResult()
      morphologicalTermType = MorphologicalTermType.PastTense
      conjugationInput = null
      conjugationRequest = null
    }
  })

  def morphologicalTermType: MorphologicalTermType = morphologicalTermTypeProperty.value
  private def morphologicalTermType_=(value: MorphologicalTermType): Unit =
    morphologicalTermTypeProperty.value = Option(value).getOrElse(MorphologicalTermType.PastTense)

  def singleConjugation: SingleConjugation = singleConjugationProperty.value
  def singleConjugation_=(value: SingleConjugation): Unit =
    singleConjugationProperty.value = Option(value).getOrElse(DefaultSingleConjugation)

  def conjugationRequest: ConjugationRequest = conjugationRequestProperty.value
  def conjugationRequest_=(value: ConjugationRequest): Unit =
    conjugationRequestProperty.value = Option(value).getOrElse(DefaultConjugationRequest)

  def conjugationInputProperty: ReadOnlyObjectProperty[ConjugationInput] =
    conjugationInputPropertyWrapper.readOnlyProperty
  def conjugationInput: ConjugationInput = conjugationInputPropertyWrapper.value
  private[conjugation_selector] def conjugationInput_=(value: ConjugationInput): Unit =
    conjugationInputPropertyWrapper.value = Option(value).getOrElse(DefaultConjugationInput)

  def otherMorphologicalTermType: Option[MorphologicalTermType] = otherMorphologicalTermTypeProperty.value
  def otherMorphologicalTermType_=(value: Option[MorphologicalTermType]): Unit =
    otherMorphologicalTermTypeProperty.value = value

  private def translationsProperty: ReadOnlyObjectProperty[VerbTranslationResult] =
    translationsPropertyWrapper.readOnlyProperty

  def translationResult: VerbTranslationResult = translationsPropertyWrapper.value
  private[conjugation_selector] def translationResult_=(value: VerbTranslationResult): Unit =
    translationsPropertyWrapper.value = value

  override def createDefaultSkin(): Skin[?] = skin.ConjugationTermSelectorSkin(this)
}

object ConjugationTermSelectorView {
  def apply(): ConjugationTermSelectorView = new ConjugationTermSelectorView()
}
