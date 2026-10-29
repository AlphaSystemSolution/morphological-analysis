package com.alphasystem
package arabic
package examples_generator
package ui

import ui.control.column.ColumnEditorDialog
import ui.control.table.TablePropertiesEditorDialog
import ui.service.SaveExampleDataService
import arabic.fx.ui.util.*
import ui.control.examples_view.ExamplesView
import ui.model.{ Action, ExampleRequest, InsertPosition }
import examples_generator.model.{ ColumnData, TableData, TableProperties, toExampleData }
import de.jensd.fx.glyphs.fontawesome.{ FontAwesomeIcon, FontAwesomeIconView }
import de.jensd.fx.glyphs.materialdesignicons.{ MaterialDesignIcon, MaterialDesignIconView }
import org.slf4j.LoggerFactory
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.geometry.{ Orientation, Pos }
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ Alert, ButtonType, ChoiceDialog, Separator, ToolBar }
import scalafx.scene.input.{ KeyCode, KeyCodeCombination, KeyCombination }
import scalafx.scene.layout.{ BorderPane, VBox }
import scalafx.stage.FileChooser.ExtensionFilter
import scalafx.stage.{ FileChooser, Screen }

import java.util.UUID
import scala.util.{ Failure, Success, Try }

object ExamplesGeneratorApp extends JFXApp3 {

  private val logger = LoggerFactory.getLogger(getClass)
  private lazy val tablePropertiesEditorDialog = TablePropertiesEditorDialog()
  private lazy val columnEditorDialog = ColumnEditorDialog()
  private lazy val mainContent = ExamplesView()
  private lazy val saveExampleDataService = SaveExampleDataService(mainContent)
  private lazy val fileChooser = {
    val fc = new FileChooser {
      initialDirectory = preferences.initialDirectory.toFile
    }
    fc.extensionFilters.addAll(new ExtensionFilter("YAML file", "*.yml"), new ExtensionFilter("YAML file", "*.yaml"))
    fc
  }

  private lazy val exitConfirmation = {
    val ok = new ButtonType("Yes, I will save files", ButtonData.OKDone)
    val cancel = new ButtonType("Yes, I understand the risk", ButtonData.No)
    new Alert(AlertType.Confirmation) {
      initOwner(JFXApp3.Stage)
      title = "Are you sure?"
      contentText = "There are unsaved changes in one for multiple files, please save them before exiting."
      buttonTypes = Seq(ok, cancel)
      onShown = _ =>
        Try(dialogPane().scene().window().onCloseRequest = (event: javafx.stage.WindowEvent) => event.consume())
    }
  }

  private lazy val fileChoices = Seq("Example", "Conjugation")
  private lazy val newFileChoiceDialog = new ChoiceDialog[String](fileChoices.head, fileChoices) {
    title = "New File"
    headerText = "Please select the type of file you want to create"
    contentText = "Please select the type of file you want to create"
  }

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

    val accelerators = stage.scene.value.accelerators
    accelerators.put(new KeyCodeCombination(KeyCode.N, KeyCombination.MetaDown), () => newFileAction())
    accelerators.put(new KeyCodeCombination(KeyCode.O, KeyCombination.MetaDown), () => fileOpenAction())
    accelerators.put(new KeyCodeCombination(KeyCode.S, KeyCombination.MetaDown), () => fileSaveAction())
    accelerators.put(new KeyCodeCombination(KeyCode.T, KeyCombination.MetaDown), () => addTableAction())
    accelerators.put(new KeyCodeCombination(KeyCode.R, KeyCombination.MetaDown), () => insertRowAfterAction())
    accelerators.put(
      new KeyCodeCombination(KeyCode.R, KeyCombination.MetaDown, KeyCombination.ShiftDown),
      () => insertRowBeforeAction()
    )
    accelerators.put(new KeyCodeCombination(KeyCode.C, KeyCombination.MetaDown), () => insertColumnAfterAction())
    accelerators.put(
      new KeyCodeCombination(KeyCode.C, KeyCombination.MetaDown, KeyCombination.ShiftDown),
      () => insertColumnBeforeAction()
    )

