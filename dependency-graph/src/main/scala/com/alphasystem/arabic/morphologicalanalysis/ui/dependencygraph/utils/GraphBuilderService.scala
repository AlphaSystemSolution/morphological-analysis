package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package utils

import ui.dependencygraph.control.LinkSupportView
import morphology.graph.model.{ DependencyGraph, GraphMetaInfo, GraphNode, Line, PhraseInfo, RelationshipInfoOld }
import morphology.persistence.cache.*
import morphology.model.{ Chapter, Location, Token }
import commons.service.ServiceFactory
import javafx.application.Platform
import org.slf4j.LoggerFactory
import scalafx.application.JFXApp3
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData

import java.util.UUID

class GraphBuilderService(serviceFactory: ServiceFactory) {

  import ServiceFactory.*

  private val logger = LoggerFactory.getLogger(classOf[GraphBuilderService])
  private val graphBuilder = GraphBuilder()
  private val demoMode = System.getProperty("demo-mode", "false").toBoolean

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
                          relationshipInfo: RelationshipInfoOld,
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
      displayGraphF(dependencyGraph)
      event.consume()
    }

    removeNodeService.onFailed = event => {
      Console.err.println(s"Failed to create dependency graph: $event")
      event.getSource.getException.printStackTrace()
      event.consume()
    }

    removeNodeService.start()
  }

  def removeGraph(dependencyGraph: DependencyGraph, displayGraphF: DependencyGraph => Unit): Unit =
    new Alert(AlertType.Confirmation) {
      initOwner(JFXApp3.Stage)
      title = "Delete Graph"
      headerText = "Delete selected graph."
      contentText = "Are you Sure?"
    }.showAndWait() match
      case Some(buttonType) if buttonType.buttonData == ButtonData.OKDone =>
        val service = serviceFactory.removeGraphService(dependencyGraph)

        service.onSucceeded = event => {
          displayGraphF(null)
          event.consume()
        }

        service.onFailed = event => {
          event.getSource.getException.printStackTrace()
          event.consume()
        }

        service.start()
      case _ => // do nothing

  private def saveAndDisplayGraph(
    dependencyGraph: DependencyGraph,
    recreate: Boolean,
    displayGraphF: DependencyGraph => Unit
  ): Unit = {
    if demoMode then displayGraphF(dependencyGraph)
    else {
      val service = serviceFactory.createDependencyGraphService(SaveDependencyGraphRequest(dependencyGraph, recreate))

      service.onSucceeded = event => {
        logger.debug(
          "Graph created/updated: {}, chapter: {}, verses: {}",
          dependencyGraph.id,
          dependencyGraph.chapterNumber,
          dependencyGraph.verseNumbers
        )
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
  }

  private def createAndDisplayGraph(
    dependencyGraph: DependencyGraph,
    node: GraphNode,
    displayGraphF: DependencyGraph => Unit
  ): Unit = {
    if demoMode then displayGraphF(dependencyGraph)
    else {
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
  }
}

object GraphBuilderService {
  def apply(serviceFactory: ServiceFactory): GraphBuilderService = new GraphBuilderService(serviceFactory)
}
