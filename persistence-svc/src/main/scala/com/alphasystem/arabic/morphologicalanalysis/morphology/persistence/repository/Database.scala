package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository

import morphology.model.{ Chapter, Location, Token, Verse }
import com.typesafe.config.{ Config, ConfigFactory }
import com.zaxxer.hikari.{ HikariConfig, HikariDataSource }
import io.getquill.*
import io.getquill.context.*

import scala.collection.immutable.Seq

class Database(config: Config) {

  private val dataSource = Database.datasourceForConfig(config)
  private val ctx: PostgresJdbcContext[Literal] = new PostgresJdbcContext(Literal, dataSource)

  private val chapterRepository = ChapterRepository(ctx)
  private val verseRepository = VerseRepository(ctx)
  private val tokenRepository = TokenRepository(ctx)
  private val locationRepository = LocationRepository(ctx)

  import ctx.*

  def createChapter(chapter: Chapter): Long = run(chapterRepository.insert(chapter))

  def createVerses(verses: Seq[Verse]): Unit = run(verseRepository.insertAll(verses))

  def createTokens(tokens: Seq[Token]): Unit = run(tokenRepository.insertAll(tokens))

  def createLocations(token: Token, locations: Seq[Location]): Unit =
    transaction {
      run(tokenRepository.update(token))
      run(locationRepository.insertAll(locations))
    }

  def findChapterById(chapterNumber: Int): Option[Chapter] =
    run(chapterRepository.findByIdQuery(chapterNumber)).headOption.map(_.toEntity)

  def findAllChapters: Seq[Chapter] = run(chapterRepository.findAllQuery).map(_.toEntity)

  def findVersesByChapterNumber(chapterNumber: Int): Seq[Verse] =
    run(verseRepository.findByChapterNumber(chapterNumber).sortBy(_.verse_number)).map(_.toEntity)

  def findTokensByChapterAndVerseNumber(chapterNumber: Int, verseNumber: Int): Seq[Token] =
    run(tokenRepository.findByChapterAndVerseNumber(chapterNumber, verseNumber).sortBy(_.token_number)).map(_.toEntity)

  def findLocationsByChapterVerseAndTokenNumber(chapterNumber: Int, verseNumber: Int, tokenNUmber: Int): Seq[Location] =
    run(
      locationRepository
        .findByChapterVerseAndTokenNumber(chapterNumber, verseNumber, tokenNUmber)
        .sortBy(_.location_number)
    ).map(_.toEntity)

  def findLocations(chapterIds: Seq[Int], verseIds: Seq[Int], tokenIds: Seq[Int]): Map[String, List[Location]] =
    run(locationRepository.findAll(chapterIds, verseIds, tokenIds).sortBy(_.location_number))
      .map(_.toEntity)
      .groupBy(_.tokenId)

  def deleteTokensByChapterAndVerseNumber(chapterNumber: Int, verseNumber: Int): Unit =
    transaction {
      run(locationRepository.findByChapterAndVerseNumber(chapterNumber, verseNumber).delete)
      run(tokenRepository.findByChapterAndVerseNumber(chapterNumber, verseNumber).delete)
    }

  def deleteLocationByChapterVerseAndTokenNumber(chapterNumber: Int, verseNumber: Int, tokenNUmber: Int): Unit =
    run(locationRepository.findByChapterVerseAndTokenNumber(chapterNumber, verseNumber, tokenNUmber).delete)
}

object Database {

  def apply(config: Config): Database = new Database(config)

  def datasourceForConfig(config: Config): CloseableDataSource = {
    val rootConfig = config.getConfig("postgres")
    val hikariConfig = new HikariConfig()

    hikariConfig.setDataSourceClassName(
      rootConfig.getString("dataSourceClassName")
    )

    val properties = rootConfig.getConfig("properties")

    hikariConfig.addDataSourceProperty(
      "databaseName",
      properties.getString("databaseName")
    )
    hikariConfig.addDataSourceProperty(
      "serverName",
      properties.getString("serverName")
    )
    hikariConfig.addDataSourceProperty(
      "portNumber",
      properties.getString("portNumber")
    )
    hikariConfig.addDataSourceProperty("user", properties.getString("user"))
    hikariConfig.addDataSourceProperty(
      "password",
      properties.getString("password")
    )
    hikariConfig.addDataSourceProperty(
      "currentSchema",
      properties.getString("currentSchema")
    )

    new HikariDataSource(hikariConfig)
  }
}
