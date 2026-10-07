package com.alphasystem
package arabic
package examples_generator
package ui
package control
package chapter_verse_selector

import ui.model.{ TokenRange, VerseRange, VerseSearchRequest }
import ui.service.{ GetChaptersInfoService, VerseSearchService }
import arabic.model.ArabicLetters
import arabic.morphologicalanalysis.ui.service.NoOpRequest
import arabic.utils.{ ChapterInfo, VerseResult }
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, ReadOnlyStringWrapper }
import scalafx.collections.ObservableBuffer

class ChapterVerseSelectionView extends Control {

  private val getChaptersInfoService = GetChaptersInfoService(this)
  private val verseSearchService = VerseSearchService(this)
  private[chapter_verse_selector] val chaptersProperty = ObservableBuffer[ChapterInfo]()
  private[chapter_verse_selector] val selectedChapterProperty = ObjectProperty[ChapterInfo](this, "selectedChapter")
  private[chapter_verse_selector] val verseRangeProperty = ObjectProperty[VerseRange](this, "verseRange")
  private[chapter_verse_selector] val tokenRangeProperty = ObjectProperty[TokenRange](this, "tokenRange")
  private[chapter_verse_selector] val verseTextProperty = ReadOnlyStringWrapper("")
  private[chapter_verse_selector] val selectedTextProperty = ReadOnlyStringWrapper("")

  setSkin(createDefaultSkin())
  getChaptersInfoService.executeService(NoOpRequest())
  verseRangeProperty.onChange((_, _, nv) => {
    if Option(nv).isDefined then {
      verseSearchService.executeService(
        VerseSearchRequest(selectedChapter.chapterNumber, nv.startVerseIndex, nv.endVerseIndex)
      )
    }
  })

  def selectedChapter: ChapterInfo = selectedChapterProperty.value
  private[chapter_verse_selector] def selectedChapter_=(value: ChapterInfo): Unit = selectedChapterProperty.value =
    value

  def verseRange: VerseRange = verseRangeProperty.value
  private[chapter_verse_selector] def verseRange_=(value: VerseRange): Unit = verseRangeProperty.value = value

  def tokenRange: TokenRange = tokenRangeProperty.value
  private[chapter_verse_selector] def tokenRange_=(value: TokenRange): Unit = tokenRangeProperty.value = value

  def verseText: String = verseTextProperty.value
  private def verseText_=(value: String): Unit = verseTextProperty.value = value

  def selectedText: String = selectedTextProperty.value
  private[chapter_verse_selector] def selectedText_=(value: String): Unit = selectedTextProperty.value = value

  def chapters: Seq[ChapterInfo] = chaptersProperty.toSeq

  def updateChapters(chapterInfos: Seq[ChapterInfo]): Unit = {
    chaptersProperty.clear()
    chaptersProperty.addAll(chapterInfos)
    selectedChapter = chapterInfos.head
  }

  def updateSelectedText(selectedVerses: Seq[VerseResult]): Unit = {
    val appendVerseNumber = selectedVerses.size > 1
    val text =
      selectedVerses.foldLeft("") { case (result, VerseResult(verseNumber, text)) =>
        val verseNumberText =
          if appendVerseNumber then s" ${ArabicLetters.NumberWordWithParenthesis(verseNumber).unicode}" else ""
        result + text + verseNumberText
      }
    verseText = text
    selectedText = text
  }

  override def createDefaultSkin(): Skin[?] = skin.VerseSelectionSkin(this)
}

object ChapterVerseSelectionView {
  def apply(): ChapterVerseSelectionView = new ChapterVerseSelectionView()
}
