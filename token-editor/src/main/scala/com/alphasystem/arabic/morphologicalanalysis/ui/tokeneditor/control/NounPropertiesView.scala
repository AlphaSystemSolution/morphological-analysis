package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.{
  NounProperties,
  defaultNounProperties
}
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin.NounPropertiesSkin
import com.alphasystem.morphologicalanalysis.morphology.model.*
import javafx.scene.control.Skin
import scalafx.beans.property.ObjectProperty

class NounPropertiesView
    extends AbstractNounPropertiesView[NounPartOfSpeechType, NounProperties] {

  override protected val initialProperties: NounProperties =
    defaultNounProperties

  val nounTypeProperty: ObjectProperty[NounType] =
    ObjectProperty[NounType](this, "nounType")

  val nounKindProperty: ObjectProperty[NounKind] =
    ObjectProperty[NounKind](this, "nounKind")

  nounTypeProperty.onChange { (_, oldValue, newValue) =>
    if oldValue != newValue then
      properties = properties.copy(nounType = newValue)
  }

  nounKindProperty.onChange { (_, oldValue, newValue) =>
    if oldValue != newValue then
      properties = properties.copy(nounKind = newValue)
  }

  properties = initialProperties
  setSkin(createDefaultSkin())

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
    nounKind = properties.nounKind
  }

  override def createDefaultSkin(): Skin[_] = NounPropertiesSkin(this)
}

object NounPropertiesView {

  def apply(): NounPropertiesView = new NounPropertiesView()
}
