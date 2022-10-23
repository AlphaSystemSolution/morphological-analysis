package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control

import morphology.model.BaseProperties
import morphology.model.{ GenderType, NumberType, PartOfSpeechType }
import javafx.scene.control.Control
import scalafx.Includes.*
import scalafx.beans.property.ObjectProperty

abstract class BasePropertiesView[P <: PartOfSpeechType, AP <: BaseProperties[P]] extends Control {

  protected val initialProperties: AP

  lazy val propertiesProperty: ObjectProperty[AP] =
    ObjectProperty[AP](this, "properties", initialProperties)

  val partOfSpeechTypeProperty: ObjectProperty[P] =
    ObjectProperty[P](this, "partOfSpeechType")

  // listeners

  partOfSpeechTypeProperty.onChange { (_, oldValue, newValue) =>
    if oldValue != newValue then properties = update(newValue, properties)
  }

  propertiesProperty.onChange { (_, oldValue, newValue) =>
    if oldValue != newValue then update(properties)
  }

  // getter & setters

  def partOfSpeechType: P = partOfSpeechTypeProperty.value

  def partOfSpeechType_=(value: P): Unit =
    partOfSpeechTypeProperty.value = value

  def properties: AP = propertiesProperty.value

  def properties_=(value: AP): Unit = propertiesProperty.value = value

  protected def update(partOfSpeechType: P, properties: AP): AP

  protected def update(properties: AP): Unit = {
    partOfSpeechType = properties.partOfSpeech
  }

  override def getUserAgentStylesheet: String = Thread
    .currentThread()
    .getContextClassLoader
    .getResource("application.css")
    .toExternalForm
}
