package com.alphasystem
package arabic
package morphologicalanalysis
package ui

import morphology.persistence.DatabaseInit
import ui.commons.service.ServiceFactory

trait AppInit extends DatabaseInit {

  import concurrent.ExecutionContext.Implicits.global
  protected val serviceFactory: ServiceFactory = ServiceFactory(cacheFactory)
}
