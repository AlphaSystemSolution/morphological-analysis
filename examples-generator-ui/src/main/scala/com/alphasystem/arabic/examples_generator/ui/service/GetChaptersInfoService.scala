package com.alphasystem
package arabic
package examples_generator
package ui
package service

import ui.control.chapter_verse_selector.ChapterVerseSelectionView
import arabic.morphologicalanalysis.ui.service.ServiceAdapter
import arabic.utils.{ ChapterInfo, VerseSearch }
import scalafx.Includes.*
import scalafx.concurrent.Service

class GetChaptersInfoService(view: ChapterVerseSelectionView) extends ServiceAdapter[Unit, Seq[ChapterInfo]](view) {

  private val verseSearch = VerseSearch()

  lazy val service: Service[Seq[ChapterInfo]] = serviceInitializer(_ => verseSearch.getChapters)(())

  override protected def doOnSucceeded(result: Seq[ChapterInfo]): Unit = view.updateChapters(result)

  override protected def doOnFailed(): Unit = ()

  def executeService(): Unit = {
    handleResponse(service)
    start(service)
  }
}

object GetChaptersInfoService {
  def apply(view: ChapterVerseSelectionView) = new GetChaptersInfoService(view)
}
