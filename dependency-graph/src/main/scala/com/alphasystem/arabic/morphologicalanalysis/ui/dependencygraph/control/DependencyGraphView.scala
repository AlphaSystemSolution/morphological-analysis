package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import skin.{ DependencyGraphSkin, DependencyGraphVerseSelectionSkin }
import commons.service.ServiceFactory
import javafx.application.Platform
import javafx.scene.control.{ Control, Skin }

class DependencyGraphView(serviceFactory: ServiceFactory) extends Control {

  private[control] val canvasView = CanvasView()
  private[control] val verseSelectionView = DependencyGraphVerseSelectionView(serviceFactory)
  private[control] val graphSettingsView = GraphSettingsView()

  graphSettingsView.graphMetaInfo = canvasView.graphMetaInfo
  canvasView.graphMetaInfoWrapperProperty.bindBidirectional(graphSettingsView.graphMetaInfoProperty)
  setSkin(createDefaultSkin())

  def createNewGraph(): Unit = {
    Platform.runLater(() => {
      // TODO: ask to save current graph if applicable
      val selectedTokens = verseSelectionView.selectedTokens.toSeq
      if selectedTokens.isEmpty then {
        // TODO: show error Alert
        println("Please select a verse")
      } else {
        verseSelectionView.clearSelection = false
        verseSelectionView.clearSelection = true
      }
    })
  }
  override def createDefaultSkin(): Skin[_] = DependencyGraphSkin(this)
}

object DependencyGraphView {

  def apply(serviceFactory: ServiceFactory): DependencyGraphView = new DependencyGraphView(serviceFactory)
}
