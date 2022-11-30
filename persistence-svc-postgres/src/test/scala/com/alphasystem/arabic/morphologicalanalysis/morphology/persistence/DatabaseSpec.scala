package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.repository.Database
import com.typesafe.config.ConfigFactory
import munit.FunSuite
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class DatabaseSpec extends FunSuite with TestData {

  private var postgresContainer: PostgreSQLContainer[?] = _
  private var database: Database = _

  test("ChapterRepository: retrieve non-existing chapter") {
    assertEquals(database.findChapterById(chapter.chapterNumber), None)
  }

  test("ChapterRepository: save and retrieve chapter") {
    database.createChapter(chapter)
    assertEquals(database.findChapterById(chapter.chapterNumber), Some(chapter))
  }

  test("ChapterRepository: create many chapters") {
    chapters.foreach(database.createChapter)
    val chapter = chapters.last
    assertEquals(database.findChapterById(chapter.chapterNumber), Some(chapter))
  }

  test("ChapterRepository: get all ids") {
    assertEquals(
      database.findAllChapters.map(_.chapterNumber),
      1 to 12
    )
  }

  test("VerseRepository: save and retrieve verse") {
    val verses = Seq(verse)
    database.createVerses(verses)
    assertEquals(database.findVersesByChapterNumber(verse.chapterNumber), verses)
  }

  test("TokenRepository: save and retrieve token") {
    val tokens = Seq(token)
    database.createTokens(tokens)
    assertEquals(database.findTokensByChapterAndVerseNumber(token.chapterNumber, token.verseNumber), tokens)
  }

  test("LocationRepository: save and retrieve location") {
    val locations = Seq(location)
    database.createLocations(token, locations)
    assertEquals(
      database
        .findLocationsByChapterVerseAndTokenNumber(location.chapterNumber, location.verseNumber, location.tokenNumber),
      locations
    )
  }

  test("TokenRepository: delete tokens") {
    database.deleteTokensByChapterAndVerseNumber(verse.chapterNumber, verse.verseNumber)
    assertEquals(database.findTokensByChapterAndVerseNumber(verse.chapterNumber, verse.verseNumber), Seq.empty)
    assertEquals(
      database
        .findLocationsByChapterVerseAndTokenNumber(location.chapterNumber, location.verseNumber, location.tokenNumber),
      Seq.empty
    )
  }

  override def beforeAll(): Unit = {
    super.beforeAll()

    initPostgres()
    val postgresHost = postgresContainer.getHost
    val postgresPort = postgresContainer.getMappedPort(5432).intValue()

    val config = ConfigFactory.parseString(s"""
                                              |postgres {
                                              |  dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
                                              |  properties = {
                                              |    serverName = $postgresHost
                                              |    portNumber = $postgresPort
                                              |    databaseName = "postgres"
                                              |    currentSchema = "morphological_analysis"
                                              |    user = "postgres"
                                              |    password = "postgres"
                                              |  }
                                              |}
                                              |""".stripMargin)

    database = Database(config)
  }

  override def afterAll(): Unit = {
    postgresContainer.stop()
    super.afterAll()
  }

  private def initPostgres(): Unit = {
    postgresContainer = new PostgreSQLContainer(
      DockerImageName.parse("postgres").withTag("14.5")
    )
    postgresContainer.withDatabaseName("postgres")
    postgresContainer.withUsername("postgres")
    postgresContainer.withPassword("postgres")
    postgresContainer.withInitScript("postgres.sql")
    postgresContainer.start()
  }
}
