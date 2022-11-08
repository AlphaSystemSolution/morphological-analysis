package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph

import control.DependencyGraphView
import de.jensd.fx.glyphs.fontawesome.{ FontAwesomeIcon, FontAwesomeIconView }
import javafx.application.Platform
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{ Button, ContentDisplay, ToolBar, Tooltip }
import scalafx.scene.input.{ KeyCode, KeyCodeCombination, KeyCombination }
import scalafx.scene.layout.BorderPane
import scalafx.stage.Screen

object DependencyGraphApp extends JFXApp3 with AppInit {

  private lazy val view = DependencyGraphView(serviceFactory)

  private def createPane = {
    new BorderPane() {
      top = createToolBar
      center = view
    }
  }

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Dependency Graph"
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
    stage
      .scene
      .value
      .accelerators
      .put(new KeyCodeCombination(KeyCode.N, KeyCombination.MetaDown), () => newGraphAction())
  }

  private def createToolBar = {
    val newButton = new Button() {
      graphic = new FontAwesomeIconView(FontAwesomeIcon.PLUS, "2em")
      contentDisplay = ContentDisplay.GraphicOnly
      tooltip = Tooltip("Create New Dependency Graph")
      onAction = event => {
        newGraphAction()
        event.consume()
      }
    }

    new ToolBar() {
      items = Seq(newButton)
    }
  }

  private def newGraphAction(): Unit = view.createNewGraph()
}
