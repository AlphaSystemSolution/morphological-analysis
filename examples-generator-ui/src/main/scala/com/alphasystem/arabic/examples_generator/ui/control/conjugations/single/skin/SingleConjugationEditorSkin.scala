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
import scalafx.beans.property.DoubleProperty
import scalafx.scene.layout.{ BorderPane, ColumnConstraints, GridPane, VBox }
import scalafx.geometry.Pos
import scalafx.scene.control.TextField

private class SingleConjugationEditorSkin(control: SingleConjugationEditorView)
    extends SkinBase[SingleConjugationEditorView](control) {

  private val sharedLabelWidth = DoubleProperty(200.0)
  private val tagField = new TextField()
  private val conjugationTermSelectorView = ConjugationTermSelectorView()
  private val settingsView = SettingsView()
  conjugationTermSelectorView.labelColumnWidthProperty <== sharedLabelWidth
  settingsView.labelColumnWidthProperty <== sharedLabelWidth

  private val gridPane = new GridPane {
    hgap = DefaultGap
    vgap = DefaultGap
    padding = DefaultInsets
    columnConstraints = Seq(
      new ColumnConstraints {
        minWidth <== sharedLabelWidth
        prefWidth <== sharedLabelWidth
        maxWidth <== sharedLabelWidth
      },
      new ColumnConstraints()
    )

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
