package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import com.alphasystem.arabic.fx.ui.Browser
import com.alphasystem.arabic.fx.ui.util.*
import com.alphasystem.arabic.model.ArabicLetterType
import com.alphasystem.arabic.morphologicalengine.asciidoc_generator.{saveData, toConjugationTemplate}
import com.alphasystem.arabic.morphologicalengine.conjugation.builder.ConjugationBuilder
import com.alphasystem.arabic.morphologicalengine.conjugation.model.{ OutputFormat, RootLetters }
import com.alphasystem.arabic.morphologicalengine.generator.docx.DocumentBuilder
import com.alphasystem.arabic.morphologicalengine.generator.model.{ ChartConfiguration, ConjugationTemplate }
import com.alphasystem.arabic.morphologicalengine.ui.control.MorphologicalChartViewerView
import com.alphasystem.arabic.morphologicalengine.ui.control.skin.MorphologicalEngineSkin.getMawridReaderUrl
import javafx.concurrent.{ Task, Service as JService }
import javafx.scene.control.SkinBase
import org.controlsfx.control.Notifications
import scalafx.Includes.*
import scalafx.application.Platform
import scalafx.beans.property.IntegerProperty
import scalafx.concurrent.Service
import scalafx.geometry.Pos
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ Alert, ButtonType, Tab, TabPane }
import scalafx.scene.layout.BorderPane
import scalafx.stage.FileChooser

import java.nio.file.{ Files, Path }
import scala.util.{ Failure, Success, Try }

class MorphologicalEngineSkin(control: MorphologicalEngineView) extends SkinBase[MorphologicalEngineView](control) {

  private lazy val dialog = ChartConfigurationDialog()
  private val openedTabs = IntegerProperty(0)
  private val browser = Browser()
  private lazy val viewerView = MorphologicalChartViewerView()
  private val conjugationBuilder = ConjugationBuilder()

  // Tracks the most recently active *project* tab's view, independent of whichever tab (including the
  // non-project Preview/Dictionary tabs) is currently selected, so the Preview tab always reflects the last
  // project the user was working on.
  private var activeView: Option[MorphologicalChartView] = None

  private val fileChooser = new FileChooser() {
    initialDirectory = UserDir.toFile
    extensionFilters.addOne(new FileChooser.ExtensionFilter("Yaml files", Seq("*.yaml", "*.yml")))
  }

  private val viewTabs: TabPane = new TabPane() {
    tabClosingPolicy = TabPane.TabClosingPolicy.Unavailable
    tabs = Seq(createChartTab(), addPreviewTab(), addDictionaryTab())
  }

  // Defensive fallback in case the default first tab's `selectedProperty` listener didn't fire on startup.
  if activeView.isEmpty then activeView = currentView

  loadDictionary()

  openedTabs.onChange((_, _, nv) => {
    val tabClosingPolicy =
      if nv.intValue() > 1 then TabPane.TabClosingPolicy.SelectedTab
      else TabPane.TabClosingPolicy.Unavailable

    viewTabs.tabClosingPolicy = tabClosingPolicy
  })

  control.actionProperty.onChange((_, _, nv) => handleActions(nv))

  getChildren.addAll(initializeSkin)

  private def initializeSkin = {
    new BorderPane() {
      center = viewTabs
    }
  }

  private def handleActions(action: Action): Unit = {
    action match
      case globalAction: GlobalAction =>
        globalAction match
          case GlobalAction.None   => // do nothing
          case GlobalAction.Open   => openAction()
          case GlobalAction.New    => newAction()
          case GlobalAction.Save   => saveAction(false)
          case GlobalAction.SaveAs => saveAction(true)
          case GlobalAction.Export => exportAction()

      case tableAction: TableAction => handleTableAction(tableAction)
  }

