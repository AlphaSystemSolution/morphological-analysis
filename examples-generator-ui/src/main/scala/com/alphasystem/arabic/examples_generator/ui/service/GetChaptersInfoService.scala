package com.alphasystem
package arabic
package examples_generator
package ui
package service

import ui.control.chapter_verse_selector.VerseSelectionView
import arabic.morphologicalanalysis.ui.service.{ NoOpRequest, ServiceAdapter }
import arabic.utils.{ ChapterInfo, VerseSearch }
import scalafx.Includes.*

class GetChaptersInfoService(view: VerseSelectionView)
    extends ServiceAdapter[NoOpRequest, Seq[ChapterInfo]](view) {

  private val verseSearch = VerseSearch()

  override protected def getResponse(request: NoOpRequest): Seq[ChapterInfo] = verseSearch.getChapters

  override protected def doOnSucceeded(result: Seq[ChapterInfo]): Unit = view.updateChapters(result)

  override protected def doOnFailed(): Unit = ()
}

object GetChaptersInfoService {
  def apply(view: VerseSelectionView) = new GetChaptersInfoService(view)
}
