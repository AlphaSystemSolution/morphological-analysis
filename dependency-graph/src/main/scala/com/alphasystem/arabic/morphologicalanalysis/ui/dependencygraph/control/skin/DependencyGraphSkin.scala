package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.scene.control.{ Accordion, SplitPane, TitledPane }
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Pos
import scalafx.scene.Node
import scalafx.scene.layout.BorderPane

class DependencyGraphSkin(control: DependencyGraphView) extends SkinBase[DependencyGraphView](control) {

  getChildren.add(initializeSkin)

  private def initializeSkin = {
    val splitPane = new SplitPane() {
      dividerPositions = 0.75
      items.addAll(new BorderPane(), initializeSelectionPane)
    }
    BorderPane.setAlignment(splitPane, Pos.Center)
    new BorderPane() {
      center = splitPane
    }
  }

  private def initializeSelectionPane = {
    val accordion = new Accordion() {
      panes = Seq(createTitledPane("Verse Selection", control.verseSelectionView, doExpand = true))
    }
    BorderPane.setAlignment(accordion, Pos.Center)
    new BorderPane() {
      center = accordion
    }
  }

  private def createTitledPane(displayText: String, displayContent: Node, doExpand: Boolean = false) = {
    new TitledPane() {
      text = displayText
      content = displayContent
      expanded = doExpand
      collapsible = true
      animated = true
    }
  }
}

object DependencyGraphSkin {

  def apply(control: DependencyGraphView): DependencyGraphSkin = new DependencyGraphSkin(control)
}
