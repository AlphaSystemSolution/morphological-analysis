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

  private[control] val locationPropertiesProperty =
    ObjectProperty[WordProperties](this, "properties")

  // initializations
  locationProperty.onChange((_, _, nv) =>
    properties = if Option(nv).isDefined then nv.properties else null
  )

  properties = WordType.NOUN.properties
  setSkin(createDefaultSkin())

  // getters & setters

  def location: Location = locationProperty.value
  def location_=(value: Location): Unit = locationProperty.value = value

  def properties: WordProperties = locationPropertiesProperty.value
  def properties_=(value: WordProperties): Unit =
    locationPropertiesProperty.value = value

  override def createDefaultSkin(): Skin[_] = LocationSkin(this)
}

object LocationView {

  def apply(): LocationView = new LocationView()
}
