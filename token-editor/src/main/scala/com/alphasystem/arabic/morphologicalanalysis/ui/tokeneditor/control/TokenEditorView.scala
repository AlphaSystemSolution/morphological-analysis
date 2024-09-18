package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control

import morphologicalanalysis.ui.commons.service.ServiceFactory
import morphology.model.Location
import skin.TokenEditorSkin
import fx.ui.util.UiUtilities
import javafx.application.Platform
import javafx.scene.control.{ Control, Skin }
import scalafx.Includes.*
import scalafx.beans.property.{
  ReadOnlyBooleanProperty,
  ReadOnlyBooleanWrapper,
  ReadOnlyStringProperty,
  ReadOnlyStringWrapper
}
import scalafx.collections.ObservableBuffer

class TokenEditorView(serviceFactory: ServiceFactory) extends Control {

  private val titlePropertyWrapper = ReadOnlyStringWrapper("")

  private val hasSelectedTokensWrapper = ReadOnlyBooleanWrapper(false)

  private[control] val tokenSelectionView = TokenSelectionView(serviceFactory)

  private[control] val tokenView = TokenView()

  private[control] val locationView = LocationView()

  tokenView
    .tokenProperty
    .onChange((_, _, nv) => {
      titlePropertyWrapper.value = Option(nv).map(_.displayName).getOrElse("")
    })

  tokenView
    .locationsProperty
    .onChange((_, changes) => {
      changes.foreach {
        case ObservableBuffer.Add(_, added)      => locationView.addProperties(added)
        case ObservableBuffer.Remove(_, removed) => locationView.removeProperties(removed)
        case _                                   => ()
      }
    })

  tokenView
    .selectedLocationProperty
    .onChange((_, _, nv) => {
      if Option(nv).isDefined && locationView.location != nv then locationView.location = nv
    })

  tokenSelectionView
    .selectedTokenProperty
    .onChange((_, _, nv) =>
      if Option(nv).isDefined then tokenView.token = nv.userData
      else tokenView.token = null
    )

  tokenSelectionView
    .selectedTokens
    .onChange((buffer, _) => {
      hasSelectedTokensWrapper.value = buffer.nonEmpty && buffer.size > 1
    })

  setSkin(createDefaultSkin())

  def title: String = titleProperty.value
  def titleProperty: ReadOnlyStringProperty = titlePropertyWrapper.readOnlyProperty

  def hasSelectedTokens: ReadOnlyBooleanProperty = hasSelectedTokensWrapper.readOnlyProperty

  override def createDefaultSkin(): Skin[?] = TokenEditorSkin(this)

  def saveLocations(): Unit = {
    UiUtilities.toWaitCursor(this)
    val token = tokenView.tokenProperty.value
    if Option(token).isDefined then {
      val updatedLocations =
        tokenView
          .locationsProperty
          .toList
          .map { location =>
            locationView.propertiesMapProperty.get(location.id) match
              case Some((wordType, properties)) => location.copy(wordType = wordType, properties = properties)
              case None                         => location
          }

      val updatedToken = token.copy(translation = Option(tokenView.translationText), locations = updatedLocations)
      val service = serviceFactory.saveData(updatedToken)

      service.onSucceeded = event => {
        tokenSelectionView.refresh(token, updatedToken)
        UiUtilities.toDefaultCursor(this)
        event.consume()
      }

      service.onFailed = event => {
        UiUtilities.toDefaultCursor(this)
        event.consume()
      }

      Platform.runLater { () => service.start() }

    } else UiUtilities.toDefaultCursor(this)
  }

  def mergeTokens(): Unit = {
    val selectedTokens = tokenSelectionView.selectedTokens.toSeq.map(_.userData).sortBy(_.tokenNumber).toList
    val tokens = tokenSelectionView.tokens.map(_.userData)
    val newTokens = merge(selectedTokens, tokens)

    val service = serviceFactory.recreateTokens(newTokens)

    service.onSucceeded = event => {
      val chapter = tokenSelectionView.selectedChapter
      val verse = tokenSelectionView.selectedVerse
      tokenSelectionView.selectedChapter = null
      tokenSelectionView.selectedChapter = chapter
      tokenSelectionView.selectedVerse = verse
      event.consume()
    }

    service.onFailed = event => {
      event.getSource.getException.printStackTrace()
      event.consume()
    }

    Platform.runLater(() => service.start())

    tokenSelectionView.doClearSelection()
  }
}

object TokenEditorView {

  def apply(serviceFactory: ServiceFactory): TokenEditorView = new TokenEditorView(serviceFactory)
}
