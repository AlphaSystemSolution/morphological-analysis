package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.VerbProperties
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin.VerbPropertiesSkin
import com.alphasystem.morphologicalanalysis.morphology.model.*
import javafx.scene.control.Skin
import scalafx.beans.property.ObjectProperty

class VerbPropertiesView
    extends AbstractPropertiesView[
      VerbPartOfSpeechType,
      VerbProperties
    ] {

  override protected val initialProperties: VerbProperties =
    VerbProperties(
      partOfSpeech = VerbPartOfSpeechType.VERB,
      number = NumberType.SINGULAR,
      gender = GenderType.MASCULINE,
      conversationType = ConversationType.FIRST_PERSON,
      verbType = VerbType.PERFECT,
      mode = VerbMode.NONE
    )

  val conversationTypeProperty: ObjectProperty[ConversationType] =
    ObjectProperty[ConversationType](this, "conversationType")

  val verbTypeProperty: ObjectProperty[VerbType] =
    ObjectProperty[VerbType](this, "verbType")

  val verbModeProperty: ObjectProperty[VerbMode] =
    ObjectProperty[VerbMode](this, "verbMode")

  conversationTypeProperty.onChange { (_, oldValue, newValue) =>
    if oldValue != newValue then
      properties = properties.copy(conversationType = newValue)
  }

  verbTypeProperty.onChange { (_, oldValue, newValue) =>
    if oldValue != newValue then
      properties = properties.copy(verbType = newValue)
  }

  properties = initialProperties
  setSkin(createDefaultSkin())

  def conversationType: ConversationType = conversationTypeProperty.value

  def conversationType_=(value: ConversationType): Unit =
    conversationTypeProperty.value = value

  def verbType: VerbType = verbTypeProperty.value

  def verbType_=(value: VerbType): Unit =
    verbTypeProperty.value = value

  def verbMode: VerbMode = verbModeProperty.value

  def verbMode_=(value: VerbMode): Unit = verbModeProperty.value = value

  override protected def update(
    partOfSpeechType: VerbPartOfSpeechType,
    properties: VerbProperties
  ): VerbProperties = properties.copy(partOfSpeech = partOfSpeechType)

  override protected def update(
    numberType: NumberType,
    properties: VerbProperties
  ): VerbProperties = properties.copy(number = numberType)

  override protected def update(
    genderType: GenderType,
    properties: VerbProperties
  ): VerbProperties = properties.copy(gender = genderType)

  override protected def update(properties: VerbProperties): Unit = {
    super.update(properties)
    conversationType = properties.conversationType
    verbType = properties.verbType
    verbMode = properties.mode
  }

  override def createDefaultSkin(): Skin[_] = VerbPropertiesSkin(this)
}

object VerbPropertiesView {

  def apply(): VerbPropertiesView = new VerbPropertiesView()
}
