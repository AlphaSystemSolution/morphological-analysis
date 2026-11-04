package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations

import ui.model.ConjugationsTreeNode
import arabic.fx.ui.util.*
import ui.model.ConjugationsTreeNode.ConjugationInputNode
import arabic.morphologicalengine.conjugation.model.{
  ConjugationInput,
  MorphologicalTermType,
  NamedTemplate,
  RootLetters
}
import scalafx.scene.control.TreeCell
import javafx.scene.control.TreeCell as JTreeCell
import scalafx.scene.layout.{ GridPane, HBox, Pane, Priority }
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
        case ConjugationsTreeNode.SingleConjugationNode(singleConjugation) =>
          val text = new Text(singleConjugation.tag) {
            font = preferences.englishFont(14)
          }
          textFlow.children = text
          setGraphic(hBox)

        case ConjugationsTreeNode.PairedConjugationsNode(pairedConjugation) => // Will be added later
        case ConjugationsTreeNode.FullConjugationsNode(conjugationTemplate) => // Will be added later
        case ConjugationsTreeNode.ConjugationRequestNode(conjugationRequest) =>
          textFlow.children = createMorphologicalTermLabel(conjugationRequest.morphologicalTermType)
          setGraphic(hBox)
        case ConjugationsTreeNode.ConjugationInputNode(conjugationInput) =>
          setGraphic(createConjugationInputNode(conjugationInput))
      }
    }
  }

  private def createMorphologicalTermLabel(morphologicalTermType: MorphologicalTermType) = {
    val label = new Text(morphologicalTermType.label) {
      font = preferences.arabicFont(20)
    }
    val separator = new Text(s"  $EmDash ") {
      font = preferences.englishFont(20)
    }
    val name = new Text(morphologicalTermType.name()) {
      font = preferences.englishFont(16)
    }
    Seq(label, separator, name)
  }

  private def createConjugationInputNode(conjugationInput: ConjugationInput) = {
    val gridPane = new GridPane() {
      hgap = 5
      vgap = 2
      minWidth = 0
    }

    var row = 0
    gridPane.add(createLabel("Family:"), 0, row)
    gridPane.add(createFamilyLabel(conjugationInput.namedTemplate), 1, row)

    row += 1
    gridPane.add(createLabel("Root Letters:"), 0, row)
    gridPane.add(createRootLettersLabel(conjugationInput.rootLetters), 1, row)

    gridPane
  }

  private def createFamilyLabel(family: NamedTemplate) = {
    val label = new Text(family.form) {
      font = preferences.englishFont(16)
    }
    val separator = new Text(s"  $EmDash ") {
      font = preferences.englishFont(16)
    }
    val name = new Text(family.word.unicode) {
      font = preferences.arabicFont(20)
    }
    new TextFlow() {
      children = Seq(label, separator, name)
    }
  }

  private def createRootLettersLabel(rootLetters: RootLetters) = {
    val label = new Text(rootLetters.rawString) {
      font = preferences.arabicFont(20)
    }
    new TextFlow() {
      children = Seq(label)
    }
  }

}

object ConjugationsTreeCell {
  def apply() = new TreeCell[ConjugationsTreeNode](new ConjugationsTreeCell())
}
