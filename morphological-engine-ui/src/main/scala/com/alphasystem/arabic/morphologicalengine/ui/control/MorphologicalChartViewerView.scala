package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import morphologicalengine.conjugation.model.MorphologicalChart
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.ObjectProperty

class MorphologicalChartViewerView extends Control {

  private[control] val morphologicalChartProperty =
    ObjectProperty[Option[MorphologicalChart]](this, "morphologicalChart", None)
  private[control] val errorProperty = ObjectProperty[Option[String]](this, "error", None)

  setSkin(createDefaultSkin())

  def morphologicalChart: Option[MorphologicalChart] = morphologicalChartProperty.value
  def morphologicalChart_=(value: Option[MorphologicalChart]): Unit = morphologicalChartProperty.value = value

  def error: Option[String] = errorProperty.value
  def error_=(value: Option[String]): Unit = errorProperty.value = value

  override def createDefaultSkin(): Skin[?] = skin.MorphologicalChartViewerSkin(this)
}

object MorphologicalChartViewerView {
  def apply(): MorphologicalChartViewerView = new MorphologicalChartViewerView()
}
