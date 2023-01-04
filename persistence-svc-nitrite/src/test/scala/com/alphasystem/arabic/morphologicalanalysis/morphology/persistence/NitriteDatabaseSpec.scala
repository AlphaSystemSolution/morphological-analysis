package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.graph.model.{
  DependencyGraph,
  GraphMetaInfo,
  PartOfSpeechInfo,
  TerminalInfo
}
import morphologicalanalysis.graph.model.GraphNodeType
import persistence.nitrite.DatabaseSettings
import morphology.utils.*
import munit.FunSuite

import java.nio.file.{ Files, Path }
import java.util.UUID
import java.util.stream.Collectors
import scala.jdk.CollectionConverters.*

class NitriteDatabaseSpec extends FunSuite with TestData {

  private val rootPath = "target".toPath + Seq(".no2")

  private val database = NitriteDatabase(rootPath, DatabaseSettings("morphological-analysis"))

  override def afterAll(): Unit = {
    super.afterAll()
    Files.list(rootPath).collect(Collectors.toList[Path]).asScala.foreach(Files.delete)
    Files.delete(rootPath)
    database.close()
  }

  test("Create single chapter") {
    database.createChapter(chapter)
    assertEquals(database.findChapterById(chapter.chapterNumber).get, chapter)
  }

  test("Create multiple chapters") {
    chapters.foreach(database.createChapter)
    val expected = 1 +: chapters.map(_.chapterNumber)
    assertEquals(database.findAllChapters.map(_.chapterNumber), expected)
  }

  test("Create and retrieve single verse") {
    database.createVerses(Seq(verse))
    assertEquals(database.findVerseById(verse.id).get, verse)
  }

  test("Retrieve verses by chapter Number") {
    val expected = database.findVersesByChapterNumber(verse.chapterNumber)
    assertEquals(Seq(verse), expected)
  }

  test("Create single token") {
    database.createTokens(Seq(token))
    assertEquals(database.findTokenById(token.id).get, token)
  }

  test("Retrieve TerminalInfo") {
    Thread.sleep(1000)
    val maybeTerminalInfo = database.findGraphInfoById(token.id.toUUID)
    assertEquals(maybeTerminalInfo.isDefined, true)
    val expectedTerminalInfo =
      TerminalInfo(graphNodeType = GraphNodeType.Terminal, token = token)
    assertEquals(maybeTerminalInfo.get, expectedTerminalInfo)
  }

  test("Update and retrieve token") {
    database.updateToken(updatedToken)
    val maybeExpectedToken = database.findTokenById(updatedToken.id)
    assertEquals(maybeExpectedToken.nonEmpty, true)
    val expected = maybeExpectedToken.get
    assertEquals(
      updatedToken,
      expected
    )
  }

  test("Retrieve updated TerminalInfo and PartOfSpeechInfo") {
    Thread.sleep(1000)
    val maybeTerminalInfo = database.findGraphInfoById(token.id.toUUID)
    assertEquals(maybeTerminalInfo.isDefined, true)
    val expectedTerminalInfo =
      TerminalInfo(graphNodeType = GraphNodeType.Terminal, token = updatedToken)
    assertEquals(maybeTerminalInfo.get, expectedTerminalInfo)

    updatedToken.locations.foreach { location =>
      val maybePartOfSpeechInfo = database.findGraphInfoById(location.id.toUUID)
      assertEquals(maybePartOfSpeechInfo.isDefined, true)
      val expectedPartOfSpeechInfo = PartOfSpeechInfo(location)
      assertEquals(maybePartOfSpeechInfo.get, expectedPartOfSpeechInfo)
    }
  }

  test("create and retrieve DependencyGraph") {
    database.createOrUpdateDependencyGraph(dependencyGraph)
    assertEquals(
      database.findDependencyGraphByChapterAndVerseNumber(updatedToken.chapterNumber, updatedToken.verseNumber),
      Seq(dependencyGraph)
    )
  }

  test("Removing token would remove terminal and part of speech info as well") {
    database.removeTokensByVerseId(verse.id)
    Thread.sleep(1000)
    assertEquals(database.findGraphInfoById(token.id.toUUID).isEmpty, true)
    updatedToken.locations.map(_.id).map(_.toUUID).foreach { id =>
      assertEquals(database.findGraphInfoById(id).isEmpty, true)
    }
  }
}
