package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package service

import morphology.model.{ Chapter, Location, Token }
import morphology.persistence.cache.{ CacheFactory, LocationRequest, TokenRequest }
import javafx.concurrent
import javafx.concurrent.{ Task, Service as JService }
import scalafx.concurrent.Service

class ServiceFactory(cacheFactory: CacheFactory) {

  private lazy val tokenRepository = cacheFactory.tokenRepository
  private lazy val locationRepository = cacheFactory.locationRepository

  lazy val chapterService: Option[Int] => Service[Seq[Chapter]] =
    (maybeChapterId: Option[Int]) =>
      new Service[Seq[Chapter]](
        new JService[Seq[Chapter]]:
          override def createTask(): Task[Seq[Chapter]] =
            new Task[Seq[Chapter]]():
              override def call(): Seq[Chapter] = cacheFactory.chapters.get(maybeChapterId)
      ) {}

  lazy val tokenService: TokenRequest => Service[Seq[Token]] =
    (tokenRequest: TokenRequest) =>
      new Service[Seq[Token]](
        new JService[Seq[Token]]:
          override def createTask(): Task[Seq[Token]] =
            new Task[Seq[Token]]():
              override def call(): Seq[Token] = cacheFactory.tokens.get(tokenRequest)
      ) {}

  lazy val locationService: LocationRequest => Service[Seq[Location]] =
    (locationRequest: LocationRequest) =>
      new Service[Seq[Location]](
        new JService[Seq[Location]]:
          override def createTask(): Task[Seq[Location]] =
            new Task[Seq[Location]]():
              override def call(): Seq[Location] = cacheFactory.locations.get(locationRequest)
      ) {}

  lazy val saveData: (LocationRequest, SaveRequest) => Service[Unit] =
    (locationRequest: LocationRequest, request: SaveRequest) =>
      new Service[Unit](
        new JService[Unit] {
          override def createTask(): Task[Unit] = {
            new Task[Unit]() {
              override def call(): Unit = {
                locationRepository.deleteByChapterVerseAndToken(
                  locationRequest.chapterNumber,
                  locationRequest.verseNumber,
                  locationRequest.tokenNumber
                )

                val token = request.token
                tokenRepository.update(token)
                cacheFactory.tokens.invalidate(TokenRequest(token.chapterNumber, token.verseNumber))

                val locations = request.locations
                locationRepository.bulkCreate(locations)
                cacheFactory.locations.put(locationRequest, locations)
              }
            }
          }
        }
      ) {}

}

object ServiceFactory {

  def apply(cacheFactory: CacheFactory): ServiceFactory =
    new ServiceFactory(cacheFactory)
}

case class SaveRequest(token: Token, locations: List[Location])
