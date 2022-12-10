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

  def recreateGraph(
    dependencyGraph: DependencyGraph,
    newInputs: Seq[TerminalNodeInput],
    displayGraphF: DependencyGraph => Unit
  ): Unit = {
    val nodes = graphBuilder.createNewGraph(dependencyGraph.id, dependencyGraph.metaInfo, newInputs)
    val updateGraph = dependencyGraph.copy(nodes = nodes)
    saveAndDisplayGraph(updateGraph, recreate = true, displayGraphF = displayGraphF)
  }

  private def saveAndDisplayGraph(
    dependencyGraph: DependencyGraph,
    recreate: Boolean,
    displayGraphF: DependencyGraph => Unit
  ): Unit = {
    val service = serviceFactory.createDependencyGraphService(SaveDependencyGraphRequest(dependencyGraph, recreate))

    service.onSucceeded = event => {
      logger.debug(
        "Graph created: {}, chapter: {}, verses: {}",
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

object GraphBuilderService {
  def apply(serviceFactory: ServiceFactory): GraphBuilderService = new GraphBuilderService(serviceFactory)
}
