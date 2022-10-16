package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.{
  Location,
  WordProperties,
  WordType
}
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin.LocationSkin
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
  locationProperty.onChange((_, _, nv) =>
    wordType = if Option(nv).isDefined then nv.wordType else WordType.NOUN
  )
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
      if location.wordType != newWordType && location.properties != wordProperties then {
        location =
          location.copy(wordType = newWordType, properties = wordProperties)
      }
      if location.wordType != newWordType then wordType = newWordType
      if location.properties != properties then properties = wordProperties
    } else {
      wordType = WordType.NOUN
      properties = WordType.NOUN.properties
    }
  }
}

object LocationView {

  def apply(): LocationView = new LocationView()
}
