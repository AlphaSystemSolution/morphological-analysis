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
import scalafx.scene.Node
import scalafx.scene.layout.BorderPane

class DependencyGraphSkin(control: DependencyGraphView) extends SkinBase[DependencyGraphView](control) {

  getChildren.add(initializeSkin)

  private def initializeSkin = {
    val splitPane = new SplitPane() {
      items.addAll(control.canvasView, initializeSelectionPane)
    }
    splitPane.setDividerPosition(0, 0.70)
    new BorderPane() {
      center = splitPane
    }
  }

  private def initializeSelectionPane = {
    val allPanes = Seq(
      createTitledPane("Verse Selection", control.verseSelectionView),
      createTitledPane("Graph Settings", control.graphSettingsView)
    )
    new Accordion() {
      panes = allPanes
      expandedPane = allPanes.head
    }
  }

  private def createTitledPane(displayText: String, displayContent: Node) = {
    new TitledPane() {
      text = displayText
      content = displayContent
      expanded = true
      collapsible = true
      animated = true
    }
  }
}

object DependencyGraphSkin {

  def apply(control: DependencyGraphView): DependencyGraphSkin = new DependencyGraphSkin(control)
}
