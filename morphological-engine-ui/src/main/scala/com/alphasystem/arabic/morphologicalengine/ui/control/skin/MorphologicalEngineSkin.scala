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
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ Alert, Tab, TabPane }
import scalafx.scene.layout.BorderPane
import scalafx.stage.FileChooser

import java.nio.file.Path

class MorphologicalEngineSkin(control: MorphologicalEngineView) extends SkinBase[MorphologicalEngineView](control) {

  private val fileChooser = new FileChooser() {
    initialDirectory = UserDir.toFile
  }

  private val viewTabs: TabPane = new TabPane() {
    tabClosingPolicy = TabPane.TabClosingPolicy.SelectedTab
    tabs = Seq(createChartTab())
  }

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
          case GlobalAction.Open   => println("Open")
          case GlobalAction.New    => viewTabs.tabs.addOne(createChartTab())
          case GlobalAction.Save   => println("Save")
          case GlobalAction.SaveAs => println("SaveAs")

      case tableAction: TableAction => handleTableAction(tableAction)
  }

  private def createChartTab(
    projectFile: Option[Path] = None,
    conjugationTemplate: ConjugationTemplate = ConjugationTemplate(ChartConfiguration(), Seq.empty)
  ) = {
    val view = MorphologicalChartView()
    view.conjugationTemplate = conjugationTemplate
    projectFile match
      case Some(value) =>
        view.projectName = Some(getBaseName(value))
        view.projectDirectory = value.getParent
      case None =>

    new Tab() {
      text = getTabTitle(projectFile)
      closable = true
      content = view
      onCloseRequest = event => {
        {
          new Alert(Alert.AlertType.Confirmation) {
            contentText = "Do you want to save data before closing?"
          }.showAndWait() match
            case Some(buttonType) if buttonType.buttonData == ButtonData.OKDone =>
            // TODO: Save data
            case _ => event.consume()
        }
      }
    }
  }

  private def getTabTitle(projectFile: Option[Path]) = {
    projectFile match
      case Some(value) => getBaseName(value)
      case None        => "Untitled"
  }

  private def currentTab = {
    val selectedItem = viewTabs.selectionModel.value.getSelectedItem
    if Option(selectedItem).isDefined then Some(selectedItem) else None
  }

  private def currentView = currentTab.map(_.getContent.asInstanceOf[MorphologicalChartView])

  private def handleTableAction(action: TableAction): Unit = currentView.foreach(_.action = action)
}

object MorphologicalEngineSkin {
  def apply(control: MorphologicalEngineView) = new MorphologicalEngineSkin(control)
}
