package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control

import morphology.model.AbstractProperties
import morphology.model.{ GenderType, NumberType, PartOfSpeechType }
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.Control
import scalafx.Includes.*
import scalafx.beans.property.ObjectProperty

abstract class AbstractPropertiesView[P <: PartOfSpeechType, AP <: AbstractProperties[P]]
    extends BasePropertiesView[P, AP] {

  protected val initialProperties: AP

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

  def numberType: NumberType = numberTypeProperty.value

  def numberType_=(value: NumberType): Unit = numberTypeProperty.value = value

  def genderType: GenderType = genderTypeProperty.value

  def genderType_=(value: GenderType): Unit = genderTypeProperty.value = value

  protected def update(numberType: NumberType, properties: AP): AP

  protected def update(genderType: GenderType, properties: AP): AP

  override protected def update(properties: AP): Unit = {
    super.update(properties)
    numberType = properties.number
    genderType = properties.gender
  }
}
