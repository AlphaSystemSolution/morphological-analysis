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
import scalafx.scene.layout.{ BorderPane, VBox }

class TokenEditorSkin(control: TokenEditorView) extends SkinBase[TokenEditorView](control) {

  import TokenEditorSkin.*

  getChildren.add(initializeSkin)

  private def initializeSkin = {
    val pane = new BorderPane()

    val vBox = new VBox() {
      spacing = Gap
      alignment = Pos.Center
      padding = Insets(Gap, Gap, Gap, Gap)
    }

    vBox.getChildren.addAll(control.chapterVerseSelectionView, control.tokenView, control.locationView)

    pane.center = vBox
    BorderPane.setAlignment(vBox, Pos.Center)

    pane
  }
}

object TokenEditorSkin {
  private val Gap = 10.0

  def apply(control: TokenEditorView): TokenEditorSkin = new TokenEditorSkin(control)
}
