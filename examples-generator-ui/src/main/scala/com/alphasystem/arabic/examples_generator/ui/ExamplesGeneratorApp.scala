package com.alphasystem
package arabic
package examples_generator
package ui

import ui.control.table.TablePropertiesEditorDialog
import ui.model.TableData
import arabic.fx.ui.util.*
import de.jensd.fx.glyphs.fontawesome.{ FontAwesomeIcon, FontAwesomeIconView }
import scalafx.application.JFXApp3
import scalafx.geometry.{ Orientation, Pos }
import scalafx.scene.Scene
import scalafx.scene.control.{ Separator, ToolBar }
import scalafx.scene.layout.{ BorderPane, VBox }
import scalafx.stage.Screen

object ExamplesGeneratorApp extends JFXApp3 {

  private lazy val dialog = TablePropertiesEditorDialog()

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
    val toolBar = createToolBar
    val topBox = new VBox {
      spacing = DefaultGap
      padding = DefaultInsets
      children = toolBar
    }

    new BorderPane() {
      top = toolBar
      BorderPane.setAlignment(topBox, Pos.TopCenter)
    }
  }

  private lazy val createToolBar = {
    val fileOpenButton = createToolBarButton(
      icon = new FontAwesomeIconView(FontAwesomeIcon.FOLDER_OPEN, DefaultIconSize),
      tooltipText = "Open file",
      action = fileOpenAction
    )
    fileOpenButton.setDisable(true)

    val newFileButton = createToolBarButton(
      icon = new FontAwesomeIconView(FontAwesomeIcon.FILE, DefaultIconSize),
      tooltipText = "Create new file",
      action = newFileAction
    )

    val saveButton = createToolBarButton(
      icon = new FontAwesomeIconView(FontAwesomeIcon.SAVE, DefaultIconSize),
      tooltipText = "Save file",
      action = fileSaveAction
    )
    saveButton.setDisable(true)

    new ToolBar {
      items = Seq(fileOpenButton, newFileButton, saveButton, Separator(Orientation.Vertical))
    }
  }

  private def fileOpenAction(): Unit = ()

  private def newFileAction(): Unit = {
    dialog.showAndWait() match {
      case Some(Some(tableData: TableData)) => println(tableData)
      case _                                => // do nothing
    }
  }

  private def fileSaveAction(): Unit = ()

  private def exitAction(): Unit = JFXApp3.Stage.close()
}
