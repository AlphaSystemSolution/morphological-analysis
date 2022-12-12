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
import scalafx.geometry.Orientation
import scalafx.scene.Scene
import scalafx.scene.control.{
  Button,
  ContentDisplay,
  Menu,
  MenuBar,
  MenuItem,
  Separator,
  SeparatorMenuItem,
  ToolBar,
  Tooltip
}
import scalafx.scene.input.{ KeyCode, KeyCodeCombination, KeyCombination }
import scalafx.scene.layout.{ BorderPane, VBox }
import scalafx.stage.Screen

object DependencyGraphApp extends JFXApp3 with AppInit {

  private lazy val view = DependencyGraphView(serviceFactory)

  private def createPane = {
    new BorderPane() {
      top = new VBox() {
        children = Seq(createMenuBar, createToolBar)
      }
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
    stage.onCloseRequest = event => {
      exitAction()
      event.consume()
    }
    val accelerators = stage.scene.value.accelerators
    accelerators.put(new KeyCodeCombination(KeyCode.N, KeyCombination.MetaDown), () => newGraphAction())
    accelerators.put(new KeyCodeCombination(KeyCode.O, KeyCombination.MetaDown), () => openGraph())
    accelerators.put(new KeyCodeCombination(KeyCode.S, KeyCombination.MetaDown), () => saveGraph())
  }

  private def createMenuBar = {

    new MenuBar() {
      menus = Seq(createFileMenu)
      useSystemMenuBar = true
    }
  }

  private def createFileMenu =
    new Menu() {
      text = "File"
      accelerator = new KeyCodeCombination(KeyCode.F)
      items = Seq(
        new MenuItem() {
          text = "New ..."
          accelerator = new KeyCodeCombination(KeyCode.N, KeyCombination.MetaDown)
          onAction = event => {
            newGraphAction()
            event.consume()
          }
        },
        new MenuItem() {
          text = "Open ..."
          accelerator = new KeyCodeCombination(KeyCode.O, KeyCombination.MetaDown)
          onAction = event => {
            openGraph()
            event.consume()
          }
        },
        new MenuItem() {
          text = "Save"
          accelerator = new KeyCodeCombination(KeyCode.S, KeyCombination.MetaDown)
          onAction = event => {
            newGraphAction()
            event.consume()
          }
        },
        new SeparatorMenuItem()
      )
    }

  private def createToolBar = {
    val newButton = new Button() {
      graphic = new FontAwesomeIconView(FontAwesomeIcon.FILE, "2em")
      contentDisplay = ContentDisplay.GraphicOnly
      tooltip = Tooltip("Create New Dependency Graph")
      onAction = event => {
        newGraphAction()
        event.consume()
      }
    }

    val openButton = new Button() {
      graphic = new FontAwesomeIconView(FontAwesomeIcon.FOLDER_OPEN, "2em")
      contentDisplay = ContentDisplay.GraphicOnly
      tooltip = Tooltip("Open Dependency Graph")
      onAction = event => {
        openGraph()
        event.consume()
      }
    }

    val saveButton = new Button() {
      graphic = new FontAwesomeIconView(FontAwesomeIcon.SAVE, "2em")
      contentDisplay = ContentDisplay.GraphicOnly
      tooltip = Tooltip("Save Dependency Graph")
      onAction = event => {
        saveGraph()
        event.consume()
      }
    }

    new ToolBar() {
      items = Seq(newButton, openButton, saveButton, Separator(Orientation.Vertical))
    }
  }

  private def newGraphAction(): Unit = view.createGraph()
  private def saveGraph(): Unit = view.saveGraph()
  private def openGraph(): Unit = view.openGraph()

  private def exitAction(): Unit = {
    database.close()
    JFXApp3.Stage.close()
  }
}
