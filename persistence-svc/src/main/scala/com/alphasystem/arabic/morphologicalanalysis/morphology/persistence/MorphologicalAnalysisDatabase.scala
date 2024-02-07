package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import morphology.graph.model.{ DependencyGraph, GraphNode, PhraseInfo, RelationshipInfo }
import morphology.model.{ Chapter, Token, Verse }

import java.util.UUID
import scala.concurrent.Future

trait MorphologicalAnalysisDatabase {
  def createChapter(chapter: Chapter): Future[Done]
  def createVerses(verses: Seq[Verse]): Future[Done]
  def createTokens(tokens: Seq[Token]): Future[Done]
  def createOrUpdateDependencyGraph(dependencyGraph: DependencyGraph): Future[Done]
  def createNode(node: GraphNode): Future[Done]
  def updateToken(token: Token): Future[Done]
  def findChapterById(chapterNumber: Int): Future[Option[Chapter]]
  def findAllChapters: Future[Seq[Chapter]]
  def findVerseById(verseId: Long): Future[Option[Verse]]
  def findVersesByChapterNumber(chapterNumber: Int): Future[Seq[Verse]]
  def findTokenById(tokenId: Long): Future[Option[Token]]
  def findTokensByVerseId(verseId: Long): Future[Seq[Token]]
  def addPhraseInfo(phraseInfo: PhraseInfo): Future[Long]
  def findPhraseInfo(id: Long): Future[Option[PhraseInfo]]
  def createRelationshipInfo(relationshipInfo: RelationshipInfo): Future[RelationshipInfo]
  def findRelationshipInfo(id: Long): Future[Option[RelationshipInfo]]
  def findGraphNodeById(id: UUID): Future[Option[GraphNode]]
  def findDependencyGraphById(dependencyGraphId: UUID): Future[Option[DependencyGraph]]
  def findDependencyGraphByChapterAndVerseNumber(chapterNumber: Int, verseNumber: Int): Future[Seq[DependencyGraph]]
  def removeTokensByVerseId(verseId: Long): Future[Done]
  def removeNode(nodeId: UUID): Future[Int]
  def removeNodesByDependencyGraphId(dependencyGraphId: UUID): Future[Int]
  def removeGraph(dependencyGraphId: UUID): Future[Done]
  def close(): Future[Done]
}
