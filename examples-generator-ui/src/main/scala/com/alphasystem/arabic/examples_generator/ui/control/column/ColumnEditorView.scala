package com.alphasystem
package arabic
package examples_generator
package ui
package control
package column

import ui.model.{ ColumnType, HighlightRange, HorizontalAlignment, VerseSearchResult, VerticalAlignment }
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, StringProperty }
import scalafx.collections.ObservableBuffer

class ColumnEditorView extends Control {

  private[column] val columnTypeProperty = ObjectProperty[ColumnType](this, "columnType", ColumnType.Arabic)
  private[column] val textProperty = StringProperty("")
  private[column] val verseSearchResultProperty =
    ObjectProperty[Option[VerseSearchResult]](this, "verseSearchResult", None)
  private[column] val colSpanProperty = StringProperty("1")
  private[column] val rowSpanProperty = StringProperty("1")
  private[column] val horizontalAlignmentProperty = ObjectProperty[HorizontalAlignment](this, "horizontalAlignment")
  private[column] val verticalAlignmentProperty = ObjectProperty[VerticalAlignment](this, "verticalAlignment")
  private[column] val highlightsProperty = ObservableBuffer[HighlightRange]()

  setSkin(createDefaultSkin())
  columnType = ColumnType.Arabic
  colSpan = "1"
  rowSpan = "1"
  horizontalAlignment = HorizontalAlignment.Left
  verticalAlignment = VerticalAlignment.Center
  verseSearchResultProperty.onChange((_, _, nv) =>
    nv match {
      case Some(result) => text = result.text
      case None         => text = ""
    }
  )

  def columnType: ColumnType = columnTypeProperty.value
  def columnType_=(value: ColumnType): Unit = columnTypeProperty.value = value

  def text: String = textProperty.value
  def text_=(value: String): Unit = textProperty.value = value

  def verseSearchResult: Option[VerseSearchResult] = verseSearchResultProperty.value
  private[column] def verseSearchResult_=(value: Option[VerseSearchResult]): Unit =
    verseSearchResultProperty.value = value

  def colSpan: String = colSpanProperty.value
  private[column] def colSpan_=(value: String): Unit = colSpanProperty.value = value

  def rowSpan: String = rowSpanProperty.value
  private[column] def rowSpan_=(value: String): Unit = rowSpanProperty.value = value

  def horizontalAlignment: HorizontalAlignment = horizontalAlignmentProperty.value
  private[column] def horizontalAlignment_=(value: HorizontalAlignment): Unit =
    horizontalAlignmentProperty.value = value

  def verticalAlignment: VerticalAlignment = verticalAlignmentProperty.value
  private[column] def verticalAlignment_=(value: VerticalAlignment): Unit = verticalAlignmentProperty.value = value

  def highlights: Seq[HighlightRange] = highlightsProperty.toSeq

  override def createDefaultSkin(): Skin[?] = skin.ColumnEditorSkin(this)
}

object ColumnEditorView {
  def apply(): ColumnEditorView = new ColumnEditorView()
}
