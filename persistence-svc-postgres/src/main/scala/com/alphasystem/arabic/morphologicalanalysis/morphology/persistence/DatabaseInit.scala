package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import persistence.cache.CacheFactory
import com.typesafe.config.ConfigFactory
import slick.jdbc.JdbcBackend.Database

trait DatabaseInit {

  import concurrent.ExecutionContext.Implicits.global
  private val config = ConfigFactory.load()
  protected val database: MorphologicalAnalysisDatabase = PostgresDatabaseImpl(Database.forConfig("postgres", config))
  protected val cacheFactory: CacheFactory = CacheFactory(database)
}
