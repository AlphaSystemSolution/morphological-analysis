package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package single

import examples_generator.morphologicalchart.ConjugationRequest
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

  setSkin(skin.SingleConjugationEditorSkin(this))

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
  conjugationRequestProperty.onChange((_, _, nv) => if Option(nv).isDefined then conjugationInput = nv.conjugationInput)
  conjugationInputProperty.onChange((_, _, nv) =>
    if Option(nv).isDefined then conjugationRequest = conjugationRequest.copy(conjugationInput = nv)
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

  override def createDefaultSkin(): Skin[?] = skin.SingleConjugationEditorSkin(this)
}

object SingleConjugationEditorView {
  def apply(): SingleConjugationEditorView = new SingleConjugationEditorView()
}
