package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package utils

import ui.dependencygraph.control.LinkSupportView
import morphology.graph.model.{ DependencyGraph, GraphMetaInfo, GraphNode, Line, PhraseInfo, RelationshipInfo }
import morphology.persistence.cache.*
import morphology.model.{ Chapter, Location, Token }
import commons.service.ServiceFactory
import javafx.application.Platform
import org.slf4j.LoggerFactory

import java.util.UUID

class GraphBuilderService(serviceFactory: ServiceFactory) {

  import ServiceFactory.*

  private val logger = LoggerFactory.getLogger(classOf[GraphBuilderService])
  private val graphBuilder = GraphBuilder()

  def createGraph(
    chapter: Chapter,
    inputs: Seq[TerminalNodeInput],
    displayGraphF: DependencyGraph => Unit
  ): Unit = {
    val tokens = inputs.map(_.token).sortBy(_.id)
    val verseNumbers = tokens.map(_.verseNumber).distinct.sorted
    val dependencyGraphId = UUID.randomUUID()
    val graphMetaInfo = defaultGraphMetaInfo
    val nodes = graphBuilder.createNewGraph(dependencyGraphId, graphMetaInfo, inputs)
    val dependencyGraph = DependencyGraph(
      id = dependencyGraphId,
      chapterNumber = chapter.chapterNumber,
      chapterName = chapter.chapterName,
      metaInfo = graphMetaInfo,
      verseNumbers = verseNumbers,
      tokens = tokens,
      nodes = nodes
    )

    saveAndDisplayGraph(dependencyGraph, recreate = false, displayGraphF = displayGraphF)
  }

  def saveGraph(dependencyGraph: DependencyGraph, displayGraphF: DependencyGraph => Unit): Unit =
    saveAndDisplayGraph(dependencyGraph, recreate = false, displayGraphF = displayGraphF)

  def recreateGraph(
    dependencyGraph: DependencyGraph,
    newInputs: Seq[TerminalNodeInput],
    otherNodes: Seq[GraphNode],
    displayGraphF: DependencyGraph => Unit
  ): Unit = {
    val nodes = graphBuilder.createNewGraph(dependencyGraph.id, dependencyGraph.metaInfo, newInputs)
    val updateGraph = dependencyGraph.copy(nodes = nodes ++ otherNodes)
    saveAndDisplayGraph(updateGraph, recreate = true, displayGraphF = displayGraphF)
  }

  def createRelationship(
    dependencyGraph: DependencyGraph,
    relationshipInfo: RelationshipInfo,
    owner: LinkSupportView[?],
    dependent: LinkSupportView[?],
    displayGraphF: DependencyGraph => Unit
  ): Unit = {
    val relationshipNode =
      graphBuilder.createRelationship(dependencyGraph.id, dependencyGraph.metaInfo, relationshipInfo, owner, dependent)
    val updateGraph = dependencyGraph.copy(nodes = dependencyGraph.nodes :+ relationshipNode)
    createAndDisplayGraph(updateGraph, relationshipNode, displayGraphF)
  }

  def createPhrase(
    dependencyGraph: DependencyGraph,
    phraseInfo: PhraseInfo,
    line: Line,
    displayGraphF: DependencyGraph => Unit
  ): Unit = {
    val phraseNode = graphBuilder.createPhrase(dependencyGraph.id, dependencyGraph.metaInfo, phraseInfo, line)
    val updateGraph = dependencyGraph.copy(nodes = dependencyGraph.nodes :+ phraseNode)
    createAndDisplayGraph(updateGraph, phraseNode, displayGraphF)
  }

  def removeNode(dependencyGraph: DependencyGraph, nodeId: UUID, displayGraphF: DependencyGraph => Unit): Unit = {
    val removeNodeService = serviceFactory.removeNodeService(RemoveNodeByIdRequest(dependencyGraph, nodeId))

    removeNodeService.onSucceeded = event => {
      getAndDisplayGraph(dependencyGraph.id, displayGraphF)
      event.consume()
    }

    removeNodeService.onFailed = event => {
      Console.err.println(s"Failed to create dependency graph: $event")
      event.getSource.getException.printStackTrace()
      event.consume()
    }

    removeNodeService.start()
  }

  private def saveAndDisplayGraph(
    dependencyGraph: DependencyGraph,
    recreate: Boolean,
    displayGraphF: DependencyGraph => Unit
  ): Unit = {
    val service = serviceFactory.createDependencyGraphService(SaveDependencyGraphRequest(dependencyGraph, recreate))

    service.onSucceeded = event => {
      logger.debug(
        "Graph created/updated: {}, chapter: {}, verses: {}",
        dependencyGraph.id,
        dependencyGraph.chapterNumber,
        dependencyGraph.verseNumbers
      )
      getAndDisplayGraph(dependencyGraph.id, displayGraphF)
      event.consume()
    }

    service.onFailed = event => {
      Console.err.println(s"Failed to create dependency graph: $event")
      event.getSource.getException.printStackTrace()
      event.consume()
    }

    service.start()
  }

  private def createAndDisplayGraph(
    dependencyGraph: DependencyGraph,
    node: GraphNode,
    displayGraphF: DependencyGraph => Unit
  ): Unit = {
    val service = serviceFactory.createNodeService(CreateNodeRequest(dependencyGraph, node))

    service.onSucceeded = event => {
      displayGraphF(dependencyGraph)
      event.consume()
    }

    service.onFailed = event => {
      Console.err.println(s"Failed to create dependency graph: $event")
      event.getSource.getException.printStackTrace()
      event.consume()
    }

    service.start()
  }

  private def getAndDisplayGraph(graphId: UUID, displayGraphF: DependencyGraph => Unit): Unit = {
    val service = serviceFactory.getDependencyGraphByIdService(graphId)

    service.onSucceeded = event => {
      event.getSource.getValue.asInstanceOf[Option[DependencyGraph]] match
        case Some(dependencyGraph) => displayGraphF(dependencyGraph)
        case None                  => Console.err.println(s"Unable to find dependency graph: $graphId")

      event.consume()
    }

    service.onFailed = event => {
      Console.err.println(s"Failed to create dependency graph: $event")
      event.getSource.getException.printStackTrace()
      event.consume()
    }

    service.start()
  }
}

object GraphBuilderService {
  def apply(serviceFactory: ServiceFactory): GraphBuilderService = new GraphBuilderService(serviceFactory)
}
