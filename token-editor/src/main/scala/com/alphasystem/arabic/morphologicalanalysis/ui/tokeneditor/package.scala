package com.alphasystem.arabic.morphologicalanalysis.ui

import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.utils.TokenEditorPreferences

package object tokeneditor {

  implicit val tokenEditorPreferences: TokenEditorPreferences =
    new TokenEditorPreferences()
}
