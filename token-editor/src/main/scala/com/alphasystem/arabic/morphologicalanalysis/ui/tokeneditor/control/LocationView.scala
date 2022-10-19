package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control

import morphology.model.WordType.{ NOUN, PARTICLE, PRO_NOUN, VERB }
import morphology.model.{
  Location,
  NounProperties,
  ParticleProperties,
  ProNounProperties,
  VerbProperties,
  WordProperties,
  WordType
}
import skin.LocationSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.ObjectProperty

class LocationView extends Control {

  val locationProperty: ObjectProperty[Location] =
    ObjectProperty[Location](this, "location")

  val wordTypeProperty: ObjectProperty[WordType] =
    ObjectProperty[WordType](this, "wordType")

  val locationPropertiesProperty: ObjectProperty[WordProperties] =
    ObjectProperty[WordProperties](this, "properties")

  // initializations & bindings
  locationProperty.onChange((_, _, nv) => {
    if Option(nv).isDefined then
      if nv.wordType != wordType then wordType = nv.wordType
    else wordType = WordType.NOUN
  })
  wordTypeProperty.onChange((_, _, nv) => updateData(nv, nv.properties))
  locationPropertiesProperty.onChange((_, _, nv) => updateData(wordType, nv))

  wordType = WordType.NOUN
  setSkin(createDefaultSkin())

  // getters & setters

  def location: Location = locationProperty.value
  def location_=(value: Location): Unit = locationProperty.value = value

  def wordType: WordType = wordTypeProperty.value
  def wordType_=(value: WordType): Unit = wordTypeProperty.value = value

  def properties: WordProperties = locationPropertiesProperty.value
  def properties_=(value: WordProperties): Unit =
    locationPropertiesProperty.value = value

  override def getUserAgentStylesheet: String = Thread
    .currentThread()
    .getContextClassLoader
    .getResource("application.css")
    .toExternalForm

  override def createDefaultSkin(): Skin[_] = LocationSkin(this)

  private def updateData(
    newWordType: WordType,
    wordProperties: WordProperties
  ): Unit = {
    if Option(location).isDefined then {
      val newLocation = location.copy(wordType = newWordType, properties = wordProperties)
      if newLocation != location then location = newLocation
    } else {
      wordType = WordType.NOUN
      properties = WordType.NOUN.properties
    }
  }
}

object LocationView {

  def apply(): LocationView = new LocationView()
}
