package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.graph.model.GraphMetaInfo
import morphology.model.*
import repository.{
  ChapterRepository,
  DependencyGraphRepository,
  GraphNodeRepository,
  LocationRepository,
  TokenRepository,
  VerseRepository
}
import io.circe.generic.*
import io.circe.syntax.*

class RepositoriesSpec extends BaseRepositorySpec with TestData {

  private var locationRepository: LocationRepository = _
  private var tokenRepository: TokenRepository = _
  private var verseRepository: VerseRepository = _
  private var chapterRepository: ChapterRepository = _
  private var dependencyGraphRepository: DependencyGraphRepository = _
  private var graphNodeRepository: GraphNodeRepository = _

  override protected def initRepositories(
    dataSource: CloseableDataSource
  ): Unit = {
    locationRepository = LocationRepository(dataSource)
    tokenRepository = TokenRepository(dataSource)
    verseRepository = VerseRepository(dataSource)
    chapterRepository = ChapterRepository(dataSource)
    dependencyGraphRepository = DependencyGraphRepository(dataSource)
    graphNodeRepository = GraphNodeRepository(dataSource)
  }

  test("LocationRepository: save and retrieve location") {
    locationRepository.create(location)
    assertEquals(locationRepository.findById(location.id), Some(location))
  }

  test("LocationRepository: findByChapterVerseAndToken") {
    val locations =
      locationRepository.findByChapterVerseAndToken(location.chapterNumber, location.verseNumber, location.tokenNumber)
    assertEquals(locations, Seq(location))
  }

  test("LocationRepository: deleteByChapterVerseAndToken") {
    locationRepository.deleteByChapterVerseAndToken(
      location.chapterNumber,
      location.verseNumber,
      location.tokenNumber
    )

    // find again
    val locations =
      locationRepository.findByChapterVerseAndToken(location.chapterNumber, location.verseNumber, location.tokenNumber)
    assertEquals(locations.isEmpty, true)
  }

  test("TokenRepository: save and retrieve token") {
    tokenRepository.create(token)
    assertEquals(tokenRepository.findById(token.id), Some(token))
  }

  test("TokenRepository: update and retrieve token") {
    val updatedToken = token.copy(translation = Some("translation"))
    tokenRepository.update(updatedToken)
    assertEquals(tokenRepository.findById(updatedToken.id), Some(updatedToken))
  }

  test("VerseRepository: save and retrieve verse") {
    verseRepository.create(verse)
    assertEquals(verseRepository.findById(verse.id), Some(verse))
  }

  test("ChapterRepository: save and retrieve token") {
    chapterRepository.create(chapter)
    assertEquals(chapterRepository.findById(chapter.id), Some(chapter))
  }

  test("ChapterRepository: bulk create") {
    chapterRepository.bulkCreate(chapters)

    val chapter = chapters.last
    assertEquals(chapterRepository.findById(chapter.id), Some(chapter))
  }

  test("ChapterRepository: get all ids") {
    assertEquals(
      chapterRepository.findAll.map(_.id),
      (1 to 12).map(index => index.toChapterId)
    )
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
