package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package utils

import morphology.graph.model.{ DependencyGraph, GraphMetaInfo }
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
    val dependencyGraphId = UUID.randomUUID()
    val graphMetaInfo = defaultGraphMetaInfo
    val nodes = graphBuilder.createNewGraph(dependencyGraphId, graphMetaInfo, tokens)
    val dependencyGraph = DependencyGraph(
      id = dependencyGraphId,
      chapterNumber = chapter.chapterNumber,
      verseNumber = tokens.head.verseNumber,
      chapterName = chapter.chapterName,
      metaInfo = graphMetaInfo,
      tokens = tokens,
      nodes = nodes
    )

    // TODO: just for debugging purpose
    displayGraphF(dependencyGraph)
    // saveAndDisplayGraph(dependencyGraph, displayGraphF)
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
