package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package cache

import morphology.graph.model.{ DependencyGraph, GraphNode }
import morphology.model.{ Chapter, Token, Verse }
import com.github.blemale.scaffeine.{ AsyncLoadingCache, Scaffeine }

import java.util.UUID
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.*

class CacheFactory(val database: MorphologicalAnalysisDatabase)(implicit ec: ExecutionContext) {

  lazy val chapters: AsyncLoadingCache[Int, Seq[Chapter]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(6.hour)
      .maximumSize(200)
      .buildAsyncFuture(_ => database.findAllChapters)

  lazy val verses: AsyncLoadingCache[Int, Seq[Verse]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(6.hour)
      .maximumSize(1000)
      .buildAsyncFuture(chapterNumber => database.findVersesByChapterNumber(chapterNumber))

  lazy val tokens: AsyncLoadingCache[TokenRequest, Seq[Token]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(6.hour)
      .maximumSize(1000)
      .buildAsyncFuture(request => database.findTokensByVerseId(request.verseId).map(_.sortBy(_.tokenNumber)))

  lazy val dependencyGraphById: AsyncLoadingCache[UUID, Option[DependencyGraph]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(6.hours)
      .maximumSize(1000)
      .buildAsyncFuture(id => database.findDependencyGraphById(id))

  lazy val dependencyGraphByChapterAndVerseNumber: AsyncLoadingCache[GetDependencyGraphRequest, Seq[DependencyGraph]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(6.hours)
      .maximumSize(1000)
      .buildAsyncFuture(request =>
        database.findDependencyGraphByChapterAndVerseNumber(request.chapterNumber, request.verseNumber)
      )

  lazy val graphNodeById: AsyncLoadingCache[UUID, Option[GraphNode]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(6.hours)
      .maximumSize(1000)
      .buildAsyncFuture(id => database.findGraphNodeById(id))
}

object CacheFactory {
  def apply(database: MorphologicalAnalysisDatabase)(implicit ec: ExecutionContext) = new CacheFactory(database)
}
