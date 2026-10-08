package com.alphasystem
package arabic
package examples_generator
package ui
package control
package column

import ui.model.{ ColumnType, VerseSearchResult }
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, StringProperty }

class CreateColumnView extends Control {

  private[column] val columnTypeProperty = ObjectProperty[ColumnType](this, "columnType", ColumnType.Arabic)
  private[column] val textProperty = StringProperty("")
  private[column] val verseSearchResultProperty =
    ObjectProperty[Option[VerseSearchResult]](this, "verseSearchResult", None)

  setSkin(createDefaultSkin())
  columnType = ColumnType.Arabic
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
  private[column] def verseSearchResult_=(value: Option[VerseSearchResult]): Unit = verseSearchResultProperty.value =
    value

  override def createDefaultSkin(): Skin[?] = skin.CreateColumnSkin(this)
}

object CreateColumnView {
  def apply(): CreateColumnView = new CreateColumnView()
}
