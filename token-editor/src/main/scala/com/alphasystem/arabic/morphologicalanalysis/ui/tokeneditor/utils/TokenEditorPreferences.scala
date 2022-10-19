package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package utils

import fx.ui.util.UIUserPreferences

class TokenEditorPreferences extends UIUserPreferences(classOf[TokenEditorPreferences]) {

  override protected val nodePrefix: String = "token-editor"
}

object TokenEditorPreferences {

  def apply(): TokenEditorPreferences = new TokenEditorPreferences()
}
