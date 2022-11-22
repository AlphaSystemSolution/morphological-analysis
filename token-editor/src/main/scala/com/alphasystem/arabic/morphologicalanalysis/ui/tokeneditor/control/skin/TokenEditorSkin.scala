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
    val splitPane = new SplitPane() {
      prefWidth = screenWidth * 0.75
      prefHeight = screenHeight * 0.75
      setDividerPosition(0, 0.70)
    }
    splitPane.items.addAll(initializeMainPane, initializeSelectionPane)

    new BorderPane() {
      center = splitPane
    }
  }

  private def initializeMainPane = {
    val vBox = new VBox() {
      spacing = DefaultGap
      alignment = Pos.Center
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
    }
    vBox.getChildren.addAll(control.tokenView, control.locationView)

    new BorderPane() {
      center = vBox
    }
  }

  private def initializeSelectionPane = {
    val vBox = new VBox() {
      spacing = DefaultGap
      alignment = Pos.Center
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
    }
    vBox.getChildren.addAll(control.tokenSelectionView)

    new BorderPane() {
      center = vBox
    }
  }
}

object TokenEditorSkin {

  def apply(control: TokenEditorView): TokenEditorSkin = new TokenEditorSkin(control)
}