    accelerators.put(new KeyCodeCombination(KeyCode.Q, KeyCombination.MetaDown), () => exitAction())
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
      center = mainContent
      BorderPane.setAlignment(topBox, Pos.TopCenter)
    }

  private lazy val createToolBar = {
    val fileOpenButton = createToolBarButton(
      icon = new FontAwesomeIconView(FontAwesomeIcon.FOLDER_OPEN, DefaultIconSize),
      tooltipText = "Open file",
      action = fileOpenAction
    )

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
    saveButton.disableProperty().bind(mainContent.dirtyProperty)

    val addTableButton = createToolBarButton(
      icon = new MaterialDesignIconView(MaterialDesignIcon.TABLE, DefaultIconSize),
      tooltipText = "Create new table",
      action = addTableAction
    )
    addTableButton.disableProperty().bind(mainContent.hasSelectedTableProperty.not)

    val addRowAfterButton = createToolBarButton(
      icon = new MaterialDesignIconView(MaterialDesignIcon.TABLE_ROW_PLUS_AFTER, DefaultIconSize),
      tooltipText = "Add new row after",
      action = insertRowAfterAction
    )
    addRowAfterButton.disableProperty().bind(mainContent.hasSelectedTableProperty.not)

    val addRowBeforeButton = createToolBarButton(
      icon = new MaterialDesignIconView(MaterialDesignIcon.TABLE_ROW_PLUS_BEFORE, DefaultIconSize),
      tooltipText = "Add new row before",
      action = insertRowBeforeAction
    )
    addRowBeforeButton.disableProperty().bind(mainContent.hasSelectedTableProperty.not)

    val addColumnAfterButton = createToolBarButton(
      icon = new MaterialDesignIconView(MaterialDesignIcon.TABLE_COLUMN_PLUS_AFTER, DefaultIconSize),
      tooltipText = "Add new column after",
      action = insertColumnAfterAction
    )
    addColumnAfterButton.disableProperty().bind(mainContent.hasSelectedColumnProperty.not)

    val addColumnBeforeButton = createToolBarButton(
      icon = new MaterialDesignIconView(MaterialDesignIcon.TABLE_COLUMN_PLUS_BEFORE, DefaultIconSize),
      tooltipText = "Add new column before",
      action = insertColumnBeforeAction
    )
    addColumnBeforeButton.disableProperty().bind(mainContent.hasSelectedColumnProperty.not)

    new ToolBar {
      items = Seq(
        fileOpenButton,
        newFileButton,
        saveButton,
        Separator(Orientation.Vertical),
        addTableButton,
        addRowAfterButton,
        addRowBeforeButton,
        addColumnAfterButton,
        addColumnBeforeButton
      )
    }
  }

  private def newFileAction(): Unit = {
    newFileChoiceDialog.selectedItem = fileChoices.head
    newFileChoiceDialog.showAndWait() match {
      case Some(value) =>
        if value == "Example" then addTableAction(true)
        else {
          // add new conjugation
        }
      case None => // do nothing
    }
  }

  private def fileOpenAction(): Unit = {
    val file = fileChooser.showOpenDialog(JFXApp3.Stage)
    if Option(file).isEmpty then return
    val path = file.toPath
    Try(toExampleData(path)) match {
      case Failure(ex) => logger.error(s"Failed to load example data from $path", ex)
      case Success(exampleData) =>
        backupFile(path) match {
          case Failure(ex) => logger.error(s"Failed to backup file $path", ex)
          case Success(_)  => // do nothing
        }
        mainContent.action = Action.OpenExampleFile(path, exampleData)
    }
  }

  private def fileSaveAction(): Unit = saveAsAction()

  private def saveAsAction(saveAs: Boolean = false): Unit = {
    val maybeExampleRequest = mainContent.exampleRequest
    val maybeExampleData = maybeExampleRequest.map(_.exampleData)
    val savedPath = maybeExampleRequest.flatMap(_.path)
    val openFileChooserDialog = savedPath.isEmpty || saveAs
    var path = savedPath
    if openFileChooserDialog then {
      val file = fileChooser.showSaveDialog(JFXApp3.Stage)
      if Option(file).isEmpty then return
      path = Option(file.toPath)
    }

    (maybeExampleData, path) match {
      case (Some(exampleData), Some(filePath)) =>
        preferences.initialDirectory = filePath.getParent
        fileChooser.setInitialDirectory(filePath.getParent.toFile)
        saveExampleDataService.executeService(ExampleRequest(Option(filePath), exampleData, openFileChooserDialog))
      case _ => // do nothing
    }

  }

  private def addTableAction(): Unit = addTableAction(false)

  private def addTableAction(newFile: Boolean): Unit = {
    tablePropertiesEditorDialog.reset()
    tablePropertiesEditorDialog.showAndWait() match {
      case Some(Some(tableProperties: TableProperties)) =>
        val tableData = TableData(UUID.randomUUID(), tableProperties)
        if newFile then mainContent.action = Action.NewExampleFile(tableData)
        else mainContent.action = Action.AddTable(tableData)
      case _ => // do nothing
    }
  }

  private def insertRowAfterAction(): Unit = addRow(InsertPosition.After)

  private def insertRowBeforeAction(): Unit = addRow(InsertPosition.Before)

  private def addRow(insertPosition: InsertPosition): Unit = mainContent.action = Action.AddRow(insertPosition)

  private def insertColumnAfterAction(): Unit = addColumn(InsertPosition.After)

  private def insertColumnBeforeAction(): Unit = addColumn(InsertPosition.Before)

  private def addColumn(insertPosition: InsertPosition): Unit = {
    columnEditorDialog.reset()
    columnEditorDialog.showAndWait() match {
      case Some(Some(columnData: ColumnData)) => mainContent.action = Action.AddColumn(columnData, insertPosition)
      case _                                  => // do nothing
    }
  }

  private def exitAction(): Unit = {
    if mainContent.multipleDirty then
      exitConfirmation.showAndWait() match {
        case Some(buttonType) if buttonType.buttonData == ButtonData.No => JFXApp3.Stage.close()
        case _                                                          => // do nothing
      }
    else JFXApp3.Stage.close()
  }
}
