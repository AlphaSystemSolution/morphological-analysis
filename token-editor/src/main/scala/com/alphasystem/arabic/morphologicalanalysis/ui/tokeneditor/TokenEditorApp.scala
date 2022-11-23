package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor

import morphology.model.Chapter
import morphology.persistence.cache.CacheFactory
import morphology.persistence.repository.*
import ui.tokeneditor.control.TokenEditorView
import ui.commons.service.ServiceFactory
import com.typesafe.config.ConfigFactory
import de.jensd.fx.glyphs.fontawesome.{ FontAwesomeIcon, FontAwesomeIconView }
import javafx.application.Platform
import scalafx.stage.Screen
import scalafx.scene.paint.Color
import scalafx.geometry.{ Insets, Pos }
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.concurrent.Service
import scalafx.scene.Scene
import scalafx.scene.control.{ Button, ToolBar, Tooltip }
import scalafx.scene.input.{ KeyCode, KeyCodeCombination, KeyCombination }
import scalafx.scene.layout.{ BorderPane, VBox }

object TokenEditorApp extends JFXApp3 with AppInit {

  // UI components
  private lazy val tokenEditorView = TokenEditorView(serviceFactory)

  private def createPane = {
    val pane = new BorderPane()

    val topBox = new VBox() {
      spacing = DefaultGap
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
    }
    topBox.getChildren.addAll(createToolBar)
    pane.top = topBox

    pane.center = tokenEditorView
    BorderPane.setAlignment(tokenEditorView, Pos.Center)
    BorderPane.setMargin(tokenEditorView, Insets(20, 400, 0, 400))

    pane
  }

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Token Editor"
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
    tokenEditorView.titleProperty.onChange((_, _, nv) => stage.title = s"Token Editor: $nv")
    val accelerators = stage.scene.value.accelerators
    accelerators.put(new KeyCodeCombination(KeyCode.S, KeyCombination.MetaDown), () => saveAction())
  }

  private def createToolBar = {
    val saveButton = new Button() {
      graphic = new FontAwesomeIconView(FontAwesomeIcon.SAVE, "2em")
      tooltip = Tooltip("Save")
      onAction = event => {
        saveAction()
        event.consume()
      }
    }
    saveButton.disableProperty().bind(tokenEditorView.titleProperty.isEmpty)

    new ToolBar() {
      items = Seq(saveButton)
    }
  }

  private def saveAction(): Unit = tokenEditorView.saveLocations()
}
