package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import persistence.cache.CacheFactory
import persistence.nitrite.DatabaseSettings
import morphology.utils.*
import com.typesafe.config.ConfigFactory

import java.nio.file.Path

trait DatabaseInit {

  private val config = ConfigFactory.load()

  private[persistence] val rootPath: Path =
    System.getProperty("user.home").toPath + Seq(".morphological_analysis", "no2")

  private[persistence] val databaseSettings: DatabaseSettings = DatabaseSettings(config.getConfig("app.nitrite"))

  protected val database: Database = NitriteDatabase(rootPath, databaseSettings)

  protected val cacheFactory: CacheFactory = CacheFactory(database)
}
