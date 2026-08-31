package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import arabic.fx.ui.util.UIUserPreferences
import morphologicalengine.conjugation.model.DetailedConjugation
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.ObjectProperty

class DetailedConjugationView(using preferences: UIUserPreferences) extends Control {

  private[control] val detailedConjugationProperty = ObjectProperty[DetailedConjugation](this, "detailedConjugation")

  setSkin(createDefaultSkin())

  def detailedConjugation: DetailedConjugation = detailedConjugationProperty.value
  def detailedConjugation_=(value: DetailedConjugation): Unit = detailedConjugationProperty.value = value

  override def createDefaultSkin(): Skin[?] =  skin.DetailedConjugationSkin(this)
}

object DetailedConjugationView {
  def apply()(using preferences: UIUserPreferences): DetailedConjugationView = new DetailedConjugationView()
}
