package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.NounProperties
import com.alphasystem.morphologicalanalysis.morphology.model.*
import scalafx.beans.property.ObjectProperty

class NounPropertiesView
    extends AbstractNounPropertiesView[NounPartOfSpeechType, NounProperties] {

  override protected val initialProperties: NounProperties =
    NounProperties(
      id = "empty",
      locationId = "empty",
      partOfSpeech = NounPartOfSpeechType.NOUN,
      status = NounStatus.ACCUSATIVE,
      number = NumberType.SINGULAR,
      gender = GenderType.MASCULINE,
      nounType = NounType.INDEFINITE,
      nounKind = NounKind.NONE
    )

  private val nounTypeProperty =
    ObjectProperty[NounType](this, "nounType")

  private val nounKindProperty =
    ObjectProperty[NounKind](this, "nounKind")

  nounTypeProperty.onChange { (_, oldValue, newValue) =>
    if oldValue != newValue then
      properties = properties.copy(nounType = newValue)
  }

  nounKindProperty.onChange { (_, oldValue, newValue) =>
    if oldValue != newValue then
      properties = properties.copy(nounKind = newValue)
  }

  def nounType: NounType = nounTypeProperty.value

  def nounType_=(value: NounType): Unit = nounTypeProperty.value = value

  def nounKind: NounKind = nounKindProperty.value

  def nounKind_=(value: NounKind): Unit = nounKindProperty.value = value

  override protected def update(
    partOfSpeechType: NounPartOfSpeechType,
    properties: NounProperties
  ): NounProperties = properties.copy(partOfSpeech = partOfSpeechType)

  override protected def update(
    numberType: NumberType,
    properties: NounProperties
  ): NounProperties = properties.copy(number = numberType)

  override protected def update(
    genderType: GenderType,
    properties: NounProperties
  ): NounProperties = properties.copy(gender = genderType)

  override def update(
    nounStatus: NounStatus,
    properties: NounProperties
  ): NounProperties = properties.copy(status = nounStatus)

  override protected def update(properties: NounProperties): Unit = {
    super.update(properties)
    nounType = properties.nounType
    nounStatus = properties.status
  }
}

object NounPropertiesView {

  def apply(): NounPropertiesView = new NounPropertiesView()
}
