package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import skin.MorphologicalEngineSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, ReadOnlyBooleanProperty, ReadOnlyBooleanWrapper }

class MorphologicalEngineView extends Control {

  private[control] val transientProjectWrapperProperty = ReadOnlyBooleanWrapper(true)
  private[control] val actionProperty = ObjectProperty[Action](this, "action", GlobalAction.None)

  setSkin(createDefaultSkin())

  def transientProjectProperty: ReadOnlyBooleanProperty = transientProjectWrapperProperty.readOnlyProperty

  def action: Action = actionProperty.value
  def action_=(value: Action): Unit = actionProperty.value = value

  override def createDefaultSkin(): Skin[?] = MorphologicalEngineSkin(this)
}

object MorphologicalEngineView {
  def apply(): MorphologicalEngineView = new MorphologicalEngineView()
}
