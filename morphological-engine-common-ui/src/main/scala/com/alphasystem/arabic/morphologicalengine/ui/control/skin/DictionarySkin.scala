package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import com.alphasystem.arabic.fx.ui.Browser
import com.alphasystem.arabic.morphologicalengine.conjugation.model.RootLetters
import javafx.scene.control.SkinBase
// import scalafx.Includes.*

class DictionarySkin(control: DictionaryView) extends SkinBase[DictionaryView](control) {

  private val dictionaryBrowser = Browser()

  loadDictionary(control.rootLetters)
  dictionaryBrowser.prefWidth = control.getPrefWidth
  dictionaryBrowser.prefWidthProperty.bind(control.prefWidthProperty)
  control.rootLettersProperty.onChange((_, _, nv) => loadDictionary(nv))

  getChildren.add(dictionaryBrowser)

  private def loadDictionary(rootLetters: RootLetters): Unit =
    dictionaryBrowser.loadUrl(DictionaryView.getMawridReaderUrl(rootLetters))
}

object DictionarySkin {
  def apply(control: DictionaryView): DictionarySkin = new DictionarySkin(control)
}
