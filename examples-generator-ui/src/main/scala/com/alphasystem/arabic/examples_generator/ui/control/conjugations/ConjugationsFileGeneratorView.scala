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
  actionProperty.onChange((_, _, nv) => {
    nv match {
      case Action.AddSingleConjugation(value) =>
        val request = conjugationsRequest
        val conjugations = request.conjugations
        conjugationsRequest = request.copy(
          conjugations = conjugations.copy(single = Some(conjugations.single.getOrElse(Seq.empty) :+ value))
        )
        action = Action.NoAction
      case Action.RemoveConjugation(rowId) =>
        val request = conjugationsRequest
        val conjugations = request.conjugations
        conjugationsRequest = request.copy(
          conjugations = conjugations.copy(
            single = conjugations.single.map(_.filterNot(_.id == rowId)),
            paired = conjugations.paired.map(_.filterNot(_.id == rowId))
          )
        )
        action = Action.NoAction
      case Action.UpdateSingleConjugation(singleConjugation) =>
        val request = conjugationsRequest
        val conjugations = request.conjugations
        conjugationsRequest = request.copy(
          conjugations = conjugations.copy(
            single = conjugations
              .single
              .map(_.map(sc => if sc.id == singleConjugation.id then singleConjugation else sc))
          )
        )
        action = Action.NoAction
      case Action.SelectSingleConjugation(_) => // handled by ConjugationsFileGeneratorSkin
      case _                                 => ()
    }
  })
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
