package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import arabic.fx.ui.util.{ DefaultGap, DefaultInsets, createLabel }
import arabic.morphologicalanalysis.ui.EnumComboBox
import morphologicalengine.generator.model.SortDirection
import javafx.scene.control.SkinBase
import org.controlsfx.control.ToggleSwitch
import scalafx.Includes.*
import scalafx.beans.property.{ BooleanProperty, StringProperty }
import scalafx.geometry.Pos
import scalafx.scene.control.{ CheckBox, ComboBox }
import scalafx.scene.layout.{ BorderPane, GridPane }
import scalafx.scene.text.Font

class ChartConfigurationSkin(control: ChartConfigurationView) extends SkinBase[ChartConfigurationView](control) {

  private var rowIndex = 0

  private val gridPane = new GridPane() {
    vgap = DefaultGap
    hgap = DefaultGap
    alignment = Pos.Center
    padding = DefaultInsets
  }

  getChildren.addAll(initializeSkin)

  private def initializeSkin = {
    initSortDirection()
    addBooleanProperty("Show Table of Content:", control.showToc, control.showTocProperty)
    addBooleanProperty("Show Title:", control.showTitle, control.showTitleProperty)
    addBooleanProperty(
      "Show abbreviated conjugation:",
      control.showAbbreviatedConjugation,
      control.showAbbreviatedConjugationProperty
    )
    addBooleanProperty(
      "Show detailed conjugation:",
      control.showDetailedConjugation,
      control.showDetailedConjugationProperty
    )
    addBooleanProperty(
      "Show Morphological term caption in abbreviated conjugation:",
      control.showMorphologicalTermCaptionInAbbreviatedConjugation,
      control.showMorphologicalTermCaptionInAbbreviatedConjugationProperty
    )
    addBooleanProperty(
      "Show Morphological term caption in detailed conjugation:",
      control.showMorphologicalTermCaptionInDetailConjugation,
      control.showMorphologicalTermCaptionInDetailConjugationProperty
    )
    new BorderPane() {
      center = gridPane
      BorderPane.setAlignment(gridPane, Pos.Center)
    }
  }

  private def initSortDirection(): Unit = {
    val label = createLabel("Sort Direction:")
    gridPane.add(label, 0, rowIndex)
    val comoBox = EnumComboBox(SortDirection.values)
    comoBox.getSelectionModel.select(control.sortDirection)
    comoBox.valueProperty().bindBidirectional(control.sortDirectionProperty)
    gridPane.add(comoBox, 1, rowIndex)
    rowIndex += 1
  }

  private def addFontFamiliesProperty(text: String, initialValue: String, property: StringProperty): Unit = {
    val label = createLabel(text)
    gridPane.add(label, 0, rowIndex)
    val comboBox = new ComboBox[String](Font.families.toList) {
      value = initialValue
    }
    comboBox.valueProperty().bindBidirectional(property)
    gridPane.add(comboBox, 1, rowIndex)
    rowIndex += 1
  }

  private def addBooleanProperty(text: String, initialValue: Boolean, property: BooleanProperty): Unit = {
    val label = createLabel(text)
    gridPane.add(label, 0, rowIndex)
    val toggleSwitch = new ToggleSwitch()
    toggleSwitch.setSelected(initialValue)
    toggleSwitch.selectedProperty().bindBidirectional(property)
    gridPane.add(toggleSwitch, 1, rowIndex)
    rowIndex += 1
  }
}

object ChartConfigurationSkin {
  def apply(control: ChartConfigurationView): ChartConfigurationSkin = new ChartConfigurationSkin(control)
}
