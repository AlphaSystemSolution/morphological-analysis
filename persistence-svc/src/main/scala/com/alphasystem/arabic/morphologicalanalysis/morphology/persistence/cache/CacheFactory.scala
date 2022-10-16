package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.{ Chapter, Location, Token, Verse }
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.repository.{
  ChapterRepository,
  LocationRepository,
  TokenRepository,
  VerseRepository
}
import com.github.blemale.scaffeine.{ LoadingCache, Scaffeine }

import scala.concurrent.duration.*

class CacheFactory(
  chapterRepository: ChapterRepository,
  verseRepository: VerseRepository,
  tokenRepository: TokenRepository,
  locationRepository: LocationRepository) {

  lazy val chapters: LoadingCache[Option[Int], Seq[Chapter]] =
    Scaffeine()
      .recordStats()
      .expireAfterWrite(6.hour)
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
      .expireAfterWrite(6.hour)
      .maximumSize(300)
      .build(chapterNumber => verseRepository.findByChapterNumber(chapterNumber))

  lazy val tokens: LoadingCache[TokenRequest, Seq[Token]] =
    Scaffeine()
      .recordStats()
      .expireAfterWrite(1.hour)
      .maximumSize(500)
      .build(request =>
        tokenRepository.findByChapterAndVerse(
          request.chapterNumber,
          request.verseNumber
        )
      )

  lazy val locations: LoadingCache[LocationRequest, Seq[Location]] =
    Scaffeine()
      .recordStats()
      .expireAfterWrite(1.hour)
      .maximumSize(500)
      .build(request =>
        locationRepository.findByChapterVerseAndToken(
          request.chapterNumber,
          request.verseNumber,
          request.tokenNumber
        )
      )
}

object CacheFactory {

  def apply(
    chapterRepository: ChapterRepository,
    verseRepository: VerseRepository,
    tokenRepository: TokenRepository,
    locationRepository: LocationRepository
  ): CacheFactory =
    new CacheFactory(
      chapterRepository,
      verseRepository,
      tokenRepository,
      locationRepository
    )
}
