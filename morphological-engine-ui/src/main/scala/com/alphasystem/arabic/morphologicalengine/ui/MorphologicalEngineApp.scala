package com.alphasystem
package arabic
package morphologicalengine
package ui

import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.geometry.Pos
import scalafx.scene.layout.BorderPane
import scalafx.stage.Screen

object MorphologicalEngineApp extends JFXApp3 {

  private lazy val view = control.MorphologicalEngineView()

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Morphological Engine"
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
    new BorderPane() {
      center = view
      BorderPane.setAlignment(view, Pos.Center)
    }
  }

  private def exitAction(): Unit = JFXApp3.Stage.close()
}
