package com.alphasystem
package arabic
package examples_generator
package ui

import ui.control.file.ExampleFileGeneratorView
import ui.control.table.TablePropertiesEditorDialog
import ui.model.{ Action, ColumnData, ExampleRequest, Row, TableData, TableProperties }
import arabic.fx.ui.util.*
import com.alphasystem.arabic.examples_generator.ui.control.column.ColumnEditorDialog
import com.alphasystem.arabic.examples_generator.ui.service.SaveExampleDataService
import de.jensd.fx.glyphs.fontawesome.{ FontAwesomeIcon, FontAwesomeIconView }
import de.jensd.fx.glyphs.materialdesignicons.{ MaterialDesignIcon, MaterialDesignIconView }
import org.slf4j.LoggerFactory
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.geometry.{ Orientation, Pos }
import scalafx.scene.Scene
import scalafx.scene.control.{ Separator, ToolBar }
import scalafx.scene.input.{ KeyCode, KeyCodeCombination, KeyCombination }
import scalafx.scene.layout.{ BorderPane, VBox }
import scalafx.stage.FileChooser.ExtensionFilter
import scalafx.stage.{ FileChooser, Screen }

import java.nio.file.Path
import java.util.UUID
import scala.util.{ Failure, Success, Try }

object ExamplesGeneratorApp extends JFXApp3 {

  private val logger = LoggerFactory.getLogger(getClass)
  private lazy val tablePropertiesEditorDialog = TablePropertiesEditorDialog()
  private lazy val columnEditorDialog = ColumnEditorDialog()
  private lazy val mainContent = ExampleFileGeneratorView()
  private lazy val saveExampleDataService = SaveExampleDataService(mainContent)
  private lazy val fileChooser = {
    val fc = new FileChooser {
      initialDirectory = preferences.initialDirectory.toFile
    }
    fc.extensionFilters.addAll(new ExtensionFilter("YAML file", "*.yml"), new ExtensionFilter("YAML file", "*.yaml"))
    fc
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
    accelerators.put(new KeyCodeCombination(KeyCode.N, KeyCombination.MetaDown), () => addTableAction())
    accelerators.put(new KeyCodeCombination(KeyCode.O, KeyCombination.MetaDown), () => fileOpenAction())
    accelerators.put(new KeyCodeCombination(KeyCode.S, KeyCombination.MetaDown), () => fileSaveAction())
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
      action = addTableAction
    )

    val saveButton = createToolBarButton(
      icon = new FontAwesomeIconView(FontAwesomeIcon.SAVE, DefaultIconSize),
      tooltipText = "Save file",
      action = fileSaveAction
    )
    saveButton.disableProperty().bind(mainContent.selectedTableProperty.isNull)

    val addTableButton = createToolBarButton(
      icon = new MaterialDesignIconView(MaterialDesignIcon.TABLE, DefaultIconSize),
      tooltipText = "Create new table",
      action = addTableAction
    )
    addTableButton.disableProperty().bind(mainContent.selectedTableProperty.isNull)

    val addRowButton = createToolBarButton(
      icon = new MaterialDesignIconView(MaterialDesignIcon.TABLE_ROW_PLUS_AFTER, DefaultIconSize),
      tooltipText = "Add new row",
      action = addRowAction
    )
    addRowButton.disableProperty().bind(mainContent.selectedTableProperty.isNull)

    val addColumnAfterButton = createToolBarButton(
      icon = new MaterialDesignIconView(MaterialDesignIcon.TABLE_COLUMN_PLUS_AFTER, DefaultIconSize),
      tooltipText = "Add new column after",
      action = addColumnAction
    )
    addColumnAfterButton.disableProperty().bind(mainContent.selectedColumnProperty.isNull)

    new ToolBar {
      items = Seq(
        fileOpenButton,
        newFileButton,
        saveButton,
        Separator(Orientation.Vertical),
        addTableButton,
        addRowButton,
        addColumnAfterButton
      )
    }
  }

  private def fileOpenAction(): Unit = {
    val file = fileChooser.showOpenDialog(JFXApp3.Stage)
    if Option(file).isEmpty then return
    val path = file.toPath
    Try(model.toExampleData(path)) match {
      case Failure(ex) => logger.error(s"Failed to load example data from $path", ex)
      case Success(exampleData) =>
        mainContent.addExampleData(exampleData)
        mainContent.filePath = path
        mainPanel.center = mainContent
    }
  }

  private def fileSaveAction(): Unit = saveAsAction()

  private def saveAsAction(saveAs: Boolean = false): Unit = {
    val savedPath = mainContent.filePath
    val openFileChooserDialog = Option(savedPath).isEmpty || saveAs
    var path: Option[Path] = None
    if openFileChooserDialog then {
      val file = fileChooser.showSaveDialog(JFXApp3.Stage)
      if Option(file).isEmpty then return
      path = Option(file.toPath)
    }

    path = if path.isEmpty then savedPath else path

    path match {
      case Some(filePath) =>
        preferences.initialDirectory = filePath.getParent
        fileChooser.setInitialDirectory(filePath.getParent.toFile)
        saveExampleDataService.executeService(ExampleRequest(filePath, mainContent.toExampleData))
      case None => // do nothing
    }

  }

  private def addTableAction(): Unit =
    tablePropertiesEditorDialog.showAndWait() match {
      case Some(Some(tableProperties: TableProperties)) =>
        mainContent.addTableData(TableData(UUID.randomUUID(), tableProperties))
        mainContent.selectTable(tableProperties.tag)
        mainPanel.center = mainContent
      case _ => // do nothing
    }

  private def addRowAction(): Unit = mainContent.action = Action.AddRow(Row(UUID.randomUUID()))

  private def addColumnAction(): Unit = {
    columnEditorDialog.reset()
    columnEditorDialog.showAndWait() match {
      case Some(Some(columnData: ColumnData)) => mainContent.action = Action.AddColumn(columnData)
      case _                                  => // do nothing
    }
  }

  private def exitAction(): Unit = JFXApp3.Stage.close()
}
