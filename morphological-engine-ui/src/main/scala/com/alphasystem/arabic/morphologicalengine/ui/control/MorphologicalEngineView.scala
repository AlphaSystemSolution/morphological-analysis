package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import skin.MorphologicalEngineSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.ObjectProperty

class MorphologicalEngineView extends Control {

  import MorphologicalEngineView.*

  private[control] val actionProperty = ObjectProperty[Action](this, "action", Action.None)

  setSkin(createDefaultSkin())

  def action: Action = actionProperty.value
  def action_=(value: Action): Unit = actionProperty.value = action

  override def createDefaultSkin(): Skin[_] = MorphologicalEngineSkin(this)
}

object MorphologicalEngineView {
  def apply(): MorphologicalEngineView = new MorphologicalEngineView()

  enum Action {

    case None extends Action
    case Open extends Action
    case New extends Action
    case Save extends Action
    case AddRow extends Action
  }
}
