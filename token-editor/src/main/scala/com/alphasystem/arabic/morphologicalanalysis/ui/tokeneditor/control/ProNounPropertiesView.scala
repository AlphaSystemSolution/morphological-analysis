package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.ProNounProperties
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin.ProNounPropertiesSkin
import com.alphasystem.morphologicalanalysis.morphology.model.*
import javafx.scene.control.Skin
import scalafx.beans.property.ObjectProperty

class ProNounPropertiesView
    extends AbstractNounPropertiesView[
      ProNounPartOfSpeechType,
      ProNounProperties
    ] {

  override protected val initialProperties: ProNounProperties =
    ProNounProperties(
      id = "empty",
      locationId = "empty",
      partOfSpeech = ProNounPartOfSpeechType.PRONOUN,
      status = NounStatus.ACCUSATIVE,
      number = NumberType.SINGULAR,
      gender = GenderType.MASCULINE,
      conversationType = ConversationType.FIRST_PERSON,
      proNounType = ProNounType.DETACHED
    )

  val conversationTypeProperty: ObjectProperty[ConversationType] =
    ObjectProperty[ConversationType](this, "conversationType")

  val proNounTypeProperty: ObjectProperty[ProNounType] =
    ObjectProperty[ProNounType](this, "proNounType")

  conversationTypeProperty.onChange { (_, oldValue, newValue) =>
    if oldValue != newValue then
      properties = properties.copy(conversationType = newValue)
  }

  proNounTypeProperty.onChange { (_, oldValue, newValue) =>
    if oldValue != newValue then
      properties = properties.copy(proNounType = newValue)
  }

  properties = initialProperties
  setSkin(createDefaultSkin())

  def conversationType: ConversationType = conversationTypeProperty.value

  def conversationType_=(value: ConversationType): Unit =
    conversationTypeProperty.value = value

  def proNounType: ProNounType = proNounTypeProperty.value

  def proNounType_=(value: ProNounType): Unit =
    proNounTypeProperty.value = value

  override protected def update(
    partOfSpeechType: ProNounPartOfSpeechType,
    properties: ProNounProperties
  ): ProNounProperties = properties.copy(partOfSpeech = partOfSpeechType)

  override protected def update(
    numberType: NumberType,
    properties: ProNounProperties
  ): ProNounProperties = properties.copy(number = numberType)

  override protected def update(
    genderType: GenderType,
    properties: ProNounProperties
  ): ProNounProperties = properties.copy(gender = genderType)

  override def update(
    nounStatus: NounStatus,
    properties: ProNounProperties
  ): ProNounProperties = properties.copy(status = nounStatus)

  override protected def update(properties: ProNounProperties): Unit = {
    super.update(properties)
    conversationType = properties.conversationType
    proNounType = properties.proNounType
  }

  override def createDefaultSkin(): Skin[_] = ProNounPropertiesSkin(this)
}

object ProNounPropertiesView {

  def apply(): ProNounPropertiesView = new ProNounPropertiesView()
}
