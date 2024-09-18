package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.*
import persistence.nitrite.DatabaseSettings
import morphology.utils.*
import munit.{ FunSuite, FutureFixture, Tag }

import java.nio.file.{ Files, Path }
import java.util.stream.Collectors
import scala.concurrent.Future
import scala.jdk.CollectionConverters.*
import scala.util.{ Failure, Success }
import scala.compiletime.uninitialized

class NitriteDatabaseSpec extends FunSuite with TestData {

  import NitriteDatabaseSpec.*
  import concurrent.ExecutionContext.Implicits.global

  private val rootPath = "target".toPath + Seq(".no2")

  private val database = NitriteDatabase(rootPath, DatabaseSettings("morphological-analysis"))

  private val databaseFixture = new FutureFixture[Result]("DatabaseSpec") {
    private var result: Result = uninitialized

    override def apply(): Result = result

    override def beforeEach(context: BeforeEach): Future[Unit] =
      context
        .test
        .tags
        .collectFirst {
          case CreateChapter(chapter) => database.createChapter(chapter).map(DoneResult.apply)
          case CreateChapters(chapters) =>
            Future.traverse(chapters)(database.createChapter).map(_ => Done).map(DoneResult.apply)
          case FindChapter(chapterNumber) => database.findChapterById(chapterNumber).map(FindChapterResult.apply)
          case FindAllChapters()          => database.findAllChapters.map(FindAllChaptersResult.apply)
          case CreateVerses(verses)       => database.createVerses(verses).map(DoneResult.apply)
          case FindVerse(chapterNumber, verseNumber) =>
            database.findVerseById(verseNumber.toVerseId(chapterNumber)).map(FindVerseResult.apply)
          case CreateTokens(tokens) => database.createTokens(tokens).map(DoneResult.apply)
          case UpdateToken(token) =>
            for {
              _ <- database.updateToken(token)
              maybeToken <- database.findTokenById(token.id)
            } yield FindTokenResult(Seq(maybeToken).flatten)
          case RemoveTokens(chapterNumber, verseNumber) =>
            for {
              _ <- database.removeTokensByVerseId(verseNumber.toVerseId(chapterNumber))
              tokens <- database.findTokensByVerseId(verseNumber.toVerseId(chapterNumber))
            } yield FindTokenResult(tokens)
          case FindToken(chapterNumber, verseNumber, tokenNumber) =>
            database
              .findTokenById(tokenNumber.toTokenId(chapterNumber, verseNumber))
              .map(maybeToken => FindTokenResult(Seq(maybeToken).flatten))
          case FindTokens(chapterNumber, verseNumber) =>
            database.findTokensByVerseId(verseNumber.toVerseId(chapterNumber)).map(FindTokenResult.apply)
        } match {
        case Some(value) => value.map(actualResult => result = actualResult)
        case None        => Future.failed(new RuntimeException("No data provided."))
      }
  }

  override def afterAll(): Unit = {
    super.afterAll()
    Files.list(rootPath).collect(Collectors.toList[Path]).asScala.foreach(Files.delete)
    Files.delete(rootPath)
    database.close().onComplete {
      case Failure(_) => ()
      case Success(_) => ()
    }
  }

  test(
    "ChapterRepository: retrieve non-existing chapter".ignore.tag(FindChapter(chapter.chapterNumber))
  ) {
    assertEquals(databaseFixture(), FindChapterResult(None))
  }

  test("ChapterRepository: save chapter".tag(CreateChapter(chapter)).ignore) {
    assertEquals(databaseFixture(), DoneResult(Done))
  }

  test("ChapterRepository: retrieve existing chapter".tag(FindChapter(chapter.chapterNumber)).ignore) {
    assertEquals(databaseFixture(), FindChapterResult(Some(chapter)))
  }

  test("ChapterRepository: create many chapters".tag(CreateChapters(chapters)).ignore) {
    assertEquals(databaseFixture(), DoneResult(Done))
  }

  test("ChapterRepository: get all chapters".tag(FindAllChapters()).ignore) {
    assertEquals(databaseFixture().asInstanceOf[FindAllChaptersResult].chapters.map(_.id), 1 to 12)
  }

  test("VerseRepository: save verse".tag(CreateVerses(verses)).ignore) {
    assertEquals(databaseFixture(), DoneResult(Done))
  }

  test("VerseRepository: find verse".tag(FindVerse(1, 3)).ignore) {
    assertEquals(databaseFixture(), FindVerseResult(Some(verses(2))))
  }

  /*test("Create single token") {
    database.createTokens(Seq(token))
    assertEquals(database.findTokenById(token.id).get, token)
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
  }*/

  /*test("create and retrieve DependencyGraph") {
    database.createOrUpdateDependencyGraph(dependencyGraph)
    assertEquals(
      database.findDependencyGraphByChapterAndVerseNumber(updatedToken.chapterNumber, updatedToken.verseNumber),
      Seq(dependencyGraph)
    )
  }*/
}

object NitriteDatabaseSpec {

  sealed abstract class DatabaseTag extends Tag("DatabaseTag")
  final case class FindChapter(chapterNumber: Int) extends DatabaseTag
  final case class CreateChapter(chapter: Chapter) extends DatabaseTag
  final case class CreateChapters(chapters: Seq[Chapter]) extends DatabaseTag
  final case class FindAllChapters() extends DatabaseTag
  final case class CreateVerses(verses: Seq[Verse]) extends DatabaseTag
  final case class FindVerse(chapterNumber: Int, verseNumber: Int) extends DatabaseTag
  final case class CreateTokens(tokens: Seq[Token]) extends DatabaseTag
  final case class UpdateToken(token: Token) extends DatabaseTag
  final case class FindToken(chapterNumber: Int, verseNumber: Int, tokenNumber: Int) extends DatabaseTag
  final case class FindTokens(chapterNumber: Int, verseNumber: Int) extends DatabaseTag
  final case class RemoveTokens(chapterNumber: Int, verseNumber: Int) extends DatabaseTag

  sealed trait Result

  final case class DoneResult(done: Done) extends Result
  final case class FindChapterResult(maybeChapter: Option[Chapter]) extends Result
  final case class FindAllChaptersResult(chapters: Seq[Chapter]) extends Result
  final case class FindVerseResult(maybeVerse: Option[Verse]) extends Result
  final case class FindTokenResult(tokens: Seq[Token]) extends Result
}
