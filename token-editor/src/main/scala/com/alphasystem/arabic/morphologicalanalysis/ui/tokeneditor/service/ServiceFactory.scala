package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.service

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.{
  Chapter,
  Location,
  Token
}
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache.{
  CacheFactory,
  LocationRequest,
  TokenRequest
}
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.service.delegate.{
  ChapterService,
  LocationService,
  TokenService
}
import javafx.concurrent
import scalafx.concurrent.Service

class ServiceFactory(cacheFactory: CacheFactory) {

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
}

object ServiceFactory {

  def apply(cacheFactory: CacheFactory): ServiceFactory =
    new ServiceFactory(cacheFactory)
}
