package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations

import examples_generator.morphologicalchart.Conjugations
import ui.model.{ Action, ConjugationsRequest }
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.ObjectProperty

class ConjugationsFileGeneratorView extends Control {

  val conjugationsRequestProperty: ObjectProperty[ConjugationsRequest] =
    ObjectProperty[ConjugationsRequest](this, "conjugationsRequest")
  private[conjugations] val actionProperty = ObjectProperty[Action](this, "action")

  conjugationsRequest = ConjugationsRequest(Conjugations())
  setSkin(skin.ConjugationsFileGeneratorSkin(this))

  def conjugationsRequest: ConjugationsRequest = conjugationsRequestProperty.value
  def conjugationsRequest_=(value: ConjugationsRequest): Unit = conjugationsRequestProperty.value = value

  def action: Action = actionProperty.value
  def action_=(value: Action): Unit = actionProperty.value = value

  override def createDefaultSkin(): Skin[?] = skin.ConjugationsFileGeneratorSkin(this)
}

object ConjugationsFileGeneratorView {
  def apply(): ConjugationsFileGeneratorView = new ConjugationsFileGeneratorView()
}
