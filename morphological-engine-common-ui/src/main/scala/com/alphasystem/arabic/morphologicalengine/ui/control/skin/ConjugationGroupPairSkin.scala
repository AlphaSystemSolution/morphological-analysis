package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import scalafx.Includes.*
import javafx.scene.control.SkinBase
import scalafx.geometry.Pos
import scalafx.scene.layout.{ BorderPane, HBox }
import morphologicalengine.conjugation.model.{ ConjugationGroup, ConjugationGroupPair, MorphologicalTermType }

abstract class ConjugationGroupPairSkin[
  R <: ConjugationGroup,
  L <: ConjugationGroup,
  G <: ConjugationGroupPair[R, L],
  C <: ConjugationGroupPairView[R, L, G]
](control: C)
    extends SkinBase[C](control) {

  private val conjugationPairs = new HBox {
    alignment = Pos.Center
    spacing = 12
  }

  protected def createRightView(termType: MorphologicalTermType, term: R): ConjugationGroupView[R]

  protected def createLeftView(termType: MorphologicalTermType, term: L): ConjugationGroupView[L]

  control
    .pairProperty
    .onChange((_, _, nv) => {
      Option(nv) match {
        case Some(value) => addConjugations(value)
        case None        => conjugationPairs.getChildren.clear()
      }
    })

  protected def initializeSkin(): BorderPane = {
    Option(control.pair).foreach(addConjugations)
    new BorderPane {
      center = conjugationPairs
      BorderPane.setAlignment(conjugationPairs, Pos.Center)
    }
  }

  private def addConjugations(pair: G): Unit = {
    val children = conjugationPairs.getChildren
    children.clear()

    val rightNode: javafx.scene.Node =
      if Option(pair.rightTerm).isDefined then createRightView(pair.rightTermType, pair.rightTerm)
      else createEmptyPanel()
    val leftNode: javafx.scene.Node =
      if Option(pair.leftTerm).isDefined then createLeftView(pair.leftTermType, pair.leftTerm)
      else createEmptyPanel()

    children.add(rightNode)
    children.add(leftNode)
  }

  private def createEmptyPanel(): javafx.scene.Node = new javafx.scene.layout.Pane()
}
