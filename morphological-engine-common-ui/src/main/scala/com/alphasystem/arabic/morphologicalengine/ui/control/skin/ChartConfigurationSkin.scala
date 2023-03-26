package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import com.alphasystem.arabic.morphologicalanalysis.ui.EnumComboBox
import morphologicalengine.generator.model.{ DocumentFormat, PageOrientation, SortDirection }
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.beans.property.{ BooleanProperty, LongProperty, StringProperty }
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.control.{ CheckBox, ComboBox, Label }
import scalafx.scene.layout.{ BorderPane, GridPane }
import scalafx.scene.text.Font
import io.circe.KeyEncoder.encodeKeyLong

import scala.util.Try

class ChartConfigurationSkin(control: ChartConfigurationView) extends SkinBase[ChartConfigurationView](control) {

  private var rowIndex = 0
  private val DefaultGap: Double = 10.0

  private val gridPane = new GridPane() {
    vgap = DefaultGap
    hgap = DefaultGap
    alignment = Pos.Center
    padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
  }

  getChildren.addAll(initializeSkin)

  private def initializeSkin = {
    initPageOrientation()
    initSortDirection()
    initDocumentFormat()
    addFontFamiliesProperty("Arabic font family:", control.arabicFontFamily, control.arabicFontFamilyProperty)
    addFontFamiliesProperty(
      "Translation font family:",
      control.translationFontFamily,
      control.translationFontFamilyProperty
    )
    addFontSizesProperty("Arabic normal font size:", control.arabicFontSize, control.arabicFontSizeProperty)
    addFontSizesProperty("Arabic heading font size:", control.headingFontSize, control.headingFontSizeProperty)
    addFontSizesProperty("Translation font size:", control.translationFontSize, control.translationFontSizeProperty)
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

  private def initPageOrientation(): Unit = {
    val label = Label("Page Orientation:")
    gridPane.add(label, 0, rowIndex)
    val comoBox = EnumComboBox(PageOrientation.values)
    comoBox.getSelectionModel.select(control.pageOrientation)
    comoBox.valueProperty().bindBidirectional(control.pageOrientationProperty)
    gridPane.add(comoBox, 1, rowIndex)
    rowIndex += 1
  }

  private def initSortDirection(): Unit = {
    val label = Label("Sort Direction:")
    gridPane.add(label, 0, rowIndex)
    val comoBox = EnumComboBox(SortDirection.values)
    comoBox.getSelectionModel.select(control.sortDirection)
    comoBox.valueProperty().bindBidirectional(control.sortDirectionProperty)
    gridPane.add(comoBox, 1, rowIndex)
    rowIndex += 1
  }

  private def initDocumentFormat(): Unit = {
    val label = Label("Document Format:")
    gridPane.add(label, 0, rowIndex)
    val comboBox = EnumComboBox(DocumentFormat.values)
    comboBox.getSelectionModel.select(control.documentFormat)
    comboBox.valueProperty().bindBidirectional(control.documentFormatProperty)
    gridPane.add(comboBox, 1, rowIndex)
    rowIndex += 1
  }

  private def addFontFamiliesProperty(text: String, initialValue: String, property: StringProperty): Unit = {
    val label = Label(text)
    gridPane.add(label, 0, rowIndex)
    val comboBox = new ComboBox[String](Font.families.toList) {
      value = initialValue
    }
    comboBox.valueProperty().bindBidirectional(property)
    gridPane.add(comboBox, 1, rowIndex)
    rowIndex += 1
  }

  private def addFontSizesProperty(text: String, initialValue: Long, property: LongProperty): Unit = {
    val label = Label(text)
    gridPane.add(label, 0, rowIndex)
    val comboBox = new ComboBox[Long](Seq(5L, 6L, 7L, 8L, 10L, 11L, 12L, 14L, 16L, 18L, 20L, 24L, 28L, 32L, 36L, 48L)) {
      value = initialValue
    }
    comboBox.valueProperty().onChange((_, _, nv) => control.arabicFontSize = nv.longValue)
    property.onChange((_, _, nv) => comboBox.selectionModel.select(nv.toString))
    gridPane.add(comboBox, 1, rowIndex)
    rowIndex += 1
  }

  private def addBooleanProperty(text: String, initialValue: Boolean, property: BooleanProperty): Unit = {
    val label = Label(text)
    gridPane.add(label, 0, rowIndex)
    val checkBox = new CheckBox() {
      selected = initialValue
    }
    checkBox.selectedProperty().bindBidirectional(property)
    gridPane.add(checkBox, 1, rowIndex)
    rowIndex += 1
  }
}

object ChartConfigurationSkin {
  def apply(control: ChartConfigurationView): ChartConfigurationSkin = new ChartConfigurationSkin(control)
}
