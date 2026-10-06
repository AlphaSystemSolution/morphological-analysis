package com.alphasystem
package arabic
package examples_generator
package ui
package control
package chapter_verse_selector

import com.alphasystem.arabic.examples_generator.ui.control.chapter_verse_selector.skin.ChapterVerseSelectionSkin
import com.alphasystem.arabic.examples_generator.ui.service.GetChaptersInfoService
import com.alphasystem.arabic.morphologicalanalysis.ui.service.NoOpRequest
import com.alphasystem.arabic.utils.ChapterInfo
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, ReadOnlyStringWrapper }
import scalafx.collections.ObservableBuffer

class ChapterVerseSelectionView extends Control {

  private val getChaptersInfoService = GetChaptersInfoService(this)
  private[chapter_verse_selector] val chaptersProperty = ObservableBuffer[ChapterInfo]()
  private[chapter_verse_selector] val selectedChapterProperty = ObjectProperty[ChapterInfo](this, "selectedChapter")
  private[chapter_verse_selector] val selectedTextProperty: ReadOnlyStringWrapper = ReadOnlyStringWrapper("")

  setSkin(createDefaultSkin())
  getChaptersInfoService.executeService(NoOpRequest())

  def selectedChapter: ChapterInfo = selectedChapterProperty.value
  private[chapter_verse_selector] def selectedChapter_=(value: ChapterInfo): Unit = selectedChapterProperty.value =
    value

  def selectedText: String = selectedTextProperty.value
  def selectedText_=(value: String): Unit = selectedTextProperty.value = value

  def chapters: Seq[ChapterInfo] = chaptersProperty.toSeq

  def updateChapters(chapterInfos: Seq[ChapterInfo]): Unit = {
    chaptersProperty.clear()
    chaptersProperty.addAll(chapterInfos)
    selectedChapter = chapterInfos.head
  }

  override def createDefaultSkin(): Skin[?] = ChapterVerseSelectionSkin(this)
}

object ChapterVerseSelectionView {
  def apply(): ChapterVerseSelectionView = new ChapterVerseSelectionView()
}
