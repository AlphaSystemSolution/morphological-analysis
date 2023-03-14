package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import arabic.fx.ui.Browser
import arabic.fx.ui.util.*
import arabic.model.ArabicLetterType
import morphologicalengine.conjugation.model.RootLetters
import morphologicalengine.generator.model.{ ChartConfiguration, ConjugationTemplate }
import morphologicalengine.ui.control.skin.MorphologicalEngineSkin.getMawridReaderUrl
import javafx.concurrent.{ Task, Service as JService }
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.application.Platform
import scalafx.beans.property.IntegerProperty
import scalafx.concurrent.Service
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ Alert, ButtonType, Tab, TabPane }
import scalafx.scene.layout.BorderPane
import scalafx.stage.FileChooser

import java.nio.file.{ Files, Path }
import scala.util.{ Failure, Success, Try }

class MorphologicalEngineSkin(control: MorphologicalEngineView) extends SkinBase[MorphologicalEngineView](control) {

  private val openedTabs = IntegerProperty(0)
  private val browser = Browser()

  private val fileChooser = new FileChooser() {
    initialDirectory = UserDir.toFile
    extensionFilters.addOne(new FileChooser.ExtensionFilter("Json files", Seq("*.json")))
  }

  private val viewTabs: TabPane = new TabPane() {
    tabClosingPolicy = TabPane.TabClosingPolicy.Unavailable
    tabs = Seq(createChartTab(), addDictionaryTab())
  }

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

      case tableAction: TableAction => handleTableAction(tableAction)
  }

  private def createChartTab(
    projectFile: Option[Path] = None,
    conjugationTemplate: ConjugationTemplate = ConjugationTemplate(ChartConfiguration(), Seq.empty)
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
          viewTabs.selectionModel.value.select(viewTabs.tabs.size - 1)
        }
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

    tab.textProperty().bind(view.projectNameProperty)
    tab
  }

  private def incrementOpenTabs(): Unit = openedTabs.value = openedTabs.value + 1
  private def decrementOpenTabs(): Unit = openedTabs.value = openedTabs.value - 1

  private def currentTab = Option(viewTabs.selectionModel.value.getSelectedItem)

  private def currentView = currentTab.map(_.getContent.asInstanceOf[MorphologicalChartView])

  private def handleTableAction(action: TableAction): Unit = currentView.foreach(_.action = action)

  private def addTab(
    projectFile: Option[Path] = None,
    conjugationTemplate: ConjugationTemplate = ConjugationTemplate(ChartConfiguration(), Seq.empty)
  ): Unit = {
    viewTabs.tabs.insert(viewTabs.tabs.size - 1, createChartTab(projectFile, conjugationTemplate))
    viewTabs.selectionModel.value.select(viewTabs.tabs.size - 2)
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
      } else {
        Try(toConjugationTemplate(path)) match
          case Failure(ex) =>
            ex.printStackTrace()
            new Alert(Alert.AlertType.Error) {
              contentText =
                s"Error opening file (${path.toAbsolutePath.toString})${System.lineSeparator()}    ${ex.getMessage}"
            }.showAndWait()

          case Success(conjugationTemplate) => addTab(Some(path), conjugationTemplate)
      }
    }
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

  def apply(control: MorphologicalEngineView) = new MorphologicalEngineSkin(control)

  private def getMawridReaderUrl(query: String): String = s"$DictionaryUrl$query"
}
