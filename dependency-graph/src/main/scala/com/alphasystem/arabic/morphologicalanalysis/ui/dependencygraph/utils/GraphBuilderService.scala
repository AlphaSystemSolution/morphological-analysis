package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package utils

import morphology.graph.model.{ DependencyGraph, GraphMetaInfo, GraphNode, PartOfSpeechNode, TerminalNode }
import morphology.persistence.cache.*
import morphology.model.{ Chapter, Location, Token }
import commons.service.{ SaveDependencyGraphRequest, ServiceFactory }
import javafx.application.Platform

class GraphBuilderService(serviceFactory: ServiceFactory) {

  private val graphBuilder = GraphBuilder()

  def createGraph(
    chapter: Chapter,
    tokens: Seq[Token],
    displayGraphF: (DependencyGraph, Seq[TerminalNode], Map[String, Seq[PartOfSpeechNode]]) => Unit
  ): Unit = {
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
        val verseTokensMap = tokens.groupBy(_.verseNumber).map { case (verseNumber, tokens) =>
          verseNumber -> tokens.map(_.tokenNumber)
        }
        val dependencyGraph = DependencyGraph(
          chapterNumber = chapter.chapterNumber,
          chapterName = chapter.chapterName,
          text = tokens.map(_.token).mkString(" "),
          metaInfo = defaultGraphMetaInfo,
          verseTokensMap = verseTokensMap
        )

        println(dependencyGraph)

        val (terminalNodes, posNodes) =
          graphBuilder.createNewGraph(dependencyGraph.id, dependencyGraph.metaInfo, tokens, locationsMap)

        saveAndDisplayGraph(dependencyGraph, terminalNodes, posNodes, displayGraphF)
      }
    }

    service.start()
  }

  def loadGraph(graph: DependencyGraph, displayGraphF: (DependencyGraph, List[GraphNode]) => Unit): Unit = {
    val graphId = graph.id
    val service = serviceFactory.getGraphNodesService(graphId)

    service.onFailed = event => {
      Console.err.println(s"Unable to load nodes for graph: $graphId")
      event.getSource.getException.printStackTrace()
      event.consume()
    }

    service.onSucceeded = event => {
      displayGraphF(graph, event.getSource.getValue.asInstanceOf[List[GraphNode]])
      event.consume()
    }

    Platform.runLater(() => service.start())
  }

  private def saveAndDisplayGraph(
    dependencyGraph: DependencyGraph,
    terminalNodes: Seq[TerminalNode],
    posNodes: Map[String, Seq[PartOfSpeechNode]],
    displayGraphF: (DependencyGraph, Seq[TerminalNode], Map[String, Seq[PartOfSpeechNode]]) => Unit
  ): Unit = {
    val service = serviceFactory.createDependencyGraphService(
      SaveDependencyGraphRequest(dependencyGraph, terminalNodes ++ posNodes.values.toSeq.flatten)
    )

    service.onSucceeded = event => {
      displayGraphF(dependencyGraph, terminalNodes, posNodes)
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
