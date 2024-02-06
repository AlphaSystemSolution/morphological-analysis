package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import morphology.graph.model.{ DependencyGraph, GraphNode, PhraseInfo }
import morphology.model.{ Chapter, Token, Verse }
import persistence.repository.impl.chapter.ChapterRepository
import persistence.repository.impl.phrase_info.PhraseInfoRepository
import persistence.repository.impl.token.TokenRepository
import persistence.repository.impl.verse.VerseRepository
import slick.jdbc.JdbcBackend.Database

import java.util.UUID
import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Try

class PostgresDatabaseImpl(db: Database)(implicit ec: ExecutionContext) extends MorphologicalAnalysisDatabase {

  private val chapterRepository = ChapterRepository(db)
  private val verseRepository = VerseRepository(db)
  private val tokenRepository = TokenRepository(db)
  private val phraseInfoRepository = PhraseInfoRepository(db)

  override def createChapter(chapter: Chapter): Future[Done] = chapterRepository.addOrUpdateChapter(chapter)

  override def createVerses(verses: Seq[Verse]): Future[Done] = verseRepository.addVerses(verses)

  override def createTokens(tokens: Seq[Token]): Future[Done] = tokenRepository.createTokens(tokens)

  override def createOrUpdateDependencyGraph(dependencyGraph: DependencyGraph): Future[Done] = ???

  override def createNode(node: GraphNode): Future[Done] = ???

  override def updateToken(token: Token): Future[Done] = tokenRepository.updateToken(token)

  override def findChapterById(chapterNumber: Int): Future[Option[Chapter]] =
    chapterRepository.getByChapterNumber(chapterNumber)

  override def findAllChapters: Future[Seq[Chapter]] = chapterRepository.findAll

  override def findVerseById(verseId: Long): Future[Option[Verse]] = verseRepository.getById(verseId)

  override def findVersesByChapterNumber(chapterNumber: Int): Future[Seq[Verse]] =
    verseRepository.getByChapterNumber(chapterNumber)

  override def findTokenById(tokenId: Long): Future[Option[Token]] = tokenRepository.findTokenById(tokenId)

  override def findTokensByVerseId(verseId: Long): Future[Seq[Token]] = tokenRepository.findTokensByVerseId(verseId)

  override def addPhraseInfo(phraseInfo: PhraseInfo): Future[Long] = phraseInfoRepository.createPhraseInfo(phraseInfo)

  override def findPhraseInfo(id: Long): Future[Option[PhraseInfo]] = phraseInfoRepository.findById(id)

  override def findGraphNodeById(id: UUID): Future[Option[GraphNode]] = ???

  override def findDependencyGraphById(dependencyGraphId: UUID): Future[Option[DependencyGraph]] = ???

  override def findDependencyGraphByChapterAndVerseNumber(
    chapterNumber: Int,
    verseNumber: Int
  ): Future[Seq[DependencyGraph]] = ???

  override def removeTokensByVerseId(verseId: Long): Future[Done] = tokenRepository.removeTokensByVerseId(verseId)

  override def removeNode(nodeId: UUID): Future[Int] = ???

  override def removeNodesByDependencyGraphId(dependencyGraphId: UUID): Future[Int] = ???

  override def removeGraph(dependencyGraphId: UUID): Future[Done] = ???

  override def close(): Future[Done] = {
    Try(db.close)
    Future.successful(Done)
  }
}

object PostgresDatabaseImpl {

  def apply(db: Database)(implicit ec: ExecutionContext): MorphologicalAnalysisDatabase = new PostgresDatabaseImpl(db)
}
