package com.alphasystem
package arabic
package morphologicalanalysis
package ui

import morphology.persistence.DatabaseInit
import morphology.persistence.cache.CacheFactory
import ui.commons.service.ServiceFactory

trait AppInit extends DatabaseInit {

  protected val serviceFactory: ServiceFactory = ServiceFactory(cacheFactory)
}
