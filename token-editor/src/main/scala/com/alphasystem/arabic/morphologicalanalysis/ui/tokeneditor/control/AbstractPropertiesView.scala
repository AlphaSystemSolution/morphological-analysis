package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.AbstractProperties
import com.alphasystem.morphologicalanalysis.morphology.model.{
  GenderType,
  NumberType,
  PartOfSpeechType
}
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.Control
import scalafx.Includes.*
import scalafx.beans.property.ObjectProperty

abstract class AbstractPropertiesView[
  P <: PartOfSpeechType,
  AP <: AbstractProperties[P]]
    extends Control {

  protected val initialProperties: AP

  lazy val propertiesProperty: ObjectProperty[AP] =
    ObjectProperty[AP](this, "properties", initialProperties)

  val partOfSpeechTypeProperty: ObjectProperty[P] =
    ObjectProperty[P](this, "partOfSpeechType")

  val numberTypeProperty: ObjectProperty[NumberType] =
    ObjectProperty[NumberType](this, "numberType")

  val genderTypeProperty: ObjectProperty[GenderType] =
    ObjectProperty[GenderType](this, "genderType")

  // listeners

  partOfSpeechTypeProperty.onChange { (_, oldValue, newValue) =>
    if oldValue != newValue then properties = update(newValue, properties)
  }

  numberTypeProperty.onChange { (_, oldValue, newValue) =>
    if oldValue != newValue then properties = update(newValue, properties)
  }

  genderTypeProperty.onChange { (_, oldValue, newValue) =>
    if oldValue != newValue then properties = update(newValue, properties)
  }

  propertiesProperty.onChange { (_, oldValue, newValue) =>
    if oldValue != newValue then update(properties)
  }

  // getter & setters

  def partOfSpeechType: P = partOfSpeechTypeProperty.value

  def partOfSpeechType_=(value: P): Unit =
    partOfSpeechTypeProperty.value = value

  def numberType: NumberType = numberTypeProperty.value

  def numberType_=(value: NumberType): Unit = numberTypeProperty.value = value

  def genderType: GenderType = genderTypeProperty.value

  def genderType_=(value: GenderType): Unit = genderTypeProperty.value = value

  def properties: AP = propertiesProperty.value

  def properties_=(value: AP): Unit = propertiesProperty.value = value

  protected def update(partOfSpeechType: P, properties: AP): AP

  protected def update(numberType: NumberType, properties: AP): AP

  protected def update(genderType: GenderType, properties: AP): AP

  protected def update(properties: AP): Unit = {
    partOfSpeechType = properties.partOfSpeech
    numberType = properties.number
    genderType = properties.gender
  }
}
