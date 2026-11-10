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
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.control.TextField
import scalafx.scene.layout.{ BorderPane, ColumnConstraints, GridPane, VBox }

private class SingleConjugationEditorSkin(control: SingleConjugationEditorView)
    extends SkinBase[SingleConjugationEditorView](control) {

  private val sharedLabelWidth = DoubleProperty(200.0)
  private val tagField = new TextField() {
    prefColumnCount = 35
  }
  private val conjugationTermSelectorView = ConjugationTermSelectorView()
  private val settingsView = SettingsView()
  conjugationTermSelectorView.labelColumnWidthProperty <== sharedLabelWidth
  settingsView.labelColumnWidthProperty <== sharedLabelWidth

  private var synchronizing = false

  private def loadEditor(): Unit =
    if !synchronizing && Option(control.singleConjugation).isDefined then {
      synchronizing = true
      try {
        val value = control.singleConjugation
        conjugationTermSelectorView.singleConjugationProperty.value = value
        settingsView.morphologicalTermType = value.request.morphologicalTermType
        settingsView.settings = value.settings
      } finally synchronizing = false
    }

  control.singleConjugationProperty.onChange((_, _, _) => loadEditor())
  control
    .otherMorphologicalTermTypeProperty
    .onChange((_, _, nv) => conjugationTermSelectorView.otherMorphologicalTermType_=(nv))
  conjugationTermSelectorView.otherMorphologicalTermType_=(control.otherMorphologicalTermType)

  conjugationTermSelectorView
    .singleConjugationProperty
    .onChange((_, _, nv) =>
      if !synchronizing && Option(nv).isDefined then
        control.singleConjugation = control.singleConjugation.copy(request = nv.request)
    )

  settingsView
    .settingsProperty
    .onChange((_, _, nv) =>
      if !synchronizing && Option(nv).isDefined then
        control.singleConjugation = control.singleConjugation.copy(settings = nv)
    )

  loadEditor()

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

    add(createLabel("Tag: "), 0, 0)
    add(tagField, 1, 0)
    tagField.textProperty().bindBidirectional(control.tagProperty)
  }
  VBox.setMargin(gridPane, Insets(0, 0, 0, 1))

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
