package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import morphology.graph.model.PhraseInfo
import morphology.model.*
import munit.{ AnyFixture, FunSuite, FutureFixture, Tag }

import scala.concurrent.Future

trait PostgresDatabaseSpec extends BaseRepositorySpec {

  self: FunSuite =>

  import PostgresDatabaseSpec.*
  import concurrent.ExecutionContext.Implicits.global

  private var currentId: Long = 0L

  private val databaseFixture = new FutureFixture[Result]("DatabaseSpec") {
    private var result: Result = _
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
          case CreatePhraseInfo(phraseInfo) =>
            database
              .addPhraseInfo(phraseInfo)
              .map { id =>
                currentId = id
                Done
              }
              .map(DoneResult.apply)
          case FindPhraseInfo(id) => database.findPhraseInfo(id).map(FindPhraseInfoResult.apply)
        } match {
        case Some(value) => value.map(actualResult => result = actualResult)
        case None        => Future.successful(DoneResult(Done))
      }
  }

  test("ChapterRepository: retrieve non-existing chapter".tag(FindChapter(chapter.chapterNumber))) {
    assertEquals(databaseFixture(), FindChapterResult(None))
  }

  test("ChapterRepository: save chapter".tag(CreateChapter(chapter))) {
    assertEquals(databaseFixture(), DoneResult(Done))
  }

  test("ChapterRepository: retrieve existing chapter".tag(FindChapter(chapter.chapterNumber))) {
    assertEquals(databaseFixture(), FindChapterResult(Some(chapter)))
  }

  test("ChapterRepository: create many chapters".tag(CreateChapters(chapters))) {
    assertEquals(databaseFixture(), DoneResult(Done))
  }

  test("ChapterRepository: get all chapters".tag(FindAllChapters())) {
    assertEquals(databaseFixture().asInstanceOf[FindAllChaptersResult].chapters.map(_.id), 1 to 12)
  }

  test("VerseRepository: save verse".tag(CreateVerses(verses))) {
    assertEquals(databaseFixture(), DoneResult(Done))
  }

  test("VerseRepository: find verse".tag(FindVerse(1, 3))) {
    assertEquals(databaseFixture(), FindVerseResult(Some(verses(2))))
  }

  test("TokenRepository: save tokens".tag(CreateTokens(tokens))) {
    assertEquals(databaseFixture(), DoneResult(Done))
  }

  test("TokenRepository: find token".tag(FindToken(1, 1, 1))) {
    assertEquals(databaseFixture(), FindTokenResult(Seq(createToken(1, 1, 1))))
  }

  test("TokenRepository: find tokens by verse".tag(FindTokens(1, 3))) {
    assertEquals(databaseFixture(), FindTokenResult(createTokens(1, 3, 1, 10)))
  }

  test("TokenRepository: remove tokens".tag(RemoveTokens(1, 5))) {
    assertEquals(databaseFixture(), FindTokenResult(Seq.empty))
  }

  test("PhraseInfoRepository: find non-existing phrase".tag(FindPhraseInfo(currentId))) {
    assertEquals(databaseFixture(), FindPhraseInfoResult(None))
  }

  test("PhraseInfoRepository: create phrase".tag(CreatePhraseInfo(createPhraseInfo()))) {
    assertEquals(databaseFixture(), DoneResult(Done))
  }

  test("PhraseInfoRepository: find phrase".tag(FindPhraseInfo(101L))) {
    assertEquals(databaseFixture(), FindPhraseInfoResult(Some(createPhraseInfo(Some(currentId)))))
  }

  /*test("LocationRepository: save and retrieve location") {
    val locations = Seq(location)
    database.createLocations(token, locations)
    assertEquals(
      database
        .findLocationsByChapterVerseAndTokenNumber(location.chapterNumber, location.verseNumber, location.tokenNumber),
      locations
    )
  }*/

  /*test("TokenRepository: delete tokens") {
    database.deleteTokensByChapterAndVerseNumber(verse.chapterNumber, verse.verseNumber)
    assertEquals(database.findTokensByChapterAndVerseNumber(verse.chapterNumber, verse.verseNumber), Seq.empty)
    assertEquals(
      database
        .findLocationsByChapterVerseAndTokenNumber(location.chapterNumber, location.verseNumber, location.tokenNumber),
      Seq.empty
    )
  }*/

  override def munitFixtures: Seq[AnyFixture[_]] = Seq(databaseFixture)
}

object PostgresDatabaseSpec {

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
  final case class CreatePhraseInfo(phraseInfo: PhraseInfo) extends DatabaseTag
  final case class FindPhraseInfo(id: Long) extends DatabaseTag

  sealed trait Result
  final case class DoneResult(done: Done) extends Result
  final case class FindChapterResult(maybeChapter: Option[Chapter]) extends Result
  final case class FindAllChaptersResult(chapters: Seq[Chapter]) extends Result
  final case class FindVerseResult(maybeVerse: Option[Verse]) extends Result
  final case class FindTokenResult(tokens: Seq[Token]) extends Result
  final case class FindPhraseInfoResult(maybePhraseInfo: Option[PhraseInfo]) extends Result
}
