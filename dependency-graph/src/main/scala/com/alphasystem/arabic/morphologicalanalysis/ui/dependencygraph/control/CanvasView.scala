package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import fx.ui.util.UiUtilities
import morphology.persistence.cache.*
import morphology.model.{ Location, Token }
import javafx.application.Platform
import morphology.graph.model.{ DependencyGraph, GraphMetaInfo, GraphNodeMetaInfo, PartOfSpeechNode, TerminalNode }
import skin.CanvasSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, ReadOnlyObjectProperty, ReadOnlyObjectWrapper }
import scalafx.scene.Node

class CanvasView extends Control {

  val dependencyGraphProperty: ObjectProperty[DependencyGraph] =
    ObjectProperty[DependencyGraph](this, "dependencyGraph", defaultDependencyGraph)

  private[control] val graphMetaInfoWrapperProperty =
    ReadOnlyObjectWrapper[GraphMetaInfo](this, "", defaultGraphMetaInfo)

  val selectedNodeProperty: ObjectProperty[GraphNodeMetaInfo] = ObjectProperty[GraphNodeMetaInfo](this, "selectedNode")

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

  def selectedNode: GraphNodeMetaInfo = selectedNodeProperty.value
  def selectedNode_=(value: GraphNodeMetaInfo): Unit = selectedNodeProperty.value = value

  def graphNodes: Seq[GraphNodeMetaInfo] = skin.nodesMap.values.map(_.source.asInstanceOf[GraphNodeMetaInfo]).toSeq

  override def createDefaultSkin(): CanvasSkin = CanvasSkin(this)

  def loadGraph(existingDependencyGraph: DependencyGraph): Unit = {
    dependencyGraph = existingDependencyGraph
    // TODO: why converting toList
    skin.loadGraph(existingDependencyGraph.metaInfo, existingDependencyGraph.nodes.toList)
  }
}

object CanvasView {
  def apply(): CanvasView = new CanvasView()
}
