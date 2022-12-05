package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package utils

import morphology.graph.model.{ DependencyGraph, GraphMetaInfo, GraphNode, PartOfSpeechNode, TerminalNode }
import morphology.persistence.cache.*
import morphology.model.{ Chapter, Location, Token }
import commons.service.ServiceFactory
import javafx.application.Platform
import org.slf4j.LoggerFactory

import java.util.UUID

class GraphBuilderService(serviceFactory: ServiceFactory) {

  private val logger = LoggerFactory.getLogger(classOf[GraphBuilderService])
  private val graphBuilder = GraphBuilder()

  def createGraph(
    chapter: Chapter,
    tokens: Seq[Token],
    displayGraphF: DependencyGraph => Unit
  ): Unit = {
    val tokenIds = tokens.map(_.id)
    val service = serviceFactory.getTerminalNodesByTokenIds(tokenIds)

    service.onFailed = event => {
      event.getSource.getException.printStackTrace()
      event.consume()
    }

    service.onSucceeded = event => {
      val terminalNodes = event.getSource.getValue.asInstanceOf[Seq[TerminalNode]]
      if terminalNodes.isEmpty then {
        Console.err.println(s"No Terminal node found for token ids: $tokenIds")
      } else {
        val dependencyGraphId = UUID.randomUUID()
        val graphMetaInfo = defaultGraphMetaInfo
        val nodes = graphBuilder.createNewGraph(dependencyGraphId, graphMetaInfo, terminalNodes)
        val dependencyGraph = DependencyGraph(
          id = dependencyGraphId,
          chapterNumber = chapter.chapterNumber,
          verseNumber = tokens.head.verseNumber,
          chapterName = chapter.chapterName,
          metaInfo = graphMetaInfo,
          tokens = tokens,
          nodes = nodes
        )

        saveAndDisplayGraph(dependencyGraph, displayGraphF)
      }
    }

    service.start()
  }

  def loadGraph(dependencyGraph: DependencyGraph, displayGraphF: DependencyGraph => Unit): Unit =
    displayGraphF(dependencyGraph)

  private def saveAndDisplayGraph(
    dependencyGraph: DependencyGraph,
    displayGraphF: DependencyGraph => Unit
  ): Unit = {
    val service = serviceFactory.createDependencyGraphService(dependencyGraph)

    service.onSucceeded = event => {
      logger.debug(
        "Graph created: {}, chapter: {}, verse: {}",
        dependencyGraph.id,
        dependencyGraph.chapterNumber,
        dependencyGraph.verseNumber
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

object GraphBuilderService {
  def apply(serviceFactory: ServiceFactory): GraphBuilderService = new GraphBuilderService(serviceFactory)
}
