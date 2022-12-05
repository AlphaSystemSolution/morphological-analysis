package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import utils.GraphBuilderService
import fx.ui.util.UiUtilities
import morphology.graph.model.GraphNodeMetaInfo
import skin.DependencyGraphSkin
import commons.service.ServiceFactory
import javafx.application.Platform
import javafx.scene.control.{ Control, Skin }
import scalafx.Includes.*
import scalafx.beans.property.ObjectProperty

class DependencyGraphView(serviceFactory: ServiceFactory) extends Control {

  val selectedNodeProperty: ObjectProperty[GraphNodeMetaInfo] =
    ObjectProperty[GraphNodeMetaInfo](this, "selectedNode", defaultTerminalNodeMetaInfo)

  private lazy val openDialog = DependencyGraphOpenDialog(serviceFactory)
  private lazy val createDialog = NewGraphDialog(serviceFactory)
  private[control] val canvasView = CanvasView()
  private[control] val graphSettingsView = GraphSettingsView()
  private val graphBuilderService = GraphBuilderService(serviceFactory)

  graphSettingsView.graphMetaInfo = canvasView.graphMetaInfo
  canvasView.graphMetaInfoWrapperProperty.bindBidirectional(graphSettingsView.graphMetaInfoProperty)
  canvasView.selectedNodeProperty.bindBidirectional(selectedNodeProperty)
  setSkin(createDefaultSkin())

  def createNewGraph(): Unit = {
    UiUtilities.toWaitCursor(this)
    Platform.runLater(() =>
      createDialog.showAndWait() match
        case Some(NewDialogResult(Some(chapter), tokens)) if tokens.nonEmpty =>
          graphBuilderService.createGraph(chapter, tokens, canvasView.loadGraph)
          UiUtilities.toDefaultCursor(this)

        case _ => UiUtilities.toDefaultCursor(this)
    )
  }

  def saveGraph(): Unit = {
    UiUtilities.toWaitCursor(this)
    val service = serviceFactory.createDependencyGraphService(
      canvasView.dependencyGraph.copy(nodes = canvasView.graphNodes)
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

    Platform.runLater(() => service.start())
  }

  def openGraph(): Unit = {
    UiUtilities.toWaitCursor(this)
    Platform.runLater(() => {
      openDialog.showAndWait() match
        case Some(OpenDialogResult(Some(dependencyGraph))) =>
          graphBuilderService.loadGraph(dependencyGraph, canvasView.loadGraph)
          UiUtilities.toDefaultCursor(this)

        case _ => UiUtilities.toDefaultCursor(this)
    })
  }

  def selectedNode: GraphNodeMetaInfo = selectedNodeProperty.value
  def selectedNode_=(value: GraphNodeMetaInfo): Unit = selectedNodeProperty.value = value

  override def createDefaultSkin(): Skin[_] = DependencyGraphSkin(this)
}

object DependencyGraphView {

  def apply(serviceFactory: ServiceFactory): DependencyGraphView = new DependencyGraphView(serviceFactory)
}
