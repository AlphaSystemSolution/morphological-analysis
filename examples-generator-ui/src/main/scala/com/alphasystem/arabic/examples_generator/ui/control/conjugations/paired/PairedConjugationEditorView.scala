package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package paired

import examples_generator.morphologicalchart.{ ConjugationRequest, PairedConjugation, Settings }
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.binding.Bindings
import scalafx.beans.property.*

class PairedConjugationEditorView extends Control {

  private[paired] val pairedConjugationProperty = ObjectProperty[PairedConjugation](this, "pairedConjugation")
  private[paired] val tagProperty = new StringProperty(this, "tag")
  private[paired] val firstConjugationRequestPropertyWrapper =
    new ReadOnlyObjectWrapper[ConjugationRequest](this, "firstConjugationRequest")
  private[paired] val secondConjugationRequestPropertyWrapper =
    new ReadOnlyObjectWrapper[ConjugationRequest](this, "secondConjugationRequest")
  private val validatedPropertyWrapper = new ReadOnlyBooleanWrapper(this, "validated", false)

  setSkin(createDefaultSkin())

  tag = ""
  firstConjugationRequest = DefaultConjugationRequest
  secondConjugationRequest = DefaultConjugationRequest
  pairedConjugation = PairedConjugation(
    tag = tag,
    settings = Settings(),
    right = Option(firstConjugationRequest),
    left = Option(secondConjugationRequest)
  )

  tagProperty.onChange((_, _, nv) => pairedConjugation = pairedConjugation.copy(tag = nv))

  firstConjugationRequestPropertyWrapper.onChange((_, _, nv) => {
    val maybeRequest = Option(nv)
    if maybeRequest.isDefined && !hasSimilarTypes then {
      secondConjugationRequest = null
    }
    pairedConjugation = pairedConjugation.copy(right = maybeRequest)
  })

  secondConjugationRequestPropertyWrapper.onChange((_, _, nv) =>
    pairedConjugation = pairedConjugation.copy(left = Option(nv))
  )

  private val similarTypeBinding = Bindings.createBooleanBinding(
    () => hasSimilarTypes,
    firstConjugationRequestPropertyWrapper,
    secondConjugationRequestPropertyWrapper
  )
  validatedPropertyWrapper.bind(similarTypeBinding.and(tagProperty.isNotEmpty))

  def validatedProperty: ReadOnlyBooleanProperty = validatedPropertyWrapper.readOnlyProperty

  def pairedConjugation: PairedConjugation = pairedConjugationProperty.value
  private[paired] def pairedConjugation_=(value: PairedConjugation): Unit = pairedConjugationProperty.value = value

  def tag: String = tagProperty.value
  private[paired] def tag_=(value: String): Unit = tagProperty.value = value

  def firstConjugationRequest: ConjugationRequest = firstConjugationRequestPropertyWrapper.value
  private[paired] def firstConjugationRequest_=(value: ConjugationRequest): Unit =
    firstConjugationRequestPropertyWrapper.value = value

  def secondConjugationRequest: ConjugationRequest = secondConjugationRequestPropertyWrapper.value
  private[paired] def secondConjugationRequest_=(value: ConjugationRequest): Unit =
    secondConjugationRequestPropertyWrapper.value = value

  def hasSimilarTypes: Boolean = Option(secondConjugationRequest).isDefined && PairedConjugation.hasSimilarTypes(
    secondConjugationRequest.morphologicalTermType,
    firstConjugationRequest.morphologicalTermType
  )

  private[paired] def reset(maybePairedConjugation: Option[PairedConjugation]): Unit = {
    pairedConjugation = maybePairedConjugation.getOrElse(
      PairedConjugation(
        tag = "",
        settings = Settings(),
        right = Some(DefaultConjugationRequest),
        left = Some(DefaultConjugationRequest)
      )
    )
    tag = pairedConjugation.tag
    firstConjugationRequest = pairedConjugation.right.getOrElse(DefaultConjugationRequest)
    secondConjugationRequest = pairedConjugation.left.orNull
  }

  private[paired] def updateSettings(settings: Settings): Unit = pairedConjugation =
    pairedConjugation.copy(settings = settings)

  override def createDefaultSkin(): Skin[?] = skin.PairedConjugationEditorSkin(this)
}

object PairedConjugationEditorView {
  def apply(): PairedConjugationEditorView = new PairedConjugationEditorView()
}
