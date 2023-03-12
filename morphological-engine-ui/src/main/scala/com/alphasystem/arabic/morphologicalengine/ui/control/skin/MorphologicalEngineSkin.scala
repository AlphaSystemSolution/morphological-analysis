package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import arabic.fx.ui.util.*
import morphologicalengine.generator.model.{ ChartConfiguration, ConjugationTemplate }
import scalafx.Includes.*
import javafx.scene.control.SkinBase
import scalafx.application.Platform
import scalafx.beans.property.IntegerProperty
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ Alert, Tab, TabPane }
import scalafx.scene.layout.BorderPane
import scalafx.stage.FileChooser

import java.nio.file.{ Files, Path }
import scala.util.{ Failure, Success, Try }

class MorphologicalEngineSkin(control: MorphologicalEngineView) extends SkinBase[MorphologicalEngineView](control) {

  private val openedTabs = IntegerProperty(0)

  private val fileChooser = new FileChooser() {
    initialDirectory = UserDir.toFile
    extensionFilters.addOne(new FileChooser.ExtensionFilter("Json files", Seq("*.json")))
  }

  private val viewTabs: TabPane = new TabPane() {
    tabClosingPolicy = TabPane.TabClosingPolicy.Unavailable
    tabs = Seq(createChartTab())
  }

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
          case GlobalAction.Save   => println("Save")
          case GlobalAction.SaveAs => println("SaveAs")

      case tableAction: TableAction => handleTableAction(tableAction)
  }

  private def createChartTab(
    projectFile: Option[Path] = None,
    conjugationTemplate: ConjugationTemplate = ConjugationTemplate(ChartConfiguration(), Seq.empty)
  ) = {
    openedTabs.value = openedTabs.value + 1
    val view = MorphologicalChartView()
    view.conjugationTemplate = conjugationTemplate
    view.projectFile = projectFile

    val tab =
      new Tab() {
        text = view.projectName
        closable = true
        content = view
        onCloseRequest = event => {
          {
            new Alert(Alert.AlertType.Confirmation) {
              contentText = "Do you want to save data before closing?"
            }.showAndWait() match
              case Some(buttonType) if buttonType.buttonData == ButtonData.OKDone =>
                // TODO: Save data
                openedTabs.value = openedTabs.value - 1
              case _ => event.consume()
          }
        }
      }

    tab.textProperty().bind(view.projectNameProperty)
    tab
  }

  private def currentTab = {
    val selectedItem = viewTabs.selectionModel.value.getSelectedItem
    if Option(selectedItem).isDefined then Some(selectedItem) else None
  }

  private def currentView = currentTab.map(_.getContent.asInstanceOf[MorphologicalChartView])

  private def handleTableAction(action: TableAction): Unit = currentView.foreach(_.action = action)

  private def addTab(
    projectFile: Option[Path] = None,
    conjugationTemplate: ConjugationTemplate = ConjugationTemplate(ChartConfiguration(), Seq.empty)
  ): Unit = {
    viewTabs.tabs.addOne(createChartTab(projectFile, conjugationTemplate))
    viewTabs.selectionModel.value.select(viewTabs.tabs.size - 1)
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
}

object MorphologicalEngineSkin {
  def apply(control: MorphologicalEngineView) = new MorphologicalEngineSkin(control)
}
