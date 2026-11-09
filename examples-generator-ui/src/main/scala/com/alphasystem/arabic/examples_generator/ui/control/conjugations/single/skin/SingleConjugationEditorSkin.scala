package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package single
package skin

import arabic.fx.ui.util.*
import control.conjugation_selector.ConjugationTermSelectorView
import control.settings.SettingsView
import javafx.scene.control.SkinBase
import scalafx.scene.layout.{ BorderPane, GridPane, VBox }
import scalafx.geometry.Pos
import scalafx.scene.control.TextField

private class SingleConjugationEditorSkin(control: SingleConjugationEditorView)
    extends SkinBase[SingleConjugationEditorView](control) {

  private val tagField = new TextField()
  private val conjugationTermSelectorView = ConjugationTermSelectorView()
  private val settingsView = SettingsView()

  private val gridPane = new GridPane {
    hgap = DefaultGap
    vgap = DefaultGap
    padding = DefaultInsets

    private val row = 0
    private val tagLabel = createLabel("Tag: ")
    add(tagLabel, 0, row)
    add(tagField, 1, row)
    tagField.textProperty().bindBidirectional(control.tagProperty)
    control
      .pairedConjugationModeProperty
      .onChange((_, _, nv) => {
        tagLabel.visible = nv
        tagField.visible = nv
      })
  }

  private val mainPanel = {
    val vbox = new VBox() {
      styleClass = Seq("border")
    }
    vbox.getChildren.addAll(gridPane, conjugationTermSelectorView, settingsView)
    new BorderPane() {
      center = vbox
      BorderPane.setAlignment(vbox, Pos.Center)
    }
  }

  getChildren.add(mainPanel)
}

object SingleConjugationEditorSkin {
  private[single] def apply(control: SingleConjugationEditorView): SingleConjugationEditorSkin =
    new SingleConjugationEditorSkin(control)
}
