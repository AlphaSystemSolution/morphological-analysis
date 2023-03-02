package com.alphasystem
package arabic
package morphologicalengine
package ui

import com.alphasystem.arabic.morphologicalengine.ui.control.MorphologicalEngineView
import de.jensd.fx.glyphs.fontawesome.{ FontAwesomeIcon, FontAwesomeIconView }
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.*
import scalafx.scene.input.{ KeyCode, KeyCodeCombination, KeyCombination }
import scalafx.scene.layout.{ BorderPane, Pane, VBox }
import scalafx.stage.Screen

object MorphologicalEngineApp extends JFXApp3 {

  private lazy val view = new MorphologicalEngineView()

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

    val accelerators = stage.scene.value.getAccelerators
    accelerators.put(new KeyCodeCombination(KeyCode.N, KeyCombination.MetaDown), () => newAction())
    accelerators.put(new KeyCodeCombination(KeyCode.S, KeyCombination.MetaDown), () => saveAction())
  }

  private def createPane = {
    new BorderPane() {
      top = new VBox() {
        children = Seq(createMenuBar, createToolBar)
      }
      center = view
    }
  }

  private def createMenuBar =
    new MenuBar() {
      menus = Seq(createFileMenu)
      useSystemMenuBar = true
    }

  private def createFileMenu = {
    val newMenuItem = new MenuItem() {
      text = "New ..."
      accelerator = new KeyCodeCombination(KeyCode.N, KeyCombination.MetaDown)
      onAction = event => {
        newAction()
        event.consume()
      }
    }

    val saveMenuItem = new MenuItem() {
      text = "Save"
      accelerator = new KeyCodeCombination(KeyCode.S, KeyCombination.MetaDown)
      onAction = event => {
        saveAction()
        event.consume()
      }
    }

    new Menu() {
      text = "File"
      accelerator = new KeyCodeCombination(KeyCode.F)
      items = Seq(newMenuItem, saveMenuItem)
    }
  }

  private def createToolBar = {
    val newButton = new Button() {
      graphic = new FontAwesomeIconView(FontAwesomeIcon.FILE, "2em")
      contentDisplay = ContentDisplay.GraphicOnly
      tooltip = Tooltip("Create New Project")
      onAction = event => {
        newAction()
        event.consume()
      }
    }

    val saveButton = new Button() {
      graphic = new FontAwesomeIconView(FontAwesomeIcon.SAVE, "2em")
      contentDisplay = ContentDisplay.GraphicOnly
      tooltip = Tooltip("Save Project")
      onAction = event => {
        saveAction()
        event.consume()
      }
    }

    new ToolBar() {
      items = Seq(newButton, saveButton)
    }
  }

  private def newAction(): Unit = ()

  private def saveAction(): Unit = ()

  private def exitAction(): Unit = JFXApp3.Stage.close()
}
