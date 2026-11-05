package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package skin

import ui.model.{ ConjugationsTreeNode, ConjugationType }
import scalafx.Includes.*
import com.alphasystem.arabic.fx.ui.util.*
import javafx.scene.control.SkinBase
import scalafx.scene.control.TreeView
import scalafx.scene.layout.{ BorderPane, Priority, VBox }

class ConjugationsFileGeneratorSkin(control: ConjugationsFileGeneratorView)
    extends SkinBase[ConjugationsFileGeneratorView](control) {

  private val treeView = new TreeView[ConjugationsTreeNode] {
    showRoot = false
    prefWidth = 800
    prefHeight = 600
    vgrow = Priority.Always
    hgrow = Priority.Always
    cellFactory = (_: TreeView[ConjugationsTreeNode]) => ConjugationsTreeCell(control)
  }

  treeView.root = ConjugationsTreeNode.buildTree(control.conjugationsRequest.conjugations)
  control
    .conjugationsRequestProperty
    .onChange((_, ov, nv) => {
      if Option(nv).isDefined then {
        treeView.root = ConjugationsTreeNode.buildTree(nv.conjugations)
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
}

object ConjugationsFileGeneratorSkin {
  def apply(control: ConjugationsFileGeneratorView): ConjugationsFileGeneratorSkin =
    new ConjugationsFileGeneratorSkin(control)
}
