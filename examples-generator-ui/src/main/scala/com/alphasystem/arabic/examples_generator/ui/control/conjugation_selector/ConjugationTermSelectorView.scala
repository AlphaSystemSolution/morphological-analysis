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
import scalafx.beans.property.{ DoubleProperty, ObjectProperty, ReadOnlyObjectProperty, ReadOnlyObjectWrapper }

class ConjugationTermSelectorView extends Control {

  private[conjugation_selector] val morphologicalTermTypeProperty =
    ObjectProperty[MorphologicalTermType](this, "morphologicalTermType")
  val singleConjugationProperty: ObjectProperty[SingleConjugation] =
    ObjectProperty[SingleConjugation](this, "singleConjugation")
  private[conjugation_selector] val conjugationRequestProperty =
    ObjectProperty[ConjugationRequest](this, "conjugationRequest")
  private val conjugationInputPropertyWrapper = new ReadOnlyObjectWrapper[ConjugationInput](this, "conjugationInput")
  private[conjugation_selector] val otherMorphologicalTermTypeProperty =
    ObjectProperty[Option[MorphologicalTermType]](this, "otherMorphologicalTermType")
  private val translationsPropertyWrapper = new ReadOnlyObjectWrapper[VerbTranslationResult](this, "translations")

  val labelColumnWidthProperty = new DoubleProperty(this, "labelColumnWidth", 200.0)

  def labelColumnWidth: Double = labelColumnWidthProperty.value
  def labelColumnWidth_=(value: Double): Unit = labelColumnWidthProperty.value = value

  private var synchronizing = false
  private var previousTranslations = VerbTranslationResult().translations

  private def synchronize(update: => Unit): Unit =
    if !synchronizing then {
      synchronizing = true
      try update
      finally synchronizing = false
    }

  private def loadRequest(request: ConjugationRequest): Unit = {
    conjugationInput = request.conjugationInput
    morphologicalTermType = request.morphologicalTermType
    translationResult = VerbTranslationResult(request.translations.getOrElse(Map.empty))
  }

  singleConjugation = DefaultSingleConjugation
  conjugationRequest = singleConjugation.request
  loadRequest(conjugationRequest)

  morphologicalTermTypeProperty.onChange((_, _, nv) =>
    synchronize {
      val translations =
        if MorphologicalTermType.NounBasedTypes.contains(nv) || previousTranslations.isEmpty then None
        else Some(previousTranslations)
      val request = conjugationRequest.copy(morphologicalTermType = nv, translations = translations)
      conjugationRequest = request
      loadRequest(request)
      singleConjugation = singleConjugation.copy(request = request)
    }
  )
  conjugationRequestProperty.onChange((_, ov, nv) =>
    if Option(nv).isDefined then
      synchronize {
        if !MorphologicalTermType.NounBasedTypes.contains(nv.morphologicalTermType) ||
          Option(ov).forall(_.morphologicalTermType != nv.morphologicalTermType)
        then previousTranslations = nv.translations.getOrElse(Map.empty)
        loadRequest(nv)
        singleConjugation = singleConjugation.copy(request = nv)
      }
  )
  conjugationInputProperty.onChange((_, _, _) =>
    if !synchronizing && Option(conjugationInput).isDefined then
      conjugationRequest = conjugationRequest.copy(conjugationInput = conjugationInput)
  )
  translationsProperty.onChange((_, _, nv) =>
    if !synchronizing && Option(nv).isDefined then {
      val filteredMap = nv.normalizedTranslations
      val translations = if filteredMap.isEmpty then None else Some(filteredMap)
      conjugationRequest = conjugationRequest.copy(translations = translations)
    }
  )
  singleConjugationProperty.onChange((_, ov, nv) =>
    synchronize {
      val value = Option(nv).getOrElse(DefaultSingleConjugation)
      if nv == null then singleConjugation = value
      if Option(ov).forall(old => old.id != value.id || old.request != value.request) then {
        previousTranslations = value.request.translations.getOrElse(Map.empty)
        conjugationRequest = value.request
        loadRequest(value.request)
      }
    }
  )

  setSkin(createDefaultSkin())

  private[conjugation_selector] def morphologicalTermType: MorphologicalTermType = morphologicalTermTypeProperty.value
  private def morphologicalTermType_=(value: MorphologicalTermType): Unit =
    morphologicalTermTypeProperty.value = Option(value).getOrElse(MorphologicalTermType.PastTense)

  def singleConjugation: SingleConjugation = singleConjugationProperty.value
  private[conjugation_selector] def singleConjugation_=(value: SingleConjugation): Unit =
    singleConjugationProperty.value = Option(value).getOrElse(DefaultSingleConjugation)

  private[conjugation_selector] def conjugationRequest: ConjugationRequest =
    Option(conjugationRequestProperty.value).getOrElse(DefaultConjugationRequest)
  def conjugationRequest_=(value: ConjugationRequest): Unit =
    conjugationRequestProperty.value = Option(value).getOrElse(DefaultConjugationRequest)

  private def conjugationInputProperty: ReadOnlyObjectProperty[ConjugationInput] =
    conjugationInputPropertyWrapper.readOnlyProperty
  private def conjugationInput: ConjugationInput = conjugationInputPropertyWrapper.value
  private[conjugation_selector] def conjugationInput_=(value: ConjugationInput): Unit =
    conjugationInputPropertyWrapper.value = Option(value).getOrElse(DefaultConjugationInput)

  def otherMorphologicalTermType: Option[MorphologicalTermType] =
    otherMorphologicalTermTypeProperty.value
  def otherMorphologicalTermType_=(value: Option[MorphologicalTermType]): Unit =
    otherMorphologicalTermTypeProperty.value = value

  private[conjugation_selector] def translationsProperty: ReadOnlyObjectProperty[VerbTranslationResult] =
    translationsPropertyWrapper.readOnlyProperty

  def translationResult: VerbTranslationResult = translationsPropertyWrapper.value
  private[conjugation_selector] def translationResult_=(value: VerbTranslationResult): Unit =
    translationsPropertyWrapper.value = value

  override def createDefaultSkin(): Skin[?] = skin.ConjugationTermSelectorSkin(this)
}

object ConjugationTermSelectorView {
  def apply(): ConjugationTermSelectorView = new ConjugationTermSelectorView()
}
