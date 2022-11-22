package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control
package skin

import morphology.model.WordType
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.control.SplitPane
import scalafx.scene.layout.{ BorderPane, VBox }

class TokenEditorSkin(control: TokenEditorView) extends SkinBase[TokenEditorView](control) {

  import TokenEditorSkin.*

  getChildren.add(initializeSkin)

  private def initializeSkin = {

    val vBox = new VBox() {
      spacing = DefaultGap
      alignment = Pos.Center
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
    }

    vBox.getChildren.addAll(control.tokenView, control.locationView)

    val mainPane = new BorderPane() {
      center = vBox
    }
    BorderPane.setAlignment(vBox, Pos.Center)

    val splitPane = new SplitPane() {
      prefWidth = screenWidth * 0.75
      prefHeight = screenHeight * 0.75
      items.addAll(mainPane, control.tokenSelectionView)
    }
    splitPane.setDividerPosition(0, 0.70)

    val pane = new BorderPane() {
      center = splitPane
    }

    BorderPane.setAlignment(pane, Pos.Center)
    pane
  }
}

object TokenEditorSkin {

  def apply(control: TokenEditorView): TokenEditorSkin = new TokenEditorSkin(control)
}
