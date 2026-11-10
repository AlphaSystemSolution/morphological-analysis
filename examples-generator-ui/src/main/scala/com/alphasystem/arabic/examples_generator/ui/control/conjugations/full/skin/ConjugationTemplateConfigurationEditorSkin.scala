package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package full
package skin

import arabic.fx.ui.util.{ DefaultGap, DefaultInsets, createLabel, createTitledPane }
import arabic.morphologicalengine.ui.control.ChartConfigurationView
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.geometry.Pos
import scalafx.scene.control.TextField
import scalafx.scene.layout.{ BorderPane, GridPane, VBox }

private class ConjugationTemplateConfigurationEditorSkin(control: ConjugationTemplateConfigurationEditorView)
    extends SkinBase[ConjugationTemplateConfigurationEditorView](control) {

  private val tagField = new TextField() {
    prefColumnCount = 35
  }
  private val chartConfigurationView = ChartConfigurationView()
  chartConfigurationView
    .chartConfigurationProperty
    .onChange((_, _, nv) =>
      if Option(nv).isDefined && control.conjugationTemplate.chartConfiguration != nv then {
        control.conjugationTemplate = control.conjugationTemplate.copy(chartConfiguration = nv)
      }
    )
  control
    .conjugationTemplateProperty
    .onChange((_, _, nv) =>
      if Option(nv).isDefined && chartConfigurationView.chartConfiguration != nv.chartConfiguration then {
        chartConfigurationView.chartConfiguration = nv.chartConfiguration
      }
    )

  private val gridPane = new GridPane {
    hgap = DefaultGap
    vgap = DefaultGap
    padding = DefaultInsets

    add(createLabel("Tag: "), 0, 0)
    add(tagField, 1, 0)
    tagField.textProperty().bindBidirectional(control.tagProperty)
  }

  private val mainPanel = {
    val vbox = new VBox() {
      styleClass = Seq("border")
    }
    vbox
      .getChildren
      .addAll(
        gridPane,
        createTitledPane("Chart ConfigurationView", chartConfigurationView, collapse = false)
      )
    new BorderPane() {
      center = vbox
      BorderPane.setAlignment(vbox, Pos.Center)
    }
  }

  getChildren.add(mainPanel)
}

object ConjugationTemplateConfigurationEditorSkin {
  private[full] def apply(
    control: ConjugationTemplateConfigurationEditorView
  ): ConjugationTemplateConfigurationEditorSkin =
    new ConjugationTemplateConfigurationEditorSkin(control)
}
