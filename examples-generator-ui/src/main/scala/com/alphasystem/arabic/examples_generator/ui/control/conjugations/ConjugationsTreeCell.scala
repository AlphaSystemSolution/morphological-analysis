package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations

import ui.model.ConjugationsTreeNode
import scalafx.scene.control.TreeCell
import javafx.scene.control.TreeCell as JTreeCell
import scalafx.scene.layout.{ HBox, Pane, Priority }
import scalafx.scene.text.{ Text, TextFlow }

private class ConjugationsTreeCell extends JTreeCell[ConjugationsTreeNode] {

  private val hBox = new HBox()
  private val textFlow = new TextFlow()
  private val pane = new Pane()
  hBox.getChildren.addAll(textFlow, pane)
  HBox.setHgrow(pane, Priority.Always)

  override def updateItem(item: ConjugationsTreeNode, empty: Boolean): Unit = {
    super.updateItem(item, empty)
    setText(null)
    setGraphic(null)

    if !empty && Option(item).isDefined then {
      item match {
        case ConjugationsTreeNode.RootNode =>
          val text = new Text("Root")
          textFlow.children = text
          setGraphic(hBox)

        case ConjugationsTreeNode.ConjugationTypeNode(conjugationType) =>
          val text = new Text(conjugationType.value)
          textFlow.children = text
          setGraphic(hBox)

        // singleConjugation doesn't get added to tree
        case ConjugationsTreeNode.SingleConjugationNode(singleConjugation)   => // do nothing
        case ConjugationsTreeNode.PairedConjugationsNode(pairedConjugation)  => // Will be added later
        case ConjugationsTreeNode.FullConjugationsNode(conjugationTemplate)  => // Will be added later
        case ConjugationsTreeNode.ConjugationRequestNode(conjugationRequest) => // Will be added later
      }
    }
  }
}

object ConjugationsTreeCell {
  def apply() = new TreeCell[ConjugationsTreeNode](new ConjugationsTreeCell())
}
