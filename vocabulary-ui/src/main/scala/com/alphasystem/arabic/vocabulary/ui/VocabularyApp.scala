package com.alphasystem
package arabic
package vocabulary
package ui

import control.VocabularyEditorView
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane
import scalafx.stage.Screen

object VocabularyApp extends JFXApp3 {

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Arabic Vocabulary Builder"
      scene = new Scene {
        content = rootPane()
      }
    }

    applyStageBounds()
  }

  private def rootPane(): BorderPane =
    new BorderPane {
      center = VocabularyEditorView()
    }

  private def applyStageBounds(): Unit = {
    val visualBounds = Screen.primary.visualBounds
    stage.x = visualBounds.width / 4
    stage.y = visualBounds.height / 7
    stage.width = visualBounds.width
    stage.height = visualBounds.height
    stage.maximized = true
    stage.resizable = true
  }
}
