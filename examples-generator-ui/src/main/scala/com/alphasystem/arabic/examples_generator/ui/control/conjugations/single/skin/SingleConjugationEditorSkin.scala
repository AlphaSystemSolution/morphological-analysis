package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package single
package skin

import control.conjugation_selector.ConjugationTermSelectorView
import control.settings.SettingsView
import arabic.fx.ui.util.*
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.beans.property.DoubleProperty
import scalafx.geometry.Pos
import scalafx.scene.control.TextField
import scalafx.scene.layout.{ BorderPane, ColumnConstraints, GridPane, VBox }

private class SingleConjugationEditorSkin(control: SingleConjugationEditorView)
    extends SkinBase[SingleConjugationEditorView](control) {

  private val sharedLabelWidth = DoubleProperty(200.0)
  private val tagField = new TextField()
  private val conjugationTermSelectorView = ConjugationTermSelectorView()
  private val settingsView = SettingsView()
  conjugationTermSelectorView.labelColumnWidthProperty <== sharedLabelWidth
  settingsView.labelColumnWidthProperty <== sharedLabelWidth

  conjugationTermSelectorView
    .singleConjugationProperty
    .onChange((_, _, nv) =>
      if Option(nv).isDefined then
        control.singleConjugation = nv.copy(tag = control.tag, settings = settingsView.settings)
    )

  settingsView
    .settingsProperty
    .onChange((_, _, nv) =>
      if Option(nv).isDefined then control.singleConjugation = control.singleConjugation.copy(settings = nv)
    )

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
    vbox
      .getChildren
      .addAll(
        gridPane,
        createTitledPane("Select Conjugation", conjugationTermSelectorView, collapse = false),
        createTitledPane("Settings", settingsView, collapse = false)
      )
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
