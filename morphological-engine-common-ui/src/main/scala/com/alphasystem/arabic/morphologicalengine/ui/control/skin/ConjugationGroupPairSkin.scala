package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import javafx.scene.control.SkinBase
import scalafx.geometry.Pos
import scalafx.scene.layout.{BorderPane, HBox, Pane}
import morphologicalengine.conjugation.model.{ConjugationGroup, ConjugationGroupPair, MorphologicalTermType}

abstract class ConjugationGroupPairSkin[
  R <: ConjugationGroup,
  L <: ConjugationGroup,
  G <: ConjugationGroupPair[R, L],
  C <: ConjugationGroupPairView[R, L, G]
](control: C)
    extends SkinBase[C](control) {

  private val contentBox = new HBox {
    alignment = Pos.Center
    spacing = 12
  }

  protected def createRightView(termType: MorphologicalTermType, term: R): ConjugationGroupView[R]

  protected def createLeftView(termType: MorphologicalTermType, term: L): ConjugationGroupView[L]

  initializeSkin()

  control
    .pairProperty
    .onChange((_, _, nv) => {
      Option(nv) match {
        case Some(value) => addConjugations(value)
        case None        => contentBox.getChildren.clear()
      }
    })

  private def initializeSkin() = {
    Option(control.pair).foreach(addConjugations)
    val pane = new BorderPane {
      center = contentBox
      BorderPane.setAlignment(contentBox, Pos.Center)
    }
    getChildren.add(pane)
  }

  private def addConjugations(pair: G): Unit = {
    val children = contentBox.getChildren
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

  private def createEmptyPanel() = {
    new Pane() {
      prefWidth = 390
    }
  }
}
