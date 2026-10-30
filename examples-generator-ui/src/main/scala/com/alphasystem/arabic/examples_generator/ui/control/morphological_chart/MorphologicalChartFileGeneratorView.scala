package com.alphasystem
package arabic
package examples_generator
package ui
package control
package morphological_chart

import ui.model.{Action, ConjugationsRequest}
import javafx.scene.control.{Control, Skin}
import scalafx.beans.property.ObjectProperty

class MorphologicalChartFileGeneratorView extends Control {

  val conjugationsRequestProperty: ObjectProperty[Option[ConjugationsRequest]] =
    ObjectProperty[Option[ConjugationsRequest]](this, "conjugationsRequest")
  private[morphological_chart] val actionProperty = ObjectProperty[Action](this, "action")

  setSkin(skin.MorphologicalChartFileGeneratorSkin(this))

  def conjugationsRequest: Option[ConjugationsRequest] = conjugationsRequestProperty.value
  def conjugationsRequest_=(value: ConjugationsRequest): Unit = conjugationsRequestProperty.value = Option(value)

  def action: Action = actionProperty.value
  def action_=(value: Action): Unit = actionProperty.value = value

  override def createDefaultSkin(): Skin[?] = skin.MorphologicalChartFileGeneratorSkin(this)
}

object MorphologicalChartFileGeneratorView {
  def apply(): MorphologicalChartFileGeneratorView = new MorphologicalChartFileGeneratorView()
}
