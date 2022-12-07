package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.graph.model.GraphMetaInfo
import com.alphasystem.arabic.morphologicalanalysis.morphology.model.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.repository.*
import io.circe.generic.*
import io.circe.syntax.*

class RepositoriesSpec extends BaseRepositorySpec with TestData {

  private var dependencyGraphRepository: DependencyGraphRepository = _
  private var graphNodeRepository: GraphNodeRepository = _

  override protected def initRepositories(
    dataSource: CloseableDataSource
  ): Unit = {
    dependencyGraphRepository = DependencyGraphRepository(dataSource)
    graphNodeRepository = GraphNodeRepository(dataSource)
  }

  test("DependencyGraphRepository: save and retrieve graph") {
    dependencyGraphRepository.create(dependencyGraph)
    assertEquals(dependencyGraphRepository.findById(dependencyGraph.id), Some(dependencyGraph))
  }

  test("DependencyGraphRepository: save and retrieve second graph") {
    dependencyGraphRepository.create(dependencyGraph2)
    assertEquals(dependencyGraphRepository.findById(dependencyGraph2.id), Some(dependencyGraph2))
  }

  test("DependencyGraphRepository: findByChapterAndVerseNumber") {
    val actual = dependencyGraphRepository.findByChapterAndVerseNumber(1, 1)
    assertEquals(actual, Seq(dependencyGraph, dependencyGraph2))
  }

  test("DependencyGraphRepository: findByChapterAndVerseNumber different verse") {
    val actual = dependencyGraphRepository.findByChapterAndVerseNumber(1, 2)
    assertEquals(actual, Seq(dependencyGraph2))
  }

  test("DependencyGraphRepository: update and retrieve graph") {
    val updated = dependencyGraph.copy(metaInfo = GraphMetaInfo(width = 1500))
    dependencyGraphRepository.update(updated)
    assertEquals(dependencyGraphRepository.findById(dependencyGraph.id), Some(updated))
  }

  test("GraphNodeRepository: save and retrieve nodes") {
    graphNodeRepository.createAll(nodes)
    assertEquals(graphNodeRepository.findByGraphId(dependencyGraph.id).toSet, nodes.toSet)
  }

  test("GraphNodeRepository: update / delete and retrieve nodes") {
    val nodeToDelete = nodes.last
    val nodesToUpdate = nodes.dropRight(1)
    graphNodeRepository.createAll(nodesToUpdate)
    assertEquals(graphNodeRepository.findByGraphId(dependencyGraph.id).toSet, nodesToUpdate.toSet)
    assertEquals(graphNodeRepository.findByPK(dependencyGraph.id, nodeToDelete.id), None)
  }
}
