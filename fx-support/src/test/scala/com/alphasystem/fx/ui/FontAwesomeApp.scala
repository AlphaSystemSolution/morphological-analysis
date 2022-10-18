package com.alphasystem.fx.ui

import scalafx.scene.Scene
import scalafx.application.JFXApp3
import scalafx.stage.Screen

object FontAwesomeApp extends JFXApp3 {

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "FontAwesome"
      scene = new Scene() {
        content = new FontAwesomeView()
        stylesheets = Seq("/styles/glyphs_custom.css")
      }
    }

    val bounds = Screen.primary.visualBounds
    stage.x = bounds.minX
    stage.y = bounds.minY
    stage.width = bounds.width
    stage.height = bounds.height
    stage.resizable = true
    stage.maximized = true
  }
}
