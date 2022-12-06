package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import morphology.graph.model.DependencyGraph
import morphology.model.{ Chapter, Token, Verse }

import java.util.UUID

trait Database {
  def createChapter(chapter: Chapter): Unit
  def createVerses(verses: Seq[Verse]): Unit
  def createTokens(tokens: Seq[Token]): Unit
  def createOrUpdateDependencyGraph(dependencyGraph: DependencyGraph): Unit
  def updateToken(token: Token): Unit
  def findChapterById(chapterNumber: Int): Option[Chapter]
  def findAllChapters: List[Chapter]
  def findVerseById(verseId: Long): Option[Verse]
  def findVersesByChapterNumber(chapterNumber: Int): List[Verse]
  def findTokenById(tokenId: Long): Option[Token]
  def findTokensByVerseId(verseId: Long): Seq[Token]
  def findDependencyGraphByChapterAndVerseNumber(chapterNumber: Int, verseNumber: Int): Seq[DependencyGraph]
  def removeTokensByVerseId(verseId: Long): Unit
  def close(): Unit
}
