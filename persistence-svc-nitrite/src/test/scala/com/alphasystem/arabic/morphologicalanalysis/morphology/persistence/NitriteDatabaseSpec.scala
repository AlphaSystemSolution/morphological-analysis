package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import morphologicalanalysis.graph.model.GraphNodeType
import persistence.nitrite.DatabaseSettings
import morphology.utils.*
import munit.FunSuite

import java.nio.file.{ Files, Path }
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

  test("Create single token and retrieve terminal node") {
    database.createTokens(Seq(token))
    assertEquals(database.findTokenById(token.id).get, token)

    val terminalNodes = database.findTerminalNodesByTokenIds(Seq(token.id))
    assertEquals(terminalNodes.size, 1)
    val terminalNode = terminalNodes.head
    assertEquals(terminalNode.graphNodeType, GraphNodeType.Terminal)
    assertEquals(terminalNode.token, token)
    assertEquals(terminalNode.partOfSpeechNodes, Seq.empty)
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

    val terminalNodes = database.findTerminalNodesByTokenIds(Seq(token.id))
    assertEquals(terminalNodes.size, 1)
    val terminalNode = terminalNodes.head
    assertEquals(terminalNode.graphNodeType, GraphNodeType.Terminal)
    assertEquals(terminalNode.token, expected)
    assertEquals(terminalNode.partOfSpeechNodes.map(_.location), expected.locations)
  }
}
