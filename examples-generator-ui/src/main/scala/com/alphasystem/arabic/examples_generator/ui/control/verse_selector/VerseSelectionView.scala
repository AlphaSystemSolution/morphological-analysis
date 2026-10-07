package com.alphasystem
package arabic
package examples_generator
package ui
package control
package verse_selector

import ui.model.{ TokenRange, VerseRange, VerseSearchRequest, VerseSearchResult }
import ui.service.{ GetChaptersInfoService, VerseSearchService }
import arabic.model.ArabicLetters
import arabic.morphologicalanalysis.ui.service.NoOpRequest
import arabic.utils.{ ChapterInfo, VerseResult }
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, ReadOnlyStringWrapper }
import scalafx.collections.ObservableBuffer

class VerseSelectionView extends Control {

  private val getChaptersInfoService = GetChaptersInfoService(this)
  private val verseSearchService = VerseSearchService(this)
  private[verse_selector] val chaptersProperty = ObservableBuffer[ChapterInfo]()
  private[verse_selector] val selectedChapterProperty = ObjectProperty[ChapterInfo](this, "selectedChapter")
  private[verse_selector] val verseRangeProperty = ObjectProperty[VerseRange](this, "verseRange")
  private[verse_selector] val tokenRangeProperty = ObjectProperty[TokenRange](this, "tokenRange")
  private[verse_selector] val verseTextProperty = ReadOnlyStringWrapper("")
  private[verse_selector] val selectedTextProperty = ReadOnlyStringWrapper("")
  private[verse_selector] var pendingSelection: Option[VerseSearchResult] = None
  private var lastVerseSearchRequest: Option[VerseSearchRequest] = None

  setSkin(createDefaultSkin())
  getChaptersInfoService.executeService(NoOpRequest())
  verseRangeProperty.onChange((_, _, nv) => {
    if Option(nv).isDefined then {
      val request = VerseSearchRequest(selectedChapter.chapterNumber, nv.startVerseIndex, nv.endVerseIndex)
      lastVerseSearchRequest = Some(request)
      verseSearchService.executeService(request)
    }
  })

  def selectedChapter: ChapterInfo = selectedChapterProperty.value
  private[verse_selector] def selectedChapter_=(value: ChapterInfo): Unit = selectedChapterProperty.value = value

  def verseRange: VerseRange = verseRangeProperty.value
  private[verse_selector] def verseRange_=(value: VerseRange): Unit = verseRangeProperty.value = value

  def tokenRange: TokenRange = tokenRangeProperty.value
  private[verse_selector] def tokenRange_=(value: TokenRange): Unit = tokenRangeProperty.value = value

  def verseText: String = verseTextProperty.value
  private def verseText_=(value: String): Unit = verseTextProperty.value = value

  def selectedText: String = selectedTextProperty.value
  private[verse_selector] def selectedText_=(value: String): Unit = selectedTextProperty.value = value

  def chapters: Seq[ChapterInfo] = chaptersProperty.toSeq

  def setInitialSelection(result: VerseSearchResult): Unit = {
    pendingSelection = Some(result)
    if chapters.nonEmpty then {
      chapters.find(_.chapterNumber == result.chapterNumber) match {
        case Some(chapterInfo) =>
          selectedChapter = chapterInfo
        case None => pendingSelection = None
      }
    }
  }

  def updateChapters(chapterInfos: Seq[ChapterInfo]): Unit = {
    chaptersProperty.clear()
    chaptersProperty.addAll(chapterInfos)
    pendingSelection match {
      case Some(result) =>
        chapterInfos.find(_.chapterNumber == result.chapterNumber) match {
          case Some(chapterInfo) => selectedChapter = chapterInfo
          case None =>
            pendingSelection = None
            selectedChapter = chapterInfos.head
        }
      case None => selectedChapter = chapterInfos.head
    }
  }

  def updateSelectedText(selectedVerses: Seq[VerseResult]): Unit = {
    val matchesLatestRequest = lastVerseSearchRequest.exists { request =>
      selectedVerses.nonEmpty &&
      selectedChapter.chapterNumber == request.chapterNumber &&
      selectedVerses.head.verseNumber == request.startVerseIndex &&
      selectedVerses.last.verseNumber == request.endVerseIndex
    }
    if !matchesLatestRequest then return

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

object VerseSelectionView {
  def apply(): VerseSelectionView = new VerseSelectionView()
}
