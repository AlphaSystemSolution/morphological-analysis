package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import morphologicalanalysis.morphology.utils.*
import ui.dependencygraph.utils.{
  AddTerminalNodeRequest,
  CreatePhraseRequest,
  CreateRelationshipRequest,
  GraphOperationRequest,
  RemoveNodeRequest,
  RemoveTerminalNodeRequest,
  TerminalNodeInput
}
import morphologicalanalysis.graph.model.GraphNodeType
import ui.commons.service.ServiceFactory
import fx.ui.util.UiUtilities
import morphology.persistence.cache.*
import morphology.model.{ Chapter, Location, Token }
import javafx.application.Platform
import morphology.graph.model.{
  DependencyGraph,
  GraphMetaInfo,
  GraphNode,
  Line,
  LinkSupport,
  PhraseInfo,
  Point,
  RelationshipInfo,
  TerminalNode
}
import skin.CanvasSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, ReadOnlyObjectProperty, ReadOnlyObjectWrapper }
import scalafx.scene.Node
import scalafx.scene.image.Image

import java.util.UUID
import scala.collection.mutable.ListBuffer

class CanvasView(serviceFactory: ServiceFactory) extends Control {

  private[control] val dependencyGraphProperty: ObjectProperty[DependencyGraph] =
    ObjectProperty[DependencyGraph](this, "dependencyGraph", defaultDependencyGraph)

  private[control] val graphMetaInfoWrapperProperty =
    ReadOnlyObjectWrapper[GraphMetaInfo](this, "", defaultGraphMetaInfo)

  private[control] val selectedNodeProperty = ObjectProperty[GraphNode](this, "selectedNode")

  private[control] val graphOperationRequestProperty =
    ObjectProperty[GraphOperationRequest](this, "graphOperationRequest")

  private val currentChapterProperty = ObjectProperty[Chapter](this, "currentChapter")

  // initializations & bindings
  dependencyGraphProperty.onChange((_, _, nv) => {
    graphMetaInfo = nv.metaInfo
    skin.loadGraph(nv.metaInfo, nv.nodes)
    val id = selectedNode.id
    nv.nodes.filter(_.id == id).foreach(graphNode => selectedNode = graphNode)
  })
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

  private[control] def addNode(nodeToAdd: TerminalNodeInput, indexToInsert: Int): Unit = {
    val nodes = dependencyGraph.nodes
    val newInputs =
      nodes
        .zipWithIndex
        .foldLeft(ListBuffer.empty[TerminalNodeInput]) { case (buffer, (node, index)) =>
          node match
            case n: TerminalNode =>
              val currentNode = TerminalNodeInput(
                id = n.token.id.toUUID,
                graphNodeType = n.graphNodeType,
                token = n.token
              )
              if index == indexToInsert then buffer.addOne(nodeToAdd).addOne(currentNode)
              else buffer.addOne(currentNode)
            case _ => buffer
        }
        .toSeq
    graphOperationRequestProperty.value = AddTerminalNodeRequest(dependencyGraph, newInputs)
  }

  private[control] def removeTerminalNode(indexToRemove: Int): Unit = {
    val nodes = dependencyGraph.nodes
    val newInputs =
      nodes
        .zipWithIndex
        .foldLeft(ListBuffer.empty[TerminalNodeInput]) { case (buffer, (node, index)) =>
          node match
            case n: TerminalNode if index != indexToRemove =>
              buffer.addOne(TerminalNodeInput(id = n.id, graphNodeType = n.graphNodeType, token = n.token))
            case _ => buffer
        }
        .toSeq

    val otherNodes = nodes.filter(node => !isTerminalNode(node.graphNodeType))

    graphOperationRequestProperty.value = RemoveTerminalNodeRequest(dependencyGraph, newInputs, otherNodes)
  }

  private[control] def createRelationship(
    relationshipInfo: RelationshipInfo,
    owner: LinkSupportView[?],
    dependent: LinkSupportView[?]
  ): Unit =
    graphOperationRequestProperty.value = CreateRelationshipRequest(dependencyGraph, relationshipInfo, owner, dependent)

  private[control] def createPhrase(phraseInfo: PhraseInfo, line: Line): Unit =
    graphOperationRequestProperty.value = CreatePhraseRequest(dependencyGraph, phraseInfo, line)

  private[control] def removeNode(nodeId: UUID): Unit =
    graphOperationRequestProperty.value = RemoveNodeRequest(dependencyGraph, nodeId)

  private[control] def toImage: Image = skin.toImage
}

object CanvasView {

  def apply(serviceFactory: ServiceFactory): CanvasView = new CanvasView(serviceFactory)
}