  private def createChartTab(
    projectFile: Option[Path] = None,
    conjugationTemplate: ConjugationTemplate = ConjugationTemplate(id = "", ChartConfiguration(), Seq.empty)
  ) = {
    incrementOpenTabs()
    val view = MorphologicalChartView()
    view.conjugationTemplate = conjugationTemplate
    view.projectFile = projectFile

    view
      .viewDictionaryProperty
      .onChange((_, _, nv) => {
        if Option(nv).isDefined then {
          loadDictionary(nv)
          Platform.runLater(() => viewTabs.selectionModel.value.select(viewTabs.tabs.size - 1))
        }
      })
    view.selectedInputProperty.onChange((_, _, _) => if isActiveView(view) then refreshPreview())
    view.conjugationTemplateProperty.onChange((_, _, _) => if isActiveView(view) then refreshPreview())
    view
      .previewInputProperty
      .onChange((_, _, nv) => {
        if Option(nv).isDefined then
          Platform.runLater(() => viewTabs.selectionModel.value.select(viewTabs.tabs.size - 2))
      })

    val tab =
      new Tab() {
        text = view.projectName
        closable = true
        content = view
        onCloseRequest = event => {
          if view.hasUnsavedChanges then {
            saveConfirmationDialog.showAndWait() match
              case Some(buttonType) if buttonType.buttonData == ButtonData.OKDone =>
                Platform.runLater(() => saveAction(false))
                decrementOpenTabs()

              case Some(buttonType) if buttonType.buttonData == ButtonData.Apply =>
                // close without saving
                decrementOpenTabs()

              case _ => event.consume()
          } else decrementOpenTabs()
        }
      }

    tab
      .selectedProperty()
      .onChange((_, _, nv) => {
        if nv then {
          control.transientProjectWrapperProperty.value = view.transientProject
          activeView = Some(view)
          refreshPreview()
        }
      })
    tab.textProperty().bind(view.projectNameProperty)
    tab
  }

  private def isActiveView(view: MorphologicalChartView): Boolean = activeView.contains(view)

  private def incrementOpenTabs(): Unit = openedTabs.value = openedTabs.value + 1
  private def decrementOpenTabs(): Unit = openedTabs.value = openedTabs.value - 1

  private def currentTab = Option(viewTabs.selectionModel.value.getSelectedItem)

  private def currentView: Option[MorphologicalChartView] =
    currentTab.flatMap(_.getContent match {
      case view: MorphologicalChartView => Some(view)
      case _                            => None
    })

  private def handleTableAction(action: TableAction): Unit = currentView.foreach(_.action = action)

  private def addTab(
    projectFile: Option[Path] = None,
    conjugationTemplate: ConjugationTemplate = ConjugationTemplate("", ChartConfiguration(), Seq.empty)
  ): Unit = {
    viewTabs.tabs.insert(viewTabs.tabs.size - 2, createChartTab(projectFile, conjugationTemplate))
    viewTabs.selectionModel.value.select(viewTabs.tabs.size - 3)
  }

  private def addPreviewTab() = {
    new Tab() {
      text = "Preview"
      userData = "preview"
      closable = false
      content = viewerView
    }
  }

  private def addDictionaryTab() = {
    browser.setDisable(true)
    new Tab() {
      text = "Dictionary"
      userData = "dictionary"
      closable = false
      content = browser
    }
  }

  private def refreshPreview(): Unit = {
    activeView.flatMap(_.selectedInput) match
      case None =>
        viewerView.morphologicalChart = None
        viewerView.error = None

      case Some(input) =>
        val chartConfiguration = activeView.get.conjugationTemplate.chartConfiguration
        Try(
          conjugationBuilder.doConjugation(
            input = input,
            outputFormat = OutputFormat.Unicode,
            removeAdverbs = chartConfiguration.removeAdverbs,
            showAbbreviatedConjugation = chartConfiguration.showAbbreviatedConjugation,
            showDetailedConjugation = chartConfiguration.showDetailedConjugation
          )
        ) match
          case Success(chart) =>
            viewerView.morphologicalChart = Some(chart)
            viewerView.error = None

          case Failure(ex) =>
            viewerView.morphologicalChart = None
            viewerView.error = Some(Option(ex.getMessage).getOrElse(ex.toString))
  }

  private def newAction(): Unit = addTab()

  private def openAction(): Unit = {
    val file = fileChooser.showOpenDialog(control.getScene.getWindow)
    val maybePath = Option(file).map(_.toPath)
    if maybePath.isDefined && maybePath.exists(Files.exists(_)) then {
      val path = maybePath.get

      val hasPath =
        viewTabs
          .tabs
          .filterNot { tab =>
            val userData = tab.userData
            Option(userData).isDefined && userData.isInstanceOf[String]
          }
          .map(_.getContent.asInstanceOf[MorphologicalChartView])
          .collect { case view if view.projectFile.isDefined => view.projectFile.get }
          .toList
          .contains(path)

      if hasPath then {
        new Alert(Alert.AlertType.Information) {
          contentText = "Chart is already open."
        }.showAndWait()
      } else loadTemplate(path)
    }
  }

