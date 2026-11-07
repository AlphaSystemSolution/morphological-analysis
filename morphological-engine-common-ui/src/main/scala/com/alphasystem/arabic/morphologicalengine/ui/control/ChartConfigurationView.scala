package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import morphologicalengine.generator.model.{ ChartConfiguration, SortDirection }
import skin.ChartConfigurationSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ BooleanProperty, ObjectProperty }

class ChartConfigurationView extends Control {

  private[control] val chartConfigurationProperty = ObjectProperty[ChartConfiguration](this, "chartConfiguration")

  private[control] val sortDirectionProperty =
    ObjectProperty[SortDirection](this, "sortDirection", SortDirection.Ascending)
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

  def sortDirection: SortDirection = sortDirectionProperty.value
  def sortDirection_=(value: SortDirection): Unit = sortDirectionProperty.value = value

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
    sortDirection = newValue.sortDirection
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
    sortDirectionProperty.onChange((_, _, nv) => chartConfiguration = chartConfiguration.copy(sortDirection = nv))
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

  override def createDefaultSkin(): Skin[?] = ChartConfigurationSkin(this)
}

object ChartConfigurationView {
  def apply(): ChartConfigurationView = new ChartConfigurationView()
}
