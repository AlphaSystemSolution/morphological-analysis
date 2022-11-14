package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package cache

import com.alphasystem.arabic.morphologicalanalysis.morphology.graph.model.{ DependencyGraph, GraphNode }
import morphology.model.{ Chapter, Location, Token, Verse }
import repository.{
  ChapterRepository,
  DependencyGraphRepository,
  GraphNodeRepository,
  LocationRepository,
  TokenRepository,
  VerseRepository
}
import com.github.blemale.scaffeine.{ LoadingCache, Scaffeine }

import scala.concurrent.duration.*

class CacheFactory(
  val chapterRepository: ChapterRepository,
  val verseRepository: VerseRepository,
  val tokenRepository: TokenRepository,
  val locationRepository: LocationRepository,
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
            Seq(chapterRepository.findByChapterNumber(chapterNumber)).flatten
          case None => chapterRepository.findAll
      )

  lazy val verses: LoadingCache[Int, Seq[Verse]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(6.hour)
      .maximumSize(300)
      .build(chapterNumber => verseRepository.findByChapterNumber(chapterNumber))

  lazy val tokens: LoadingCache[TokenRequest, Seq[Token]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(1.hour)
      .maximumSize(500)
      .build(request =>
        tokenRepository
          .findByChapterAndVerse(
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
        locationRepository.findByChapterVerseAndToken(
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
      .build(request => locationRepository.findByTokenIds(request.map(_.toTokenId).toSet))

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

  lazy val graphNodes: LoadingCache[String, Seq[GraphNode]] =
    Scaffeine()
      .recordStats()
      .expireAfterWrite(5.hours)
      .maximumSize(1000)
      .build(id => graphNodeRepository.findByGraphId(id))
}

object CacheFactory {

  def apply(
    chapterRepository: ChapterRepository,
    verseRepository: VerseRepository,
    tokenRepository: TokenRepository,
    locationRepository: LocationRepository,
    dependencyGraphRepository: DependencyGraphRepository,
    graphNodeRepository: GraphNodeRepository
  ): CacheFactory =
    new CacheFactory(
      chapterRepository,
      verseRepository,
      tokenRepository,
      locationRepository,
      dependencyGraphRepository,
      graphNodeRepository
    )
}
