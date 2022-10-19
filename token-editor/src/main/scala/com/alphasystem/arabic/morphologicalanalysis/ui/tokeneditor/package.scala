package com.alphasystem
package arabic
package morphologicalanalysis
package ui

import tokeneditor.utils.TokenEditorPreferences

package object tokeneditor {

  implicit val tokenEditorPreferences: TokenEditorPreferences =
    new TokenEditorPreferences()
}
