package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.{ Location, Token }
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache.LocationRequest
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin.TokenEditorSkin
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.service.ServiceFactory
import com.alphasystem.fx.ui.util.UiUtilities
import javafx.application.Platform
import javafx.scene.control.{ Control, Skin }
import scalafx.Includes.*
import scalafx.beans.property.{ ObjectProperty, ReadOnlyStringProperty, ReadOnlyStringWrapper, StringProperty }
import scalafx.collections.ObservableBuffer

import java.util.concurrent.{ Executors, TimeUnit }

class TokenEditorView(serviceFactory: ServiceFactory) extends Control {

  private val executorService = Executors.newSingleThreadScheduledExecutor()

  private val titlePropertyWrapper = ReadOnlyStringWrapper("")

  private[control] val tokenProperty: ObjectProperty[Token] =
    ObjectProperty[Token](this, "token")

  private[control] val locationsProperty: ObservableBuffer[Location] =
    ObservableBuffer.empty[Location]

  tokenProperty.onChange((_, _, nv) => {
    titlePropertyWrapper.value = Option(nv).map(_.displayName).getOrElse("")
  })

  setSkin(createDefaultSkin())

  def title: String = titleProperty.value

  def titleProperty: ReadOnlyStringProperty = titlePropertyWrapper.readOnlyProperty

  override def createDefaultSkin(): Skin[_] =
    new TokenEditorSkin(this, serviceFactory)

  def saveLocations(): Unit =
    executorService.schedule(
      new Runnable {
        override def run(): Unit = saveLocationsInternal()
      },
      500,
      TimeUnit.MILLISECONDS
    )

  private def saveLocationsInternal(): Unit = {
    UiUtilities.toWaitCursor(this)
    val token = tokenProperty.value
    if Option(token).isDefined && locationsProperty.nonEmpty then {
      val service = serviceFactory.createLocations(
        LocationRequest(token.chapterNumber, token.verseNumber, token.tokenNumber),
        locationsProperty.toList
      )

      service.onSucceeded = event => {
        UiUtilities.toDefaultCursor(this)
        event.consume()
      }

      service.onFailed = event => {
        UiUtilities.toDefaultCursor(this)
        event.consume()
      }

      Platform.runLater { () =>
        service.start()
      }

    } else UiUtilities.toDefaultCursor(this)
  }
}

object TokenEditorView {

  def apply(serviceFactory: ServiceFactory): TokenEditorView =
    new TokenEditorView(serviceFactory)
}
