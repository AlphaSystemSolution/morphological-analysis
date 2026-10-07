package com.alphasystem
package arabic
package examples_generator
package ui

import com.alphasystem.arabic.examples_generator.ui.model.VerseSearchResult
import ui.control.verse_selector.VerseSelectionDialog
import scalafx.application.JFXApp3
import scalafx.Includes.*
import scalafx.geometry.Pos
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.BorderPane
import scalafx.stage.Screen

object ExamplesGeneratorApp extends JFXApp3 {

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Examples Generator"
      scene = new Scene {
        content = createPane
        stylesheets = Seq("/styles/glyphs_custom.css")
      }
    }

    val bounds = Screen.primary.visualBounds
    stage.x = bounds.width / 4
    stage.y = bounds.height / 6
    stage.width = bounds.width
    stage.height = bounds.height
    stage.maximized = true
    stage.resizable = true
    stage.onCloseRequest = event => {
      exitAction()
      event.consume()
    }
  }

  private def createPane = {
    val button = new Button {
      text = "Select Verse(s) ..."
      onAction = event => {
        val dialog = VerseSelectionDialog()
        dialog.setDialogContent(
          VerseSearchResult(
            chapterNumber = 3,
            chapterName = "آل عمران",
            verseCount = 200,
            startVerseIndex = 119,
            endVerseIndex = 119,
            startTokenIndex = 26,
            endTokenIndex = 30,
            text = "إِنَّ اللَّهَ عَلِيمٌ بِذَاتِ الصُّدُورِ"
          )
        )
        dialog.showAndWait() match {
          case Some(Some(value)) => println(value)
          case _                 => println("Dialog was cancelled")
        }
        event.consume()
      }
    }
    new BorderPane() {
      center = button
      BorderPane.setAlignment(button, Pos.Center)
    }
  }

  private def exitAction(): Unit = JFXApp3.Stage.close()
}
