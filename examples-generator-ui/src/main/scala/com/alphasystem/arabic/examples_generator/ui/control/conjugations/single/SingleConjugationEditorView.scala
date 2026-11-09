package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package single

import examples_generator.morphologicalchart.{ Settings, SingleConjugation }
import arabic.morphologicalengine.conjugation.model.MorphologicalTermType
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ BooleanProperty, ObjectProperty, ReadOnlyObjectWrapper, StringProperty }

class SingleConjugationEditorView extends Control {

  private[single] val tagProperty = new StringProperty(this, "tag")
  private[single] val pairedConjugationModeProperty = new BooleanProperty(this, "pairedConjugationMode")
  private[single] val singleConjugationProperty = ObjectProperty[SingleConjugation](this, "singleConjugation")
  private val settingsPropertyWrapper = new ReadOnlyObjectWrapper[Settings](this, "settings")
  private[single] val otherMorphologicalTermTypeProperty =
    ObjectProperty[Option[MorphologicalTermType]](this, "otherMorphologicalTermType")

  setSkin(createDefaultSkin())

  tag = ""
  pairedConjugationMode = false
  settings = DefaultSettings
  singleConjugation = DefaultSingleConjugation

  tagProperty.onChange((_, _, nv) => singleConjugation = singleConjugation.copy(tag = nv))

  settingsPropertyWrapper.onChange((_, _, _) =>
    if Option(settings).isDefined then singleConjugation = singleConjugation.copy(settings = settings)
  )
  singleConjugationProperty.onChange((_, _, nv) => {
    if Option(nv).isDefined then {
      tag = nv.tag
      settings = nv.settings
    } else {
      tag = ""
      settings = null
    }
  })

  def tag: String = tagProperty.value
  private[single] def tag_=(value: String): Unit = tagProperty.value = value

  def pairedConjugationMode: Boolean = pairedConjugationModeProperty.value
  private[single] def pairedConjugationMode_=(value: Boolean): Unit = pairedConjugationModeProperty.value = value

  def singleConjugation: SingleConjugation = singleConjugationProperty.value
  private[single] def singleConjugation_=(value: SingleConjugation): Unit =
    singleConjugationProperty.value = Option(value).getOrElse(DefaultSingleConjugation)
  def otherMorphologicalTermType: Option[MorphologicalTermType] = otherMorphologicalTermTypeProperty.value
  private[single] def otherMorphologicalTermType_=(value: Option[MorphologicalTermType]): Unit =
    otherMorphologicalTermTypeProperty.value = value

  def settings: Settings = settingsPropertyWrapper.value
  def settings_=(value: Settings): Unit = settingsPropertyWrapper.value = Option(value).getOrElse(DefaultSettings)

  override def createDefaultSkin(): Skin[?] = skin.SingleConjugationEditorSkin(this)
}

object SingleConjugationEditorView {
  def apply(): SingleConjugationEditorView = new SingleConjugationEditorView()
}
