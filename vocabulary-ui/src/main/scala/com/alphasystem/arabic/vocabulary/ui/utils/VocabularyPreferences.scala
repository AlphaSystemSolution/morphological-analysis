package com.alphasystem
package arabic
package vocabulary
package ui
package utils

import fx.ui.util.UIUserPreferences

class VocabularyPreferences extends UIUserPreferences(classOf[VocabularyPreferences]) {

  override protected val nodePrefix: String = "vocabulary-ui"
}

object VocabularyPreferences {
  def apply(): VocabularyPreferences = new VocabularyPreferences()
}
