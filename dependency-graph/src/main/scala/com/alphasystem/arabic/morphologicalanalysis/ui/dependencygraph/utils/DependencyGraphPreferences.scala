package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package utils

import fx.ui.util.UIUserPreferences

class DependencyGraphPreferences extends UIUserPreferences(classOf[DependencyGraphPreferences]) {

  override protected val nodePrefix: String = "dependency-graph"
}

object DependencyGraphPreferences {

  def apply(): DependencyGraphPreferences = new DependencyGraphPreferences()
}
