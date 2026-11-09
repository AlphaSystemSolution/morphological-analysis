package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations

import ui.model.{ Action, ConjugationsTreeNode }
import ui.model.ConjugationsTreeNode.ConjugationInputNode
import arabic.fx.ui.util.*
import arabic.morphologicalengine.conjugation.model.{ ConjugationInput, NamedTemplate, RootLetters }
import de.jensd.fx.glyphs.fontawesome.{ FontAwesomeIcon, FontAwesomeIconView }
import de.jensd.fx.glyphs.materialdesignicons.{ MaterialDesignIcon, MaterialDesignIconView }
import javafx.scene.control.TreeCell as JTreeCell
import scalafx.geometry.NodeOrientation
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ Alert, Label, TreeCell }
import scalafx.scene.layout.{ GridPane, HBox, Pane, Priority }

import scala.collection.mutable.ListBuffer

private class ConjugationsTreeCell(control: ConjugationsFileGeneratorView) extends JTreeCell[ConjugationsTreeNode] {

  private val editButton = createToolBarButton(
    icon = new FontAwesomeIconView(FontAwesomeIcon.EDIT),
    tooltipText = "Edit node",
    action = editNode
  )

  private val addConjugationButton = createToolBarButton(
    icon = new MaterialDesignIconView(MaterialDesignIcon.PLUS_BOX),
    tooltipText = "Add conjugation node",
    action = addConjugation
  )

  private val deleteButton = createToolBarButton(
    icon = new MaterialDesignIconView(MaterialDesignIcon.DELETE),
    tooltipText = "Remove node",
    action = deleteNode
  )

  override def updateItem(item: ConjugationsTreeNode, empty: Boolean): Unit = {
    super.updateItem(item, empty)
    setText(null)
    setGraphic(null)

    if !empty && Option(item).isDefined then {
      item match {
        case ConjugationsTreeNode.RootNode =>
          setGraphic(createHBoxGraphic(new Label("Root")))

        case ConjugationsTreeNode.ConjugationTypeNode(conjugationType) =>
          setGraphic(createHBoxGraphic(new Label(conjugationType.value)))

        case ConjugationsTreeNode.SingleConjugationNode(singleConjugation) =>
          setGraphic(
            createHBoxGraphic(
              label = new Label(singleConjugation.tag) {
                font = preferences.englishFont(14)
              },
              showEditButton = true,
              showDeleteButton = true
            )
          )

        case ConjugationsTreeNode.PairedConjugationsNode(pairedConjugation) =>
          setGraphic(
            createHBoxGraphic(
              label = new Label(pairedConjugation.tag) {
                font = preferences.englishFont(14)
              },
              showEditButton = true,
              showDeleteButton = true
            )
          )

        case ConjugationsTreeNode.ConjugationRequestNode(conjugationRequest) =>
          val termType = conjugationRequest.morphologicalTermType
          val label = new Label(s"${termType.label} $EmDash ${termType.name}") {
            font = preferences.arabicFont(20)
            nodeOrientation = NodeOrientation.RightToLeft
          }
          setGraphic(createHBoxGraphic(label))

        case ConjugationsTreeNode.ConjugationInputNode(conjugationInput) =>
          setGraphic(createConjugationInputNode(conjugationInput))

        case ConjugationsTreeNode.ConjugationTemplateNode(conjugationTemplate) =>
          setGraphic(
            createHBoxGraphic(
              label = new Label(conjugationTemplate.tag) {
                font = preferences.englishFont(14)
              },
              showEditButton = true,
              showDeleteButton = true,
              showAddConjugationButton = true
            )
          )
      }
    }
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

  private def createFamilyLabel(family: NamedTemplate) =
    new Label() {
      text = s"${family.form} $EmDash ${family.word.unicode}"
      font = preferences.arabicFont(20)
      nodeOrientation = NodeOrientation.RightToLeft
    }

  private def createRootLettersLabel(rootLetters: RootLetters) =
    new Label() {
      text = rootLetters.rawString
      font = preferences.arabicFont(20)
    }

  private def editNode(): Unit = {
    val treeItem = getTreeItem
    if Option(treeItem).isDefined then {
      treeItem.getValue match {
        case ConjugationsTreeNode.SingleConjugationNode(singleConjugation) =>
          control.action = Action.NoAction
          control.action = Action.SelectSingleConjugation(singleConjugation)
        case ConjugationsTreeNode.PairedConjugationsNode(pairedConjugation) =>
          control.action = Action.NoAction
          control.action = Action.SelectPairedConjugation(pairedConjugation)
        case _ => // do nothing, operation is not supported
      }
    }
  }

  private def addConjugation(): Unit = ()

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
            case ConjugationsTreeNode.ConjugationTemplateNode(conjugationTemplate) =>
              control.action = Action.RemoveConjugation(conjugationTemplate.id)
            case _ => // do nothing, operation is not supported
          }
        }
      case _ => // do nothing
    }

  private def createHBoxGraphic(
    label: Label,
    showEditButton: Boolean = false,
    showDeleteButton: Boolean = false,
    showAddConjugationButton: Boolean = false
  ): HBox = {
    val hBox = new HBox()
    val pane = new Pane()
    val children = ListBuffer.from(Seq(label, pane))
    if showEditButton then {
      children += editButton
    }
    if showAddConjugationButton then {
      children += addConjugationButton
    }
    if showDeleteButton then {
      children += deleteButton
    }
    hBox.children = children
    HBox.setHgrow(pane, Priority.Always)
    hBox
  }
}

object ConjugationsTreeCell {
  def apply(control: ConjugationsFileGeneratorView) =
    new TreeCell[ConjugationsTreeNode](new ConjugationsTreeCell(control))
}
