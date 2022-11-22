package com.alphasystem
package arabic
package morphologicalanalysis
package ui

import morphology.persistence.cache.CacheFactory
import morphology.persistence.repository.{ Database, DependencyGraphRepository, GraphNodeRepository }
import com.alphasystem.arabic.morphologicalanalysis.ui.commons.service.ServiceFactory
import com.typesafe.config.ConfigFactory

trait AppInit {

  private val config = ConfigFactory.load()
  private val dataSource = Database.datasourceForConfig(config)
  private val database = Database(config)
  private val dependencyGraphRepository = DependencyGraphRepository(dataSource)
  private val graphNodeRepository = GraphNodeRepository(dataSource)
  private val cacheFactory = CacheFactory(
    database,
    dependencyGraphRepository,
    graphNodeRepository
  )
  protected val serviceFactory: ServiceFactory = ServiceFactory(cacheFactory)
}
