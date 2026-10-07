package com.alphasystem
package arabic
package examples_generator
package ui
package service

import com.alphasystem.arabic.examples_generator.ui.control.verse_selector.VerseSelectionView
import com.alphasystem.arabic.examples_generator.ui.model.VerseSearchRequest
import com.alphasystem.arabic.morphologicalanalysis.ui.service.ServiceAdapter
import com.alphasystem.arabic.utils.{ VerseResult, VerseSearch }
import scalafx.Includes.*

class VerseSearchService(view: VerseSelectionView) extends ServiceAdapter[VerseSearchRequest, Seq[VerseResult]](view) {

  private val verseSearch = VerseSearch()

  override protected def getResponse(request: VerseSearchRequest): Seq[VerseResult] =
    verseSearch.searchVerses(request.chapterNumber, request.startVerseIndex, request.endVerseIndex)

  override protected def doOnSucceeded(result: Seq[VerseResult]): Unit = view.updateSelectedText(result)

  override protected def doOnFailed(): Unit = ()
}

object VerseSearchService {
  def apply(view: VerseSelectionView): VerseSearchService = new VerseSearchService(view)
}
