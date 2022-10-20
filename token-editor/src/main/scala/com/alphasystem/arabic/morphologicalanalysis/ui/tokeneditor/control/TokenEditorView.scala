package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control

import morphology.model.{ Location, Token }
import morphology.persistence.cache.*
import skin.TokenEditorSkin
import service.ServiceFactory
import fx.ui.util.UiUtilities
import javafx.application.Platform
import javafx.scene.control.{ Control, Skin }
import scalafx.Includes.*
import scalafx.beans.property.{ ReadOnlyStringProperty, ReadOnlyStringWrapper }
import scalafx.collections.ObservableBuffer

import java.util.concurrent.{ Executors, TimeUnit }

class TokenEditorView(serviceFactory: ServiceFactory) extends Control {

  private val executorService = Executors.newSingleThreadScheduledExecutor()

  private val titlePropertyWrapper = ReadOnlyStringWrapper("")

  private[control] val chapterVerseSelectionView =
    ChapterVerseSelectionView(serviceFactory)

  private[control] val tokenView = TokenView(serviceFactory)

  private[control] val locationView = LocationView()

  tokenView
    .tokenProperty
    .onChange((_, _, nv) => {
      titlePropertyWrapper.value = Option(nv).map(_.displayName).getOrElse("")
    })

  tokenView
    .selectedLocationProperty
    .onChange((_, _, nv) => {
      if Option(nv).isDefined && locationView.location != nv then locationView.location = nv
    })

  locationView
    .locationProperty
    .onChange((_, _, nv) => if Option(nv).isDefined then tokenView.updateLocation(nv.wordType, nv.properties))

  chapterVerseSelectionView
    .selectedTokenProperty
    .onChange((_, _, nv) =>
      if Option(nv).isDefined then tokenView.token = nv.userData
      else tokenView.token = null
    )

  setSkin(createDefaultSkin())

  def title: String = titleProperty.value
  def titleProperty: ReadOnlyStringProperty = titlePropertyWrapper.readOnlyProperty

  override def createDefaultSkin(): Skin[_] = TokenEditorSkin(this)

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
    val token = tokenView.tokenProperty.value
    if Option(token).isDefined && tokenView.locationsProperty.nonEmpty then {
      val service = serviceFactory.createLocations(token.toLocationRequest, tokenView.locationsProperty.toList)

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
