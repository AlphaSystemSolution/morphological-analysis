package com.alphasystem
package arabic
package morphologicalengine
package ui
package utils

import fx.ui.util.UIUserPreferences

class MorphologicalEnginePreferences extends UIUserPreferences(classOf[MorphologicalEnginePreferences]) {

  override protected val nodePrefix: String = "morphological-engine"
}

object MorphologicalEnginePreferences {
  def apply(): MorphologicalEnginePreferences = new MorphologicalEnginePreferences()
}
