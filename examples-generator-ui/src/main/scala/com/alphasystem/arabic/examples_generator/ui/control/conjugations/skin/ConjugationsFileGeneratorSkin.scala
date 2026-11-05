package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package skin

import ui.model.{ Action, ConjugationsTreeNode, ConjugationType }
import examples_generator.morphologicalchart.SingleConjugation
import single.SingleConjugationEditorDialog
import scalafx.Includes.*
import com.alphasystem.arabic.fx.ui.util.*
import javafx.scene.control.SkinBase
import scalafx.scene.control.TreeView
import scalafx.scene.layout.{ BorderPane, Priority, VBox }

import java.util.UUID
import scala.jdk.CollectionConverters.*

class ConjugationsFileGeneratorSkin(control: ConjugationsFileGeneratorView)
    extends SkinBase[ConjugationsFileGeneratorView](control) {

  private val singleConjugationEditorDialog = SingleConjugationEditorDialog()
  private var pendingSelectId: Option[UUID] = None

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
        case _ => // mutations are rendered via the conjugationsRequestProperty rebuild
      }
    )

  treeView.root = ConjugationsTreeNode.buildTree(control.conjugationsRequest.conjugations)
  control
    .conjugationsRequestProperty
    .onChange((_, ov, nv) => {
      if Option(nv).isDefined then {
        treeView.root = ConjugationsTreeNode.buildTree(nv.conjugations)
        pendingSelectId match {
          case Some(id) =>
            selectSingleConjugationNode(node => node.id == id)
            pendingSelectId = None
          case None =>
            val previous = Option(ov).flatMap(_.conjugations.single).getOrElse(Seq.empty)
            val current = nv.conjugations.single.getOrElse(Seq.empty)
            if current.size == previous.size + 1 && current.init == previous then {
              treeView
                .root
                .value
                .children
                .find(_.getValue == ConjugationsTreeNode.ConjugationTypeNode(ConjugationType.Single))
                .foreach { category =>
                  category.setExpanded(true)
                  val node = category.getChildren.getLast
                  node.setExpanded(true)
                  treeView.selectionModel.value.select(node)
                  treeView.scrollTo(treeView.getRow(node))
                }
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
        pendingSelectId = Some(updated.id)
        control.action = Action.UpdateSingleConjugation(updated)
      case _ => // do nothing
    }
  }

  private def selectSingleConjugationNode(predicate: SingleConjugation => Boolean): Unit =
    treeView
      .root
      .value
      .children
      .find(_.getValue == ConjugationsTreeNode.ConjugationTypeNode(ConjugationType.Single))
      .foreach { category =>
        category.setExpanded(true)
        category
          .getChildren
          .asScala
          .find(item =>
            item.getValue match {
              case ConjugationsTreeNode.SingleConjugationNode(singleConjugation) =>
                predicate(singleConjugation)
              case _ => false
            }
          )
          .foreach { node =>
            node.setExpanded(true)
            treeView.selectionModel.value.select(node)
            treeView.scrollTo(treeView.getRow(node))
          }
      }
}

object ConjugationsFileGeneratorSkin {
  def apply(control: ConjugationsFileGeneratorView): ConjugationsFileGeneratorSkin =
    new ConjugationsFileGeneratorSkin(control)
}
