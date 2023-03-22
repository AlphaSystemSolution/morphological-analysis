package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import morphologicalengine.generator.model.{ ChartConfiguration, DocumentFormat, PageOrientation, SortDirection }
import skin.ChartConfigurationSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ BooleanProperty, IntegerProperty, LongProperty, ObjectProperty, StringProperty }

class ChartConfigurationView extends Control {

  private[control] val chartConfigurationProperty = ObjectProperty[ChartConfiguration](this, "chartConfiguration")
  private[control] val pageOrientationProperty =
    ObjectProperty[PageOrientation](this, "pageOrientation", PageOrientation.Portrait)
  private[control] val sortDirectionProperty =
    ObjectProperty[SortDirection](this, "sortDirection", SortDirection.Ascending)
  private[control] val documentFormatProperty =
    ObjectProperty[DocumentFormat](this, "documentFormat", DocumentFormat.Classic)
  private[control] val arabicFontFamilyProperty =
    new StringProperty(this, "arabicFontFamily", "KFGQPC Uthman Taha Naskh")
  private[control] val translationFontFamilyProperty =
    new StringProperty(this, "translationFontFamily", "Candara")
  private[control] val arabicFontSizeProperty = new LongProperty(this, "arabicFontSize", 12)
  private[control] val translationFontSizeProperty = new LongProperty(this, "translationFontSize", 10)
  private[control] val headingFontSizeProperty = new LongProperty(this, "headingFontSize", 16)
  private[control] val showTocProperty = new BooleanProperty(this, "showToc", true)
  private[control] val showTitleProperty = new BooleanProperty(this, "showTitle", true)
  private[control] val showLabelsProperty = new BooleanProperty(this, "showLabels", true)
  private[control] val showAbbreviatedConjugationProperty =
    new BooleanProperty(this, "showAbbreviatedConjugation", true)
  private[control] val showDetailedConjugationProperty =
    new BooleanProperty(this, "showDetailedConjugation", true)
  private[control] val showMorphologicalTermCaptionInAbbreviatedConjugationProperty =
    new BooleanProperty(this, "showMorphologicalTermCaptionInAbbreviatedConjugation", true)
  private[control] val showMorphologicalTermCaptionInDetailConjugationProperty =
    new BooleanProperty(this, "showMorphologicalTermCaptionInDetailConjugation", true)

  chartConfiguration = ChartConfiguration()
  updateChartConfiguration()
  setSkin(createDefaultSkin())

  def chartConfiguration: ChartConfiguration = chartConfigurationProperty.value
  def chartConfiguration_=(value: ChartConfiguration): Unit = chartConfigurationProperty.value = value

  def pageOrientation: PageOrientation = pageOrientationProperty.value
  def pageOrientation_=(value: PageOrientation): Unit = pageOrientationProperty.value = value

  def sortDirection: SortDirection = sortDirectionProperty.value
  def sortDirection_=(value: SortDirection): Unit = sortDirectionProperty.value = value

  def documentFormat: DocumentFormat = documentFormatProperty.value
  def documentFormat_=(value: DocumentFormat): Unit = documentFormatProperty.value = value

  def arabicFontFamily: String = arabicFontFamilyProperty.value
  def arabicFontFamily_=(value: String): Unit = arabicFontFamilyProperty.value = value

  def translationFontFamily: String = translationFontFamilyProperty.value
  def translationFontFamily_=(value: String): Unit = translationFontFamilyProperty.value = value

  def arabicFontSize: Long = arabicFontSizeProperty.value
  def arabicFontSize_=(value: Long): Unit = arabicFontSizeProperty.value = value

  def translationFontSize: Long = translationFontSizeProperty.value
  def translationFontSize_=(value: Long): Unit = translationFontSizeProperty.value = value

  def headingFontSize: Long = headingFontSizeProperty.value
  def headingFontSize_=(value: Long): Unit = headingFontSizeProperty.value = value

  def showToc: Boolean = showTocProperty.value
  def showToc_=(value: Boolean): Unit = showTocProperty.value = value

  def showTitle: Boolean = showTitleProperty.value
  def showTitle_=(value: Boolean): Unit = showTitleProperty.value = value

  def showLabels: Boolean = showLabelsProperty.value
  def showLabels_=(value: Boolean): Unit = showLabelsProperty.value = value

  def showAbbreviatedConjugation: Boolean = showAbbreviatedConjugationProperty.value
  def showAbbreviatedConjugation_=(value: Boolean): Unit = showAbbreviatedConjugationProperty.value = value