  private def loadTemplate(path: Path): Unit = {
    val service = new Service[ConjugationTemplate](new JService[ConjugationTemplate] {
      override def createTask(): Task[ConjugationTemplate] = {
        new Task[ConjugationTemplate]():
          override def call(): ConjugationTemplate = {
            Try(toConjugationTemplate(path)) match
              case Failure(ex)                  => throw ex
              case Success(conjugationTemplate) => conjugationTemplate
          }
      }
    }) {}

    service.onSucceeded = event => {
      val conjugationTemplate = event.getSource.getValue.asInstanceOf[ConjugationTemplate]
      addTab(Some(path), conjugationTemplate)
      event.consume()
    }
    service.onFailed = event => {
      event.getSource.getException.printStackTrace()
      event.consume()
    }
    service.start()
  }

  private def saveAction(saveAs: Boolean): Unit = {
    val showFileChooser = saveAs || currentView.exists(view => view.transientProject && view.hasUnsavedChanges)
    if showFileChooser then {
      val maybePath = Option(fileChooser.showSaveDialog(control.getScene.getWindow)).map(_.toPath)
      maybePath match
        case Some(path) =>
          currentView.foreach { view =>
            view.action = TableAction.None
            view.action = TableAction.GetData
            Try {
              saveData(view.conjugationTemplate, path)
              view.projectFile = Some(path)
              view.hasUnsavedChanges = false
            }
          }

        case None => // do nothing
    }
  }

  private def exportAction(): Unit = {
    Platform.runLater(() => {
      dialog.showAndWait() match
        case Some(Some(chartConfiguration: ChartConfiguration)) => exportToWordService(chartConfiguration)
        case _                                                  =>
    })
  }

  private def exportToWordService(chartConfiguration: ChartConfiguration): Unit = {
    val service =
      new Service[Path](new JService[Path] {
        override def createTask(): Task[Path] = {
          new Task[Path]():
            override def call(): Path = {
              currentView match
                case Some(view) =>
                  view.action = TableAction.None
                  view.action = TableAction.GetData
                  val conjugationTemplate = view.conjugationTemplate

                  val path = toDocFile(view.projectFile.get)
                  val documentBuilder = DocumentBuilder(
                    chartConfiguration = chartConfiguration,
                    path = path,
                    inputs = conjugationTemplate.inputs*
                  )

                  documentBuilder.generateDocument()
                  path

                case None => throw new RuntimeException("Current view is null")
            }
        }
      }) {}

    service.onSucceeded = event => {
      val path = event.getSource.getValue.asInstanceOf[Path]
      exportNotification(path)
      event.consume()
    }
    service.onFailed = event => {
      event.getSource.getException.printStackTrace()
      event.consume()
    }
    service.start()
  }

  private lazy val saveConfirmationDialog = {
    val buttonTypeOkDone = new ButtonType("Save & close", ButtonData.OKDone)
    val buttonTypeApply = new ButtonType("Close (without saving)", ButtonData.Apply)
    val buttonTypeCancel = new ButtonType("Cancel", ButtonData.CancelClose)

    new Alert(AlertType.Confirmation) {
      initOwner(control.getScene.getWindow)
      contentText = "Do you want to save data before closing?"
      buttonTypes = Seq(buttonTypeCancel, buttonTypeApply, buttonTypeOkDone)
    }
  }

  private def exportNotification(path: Path): Unit = {
    Notifications
      .create()
      .position(Pos.TopRight)
      .title("Chart exported")
      .text(s"File ${path.toAbsolutePath.toString} has been created.")
      .showInformation()
  }

  private def loadDictionary(
    rootLetters: RootLetters = RootLetters(
      firstRadical = ArabicLetterType.Fa,
      secondRadical = ArabicLetterType.Ain,
      thirdRadical = ArabicLetterType.Lam
    )
  ): Unit = {
    val service =
      new Service[Unit](new JService[Unit] {
        override def createTask(): Task[Unit] =
          new Task[Unit]():
            override def call(): Unit =
              Platform.runLater(() => browser.loadUrl(getMawridReaderUrl(rootLetters.buckWalterString)))
      }) {}
    service.onSucceeded = event => {
      event.consume()
    }
    service.onFailed = event => {
      event.getSource.getException.printStackTrace()
      event.consume()
    }
    service.start()
  }
}

object MorphologicalEngineSkin {

  private val DictionaryUrl = "https://ejtaal.net/aa/index.html#bwq="

  private def normalizeDictionaryQuery(query: String): String =
    if query.startsWith("'") then s"a${query.drop(1)}" else query

  def apply(control: MorphologicalEngineView) = new MorphologicalEngineSkin(control)

  private def getMawridReaderUrl(query: String): String =
    s"$DictionaryUrl${normalizeDictionaryQuery(query)}"
}
