package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import skin.MorphologicalEngineSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.ObjectProperty

class MorphologicalEngineView extends Control {

  private[control] val actionProperty = ObjectProperty[Action](this, "action")

  setSkin(createDefaultSkin())

  def action: Action = actionProperty.value
  def action_=(value: Action): Unit = actionProperty.value = value

  override def createDefaultSkin(): Skin[_] = MorphologicalEngineSkin(this)
}

object MorphologicalEngineView {
  def apply(): MorphologicalEngineView = new MorphologicalEngineView()
}
