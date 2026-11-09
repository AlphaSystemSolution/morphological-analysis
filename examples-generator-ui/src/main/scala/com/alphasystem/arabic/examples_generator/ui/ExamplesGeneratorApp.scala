package com.alphasystem
package arabic
package examples_generator
package ui

import ui.control.column.ColumnEditorDialog
import ui.control.table.TablePropertiesEditorDialog
import ui.service.SaveExampleDataService
import arabic.fx.ui.util.*
import ui.control.conjugations.paired.PairedConjugationEditorDialog
import examples_generator.morphologicalchart.{ PairedConjugation, SingleConjugation, toConjugations }
import ui.control.conjugations.single.SingleConjugationEditorDialog
import ui.control.examples_view.ExamplesView
import ui.model.{ Action, FileType, InsertPosition, SaveFileRequest }
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
import scalafx.scene.control.{ Alert, ButtonType, ChoiceDialog, Separator, TextInputDialog, ToolBar }
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
  private lazy val singleConjugationEditorDialog = SingleConjugationEditorDialog()
  private lazy val pairedConjugationEditorDialog = PairedConjugationEditorDialog()
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
    accelerators.put(new KeyCodeCombination(KeyCode.N, KeyCombination.MetaDown), () => newFileButton.fire())
    accelerators.put(new KeyCodeCombination(KeyCode.O, KeyCombination.MetaDown), () => fileOpenButton.fire())
    accelerators.put(new KeyCodeCombination(KeyCode.S, KeyCombination.MetaDown), () => saveButton.fire())
    accelerators.put(new KeyCodeCombination(KeyCode.T, KeyCombination.MetaDown), () => addTableButton.fire())
    accelerators.put(new KeyCodeCombination(KeyCode.R, KeyCombination.MetaDown), () => addRowAfterButton.fire())
    accelerators.put(
      new KeyCodeCombination(KeyCode.R, KeyCombination.MetaDown, KeyCombination.ShiftDown),
      () => addRowBeforeButton.fire()
    )
    accelerators.put(new KeyCodeCombination(KeyCode.C, KeyCombination.MetaDown), () => addColumnAfterButton.fire())
    accelerators.put(
      new KeyCodeCombination(KeyCode.C, KeyCombination.MetaDown, KeyCombination.ShiftDown),
      () => addColumnBeforeButton.fire()
    )
    accelerators.put(
      new KeyCodeCombination(KeyCode.Digit1, KeyCombination.MetaDown),
      () => singleConjugationButton.fire()
    )
    accelerators.put(
      new KeyCodeCombination(KeyCode.Digit2, KeyCombination.MetaDown),
      () => pairedConjugationButton.fire()
    )
    accelerators.put(
      new KeyCodeCombination(KeyCode.Digit3, KeyCombination.MetaDown),
      () => fullConjugationButton.fire()
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

  private lazy val fileOpenButton = createToolBarButton(
    icon = new FontAwesomeIconView(FontAwesomeIcon.FOLDER_OPEN, DefaultIconSize),
    tooltipText = "Open file",
    action = fileOpenAction
  )

  private lazy val newFileButton = createToolBarButton(
    icon = new FontAwesomeIconView(FontAwesomeIcon.FILE, DefaultIconSize),
    tooltipText = "Create new file",
    action = newFileAction
  )

  private lazy val saveButton = {
    val button = createToolBarButton(
      icon = new FontAwesomeIconView(FontAwesomeIcon.SAVE, DefaultIconSize),
      tooltipText = "Save file",
      action = fileSaveAction
    )
    button.disable = true
    button.disableProperty().bind(mainContent.dirtyProperty.not)
    button
  }

  private lazy val addTableButton = {
    val button = createToolBarButton(
      icon = new MaterialDesignIconView(MaterialDesignIcon.TABLE, DefaultIconSize),
      tooltipText = "Create new table",
      action = addTableAction
    )
    button.disableProperty().bind(mainContent.hasSelectedTableProperty.not)
    button
  }

  private lazy val addRowAfterButton = {
    val button = createToolBarButton(
      icon = new MaterialDesignIconView(MaterialDesignIcon.TABLE_ROW_PLUS_AFTER, DefaultIconSize),
      tooltipText = "Add new row after",
      action = insertRowAfterAction
    )
    button.disableProperty().bind(mainContent.hasSelectedTableProperty.not)
    button
  }

  private lazy val addRowBeforeButton = {
    val button = createToolBarButton(
      icon = new MaterialDesignIconView(MaterialDesignIcon.TABLE_ROW_PLUS_BEFORE, DefaultIconSize),
      tooltipText = "Add new row before",
      action = insertRowBeforeAction
    )
    button.disableProperty().bind(mainContent.hasSelectedTableProperty.not)
    button
  }

  private lazy val addColumnAfterButton = {
    val button = createToolBarButton(
      icon = new MaterialDesignIconView(MaterialDesignIcon.TABLE_COLUMN_PLUS_AFTER, DefaultIconSize),
      tooltipText = "Add new column after",
      action = insertColumnAfterAction
    )
    button.disableProperty().bind(mainContent.hasSelectedColumnProperty.not)
    button
  }

  private lazy val addColumnBeforeButton = {
    val button = createToolBarButton(
      icon = new MaterialDesignIconView(MaterialDesignIcon.TABLE_COLUMN_PLUS_BEFORE, DefaultIconSize),
      tooltipText = "Add new column before",
      action = insertColumnBeforeAction
    )
    button.disableProperty().bind(mainContent.hasSelectedColumnProperty.not)
    button
  }

  private lazy val singleConjugationButton = {
    val button = createToolBarButton(
      icon = new MaterialDesignIconView(MaterialDesignIcon.NUMERIC_1_BOX_MULTIPLE_OUTLINE, DefaultIconSize),
      tooltipText = "Add chart for single morphological type",
      action = singleConjugationAction
    )
    button.disable = true
    button.disableProperty().bind(mainContent.fileTypeProperty.isNotEqualTo(FileType.Conjugation))
    button
  }

  private lazy val pairedConjugationButton = {
    val button = createToolBarButton(
      icon = new MaterialDesignIconView(MaterialDesignIcon.NUMERIC_2_BOX_MULTIPLE_OUTLINE, DefaultIconSize),
      tooltipText = "Add chart for two morphological types",
      action = pairedConjugationAction
    )
    button.disable = true
    button.disableProperty().bind(mainContent.fileTypeProperty.isNotEqualTo(FileType.Conjugation))
    button
  }

  private lazy val fullConjugationButton = {
    val button = createToolBarButton(
      icon = new MaterialDesignIconView(MaterialDesignIcon.TABLE_LARGE, DefaultIconSize),
      tooltipText = "Add full morphological chart",
      action = fullConjugationAction
    )
    button.disable = true
    button.disableProperty().bind(mainContent.fileTypeProperty.isNotEqualTo(FileType.Conjugation))
    button
  }

  private lazy val createToolBar =
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
        addColumnBeforeButton,
        Separator(Orientation.Vertical),
        singleConjugationButton,
        pairedConjugationButton,
        fullConjugationButton
      )
    }

  private def newFileAction(): Unit = {
    newFileChoiceDialog.selectedItem = fileChoices.head
    newFileChoiceDialog.showAndWait() match {
      case Some(value) =>
        if value == "Example" then addTableAction(true)
        else mainContent.action = Action.NewConjugationFile
      case None => // do nothing
    }
  }

  private def fileOpenAction(): Unit = {
    val file = fileChooser.showOpenDialog(JFXApp3.Stage)
    if Option(file).isEmpty then return
    val path = file.toPath
    Try(toExampleData(path)) match {
      case Failure(_) =>
        Try(toConjugations(path)) match {
          case Failure(ex) => logger.error(s"Failed to load example data from $path", ex)
          case Success(conjugations) =>
            backupFile(path) match {
              case Failure(ex) => logger.error(s"Failed to backup file $path", ex)
              case Success(_)  => mainContent.action = Action.OpenConjugationsFile(path, conjugations)
            }
        }
      case Success(exampleData) =>
        backupFile(path) match {
          case Failure(ex) => logger.error(s"Failed to backup file $path", ex)
          case Success(_)  => mainContent.action = Action.OpenExampleFile(path, exampleData)
        }
    }
  }

  private def fileSaveAction(): Unit = saveAsAction()

  private def saveAsAction(saveAs: Boolean = false): Unit = {
    val fileType = mainContent.fileType
    mainContent.fileType match {
      case FileType.Example =>
        val maybeExampleRequest = mainContent.exampleRequest
        maybeExampleRequest.map(_.exampleData) match {
          case Some(exampleData) =>
            val savedPath = maybeExampleRequest.flatMap(_.path)
            val openFileChooserDialog = savedPath.isEmpty || saveAs
            val maybePath = if openFileChooserDialog then selectFile(savedPath) else savedPath
            maybePath match {
              case Some(filePath) =>
                val parentFile = filePath.getParent
                preferences.initialDirectory = parentFile
                fileChooser.setInitialDirectory(parentFile.toFile)
                saveExampleDataService.executeService(
                  SaveFileRequest(
                    path = filePath,
                    exampleData = Some(exampleData),
                    newFileSaved = openFileChooserDialog
                  )
                )
              case None => logger.debug("No example file is selected")
            }
          case None => logger.warn("No data is available")
        }

      case FileType.Conjugation =>
        val conjugationsRequest = mainContent.conjugationsRequest
        if Option(conjugationsRequest).isDefined then {
          val conjugations = conjugationsRequest.conjugations
          val savedPath = conjugationsRequest.path
          val openFileChooserDialog = savedPath.isEmpty || saveAs
          val maybePath = if openFileChooserDialog then selectFile(savedPath) else savedPath
          maybePath match {
            case Some(filePath) =>
              val parentFile = filePath.getParent
              preferences.initialDirectory = parentFile
              fileChooser.setInitialDirectory(parentFile.toFile)
              saveExampleDataService.executeService(
                SaveFileRequest(
                  path = filePath,
                  conjugations = Some(conjugations),
                  newFileSaved = openFileChooserDialog
                )
              )
            case None => logger.debug("No conjugations file is selected")
          }
        }
      case _ => // do nothing
    }
  }

  private def selectFile(maybePath: Option[Path]) = {
    val file = fileChooser.showSaveDialog(JFXApp3.Stage)
    if Option(file).isDefined then Option(file.toPath) else None
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

  private def singleConjugationAction(): Unit = {
    singleConjugationEditorDialog.reset()
    singleConjugationEditorDialog.showAndWait() match {
      case Some(Some(singleConjugation: SingleConjugation)) =>
        println(singleConjugation)
        mainContent.action = Action.AddSingleConjugation(singleConjugation)
      case _ => // do nothing
    }
  }

  private def pairedConjugationAction(): Unit = {
    pairedConjugationEditorDialog.reset()
    pairedConjugationEditorDialog.showAndWait() match {
      case Some(Some(pairedConjugation: PairedConjugation)) =>
        mainContent.action = Action.AddPairedConjugation(pairedConjugation)
      case _ => // do nothing
    }
  }

  private def fullConjugationAction(): Unit = {
    val dialog = new TextInputDialog() {
      title = "Full Morphological Chart"
      headerText = "Create full morphological chart"
      contentText = "Enter the name of the tag:"
    }

    dialog.dialogPane().lookupButton(ButtonType.OK).disableProperty().bind(dialog.editor.textProperty().isEmpty)

    dialog.showAndWait() match {
      case Some(tag) => mainContent.action = Action.CreateFullConjugationChart(tag)
      case None      => // do nothing
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
