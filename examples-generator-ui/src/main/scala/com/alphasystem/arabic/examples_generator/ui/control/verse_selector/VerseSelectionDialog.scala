package com.alphasystem
package arabic
package examples_generator
package ui
package control
package verse_selector

import scalafx.application.JFXApp3
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.Includes.*
import ui.model.VerseSearchResult
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

  def setDialogContent(verseSearchResult: VerseSearchResult): Unit =
    dialogContent.setInitialSelection(verseSearchResult)
}

object VerseSelectionDialog {
  def apply(): VerseSelectionDialog = new VerseSelectionDialog()
}
