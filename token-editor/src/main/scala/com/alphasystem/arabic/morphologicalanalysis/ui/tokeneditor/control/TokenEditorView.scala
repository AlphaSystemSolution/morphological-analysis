package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.{
  Location,
  Token,
  WordType
}
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache.LocationRequest
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin.TokenEditorSkin
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.service.ServiceFactory
import javafx.application.Platform
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, StringProperty }
import scalafx.collections.ObservableBuffer
import scalafx.concurrent.Service

class TokenEditorView(serviceFactory: ServiceFactory) extends Control {

  val tokenProperty: ObjectProperty[Token] =
    ObjectProperty[Token](this, "token")

  private[control] val selectedLocationProperty =
    ObjectProperty[Location](this, "selectedLocation")

  private[control] val translationTextProperty = StringProperty(null)

  private[control] val locationsProperty: ObservableBuffer[Location] =
    ObservableBuffer.empty[Location]

  private[control] val wordTypeProperty =
    ObjectProperty[WordType](this, "wordType")

  private val locationServiceF = serviceFactory.locationService

  def token: Token = tokenProperty.value
  def token_=(value: Token): Unit = tokenProperty.value = value

  def translationText: String = translationTextProperty.value
  def translationText_=(value: String): Unit =
    translationTextProperty.value = value

  def selectedLocation: Location = selectedLocationProperty.value
  def selectedLocation_=(value: Location): Unit =
    selectedLocationProperty.value = value

  def wordType: WordType = wordTypeProperty.value
  def wordType_=(value: WordType): Unit =
    wordTypeProperty.value = value

  // initialization
  selectedLocationProperty.onChange((_, _, nv) =>
    wordType = if Option(nv).isDefined then nv.wordType else null
  )
  tokenProperty.onChange((_, _, nv) => loadToken(nv))
  // TODO: update locations

  setSkin(createDefaultSkin())

  override def getUserAgentStylesheet: String = Thread
    .currentThread()
    .getContextClassLoader
    .getResource("application.css")
    .toExternalForm

  override def createDefaultSkin(): Skin[_] = TokenEditorSkin(this)

  private def loadToken(token: Token): Unit =
    if Option(token).isDefined then {
      translationText = token.translation.orNull
      loadLocations(token)
    } else clearFields()

  private def loadLocations(token: Token): Unit = {
    if Option(token).isDefined then {
      val locationService = locationServiceF(
        LocationRequest(
          token.chapterNumber,
          token.verseNumber,
          token.tokenNumber
        )
      )

      Platform.runLater(() => {
        locationService.onSucceeded = event => {
          clearFields()
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
        locationService.start()
      }) // end of Platform.runLater
    } else clearFields()
  }

  private def clearFields(): Unit = {
    locationsProperty.clear()
    selectedLocation = null
    translationText = null
    wordType = null
  }
}

object TokenEditorView {

  def apply(serviceFactory: ServiceFactory): TokenEditorView =
    new TokenEditorView(serviceFactory)
}
