package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import arabic.model.ArabicWord
import com.alphasystem.arabic.morphologicalengine.conjugation.model.ConjugationGroup
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.ObjectProperty

abstract class ConjugationGroupView[G <: ConjugationGroup] extends Control {

  private[control] val groupProperty = ObjectProperty[G](this, "group")

  private[control] val termProperty = ObjectProperty[ArabicWord](this, "term", ArabicWord())

  def group: G = groupProperty.value
  def group_=(value: G): Unit = groupProperty.value = value

  def term: ArabicWord = termProperty.value
  def term_=(value: ArabicWord): Unit = termProperty.value = value

  def isEmpty: Boolean
}
