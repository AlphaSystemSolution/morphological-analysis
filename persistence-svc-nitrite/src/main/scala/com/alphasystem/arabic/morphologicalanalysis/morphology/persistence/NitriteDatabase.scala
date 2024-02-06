package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import morphology.graph.model.{ DependencyGraph, GraphNode, PhraseInfo }
import morphology.utils.*
import morphology.model.{ Chapter, Token, Verse }
import persistence.nitrite.DatabaseSettings
import persistence.nitrite.collections.{
  ChapterCollection,
  DependencyGraphCollection,
  GraphNodeCollection,
  TokenCollection,
  VerseCollection
}
import org.dizitart.no2.Nitrite

import java.nio.file.Path
import java.util.UUID
import scala.concurrent.Future
import scala.util.{ Failure, Success, Try }

class NitriteDatabase(rootPath: Path, dbSettings: DatabaseSettings) extends MorphologicalAnalysisDatabase {

  private val db: Nitrite = {
    val _db = Nitrite
      .builder()
      .compressed()
      .filePath((rootPath -> dbSettings.fileName).toString)

    dbSettings.userName match {
      case Some(userName) => _db.openOrCreate(userName, dbSettings.password.getOrElse(userName))
      case None           => _db.openOrCreate()
    }
  }

  private val chapterCollection = ChapterCollection(db)
  private val verseCollection = VerseCollection(db)
  private val tokenCollection = TokenCollection(db)
  private val dependencyGraphCollection = DependencyGraphCollection(db)
  private val graphNodeCollection = GraphNodeCollection(db)

  override def createChapter(chapter: Chapter): Future[Done] =
    Try(chapterCollection.createChapter(chapter)) match
      case Failure(ex) => Future.failed(ex)
      case Success(_)  => Future.successful(Done)

  override def createVerses(verses: Seq[Verse]): Future[Done] =
    Try(verseCollection.createVerses(verses)) match
      case Failure(ex) => Future.failed(ex)
      case Success(_)  => Future.successful(Done)

  override def createTokens(tokens: Seq[Token]): Future[Done] =
    Try(tokenCollection.createTokens(tokens)) match
      case Failure(ex) => Future.failed(ex)
      case Success(_)  => Future.successful(Done)

  override def createOrUpdateDependencyGraph(dependencyGraph: DependencyGraph): Future[Done] = {
    Try {
      dependencyGraphCollection.upsertDependencyGraph(dependencyGraph)
      graphNodeCollection.upsertNodes(dependencyGraph.nodes)
    } match
      case Failure(ex) => Future.failed(ex)
      case Success(_)  => Future.successful(Done)
  }

  override def createNode(node: GraphNode): Future[Done] =
    Try(graphNodeCollection.upsertNodes(Seq(node))) match
      case Failure(ex) => Future.failed(ex)
      case Success(_)  => Future.successful(Done)

  override def updateToken(token: Token): Future[Done] =
    Try(tokenCollection.update(token)) match
      case Failure(ex) => Future.failed(ex)
      case Success(_)  => Future.successful(Done)

  override def findChapterById(chapterNumber: Int): Future[Option[Chapter]] =
    Future.successful(chapterCollection.findByChapterNumber(chapterNumber))

  override def findAllChapters: Future[Seq[Chapter]] = Future.successful(chapterCollection.findAll)

  override def findVerseById(verseId: Long): Future[Option[Verse]] =
    Future.successful(verseCollection.findById(verseId))

  override def findVersesByChapterNumber(chapterNumber: Int): Future[Seq[Verse]] =
    Future.successful(verseCollection.findByChapterNumber(chapterNumber))

  override def findTokenById(tokenId: Long): Future[Option[Token]] =
    Future.successful(tokenCollection.findById(tokenId))

  override def findTokensByVerseId(verseId: Long): Future[Seq[Token]] =
    Future.successful(tokenCollection.findByVerseId(verseId))

  override def findGraphNodeById(id: UUID): Future[Option[GraphNode]] =
    Future.successful(graphNodeCollection.findById(id))

  override def findDependencyGraphById(dependencyGraphId: UUID): Future[Option[DependencyGraph]] =
    Future.successful(dependencyGraphCollection.findById(dependencyGraphId))

  override def findDependencyGraphByChapterAndVerseNumber(
    chapterNumber: Int,
    verseNumber: Int
  ): Future[Seq[DependencyGraph]] =
    Future.successful(dependencyGraphCollection.findByChapterAndVerseNumber(chapterNumber, verseNumber))

  override def removeTokensByVerseId(verseId: Long): Future[Done] =
    Try(tokenCollection.deleteByVerseId(verseId)) match
      case Failure(ex) => Future.failed(ex)
      case Success(_)  => Future.successful(Done)

  override def removeNode(nodeId: UUID): Future[Int] = Future.successful(graphNodeCollection.removeNode(nodeId))

  override def removeNodesByDependencyGraphId(dependencyGraphId: UUID): Future[Int] =
    Future.successful(graphNodeCollection.removeByDependencyGraphId(dependencyGraphId))

  override def removeGraph(dependencyGraphId: UUID): Future[Done] = {
    dependencyGraphCollection.removeGraph(dependencyGraphId)
    Try(graphNodeCollection.removeByDependencyGraphId(dependencyGraphId)) match
      case Failure(ex) => Future.failed(ex)
      case Success(_)  => Future.successful(Done)
  }

  override def close(): Future[Done] = {
    chapterCollection.collection.close()
    verseCollection.collection.close()
    tokenCollection.collection.close()
    Try(graphNodeCollection.collection.close()) match
      case Failure(ex) => Future.failed(ex)
      case Success(_)  => Future.successful(Done)
  }

  override def addPhraseInfo(phraseInfo: PhraseInfo): Future[Done] = ???

  override def findPhraseInfo(id: UUID): Future[Option[PhraseInfo]] = ???
}

object NitriteDatabase {
  def apply(rootPath: Path, dbSettings: DatabaseSettings): MorphologicalAnalysisDatabase =
    new NitriteDatabase(rootPath, dbSettings)
}
