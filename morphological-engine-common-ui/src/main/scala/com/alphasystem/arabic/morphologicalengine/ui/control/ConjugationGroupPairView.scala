package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import javafx.scene.control.Control
import morphologicalengine.conjugation.model.{ ConjugationGroup, ConjugationGroupPair }
import scalafx.beans.property.ObjectProperty

abstract class ConjugationGroupPairView[R <: ConjugationGroup, L <: ConjugationGroup, G <: ConjugationGroupPair[R, L]]
    extends Control {

  private[control] val pairProperty = ObjectProperty[G](this, "pair")

  def pair: G = pairProperty.value
  def pair_=(value: G): Unit = pairProperty.value = value
}
