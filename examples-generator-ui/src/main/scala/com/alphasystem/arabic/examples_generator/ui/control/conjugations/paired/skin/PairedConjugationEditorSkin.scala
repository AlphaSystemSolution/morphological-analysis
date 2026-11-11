package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package paired
package skin

import examples_generator.morphologicalchart.ConjugationRequest
import control.conjugation_selector.ConjugationTermSelectorView
import control.settings.SettingsView
import arabic.fx.ui.util.{ DefaultGap, DefaultInsets, createLabel, createTitledPane }
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.beans.property.DoubleProperty
import scalafx.scene.control.{ Accordion, ScrollPane, TextField }
import scalafx.scene.layout.{ BorderPane, ColumnConstraints, GridPane }

private class PairedConjugationEditorSkin(control: PairedConjugationEditorView)
    extends SkinBase[PairedConjugationEditorView](control) {

  private val sharedLabelWidth = DoubleProperty(200.0)
  private val tagField = new TextField() {
    prefColumnCount = 35
  }
  private val firstConjugationTermSelectorView = ConjugationTermSelectorView()
  private val secondConjugationTermSelectorView = ConjugationTermSelectorView()
  private val settingsView = SettingsView()
  firstConjugationTermSelectorView.labelColumnWidthProperty <== sharedLabelWidth
  secondConjugationTermSelectorView.labelColumnWidthProperty <== sharedLabelWidth
  settingsView.labelColumnWidthProperty <== sharedLabelWidth

  private var loadingSelectors = false

  private def loadSelector(selector: ConjugationTermSelectorView, request: ConjugationRequest): Unit =
    if Option(request).isDefined then {
      loadingSelectors = true
      try selector.conjugationRequest_=(request)
      finally loadingSelectors = false
    }

  control
    .firstConjugationRequestPropertyWrapper
    .onChange((_, _, nv) => {
      if Option(nv).isDefined then {
        loadSelector(firstConjugationTermSelectorView, nv)
        secondConjugationTermSelectorView.otherMorphologicalTermType = Some(nv.morphologicalTermType)
      }
    })
  control
    .secondConjugationRequestPropertyWrapper
    .onChange((_, _, nv) => loadSelector(secondConjugationTermSelectorView, nv))
  firstConjugationTermSelectorView
    .singleConjugationProperty
    .onChange((_, _, nv) =>
      if !loadingSelectors && Option(nv).isDefined then control.firstConjugationRequest = nv.request
    )
  secondConjugationTermSelectorView
    .singleConjugationProperty
    .onChange((_, _, nv) =>
      if !loadingSelectors && Option(nv).isDefined then control.secondConjugationRequest = nv.request
    )

  loadSelector(firstConjugationTermSelectorView, control.firstConjugationRequest)
  secondConjugationTermSelectorView.otherMorphologicalTermType =
    Option(control.firstConjugationRequest).map(_.morphologicalTermType)
  loadSelector(secondConjugationTermSelectorView, control.secondConjugationRequest)

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

  private val mainPanel = {
    val sections = Seq(
      createTitledPane("Select First Conjugation", firstConjugationTermSelectorView, expand = false),
      createTitledPane("Select Second Conjugation", secondConjugationTermSelectorView, expand = false),
      createTitledPane("Settings", settingsView, expand = false)
    )
    val accordion = new Accordion() {
      panes = sections
      expandedPane = sections.head
    }
    val scrollPane = new ScrollPane() {
      content = accordion
      fitToWidth = true
      minWidth = 0
      minHeight = 0
      prefViewportWidth = 760
      prefViewportHeight = 420
    }
    new BorderPane() {
      styleClass = Seq("border")
      top = gridPane
      center = scrollPane
    }
  }

  getChildren.add(mainPanel)
}

object PairedConjugationEditorSkin {
  private[paired] def apply(control: PairedConjugationEditorView): PairedConjugationEditorSkin =
    new PairedConjugationEditorSkin(control)
}
