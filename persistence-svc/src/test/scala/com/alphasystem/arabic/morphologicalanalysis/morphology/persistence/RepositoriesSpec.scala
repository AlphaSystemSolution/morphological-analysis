package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import morphology.model.*
import repository.{ ChapterRepository, LocationRepository, TokenRepository, VerseRepository }
import io.circe.generic.*
import io.circe.syntax.*

class RepositoriesSpec extends BaseRepositorySpec with TestData {

  private var locationRepository: LocationRepository = _
  private var tokenRepository: TokenRepository = _
  private var verseRepository: VerseRepository = _
  private var chapterRepository: ChapterRepository = _

  override protected def initRepositories(
    dataSource: CloseableDataSource
  ): Unit = {
    locationRepository = LocationRepository(dataSource)
    tokenRepository = TokenRepository(dataSource)
    verseRepository = VerseRepository(dataSource)
    chapterRepository = ChapterRepository(dataSource)
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
}
