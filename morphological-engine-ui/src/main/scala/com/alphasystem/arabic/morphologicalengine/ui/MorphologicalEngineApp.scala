package com.alphasystem
package arabic
package morphologicalengine
package ui

import fx.ui.util.*
import ui.control.{ GlobalAction, MorphologicalEngineView, TableAction }
import de.jensd.fx.glyphs.fontawesome.{ FontAwesomeIcon, FontAwesomeIconView }
import de.jensd.fx.glyphs.materialicons.{ MaterialIcon, MaterialIconView }
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.geometry.Orientation
import scalafx.scene.Scene
import scalafx.scene.control.*
import scalafx.scene.input.{ KeyCode, KeyCodeCombination, KeyCombination }
import scalafx.scene.layout.{ BorderPane, Pane, VBox }
import scalafx.stage.Screen

object MorphologicalEngineApp extends JFXApp3 {

  private lazy val view = MorphologicalEngineView()

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
    accelerators.put(
      new KeyCodeCombination(KeyCode.N, KeyCombination.MetaDown, KeyCombination.ShiftDown),
      () => addRowAction()
    )
    accelerators.put(
      new KeyCodeCombination(KeyCode.D, KeyCombination.MetaDown, KeyCombination.ShiftDown),
      () => duplicateRowAction()
    )
    accelerators.put(
      new KeyCodeCombination(KeyCode.R, KeyCombination.MetaDown, KeyCombination.ShiftDown),
      () => removeRowAction()
    )
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
      menus = Seq(createFileMenu, createChartMenu)
      useSystemMenuBar = true
    }

  private def createFileMenu = {
    val newMenuItem = createMenuItem("New ...", new KeyCodeCombination(KeyCode.N, KeyCombination.MetaDown), newAction)
    val saveMenuItem = createMenuItem("Save", new KeyCodeCombination(KeyCode.S, KeyCombination.MetaDown), saveAction)

    new Menu() {
      text = "File"
      accelerator = new KeyCodeCombination(KeyCode.F)
      items = Seq(newMenuItem, saveMenuItem)
    }
  }

  private def createChartMenu = {
    val addNewRowMenuItem = createMenuItem(
      "Add Row",
      new KeyCodeCombination(KeyCode.N, KeyCombination.MetaDown, KeyCombination.ShiftDown),
      addRowAction
    )

    val duplicateRowMenuItem = createMenuItem(
      "Duplicate Selected Row(s)",
      new KeyCodeCombination(KeyCode.D, KeyCombination.MetaDown, KeyCombination.ShiftDown),
      duplicateRowAction
    )

    val removeRowMenuItem = createMenuItem(
      "Remove Selected Row(s)",
      new KeyCodeCombination(KeyCode.R, KeyCombination.MetaDown, KeyCombination.ShiftDown),
      removeRowAction
    )

    new Menu() {
      text = "Chart"
      accelerator = new KeyCodeCombination(KeyCode.C)
      items = Seq(addNewRowMenuItem, duplicateRowMenuItem, removeRowMenuItem)
    }
  }

  private def createToolBar = {
    val openButton =
      createToolbarButton(
        new FontAwesomeIconView(FontAwesomeIcon.FOLDER_OPEN_ALT, "2em"),
        "Open Existing Project",
        openAction
      )

    val newButton =
      createToolbarButton(new FontAwesomeIconView(FontAwesomeIcon.FILE_ALT, "2em"), "Create New Project", newAction)

    val saveButton =
      createToolbarButton(new FontAwesomeIconView(FontAwesomeIcon.SAVE, "2em"), "Save Project", saveAction)

    val addNewRowButton =
      createToolbarButton(new MaterialIconView(MaterialIcon.ADD_BOX, "2em"), "Add Row", addRowAction)

    val duplicateRowButton =
      createToolbarButton(
        new FontAwesomeIconView(FontAwesomeIcon.CLONE, "2em"),
        "Duplicate Selected Row(s)",
        duplicateRowAction
      )

    val removeRowButton =
      createToolbarButton(
        new FontAwesomeIconView(FontAwesomeIcon.REMOVE, "2em"),
        "Remove Selected Row(s)",
        removeRowAction
      )

    new ToolBar() {
      items = Seq(
        openButton,
        newButton,
        saveButton,
        Separator(Orientation.Vertical),
        addNewRowButton,
        duplicateRowButton,
        removeRowButton
      )
    }
  }

  private def openAction(): Unit = {
    view.action = GlobalAction.None
    view.action = GlobalAction.Open
  }

  private def newAction(): Unit = {
    view.action = GlobalAction.None
    view.action = GlobalAction.New
  }

  private def saveAction(): Unit = {
    view.action = GlobalAction.None
    view.action = GlobalAction.Save
  }

  private def addRowAction(): Unit = {
    view.action = TableAction.None
    view.action = TableAction.Add
  }

  private def duplicateRowAction(): Unit = {
    view.action = TableAction.None
    view.action = TableAction.Duplicate
  }

  private def removeRowAction(): Unit = {
    view.action = TableAction.None
    view.action = TableAction.Remove
  }

  private def exitAction(): Unit = JFXApp3.Stage.close()
}
