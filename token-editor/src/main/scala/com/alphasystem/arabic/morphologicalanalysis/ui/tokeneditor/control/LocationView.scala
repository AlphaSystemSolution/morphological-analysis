package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.WordType.{ NOUN, PARTICLE, PRO_NOUN, VERB }
import com.alphasystem.arabic.morphologicalanalysis.morphology.model.{
  Location,
  NounProperties,
  ParticleProperties,
  ProNounProperties,
  VerbProperties,
  WordProperties,
  WordType
}
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin.LocationSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableMap
import scalafx.event.subscriptions.Subscription

import scala.collection.mutable.ListBuffer

class LocationView extends Control {

  val locationProperty: ObjectProperty[Location] =
    ObjectProperty[Location](this, "location")

  val wordTypeProperty: ObjectProperty[WordType] =
    ObjectProperty[WordType](this, "wordType")

  val locationPropertiesProperty: ObjectProperty[WordProperties] =
    ObjectProperty[WordProperties](this, "properties")

  val propertiesMapProperty: ObservableMap[String, (WordType, WordProperties)] =
    ObservableMap.empty[String, (WordType, WordProperties)]

  private val subscriptions = ListBuffer.empty[Subscription]

  // initializations & bindings
  locationProperty.onChange((_, _, nv) => {
    if Option(nv).isDefined then {
      propertiesMapProperty.get(nv.id) match
        case Some(newWordType, newProperties) =>
          subscriptions.foreach(_.cancel())
          subscriptions.clear()
          wordType = newWordType
          properties = newProperties
          addSubscriptions()

        case None => ()
    }
  })

  addSubscriptions()
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

  def addProperties(added: Iterable[Location]): Unit = {
    propertiesMapProperty.clear()
    added.foreach(location => propertiesMapProperty.addOne(location.id, (location.wordType, location.properties)))
    if added.nonEmpty then {
      val head = propertiesMapProperty(added.head.id)
      wordType = head._1
      locationPropertiesProperty.value = head._2
    }
  }

  def removeProperties(removed: Iterable[Location]): Unit = {
    removed.map(_.id).foreach(propertiesMapProperty.remove)
    if propertiesMapProperty.nonEmpty then
      wordType = propertiesMapProperty.headOption.map(_._2._1).getOrElse(WordType.NOUN)
      locationPropertiesProperty.value =
        propertiesMapProperty.headOption.map(_._2._2).getOrElse(WordType.NOUN.properties)
  }

  private def addSubscriptions(): Unit = {
    subscriptions.addOne(wordPropertySubscription)
    subscriptions.addOne(propertiesSubscription)
  }
  private def wordPropertySubscription =
    wordTypeProperty.onChange((_, _, nv) => {
      val newWordType = Option(nv).getOrElse(WordType.NOUN)
      val newProperties = newWordType.properties
      locationPropertiesProperty.value = newProperties

      if Option(location).isDefined then {
        val id = location.id
        propertiesMapProperty.remove(id)
        propertiesMapProperty.addOne(id, (newWordType, newProperties))
      }
    })

  private def propertiesSubscription =
    locationPropertiesProperty.onChange((_, _, nv) => {
      val (newWordType, newProperties) =
        Option(nv).map(wp => (wordType, wp)).getOrElse(WordType.NOUN, WordType.NOUN.properties)

      if Option(location).isDefined then {
        val id = location.id
        propertiesMapProperty.remove(id)
        propertiesMapProperty.addOne(id, (newWordType, newProperties))
      }
    })
}

object LocationView {

  def apply(): LocationView = new LocationView()
}
