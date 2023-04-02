package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor

import morphology.model.Chapter
import morphology.persistence.cache.CacheFactory
import ui.tokeneditor.control.TokenEditorView
import ui.commons.service.ServiceFactory
import com.typesafe.config.ConfigFactory
import de.jensd.fx.glyphs.fontawesome.{ FontAwesomeIcon, FontAwesomeIconView }
import javafx.application.Platform
import javafx.beans.binding.Bindings
import scalafx.stage.Screen
import scalafx.scene.paint.Color
import scalafx.geometry.{ Insets, Orientation, Pos }
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.concurrent.Service
import scalafx.scene.Scene
import scalafx.scene.control.{ Button, Separator, ToolBar, Tooltip }
import scalafx.scene.input.{ KeyCode, KeyCodeCombination, KeyCombination }
import scalafx.scene.layout.{ BorderPane, VBox }

object TokenEditorApp extends JFXApp3 with AppInit {

  private val IconSize = "2em"

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
    BorderPane.setMargin(tokenEditorView, Insets(0, 0, 0, 10))

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

    stage.resizable = true
    tokenEditorView.titleProperty.onChange((_, _, nv) => stage.title = s"Token Editor: $nv")
    val accelerators = stage.scene.value.accelerators
    accelerators.put(new KeyCodeCombination(KeyCode.S, KeyCombination.MetaDown), () => saveAction())
    accelerators.put(new KeyCodeCombination(KeyCode.M, KeyCombination.MetaDown), () => mergeAction())
  }

  private def createToolBar = {
    val saveButton = new Button() {
      graphic = new FontAwesomeIconView(FontAwesomeIcon.SAVE, IconSize)
      tooltip = Tooltip("Save")
      onAction = event => {
        saveAction()
        event.consume()
      }
    }
    saveButton.disableProperty().bind(tokenEditorView.titleProperty.isEmpty)

    val mergeButton = new Button() {
      graphic = new FontAwesomeIconView(FontAwesomeIcon.COMPRESS, IconSize)
      tooltip = Tooltip("Merge Tokens")
      onAction = event => {
        mergeAction()
        event.consume()
      }
    }
    mergeButton.disableProperty().bind(Bindings.not(tokenEditorView.hasSelectedTokens))

    new ToolBar() {
      items = Seq(saveButton, Separator(Orientation.Vertical), mergeButton)
    }
  }

  private def saveAction(): Unit = tokenEditorView.saveLocations()

  private def mergeAction(): Unit = tokenEditorView.mergeTokens()
}
