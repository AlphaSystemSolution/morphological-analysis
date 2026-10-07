package com.alphasystem
package arabic
package examples_generator
package ui

import ui.control.verse_selector.VerseSelectionView
import scalafx.application.JFXApp3
import scalafx.Includes.*
import scalafx.geometry.Pos
import scalafx.scene.Scene
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
    val view = VerseSelectionView()
    new BorderPane() {
      center = view
      BorderPane.setAlignment(view, Pos.Center)
    }
  }

  private def exitAction(): Unit = JFXApp3.Stage.close()
}
