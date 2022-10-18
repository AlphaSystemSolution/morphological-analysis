package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.service

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.{ Chapter, Location, Token }
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache.{
  CacheFactory,
  LocationRequest,
  TokenRequest
}
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.repository.LocationRepository
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.service.delegate.{
  ChapterService,
  LocationService,
  TokenService
}
import javafx.concurrent
import javafx.concurrent.{ Task, Service as JService }
import scalafx.concurrent.Service

class ServiceFactory(cacheFactory: CacheFactory) {

  private lazy val locationRepository: LocationRepository = cacheFactory.locationRepository

  lazy val chapterService: Int => Service[Seq[Chapter]] =
    (chapterId: Int) =>
      new Service[Seq[Chapter]](
        new ChapterService(cacheFactory)
      ) {}

  lazy val tokenService: TokenRequest => Service[Seq[Token]] =
    (tokenRequest: TokenRequest) =>
      new Service[Seq[Token]](
        new TokenService(cacheFactory, tokenRequest)
      ) {}

  lazy val locationService: LocationRequest => Service[Seq[Location]] =
    (locationRequest: LocationRequest) =>
      new Service[Seq[Location]](
        new LocationService(cacheFactory, locationRequest)
      ) {}

  lazy val createLocations: (LocationRequest, List[Location]) => Service[Unit] =
    (locationRequest: LocationRequest, locations: List[Location]) =>
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
