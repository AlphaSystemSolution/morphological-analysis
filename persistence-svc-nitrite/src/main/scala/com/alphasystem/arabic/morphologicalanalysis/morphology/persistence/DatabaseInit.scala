package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache.CacheFactory
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.nitrite.DatabaseSettings
import morphology.utils.*
import com.typesafe.config.ConfigFactory

trait DatabaseInit {

  private val config = ConfigFactory.load()

  private val rootPath = System.getProperty("user.home").toPath + Seq(".morphological_analysis", "no2")

  protected val database: Database = NitriteDatabase(rootPath, DatabaseSettings(config.getConfig("app.nitrite")))

  protected val cacheFactory: CacheFactory = CacheFactory(database)
}
