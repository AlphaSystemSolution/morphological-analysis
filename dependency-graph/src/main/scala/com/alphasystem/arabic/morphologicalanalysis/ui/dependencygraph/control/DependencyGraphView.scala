package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import DependencyGraphOpenDialog.Result
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

  private lazy val openDialog = DependencyGraphOpenDialog(serviceFactory)
  private lazy val createDialog = NewGraphDialog(serviceFactory)
  private[control] val canvasView = CanvasView(serviceFactory)
  private[control] val graphSettingsView = GraphSettingsView()

  graphSettingsView.graphMetaInfo = canvasView.graphMetaInfo
  canvasView.graphMetaInfoWrapperProperty.bindBidirectional(graphSettingsView.graphMetaInfoProperty)
  canvasView.selectedNodeProperty.bindBidirectional(selectedNodeProperty)
  setSkin(createDefaultSkin())

  def createNewGraph(): Unit = {
    UiUtilities.toWaitCursor(this)
    Platform.runLater(() =>
      createDialog.showAndWait() match
        case Some(Some(_)) =>
          UiUtilities.toDefaultCursor(this)

        case _ => UiUtilities.toDefaultCursor(this)
    )
  }

  // TODO: remove this
  /*def createNewGraphOld(): Unit =
    Platform.runLater(() => {
      // TODO: ask to save current graph if applicable
      val selectedTokens = verseSelectionView.selectedTokens.toSeq.map(_.userData)
      if selectedTokens.isEmpty then {
        // TODO: show error Alert
        println("Please select a verse")
      } else {
        verseSelectionView.clearSelection = false
        verseSelectionView.clearSelection = true
        canvasView.loadNewGraph(verseSelectionView.selectedChapter.userData.chapterName, selectedTokens)
      }
    })*/

  def saveGraph(): Unit = {
    UiUtilities.toWaitCursor(this)
    Platform.runLater(() => {
      val service = serviceFactory.saveDependencyGraphService(
        SaveDependencyGraphRequest(canvasView.dependencyGraph, canvasView.graphNodes)
      )

      service.onSucceeded = event => {
        UiUtilities.toDefaultCursor(this)
        event.consume()
      }

      service.onFailed = event => {
        Console.err.println(s"Failed to save dependency graph: $event")
        event.getSource.getException.printStackTrace()
        UiUtilities.toDefaultCursor(this)
        event.consume()
      }

      service.start()
    })
  }

  def openGraph(): Unit = {
    UiUtilities.toWaitCursor(this)
    Platform.runLater(() => {
      openDialog.showAndWait() match
        case Some(Result(Some(dependencyGraph))) =>
          canvasView.loadGraph(dependencyGraph)
          UiUtilities.toWaitCursor(this)

        case _ => UiUtilities.toDefaultCursor(this)
    })
  }

  def selectedNode: GraphNode = selectedNodeProperty.value
  def selectedNode_=(value: GraphNode): Unit = selectedNodeProperty.value = value

  override def createDefaultSkin(): Skin[_] = DependencyGraphSkin(this)
}

object DependencyGraphView {

  def apply(serviceFactory: ServiceFactory): DependencyGraphView = new DependencyGraphView(serviceFactory)
}
