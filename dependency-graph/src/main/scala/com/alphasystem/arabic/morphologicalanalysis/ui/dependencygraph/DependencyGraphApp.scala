package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph

import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane
import scalafx.scene.paint.Color
import scalafx.stage.Screen

object DependencyGraphApp extends JFXApp3 {

  private def createPane = {
    new BorderPane()
  }

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Dependency Graph"
      scene = new Scene {
        content = createPane
        fill = Color.Beige
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
  }
}
