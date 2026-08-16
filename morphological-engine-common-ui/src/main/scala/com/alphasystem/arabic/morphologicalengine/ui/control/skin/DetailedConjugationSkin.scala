package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import arabic.fx.ui.util.UIUserPreferences
import morphologicalengine.conjugation.model.{
  ConjugationGroupPair,
  DetailedConjugation,
  NounConjugationGroupPair,
  VerbConjugationGroupPair
}
import scalafx.Includes.*
import javafx.scene.control.SkinBase
import scalafx.geometry.Pos
import scalafx.scene.control.ScrollPane
import scalafx.scene.layout.{ BorderPane, VBox }

class DetailedConjugationSkin(control: DetailedConjugationView)(using preferences: UIUserPreferences)
    extends SkinBase[DetailedConjugationView](control) {

  private val contentBox = new VBox {
    alignment = Pos.Center
    spacing = 12
  }

  private val scrollPane = new ScrollPane() {
    content = contentBox
    fitToWidth = true
    fitToHeight = false
    vbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
    hbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
  }

  initializeSkin()

  private def initializeSkin() = {
    control.detailedConjugationProperty.onChange((_, _, nv) => setup(nv))
    setup(control.detailedConjugation)
    val pane = new BorderPane {
      center = scrollPane
      BorderPane.setAlignment(scrollPane, Pos.Center)
    }
    getChildren.add(pane)
  }

  private def setup(detailedConjugation: DetailedConjugation) = {
    contentBox.getChildren.clear()
    if detailedConjugation != null then {
      addVerbPairs(
        ConjugationGroupPair.createActiveVerbPair(detailedConjugation.pastTense, detailedConjugation.presentTense)
      )

      addNounPairs(
        ConjugationGroupPair.createActiveNounPair(
          detailedConjugation.feminineActiveParticiple,
          detailedConjugation.masculineActiveParticiple
        )
      )

      (detailedConjugation.pastPassiveTense, detailedConjugation.presentPassiveTense) match {
        case (Some(pastPassiveTense), Some(presentPassiveTense)) =>
          addVerbPairs(
            ConjugationGroupPair.createPassiveVerbPair(pastPassiveTense, presentPassiveTense)
          )
        case _ => // do nothing
      }

      (detailedConjugation.masculinePassiveParticiple, detailedConjugation.femininePassiveParticiple) match {
        case (Some(masculinePassiveParticiple), Some(femininePassiveParticiple)) =>
          addNounPairs(
            ConjugationGroupPair.createPassiveNounPair(masculinePassiveParticiple, femininePassiveParticiple)
          )
        case _ => // do nothing
      }
    }
  }

  private def addVerbPairs(pair: VerbConjugationGroupPair) = {
    val control = VerbConjugationGroupPairView()
    control.pair = pair
    contentBox.getChildren.add(control)
  }

  private def addNounPairs(pair: NounConjugationGroupPair) = {
    val control = NounConjugationGroupPairView()
    control.pair = pair
    contentBox.getChildren.add(control)
  }
}
