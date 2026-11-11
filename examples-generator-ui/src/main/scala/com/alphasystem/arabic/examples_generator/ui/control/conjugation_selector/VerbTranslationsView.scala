package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugation_selector

import arabic.model.ProNoun
import javafx.scene.control.{ Control, Skin }
import scalafx.collections.ObservableMap

class VerbTranslationsView extends Control {

  private[conjugation_selector] val translationsProperties = ObservableMap[ProNoun, String]()

  setSkin(createDefaultSkin())

  def translations: Map[ProNoun, String] = translationsProperties.toMap

  def update(translations: Map[ProNoun, String]): Unit = translationsProperties.addAll(translations)

  override def createDefaultSkin(): Skin[?] = skin.VerbTranslationsSkin(this)
}

object VerbTranslationsView {
  def apply(): VerbTranslationsView = new VerbTranslationsView()
}
