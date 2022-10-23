package com.alphasystem
package arabic
package morphologicalanalysis
package ui

import morphology.persistence.cache.CacheFactory
import morphology.persistence.repository.{
  ChapterRepository,
  Database,
  LocationRepository,
  TokenRepository,
  VerseRepository
}
import commons.service.ServiceFactory
import com.typesafe.config.ConfigFactory

trait AppInit {

  private val config = ConfigFactory.load()
  private val dataSource = Database.datasourceForConfig(config)
  private val chapterRepository = ChapterRepository(dataSource)
  private val verseRepository = VerseRepository(dataSource)
  private val tokenRepository = TokenRepository(dataSource)
  private val locationRepository = LocationRepository(dataSource)
  private val cacheFactory = CacheFactory(
    chapterRepository,
    verseRepository,
    tokenRepository,
    locationRepository
  )
  protected val serviceFactory: ServiceFactory = ServiceFactory(cacheFactory)
}
