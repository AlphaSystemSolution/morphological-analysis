package com.alphasystem
package arabic
package examples_generator
package ui

import ui.control.file.ExampleFileGeneratorView
import ui.control.table.TablePropertiesEditorDialog
import ui.model.{ Action, Row, TableData, TableProperties }
import arabic.fx.ui.util.*
import de.jensd.fx.glyphs.fontawesome.{ FontAwesomeIcon, FontAwesomeIconView }
import de.jensd.fx.glyphs.materialdesignicons.{ MaterialDesignIcon, MaterialDesignIconView }
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.geometry.{ Orientation, Pos }
import scalafx.scene.Scene
import scalafx.scene.control.{ Separator, ToolBar }
import scalafx.scene.layout.{ BorderPane, VBox }
import scalafx.stage.Screen

import java.util.UUID

object ExamplesGeneratorApp extends JFXApp3 {

  private lazy val dialog = TablePropertiesEditorDialog()
  private lazy val mainContent = ExampleFileGeneratorView()

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Examples Generator"
      scene = new Scene {
        content = mainPanel
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

  private lazy val topBox = {
    val toolBar = createToolBar
    new VBox {
      spacing = DefaultGap
      padding = DefaultInsets
      children = toolBar
    }
  }

  private lazy val mainPanel =
    new BorderPane() {
      top = topBox
      BorderPane.setAlignment(topBox, Pos.TopCenter)
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

    val addTableButton = createToolBarButton(
      icon = new MaterialDesignIconView(MaterialDesignIcon.TABLE, DefaultIconSize),
      tooltipText = "Create new table",
      action = addTableAction
    )

    val addRowButton = createToolBarButton(
      icon = new MaterialDesignIconView(MaterialDesignIcon.TABLE_ROW_PLUS_AFTER, DefaultIconSize),
      tooltipText = "Add new row",
      action = addRowAction
    )

    val addColumnButton = createToolBarButton(
      icon = new MaterialDesignIconView(MaterialDesignIcon.TABLE_COLUMN_PLUS_AFTER, DefaultIconSize),
      tooltipText = "Add new column",
      action = addColumnAction
    )

    new ToolBar {
      items = Seq(
        fileOpenButton,
        newFileButton,
        saveButton,
        Separator(Orientation.Vertical),
        addTableButton,
        addRowButton,
        addColumnButton
      )
    }
  }

  private def fileOpenAction(): Unit = ()

  private def newFileAction(): Unit = {
    dialog.showAndWait() match {
      case Some(Some(tableProperties: TableProperties)) =>
        mainContent.addTableData(TableData(tableProperties))
        mainPanel.center = mainContent
      case _ => // do nothing
    }
  }

  private def fileSaveAction(): Unit = ()

  private def addTableAction(): Unit = ()

  private def addRowAction(): Unit = {
    mainContent.action = Action.NoAction
    mainContent.action = Action.AddRow(Row(UUID.randomUUID()))
  }

  private def addColumnAction(): Unit = ()

  private def exitAction(): Unit = JFXApp3.Stage.close()
}
