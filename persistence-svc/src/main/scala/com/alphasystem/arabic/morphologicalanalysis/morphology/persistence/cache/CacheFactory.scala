package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package cache

import morphology.graph.model.DependencyGraph
import morphology.model.{ Token, Verse }
import com.github.blemale.scaffeine.{ LoadingCache, Scaffeine }

import java.util.UUID

import scala.concurrent.duration.*

class CacheFactory(val database: MorphologicalAnalysisDatabase) {

  lazy val verses: LoadingCache[Int, Seq[Verse]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(6.hour)
      .maximumSize(1000)
      .build(chapterNumber => database.findVersesByChapterNumber(chapterNumber))

  lazy val tokens: LoadingCache[TokenRequest, Seq[Token]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(6.hour)
      .maximumSize(1000)
      .build(request => database.findTokensByVerseId(request.verseId).sortBy(_.tokenNumber))

  lazy val dependencyGraphById: LoadingCache[UUID, Option[DependencyGraph]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(6.hours)
      .maximumSize(1000)
      .build(id => database.findDependencyGraphById(id))

  lazy val dependencyGraphByChapterAndVerseNumber: LoadingCache[GetDependencyGraphRequest, Seq[DependencyGraph]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(6.hours)
      .maximumSize(1000)
      .build(request => database.findDependencyGraphByChapterAndVerseNumber(request.chapterNumber, request.verseNumber))
}

object CacheFactory {
  def apply(database: MorphologicalAnalysisDatabase) = new CacheFactory(database)
}
