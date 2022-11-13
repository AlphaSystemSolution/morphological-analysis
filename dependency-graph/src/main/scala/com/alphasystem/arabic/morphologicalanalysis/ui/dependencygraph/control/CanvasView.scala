package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import morphology.persistence.cache.*
import morphology.model.{ Location, Token }
import commons.service.ServiceFactory
import morphology.graph.model.{ DependencyGraph, GraphMetaInfo, GraphNode }
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

  def loadNewGraph(chapterName: String, tokens: Seq[Token]): Unit = {
    val service = serviceFactory.bulkLocationService(tokens.map(_.toLocationRequest))

    service.onFailed = event => {
      Console.err.println(s"Failed to load locations: $event")
      event.consume()
    }

    service.onSucceeded = event => {
      val locationsMap = event.getSource.getValue.asInstanceOf[Map[String, Seq[Location]]]
      val emptyLocations = locationsMap.filter(_._2.isEmpty)
      if emptyLocations.nonEmpty then {
        Console.err.println(s"Locations cannot be empty: $emptyLocations")
      } else {
        val token = tokens.head
        dependencyGraph = DependencyGraph(
          chapterNumber = token.chapterNumber,
          chapterName = chapterName,
          verseNumber = token.verseNumber,
          startTokenNumber = token.tokenNumber,
          endTokenNumber = tokens.last.tokenNumber,
          text = tokens.map(_.token).mkString(" "),
          metaInfo = defaultGraphMetaInfo
        )
        skin.createGraph(dependencyGraph.id, graphMetaInfo, tokens, locationsMap)
      }
      event.consume()
    }

    service.start()
  }
}

object CanvasView {
  def apply(serviceFactory: ServiceFactory): CanvasView = new CanvasView(serviceFactory)
}
