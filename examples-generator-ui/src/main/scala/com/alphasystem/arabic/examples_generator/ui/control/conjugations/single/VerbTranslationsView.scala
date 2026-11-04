package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package single

import com.alphasystem.arabic.model.ProNoun
import javafx.scene.control.{ Control, Skin }
import scalafx.collections.ObservableMap

class VerbTranslationsView extends Control {

  private[single] val translationsProperties = ObservableMap[ProNoun, String]()

  setSkin(createDefaultSkin())

  def translations: Map[ProNoun, String] = translationsProperties.toMap

  def update(translations: Map[ProNoun, String]): Unit = {
    translationsProperties.clear()
    translationsProperties.addAll(translations)
  }

  override def createDefaultSkin(): Skin[?] = skin.VerbTranslationsSkin(this)
}

object VerbTranslationsView {
  def apply(): VerbTranslationsView = new VerbTranslationsView()
}
