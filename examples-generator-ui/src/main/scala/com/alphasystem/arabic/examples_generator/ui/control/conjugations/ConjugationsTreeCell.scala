package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations

import ui.model.{ Action, ConjugationsTreeNode }
import ui.model.ConjugationsTreeNode.ConjugationInputNode
import arabic.fx.ui.util.*
import arabic.morphologicalengine.conjugation.model.{
  ConjugationInput,
  MorphologicalTermType,
  NamedTemplate,
  RootLetters
}
import de.jensd.fx.glyphs.fontawesome.{ FontAwesomeIcon, FontAwesomeIconView }
import de.jensd.fx.glyphs.materialdesignicons.{ MaterialDesignIcon, MaterialDesignIconView }
import javafx.scene.control.TreeCell as JTreeCell
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ Alert, Button, TreeCell }
import scalafx.scene.layout.{ GridPane, HBox, Pane, Priority }
import scalafx.scene.text.{ Text, TextFlow }

private class ConjugationsTreeCell(control: ConjugationsFileGeneratorView) extends JTreeCell[ConjugationsTreeNode] {

  private val hBox = new HBox()
  private val textFlow = new TextFlow()
  private val pane = new Pane()
  private val editButton = createToolBarButton(
    icon = new FontAwesomeIconView(FontAwesomeIcon.EDIT),
    tooltipText = "Edit node",
    action = editNode
  )
  private val deleteButton = createToolBarButton(
    icon = new MaterialDesignIconView(MaterialDesignIcon.DELETE),
    tooltipText = "Remove node",
    action = deleteNode
  )
  hBox.getChildren.addAll(textFlow, pane, editButton, deleteButton)
  HBox.setHgrow(pane, Priority.Always)

  override def updateItem(item: ConjugationsTreeNode, empty: Boolean): Unit = {
    super.updateItem(item, empty)
    setText(null)
    setGraphic(null)
    editButton.setVisible(false)
    deleteButton.setVisible(false)

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
          editButton.setVisible(true)
          deleteButton.setVisible(true)
          val text = new Text(singleConjugation.tag) {
            font = preferences.englishFont(14)
          }
          textFlow.children = text
          setGraphic(hBox)

        case ConjugationsTreeNode.PairedConjugationsNode(pairedConjugation) =>
          deleteButton.setVisible(true)
          val text = new Text(pairedConjugation.tag) {
            font = preferences.englishFont(14)
          }
          textFlow.children = text
          setGraphic(hBox)
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

  private def editNode(): Unit = {
    val treeItem = getTreeItem
    if Option(treeItem).isDefined then {
      treeItem.getValue match {
        case ConjugationsTreeNode.SingleConjugationNode(singleConjugation) =>
          control.action = Action.NoAction
          control.action = Action.SelectSingleConjugation(singleConjugation)
        case _ => // do nothing, operation is not supported
      }
    }
  }

  private def deleteNode(): Unit =
    new Alert(AlertType.Confirmation) {
      title = "Delete Node"
      headerText = "Are you sure you want to delete this node?"
      contentText = "This action cannot be undone."
    }.showAndWait() match {
      case Some(buttonType) if buttonType.buttonData == ButtonData.OKDone =>
        val treeItem = getTreeItem
        if Option(treeItem).isDefined then {
          treeItem.getValue match {
            case ConjugationsTreeNode.SingleConjugationNode(singleConjugation) =>
              control.action = Action.RemoveConjugation(singleConjugation.id)
            case ConjugationsTreeNode.PairedConjugationsNode(pairedConjugation) =>
              control.action = Action.RemoveConjugation(pairedConjugation.id)
            case _ => // do nothing, operation is not supported
          }
        }
      case _ => // do nothing
    }
}

object ConjugationsTreeCell {
  def apply(control: ConjugationsFileGeneratorView) =
    new TreeCell[ConjugationsTreeNode](new ConjugationsTreeCell(control))
}
