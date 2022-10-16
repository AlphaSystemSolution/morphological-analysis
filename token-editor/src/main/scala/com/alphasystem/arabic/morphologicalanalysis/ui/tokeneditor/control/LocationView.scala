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

  private[control] val wordTypeProperty =
    ObjectProperty[WordType](this, "wordType")

  private[control] val locationPropertiesProperty =
    ObjectProperty[WordProperties](this, "properties")

  // initializations
  locationProperty.onChange((_, _, nv) =>
    wordType = if Option(nv).isDefined then nv.wordType else WordType.NOUN
  )
  wordTypeProperty.onChange((_, _, nv) => properties = nv.properties)

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
}

object LocationView {

  def apply(): LocationView = new LocationView()
}
