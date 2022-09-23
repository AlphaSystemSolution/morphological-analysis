package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.utils

import com.alphasystem.fx.ui.util.UIUserPreferences

class TokenEditorPreferences
    extends UIUserPreferences(classOf[TokenEditorPreferences]) {

  override protected val nodePrefix: String = "token-editor"
}

object TokenEditorPreferences {

  def apply(): TokenEditorPreferences = new TokenEditorPreferences()
}
