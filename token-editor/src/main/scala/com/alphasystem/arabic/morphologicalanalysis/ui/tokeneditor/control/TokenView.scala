package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control

import morphology.model.{ Location, Token, WordProperties, WordType }
import morphology.persistence.cache.LocationRequest
import skin.TokenSkin
import service.ServiceFactory
import javafx.application.Platform
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, StringProperty }
import scalafx.collections.ObservableBuffer
import scalafx.concurrent.Service

class TokenView(serviceFactory: ServiceFactory) extends Control {

  val tokenProperty: ObjectProperty[Token] =
    ObjectProperty[Token](this, "token")

  val selectedLocationProperty: ObjectProperty[Location] =
    ObjectProperty[Location](this, "selectedLocation")

  private[control] val translationTextProperty = StringProperty(null)

  private[control] val locationsProperty: ObservableBuffer[Location] =
    ObservableBuffer.empty[Location]

  private val locationServiceF = serviceFactory.locationService

  def token: Token = tokenProperty.value
  def token_=(value: Token): Unit = tokenProperty.value = value

  def translationText: String = translationTextProperty.value
  def translationText_=(value: String): Unit =
    translationTextProperty.value = value

  def selectedLocation: Location = selectedLocationProperty.value
  def selectedLocation_=(value: Location): Unit =
    selectedLocationProperty.value = value

  // initialization
  tokenProperty.onChange((_, _, nv) => loadToken(nv))

  setSkin(createDefaultSkin())

  override def getUserAgentStylesheet: String = Thread
    .currentThread()
    .getContextClassLoader
    .getResource("application.css")
    .toExternalForm

  override def createDefaultSkin(): Skin[_] = TokenSkin(this)

  def updateLocation(wordType: WordType, properties: WordProperties): Unit = {
    if Option(selectedLocation).isDefined then
      refresh(
        selectedLocation.copy(wordType = wordType, properties = properties)
      )
  }

  private[control] def refresh(
    updatedLocation: Location,
    newLocation: Option[Location] = None
  ): Unit = {
    val selectedLocationId = selectedLocation.id
    val currentIndex = locationsProperty
      .toArray
      .zipWithIndex
      .find(_._1.id == selectedLocationId)
      .map(_._2)
      .getOrElse(-1)

    if currentIndex >= 0 then {
      val currentLocations = ObservableBuffer.from(locationsProperty)
      locationsProperty.clear()
      currentLocations.update(currentIndex, updatedLocation)
      newLocation.foreach(currentLocations.addOne)
      locationsProperty.addAll(currentLocations.toArray)
      selectedLocation = null
      selectedLocation = updatedLocation
    }
  }

  private def loadToken(token: Token): Unit =
    if Option(token).isDefined then {
      translationText = token.translation.orNull
      loadLocations(token)
    } else clearFields()

  private def loadLocations(token: Token): Unit = {
    if Option(token).isDefined then {
      clearFields()

      val locationService = locationServiceF(
        LocationRequest(
          token.chapterNumber,
          token.verseNumber,
          token.tokenNumber
        )
      )

      Platform.runLater(() => {
        locationService.start()
      })

      locationService.onSucceeded = event => {
        val savedLocations =
          event.getSource.getValue.asInstanceOf[Seq[Location]]

        val locations =
          if savedLocations.isEmpty then
            Seq(
              Location(
                chapterNumber = token.chapterNumber,
                verseNumber = token.verseNumber,
                tokenNumber = token.tokenNumber,
                locationNumber = 1,
                hidden = false,
                startIndex = 0,
                endIndex = token.token.length,
                derivedText = token.token,
                text = token.token,
                alternateText = token.token
              )
            )
          else savedLocations

        locationsProperty.addAll(locations)
        selectedLocation = locations.head
        event.consume()
      }

      locationService.onFailed = event => {
        println(s">>>> ${event.getSource.getException}")
        event.consume()
      }
    } else clearFields()
  }

  private def clearFields(): Unit = {
    locationsProperty.clear()
    selectedLocation = null
    translationText = null
  }
}

object TokenView {

  def apply(serviceFactory: ServiceFactory): TokenView =
    new TokenView(serviceFactory)
}
