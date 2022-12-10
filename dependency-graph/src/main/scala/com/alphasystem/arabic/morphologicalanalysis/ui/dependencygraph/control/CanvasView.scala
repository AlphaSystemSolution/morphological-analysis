package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import ui.dependencygraph.utils.TerminalNodeInput
import morphologicalanalysis.graph.model.GraphNodeType
import ui.commons.service.ServiceFactory
import fx.ui.util.UiUtilities
import morphology.persistence.cache.*
import morphology.model.{ Chapter, Location, Token }
import javafx.application.Platform
import morphology.graph.model.{ DependencyGraph, GraphMetaInfo, GraphNode }
import skin.CanvasSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, ReadOnlyObjectProperty, ReadOnlyObjectWrapper }
import scalafx.scene.Node

import java.util.UUID
import scala.collection.mutable.ListBuffer

class CanvasView(serviceFactory: ServiceFactory) extends Control {

  import CanvasView.*

  private[control] val dependencyGraphProperty: ObjectProperty[DependencyGraph] =
    ObjectProperty[DependencyGraph](this, "dependencyGraph", defaultDependencyGraph)

  private[control] val graphMetaInfoWrapperProperty =
    ReadOnlyObjectWrapper[GraphMetaInfo](this, "", defaultGraphMetaInfo)

  private[control] val selectedNodeProperty = ObjectProperty[GraphNode](this, "selectedNode")

  private[control] val addNodeProperty = ObjectProperty[AddNodeRequest](this, "addNode")

  private val currentChapterProperty = ObjectProperty[Chapter](this, "currentChapter")

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

  def currentChapter: Chapter = currentChapterProperty.value
  def currentChapter_=(value: Chapter): Unit = currentChapterProperty.value = value

  def graphNodes: Seq[GraphNode] = skin.graphNodes

  override def createDefaultSkin(): CanvasSkin = CanvasSkin(this, serviceFactory)

  def loadGraph(existingDependencyGraph: DependencyGraph): Unit = {
    dependencyGraph = existingDependencyGraph
    skin.loadGraph(existingDependencyGraph.metaInfo, existingDependencyGraph.nodes)
  }

  private[control] def addNode(nodeToAdd: TerminalNodeInput, indexToInsert: Int): Unit = {
    val tokens = dependencyGraph.tokens
    val newInputs =
      tokens
        .zipWithIndex
        .foldLeft(ListBuffer.empty[TerminalNodeInput]) { case (buffer, (token, index)) =>
          val currentNode = TerminalNodeInput(
            id = UUID.nameUUIDFromBytes(token.id.toString.getBytes),
            graphNodeType = GraphNodeType.Terminal,
            token = token
          )
          if index == indexToInsert then buffer.addOne(nodeToAdd).addOne(currentNode)
          else buffer.addOne(currentNode)
        }
        .toSeq
    addNodeProperty.value = AddNodeRequest(dependencyGraph, newInputs)
  }
}

object CanvasView {

  case class AddNodeRequest(dependencyGraph: DependencyGraph, inputs: Seq[TerminalNodeInput])
  def apply(serviceFactory: ServiceFactory): CanvasView = new CanvasView(serviceFactory)
}
