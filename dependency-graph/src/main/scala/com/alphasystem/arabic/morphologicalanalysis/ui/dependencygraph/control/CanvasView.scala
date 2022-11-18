package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import fx.ui.util.UiUtilities
import morphology.persistence.cache.*
import morphology.model.{ Location, Token }
import commons.service.ServiceFactory
import javafx.application.Platform
import morphology.graph.model.{ DependencyGraph, GraphMetaInfo, GraphNode, PartOfSpeechNode, TerminalNode }
import skin.CanvasSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, ReadOnlyObjectProperty, ReadOnlyObjectWrapper }
import scalafx.scene.Node

class CanvasView(serviceFactory: ServiceFactory) extends Control {

  val dependencyGraphProperty: ObjectProperty[DependencyGraph] =
    ObjectProperty[DependencyGraph](this, "dependencyGraph", defaultDependencyGraph)

  private[control] val graphMetaInfoWrapperProperty =
    ReadOnlyObjectWrapper[GraphMetaInfo](this, "", defaultGraphMetaInfo)

  val selectedNodeProperty: ObjectProperty[GraphNode] = ObjectProperty[GraphNode](this, "selectedNode")

  // initializations & bindings
  dependencyGraphProperty.onChange((_, _, nv) => graphMetaInfo = nv.metaInfo)
  graphMetaInfoProperty.onChange((_, _, nv) => dependencyGraph = dependencyGraph.copy(metaInfo = nv))
  private val skin = createDefaultSkin()
  setSkin(skin)

  // getters & setters
  def dependencyGraph: DependencyGraph = dependencyGraphProperty.value
  def dependencyGraph_=(value: DependencyGraph): Unit = dependencyGraphProperty.value = value

  def graphMetaInfo: GraphMetaInfo = graphMetaInfoWrapperProperty.value
  private[control] def graphMetaInfo_=(value: GraphMetaInfo): Unit = graphMetaInfoWrapperProperty.value = value
  def graphMetaInfoProperty: ReadOnlyObjectProperty[GraphMetaInfo] = graphMetaInfoWrapperProperty.readOnlyProperty

  def selectedNode: GraphNode = selectedNodeProperty.value
  def selectedNode_=(value: GraphNode): Unit = selectedNodeProperty.value = value

  def graphNodes: Seq[GraphNode] = skin.nodesMap.values.map(_.source.asInstanceOf[GraphNode]).toSeq

  override def createDefaultSkin(): CanvasSkin = CanvasSkin(this)

  def loadNewGraph(
    dg: DependencyGraph,
    terminalNodes: Seq[TerminalNode],
    posNodes: Map[String, Seq[PartOfSpeechNode]]
  ): Unit = {
    dependencyGraph = dg
    skin.createGraph(terminalNodes, posNodes)
  }

  def loadGraph(graph: DependencyGraph): Unit = {
    val graphId = graph.id
    val service = serviceFactory.getGraphNodesService(graphId)

    service.onFailed = event => {
      Console.err.println(s"Unable to load nodes for graph: $graphId")
      event.getSource.getException.printStackTrace()
      event.consume()
    }

    service.onSucceeded = event => {
      dependencyGraph = graph
      skin.loadGraph(graph.metaInfo, event.getSource.getValue.asInstanceOf[List[GraphNode]])
      event.consume()
    }

    Platform.runLater(() => service.start())
  }
}

object CanvasView {
  def apply(serviceFactory: ServiceFactory): CanvasView = new CanvasView(serviceFactory)
}
