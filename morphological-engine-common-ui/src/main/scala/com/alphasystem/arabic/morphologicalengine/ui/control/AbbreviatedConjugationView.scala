package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import arabic.fx.ui.util.UIUserPreferences
import morphologicalengine.conjugation.model.{ AbbreviatedConjugation, ConjugationHeader }
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.ObjectProperty

class AbbreviatedConjugationView(using preferences: UIUserPreferences) extends Control {

  private[control] val conjugationHeaderProperty = ObjectProperty[ConjugationHeader](this, "conjugationHeader")
  private[control] val abbreviatedConjugationProperty =
    ObjectProperty[AbbreviatedConjugation](this, "abbreviatedConjugation")

  setSkin(createDefaultSkin())

  def abbreviatedConjugation: AbbreviatedConjugation = abbreviatedConjugationProperty.value
  def abbreviatedConjugation_=(value: AbbreviatedConjugation): Unit = abbreviatedConjugationProperty.value = value

  def conjugationHeader: ConjugationHeader = conjugationHeaderProperty.value
  def conjugationHeader_=(value: ConjugationHeader): Unit = conjugationHeaderProperty.value = value

  override def createDefaultSkin(): Skin[?] = skin.AbbreviatedConjugationSkin(this)
}

object AbbreviatedConjugationView {
  def apply()(using preferences: UIUserPreferences): AbbreviatedConjugationView = new AbbreviatedConjugationView()
}
