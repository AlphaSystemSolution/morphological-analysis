package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import fx.ui.util.UiUtilities
import morphology.graph.model.GraphNode
import skin.{ DependencyGraphSkin, DependencyGraphVerseSelectionSkin }
import commons.service.{ SaveDependencyGraphRequest, ServiceFactory }
import javafx.application.Platform
import javafx.scene.control.{ Control, Skin }
import scalafx.Includes.*
import scalafx.beans.property.ObjectProperty

class DependencyGraphView(serviceFactory: ServiceFactory) extends Control {

  val selectedNodeProperty: ObjectProperty[GraphNode] =
    ObjectProperty[GraphNode](this, "selectedNode", defaultTerminalNode)

  private[control] val canvasView = CanvasView(serviceFactory)
  private[control] val verseSelectionView = DependencyGraphVerseSelectionView(serviceFactory)
  private[control] val graphSettingsView = GraphSettingsView()

  graphSettingsView.graphMetaInfo = canvasView.graphMetaInfo
  canvasView.graphMetaInfoWrapperProperty.bindBidirectional(graphSettingsView.graphMetaInfoProperty)
  canvasView.selectedNodeProperty.bindBidirectional(selectedNodeProperty)
  setSkin(createDefaultSkin())

  def createNewGraph(): Unit =
    Platform.runLater(() => {
      // TODO: ask to save current graph if applicable
      val selectedTokens = verseSelectionView.selectedTokens.toSeq.map(_.userData)
      if selectedTokens.isEmpty then {
        // TODO: show error Alert
        println("Please select a verse")
      } else {
        verseSelectionView.clearSelection = false
        verseSelectionView.clearSelection = true
        canvasView.loadNewGraph(selectedTokens)
      }
    })

  def saveGraph(): Unit = {
    Platform.runLater(() => {
      UiUtilities.toWaitCursor(this)
      val service = serviceFactory.saveDependencyGraphService(
        SaveDependencyGraphRequest(canvasView.dependencyGraph, canvasView.graphNodes)
      )

      service.onSucceeded = event => {
        UiUtilities.toDefaultCursor(this)
        event.consume()
      }

      service.onFailed = event => {
        Console.err.println(s"Failed to save dependency graph: $event")
        UiUtilities.toDefaultCursor(this)
        event.consume()
      }

      service.start()
    })
  }

  def openGraph(): Unit =
    Platform.runLater(() => {})

  def selectedNode: GraphNode = selectedNodeProperty.value
  def selectedNode_=(value: GraphNode): Unit = selectedNodeProperty.value = value

  override def createDefaultSkin(): Skin[_] = DependencyGraphSkin(this)
}

object DependencyGraphView {

  def apply(serviceFactory: ServiceFactory): DependencyGraphView = new DependencyGraphView(serviceFactory)
}
