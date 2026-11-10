package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package skin

import examples_generator.morphologicalchart.{ PairedConjugation, SingleConjugation }
import ui.model.{ Action, ConjugationType, ConjugationsTreeNode }
import arabic.fx.ui.util.*
import arabic.morphologicalengine.generator.model.ConjugationTemplate
import com.alphasystem.arabic.morphologicalengine.conjugation.model.ConjugationInput
import control.root_info.RootInfoEditorDialog
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.scene.control.TreeView
import scalafx.scene.layout.{ BorderPane, Priority, VBox }

import java.util.UUID
import scala.jdk.CollectionConverters.*

class ConjugationsFileGeneratorSkin(control: ConjugationsFileGeneratorView)
    extends SkinBase[ConjugationsFileGeneratorView](control) {

  private val singleConjugationEditorDialog = single.SingleConjugationEditorDialog()
  private val pairedConjugationEditorDialog = paired.PairedConjugationEditorDialog()
  private val templateEditorDialog = full.ConjugationTemplateConfigurationEditorDialog()
  private val rootInfoDialog = RootInfoEditorDialog()
  private var pendingSelect: Option[(ConjugationType, UUID)] = None

  private val treeView = new TreeView[ConjugationsTreeNode] {
    showRoot = false
    prefWidth = 800
    prefHeight = 600
    vgrow = Priority.Always
    hgrow = Priority.Always
    cellFactory = (_: TreeView[ConjugationsTreeNode]) => ConjugationsTreeCell(control)
  }

  control
    .actionProperty
    .onChange((_, _, nv) =>
      nv match {
        case Action.SelectSingleConjugation(singleConjugation) => editConjugation(singleConjugation)
        case Action.SelectPairedConjugation(pairedConjugation) => editConjugation(pairedConjugation)
        case Action.EditCharConfiguration(conjugationTemplate) => editConjugationTemplate(conjugationTemplate)
        case Action.AddConjugationInput(templateId)            => addConjugationInput(templateId)
        case Action.UpdateConjugationInput(inputId)            => updateConjugationInput(inputId)
        case _ => // mutations are rendered via the conjugationsRequestProperty rebuild
      }
    )

  treeView.root = ConjugationsTreeNode.buildTree(control.conjugationsRequest.conjugations)
  control
    .conjugationsRequestProperty
    .onChange((_, ov, nv) => {
      if Option(nv).isDefined then {
        treeView.root = ConjugationsTreeNode.buildTree(nv.conjugations)
        pendingSelect match {
          case Some((conjugationType, id)) =>
            selectConjugationNode(conjugationType, id)
            pendingSelect = None
          case None =>
            val previousSingle = Option(ov).flatMap(_.conjugations.single).getOrElse(Seq.empty)
            val currentSingle = nv.conjugations.single.getOrElse(Seq.empty)
            val previousPaired = Option(ov).flatMap(_.conjugations.paired).getOrElse(Seq.empty)
            val currentPaired = nv.conjugations.paired.getOrElse(Seq.empty)
            val previousFull = Option(ov).flatMap(_.conjugations.full).getOrElse(Seq.empty)
            val currentFull = nv.conjugations.full.getOrElse(Seq.empty)
            if currentSingle.size == previousSingle.size + 1 && currentSingle.init == previousSingle then {
              selectLastChildNode(ConjugationType.Single)
            } else if currentPaired.size == previousPaired.size + 1 && currentPaired.init == previousPaired then {
              selectLastChildNode(ConjugationType.Paired)
            } else if currentFull.size == previousFull.size + 1 && currentFull.init == previousFull then {
              selectLastChildNode(ConjugationType.Full)
            }
        }
      }
    })

  private val mainPanel = new BorderPane {
    center = new VBox {
      spacing = DefaultGap
      padding = DefaultInsets
      children = Seq(treeView)
      vgrow = Priority.Always
      hgrow = Priority.Always
    }
  }

  getChildren.add(mainPanel)

  private def editConjugation(singleConjugation: SingleConjugation): Unit = {
    singleConjugationEditorDialog.reset(maybeSingleConjugation = Some(singleConjugation))
    singleConjugationEditorDialog.showAndWait() match {
      case Some(Some(updated: SingleConjugation)) =>
        pendingSelect = Some(ConjugationType.Single -> updated.id)
        control.action = Action.UpdateSingleConjugation(updated)
      case _ => // do nothing
    }
  }

  private def editConjugation(pairedConjugation: PairedConjugation): Unit = {
    pairedConjugationEditorDialog.reset(maybePairedConjugation = Some(pairedConjugation))
    pairedConjugationEditorDialog.showAndWait() match {
      case Some(Some(updated: PairedConjugation)) =>
        pendingSelect = Some(ConjugationType.Paired -> updated.id)
        control.action = Action.UpdatePairedConjugation(updated)
      case _ => // do nothing
    }
  }

  private def editConjugationTemplate(conjugationTemplate: ConjugationTemplate): Unit = {
    templateEditorDialog.reset(conjugationTemplate)
    templateEditorDialog.showAndWait() match {
      case Some(Some(updated: ConjugationTemplate)) =>
        pendingSelect = Some(ConjugationType.Full -> updated.id)
        control.action = Action.UpdateConjugationTemplate(updated)
      case _ => // do nothing
    }
  }

  private def addConjugationInput(templateId: UUID): Unit = {
    rootInfoDialog.reset()
    rootInfoDialog.showAndWait() match {
      case Some(Some(conjugationInput: ConjugationInput)) =>
        control
          .conjugationsRequest
          .conjugations
          .full
          .getOrElse(Seq.empty)
          .find(_.id == templateId)
          .foreach { template =>
            pendingSelect = Some(ConjugationType.Full -> conjugationInput.id)
            control.action = Action.UpdateConjugationTemplate(
              template.copy(inputs = template.inputs :+ conjugationInput)
            )
          }
      case _ => // do nothing
    }
  }

  private def updateConjugationInput(inputId: UUID): Unit =
    control
      .conjugationsRequest
      .conjugations
      .full
      .getOrElse(Seq.empty)
      .flatMap(template => template.inputs.find(_.id == inputId).map(template -> _))
      .headOption
      .foreach { (template, conjugationInput) =>
        rootInfoDialog.setConjugationInput(conjugationInput)
        rootInfoDialog.showAndWait() match {
          case Some(Some(updated: ConjugationInput)) =>
            pendingSelect = Some(ConjugationType.Full -> updated.id)
            control.action = Action.UpdateConjugationTemplate(
              template.copy(inputs = template.inputs.map(input => if input.id == inputId then updated else input))
            )
          case _ => // do nothing
        }
      }

  private def selectLastChildNode(conjugationType: ConjugationType): Unit =
    treeView
      .root
      .value
      .children
      .find(_.getValue == ConjugationsTreeNode.ConjugationTypeNode(conjugationType))
      .foreach { category =>
        category.setExpanded(true)
        val node = category.getChildren.getLast
        node.setExpanded(true)
        treeView.selectionModel.value.select(node)
        treeView.scrollTo(treeView.getRow(node))
      }

  private def selectConjugationNode(conjugationType: ConjugationType, id: UUID): Unit =
    treeView
      .root
      .value
      .children
      .find(_.getValue == ConjugationsTreeNode.ConjugationTypeNode(conjugationType))
      .foreach { category =>
        category.setExpanded(true)
        category
          .getChildren
          .asScala
          .flatMap(item => item +: item.getChildren.asScala)
          .find(item => nodeId(item.getValue).contains(id))
          .foreach { node =>
            Option(node.getParent).filter(_ != category).foreach(_.setExpanded(true))
            node.setExpanded(true)
            treeView.selectionModel.value.select(node)
            treeView.scrollTo(treeView.getRow(node))
          }
      }

  private def nodeId(node: ConjugationsTreeNode): Option[UUID] =
    node match {
      case ConjugationsTreeNode.SingleConjugationNode(singleConjugation)  => Some(singleConjugation.id)
      case ConjugationsTreeNode.PairedConjugationsNode(pairedConjugation) => Some(pairedConjugation.id)
      case ConjugationsTreeNode.ConjugationTemplateNode(template)         => Some(template.id)
      case ConjugationsTreeNode.ConjugationInputNode(conjugationInput)    => Some(conjugationInput.id)
      case _                                                              => None
    }
}

object ConjugationsFileGeneratorSkin {
  def apply(control: ConjugationsFileGeneratorView): ConjugationsFileGeneratorSkin =
    new ConjugationsFileGeneratorSkin(control)
}