  def showDetailedConjugation: Boolean = showDetailedConjugationProperty.value
  def showDetailedConjugation_=(value: Boolean): Unit = showDetailedConjugationProperty.value = value

  def showMorphologicalTermCaptionInAbbreviatedConjugation: Boolean =
    showMorphologicalTermCaptionInAbbreviatedConjugationProperty.value
  def showMorphologicalTermCaptionInAbbreviatedConjugation_=(value: Boolean): Unit =
    showMorphologicalTermCaptionInAbbreviatedConjugationProperty.value = value

  def showMorphologicalTermCaptionInDetailConjugation: Boolean =
    showMorphologicalTermCaptionInDetailConjugationProperty.value

  def showMorphologicalTermCaptionInDetailConjugation_=(value: Boolean): Unit =
    showMorphologicalTermCaptionInDetailConjugationProperty.value = value

  private def initValues(chartConfiguration: ChartConfiguration): Unit = {
    val newValue = if Option(chartConfiguration).isDefined then chartConfiguration else ChartConfiguration()
    pageOrientation = newValue.pageOrientation
    sortDirection = newValue.sortDirection
    documentFormat = newValue.format
    arabicFontFamily = newValue.arabicFontFamily
    translationFontFamily = newValue.translationFontFamily
    arabicFontSize = newValue.arabicFontSize
    translationFontSize = newValue.translationFontSize
    headingFontSize = newValue.headingFontSize
    showToc = newValue.showToc
    showTitle = newValue.showTitle
    showLabels = newValue.showLabels
    showAbbreviatedConjugation = newValue.showAbbreviatedConjugation
    showDetailedConjugation = newValue.showDetailedConjugation
    showMorphologicalTermCaptionInAbbreviatedConjugation = newValue.showMorphologicalTermCaptionInAbbreviatedConjugation
    showMorphologicalTermCaptionInDetailConjugation = newValue.showMorphologicalTermCaptionInDetailConjugation
  }

  private def updateChartConfiguration(): Unit = {
    chartConfigurationProperty.onChange((_, _, nv) => initValues(nv))
    pageOrientationProperty.onChange((_, _, nv) => chartConfiguration = chartConfiguration.copy(pageOrientation = nv))
    sortDirectionProperty.onChange((_, _, nv) => chartConfiguration = chartConfiguration.copy(sortDirection = nv))
    documentFormatProperty.onChange((_, _, nv) => chartConfiguration = chartConfiguration.copy(format = nv))
    arabicFontFamilyProperty.onChange((_, _, nv) => chartConfiguration = chartConfiguration.copy(arabicFontFamily = nv))
    translationFontFamilyProperty.onChange((_, _, nv) =>
      chartConfiguration = chartConfiguration.copy(translationFontFamily = nv)
    )
    arabicFontSizeProperty.onChange((_, _, nv) =>
      chartConfiguration = chartConfiguration.copy(arabicFontSize = nv.longValue())
    )
    translationFontSizeProperty.onChange((_, _, nv) =>
      chartConfiguration = chartConfiguration.copy(translationFontSize = nv.longValue())
    )
    headingFontSizeProperty.onChange((_, _, nv) =>
      chartConfiguration = chartConfiguration.copy(headingFontSize = nv.longValue())
    )
    showTocProperty.onChange((_, _, nv) => chartConfiguration = chartConfiguration.copy(showToc = nv))
    showTitleProperty.onChange((_, _, nv) => chartConfiguration = chartConfiguration.copy(showTitle = nv))
    showLabelsProperty.onChange((_, _, nv) => chartConfiguration = chartConfiguration.copy(showLabels = nv))
    showAbbreviatedConjugationProperty.onChange((_, _, nv) =>
      chartConfiguration = chartConfiguration.copy(showAbbreviatedConjugation = nv)
    )
    showDetailedConjugationProperty.onChange((_, _, nv) =>
      chartConfiguration = chartConfiguration.copy(showDetailedConjugation = nv)
    )
    showMorphologicalTermCaptionInAbbreviatedConjugationProperty.onChange((_, _, nv) =>
      chartConfiguration = chartConfiguration.copy(showMorphologicalTermCaptionInAbbreviatedConjugation = nv)
    )
    showMorphologicalTermCaptionInDetailConjugationProperty.onChange((_, _, nv) =>
      chartConfiguration = chartConfiguration.copy(showMorphologicalTermCaptionInDetailConjugation = nv)
    )
  }

  override def createDefaultSkin(): Skin[_] = ChartConfigurationSkin(this)
}

object ChartConfigurationView {
  def apply(): ChartConfigurationView = new ChartConfigurationView()
}
