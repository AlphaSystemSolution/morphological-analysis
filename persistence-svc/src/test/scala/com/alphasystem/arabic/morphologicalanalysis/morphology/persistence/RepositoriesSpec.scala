package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.repository.{
  ChapterRepository,
  LocationRepository,
  TokenRepository,
  VerseRepository,
  WordPropertiesRepository
}
import com.alphasystem.morphologicalanalysis.morphology.model.*
import io.circe.generic.*
import io.circe.syntax.*

class RepositoriesSpec extends BaseRepositorySpec with TestData {

  private var wordPropertiesRepository: WordPropertiesRepository = _
  private var locationRepository: LocationRepository = _
  private var tokenRepository: TokenRepository = _
  private var verseRepository: VerseRepository = _
  private var chapterRepository: ChapterRepository = _

  override protected def initRepositories(
    dataSource: CloseableDataSource
  ): Unit = {
    wordPropertiesRepository = WordPropertiesRepository(dataSource)
    locationRepository = LocationRepository(dataSource)
    tokenRepository = TokenRepository(dataSource)
    verseRepository = VerseRepository(dataSource)
    chapterRepository = ChapterRepository(dataSource)
  }

  test(
    "WordPropertiesRepository: returns empty result for non-existing entry"
  ) {
    assertEquals(wordPropertiesRepository.findById("1"), None)
  }

  test(
    "WordPropertiesRepository: returns empty result for non-existing entry for findByLocationId"
  ) {
    assertEquals(wordPropertiesRepository.findByLocationId("1"), Seq.empty)
  }

  test("WordPropertiesRepository: returns single result for findById") {
    wordPropertiesRepository.create(nounProperties)
    assertEquals(wordPropertiesRepository.findById("1"), Some(nounProperties))
  }

  test("WordPropertiesRepository: returns all properties for given location") {
    wordPropertiesRepository.create(proNounProperties)

    val expectedProperties = Seq(nounProperties, proNounProperties)
    assertEquals(
      wordPropertiesRepository.findByLocationId("1"),
      expectedProperties
    )
  }

  test("LocationRepository: save and retrieve location") {
    locationRepository.create(location)
    assertEquals(locationRepository.findById(location.id), Some(location))
  }

  test("TokenRepository: save and retrieve token") {
    tokenRepository.create(token)
    assertEquals(tokenRepository.findById(token.id), Some(token))
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
