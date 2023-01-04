package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import morphology.graph.model.{ DependencyGraph, GraphInfo, GraphNode }
import morphology.model.{ Chapter, Token, Verse }

import java.util.UUID

trait Database {
  def createChapter(chapter: Chapter): Unit
  def createVerses(verses: Seq[Verse]): Unit
  def createTokens(tokens: Seq[Token]): Unit
  def createOrUpdateDependencyGraph(dependencyGraph: DependencyGraph): Unit
  def createNode(node: GraphNode): Unit
  def updateToken(token: Token): Unit
  def findChapterById(chapterNumber: Int): Option[Chapter]
  def findAllChapters: List[Chapter]
  def findVerseById(verseId: Long): Option[Verse]
  def findVersesByChapterNumber(chapterNumber: Int): List[Verse]
  def findTokenById(tokenId: Long): Option[Token]
  def findTokensByVerseId(verseId: Long): Seq[Token]
  def findDependencyGraphById(dependencyGraphId: UUID): Option[DependencyGraph]
  def findDependencyGraphByChapterAndVerseNumber(chapterNumber: Int, verseNumber: Int): Seq[DependencyGraph]
  def findGraphInfoById(id: UUID): Option[GraphInfo]
  def removeTokensByVerseId(verseId: Long): Unit
  def removeNode(nodeId: UUID): Int
  def removeNodesByDependencyGraphId(dependencyGraphId: UUID): Int
  def close(): Unit
}
