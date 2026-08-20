package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import javafx.scene.control.{Control, SkinBase}
import scalafx.Includes.*
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.layout.{BorderPane, GridPane, Pane, VBox}

class MorphologicalEngineSkin2(control: MorphologicalEngineView2) extends SkinBase[Control](control) {

  private val wordEditorView = WordEditorView()

  private val mainPane = {
    val editorView = new VBox {
      padding = Insets(12)
      spacing = 10
      style = "-fx-border-color: grey; -fx-border-width: 1px; -fx-border-radius: 4px;"
    }
    val emptyPane = new Pane {
      prefHeight = 50
    }
    editorView.getChildren.addAll(wordEditorView)

    new BorderPane {
      top = emptyPane
      center = editorView
      BorderPane.setAlignment(editorView, Pos.Center)
      BorderPane.setAlignment(emptyPane, Pos.Center)
    }
  }

  getChildren.addAll(mainPane)
}

object MorphologicalEngineSkin2 {
  def apply(control: MorphologicalEngineView2): MorphologicalEngineSkin2 = new MorphologicalEngineSkin2(control)
}
