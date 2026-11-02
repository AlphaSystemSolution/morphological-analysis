package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package skin

import ui.model.ConjugationsTreeNode
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
    cellFactory = (_: TreeView[ConjugationsTreeNode]) => ConjugationsTreeCell()
  }

  treeView.root = ConjugationsTreeNode.buildTree(control.conjugationsRequest.conjugations)
  control
    .conjugationsRequestProperty
    .onChange((_, _, nv) => {
      if Option(nv).isDefined then {
        treeView.root = ConjugationsTreeNode.buildTree(nv.conjugations)
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
