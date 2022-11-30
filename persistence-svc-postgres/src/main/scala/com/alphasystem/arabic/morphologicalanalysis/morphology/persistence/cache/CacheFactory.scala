package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package cache

import com.alphasystem.arabic.morphologicalanalysis.morphology.graph.model.{ DependencyGraph, GraphNode }
import com.alphasystem.arabic.morphologicalanalysis.morphology.model.{ Chapter, Location, Token, Verse }
import morphology.persistence.repository.{ Database, DependencyGraphRepository, GraphNodeRepository }
import com.github.blemale.scaffeine.{ LoadingCache, Scaffeine }

import scala.concurrent.duration.*

class CacheFactory(
  val database: Database,
  val dependencyGraphRepository: DependencyGraphRepository,
  val graphNodeRepository: GraphNodeRepository) {

  lazy val chapters: LoadingCache[Option[Int], Seq[Chapter]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(6.hour)
      .maximumSize(200)
      .build(maybeChapterNumber =>
        maybeChapterNumber match
          case Some(chapterNumber) =>
            Seq(database.findChapterById(chapterNumber)).flatten
          case None => database.findAllChapters
      )

  lazy val verses: LoadingCache[Int, Seq[Verse]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(6.hour)
      .maximumSize(300)
      .build(chapterNumber => database.findVersesByChapterNumber(chapterNumber))

  lazy val tokens: LoadingCache[TokenRequest, Seq[Token]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(1.hour)
      .maximumSize(500)
      .build(request =>
        database
          .findTokensByChapterAndVerseNumber(
            request.chapterNumber,
            request.verseNumber
          )
          .sortBy(_.tokenNumber)
      )

  lazy val locations: LoadingCache[LocationRequest, Seq[Location]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(1.hour)
      .maximumSize(500)
      .build(request =>
        database.findLocationsByChapterVerseAndTokenNumber(
          request.chapterNumber,
          request.verseNumber,
          request.tokenNumber
        )
      )

  lazy val bulkLocations: LoadingCache[Seq[LocationRequest], Map[String, Seq[Location]]] =
    Scaffeine()
      .recordStats()
      .expireAfterWrite(1.hour)
      .maximumSize(500)
      .build { request =>
        val chapterIds = request.map(_.chapterNumber)
        val verseIds = request.map(_.verseNumber)
        val tokenIds = request.map(_.tokenNumber)
        database.findLocations(chapterIds, verseIds, tokenIds)
      }

  lazy val dependencyGraph: LoadingCache[String, Option[DependencyGraph]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(1.hour)
      .maximumSize(500)
      .build(id => dependencyGraphRepository.findById(id))

  lazy val dependencyGraphByChapterAndVerseNumber: LoadingCache[GetDependencyGraphRequest, Seq[DependencyGraph]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(2.hours)
      .maximumSize(1000)
      .build(request =>
        dependencyGraphRepository.findByChapterAndVerseNumber(request.chapterNumber, request.verseNumber)
      )

  lazy val graphNodes: LoadingCache[String, List[GraphNode]] =
    Scaffeine()
      .recordStats()
      .expireAfterWrite(5.hours)
      .maximumSize(1000)
      .build(id => graphNodeRepository.findByGraphId(id))
}

object CacheFactory {

  def apply(
    database: Database,
    dependencyGraphRepository: DependencyGraphRepository,
    graphNodeRepository: GraphNodeRepository
  ): CacheFactory =
    new CacheFactory(
      database,
      dependencyGraphRepository,
      graphNodeRepository
    )
}
