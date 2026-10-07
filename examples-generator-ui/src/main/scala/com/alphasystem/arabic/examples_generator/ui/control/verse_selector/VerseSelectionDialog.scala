package com.alphasystem
package arabic
package examples_generator
package ui
package control
package verse_selector

import com.alphasystem.arabic.utils.ChapterInfo
import scalafx.application.JFXApp3
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.Includes.*
import ui.model.{ TokenRange, VerseRange, VerseSearchResult }
import scalafx.scene.control.{ ButtonType, Dialog }

class VerseSelectionDialog extends Dialog[Option[VerseSearchResult]] {

  private val dialogContent = VerseSelectionView()
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)

  initOwner(JFXApp3.Stage)
  title = "Select Verse(s)"
  headerText = "Select/search Quranic verse(s)"
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)
  dialogPane().content = dialogContent

  resultConverter = dialogButtonType =>
    if dialogButtonType == okButtonType then {
      Some(
        VerseSearchResult(
          chapterNumber = dialogContent.selectedChapter.chapterNumber,
          chapterName = dialogContent.selectedChapter.chapterName,
          verseCount = dialogContent.selectedChapter.verseCount,
          startVerseIndex = dialogContent.verseRange.startVerseIndex,
          endVerseIndex = dialogContent.verseRange.endVerseIndex,
          startTokenIndex = dialogContent.tokenRange.startTokenIndex,
          endTokenIndex = dialogContent.tokenRange.endTokenIndex,
          text = dialogContent.selectedText
        )
      )
    } else None

  def setDialogContent(verseSearchResult: VerseSearchResult): Unit = {
    dialogContent.selectedChapter =
      ChapterInfo(verseSearchResult.chapterNumber, verseSearchResult.chapterName, verseSearchResult.verseCount)
    dialogContent.verseRange = VerseRange(verseSearchResult.startVerseIndex, verseSearchResult.endVerseIndex)
    dialogContent.tokenRange = TokenRange(verseSearchResult.startTokenIndex, verseSearchResult.endTokenIndex)
  }
}

object VerseSelectionDialog {
  def apply(): VerseSelectionDialog = new VerseSelectionDialog()
}
