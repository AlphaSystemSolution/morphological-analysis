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
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ Alert, Tab, TabPane }
import scalafx.scene.layout.BorderPane
import scalafx.stage.FileChooser

import java.nio.file.Path

class MorphologicalEngineSkin(control: MorphologicalEngineView) extends SkinBase[MorphologicalEngineView](control) {

  private var untitledProjectCount = 0
  private val fileChooser = new FileChooser() {
    initialDirectory = UserDir.toFile
  }

  private val tabPane = new TabPane() {
    tabClosingPolicy = TabPane.TabClosingPolicy.SelectedTab
    tabs = Seq(createChartTab())
  }

  getChildren.addAll(initializeSkin)

  private def initializeSkin = {
    new BorderPane() {
      center = tabPane
    }
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
      content = view
      onCloseRequest = event => {
        {
          new Alert(Alert.AlertType.Confirmation) {
            contentText = "Do you want to save data before closing?"
          }.showAndWait() match
            case Some(buttonType) if buttonType.buttonData == ButtonData.OKDone =>
            // TODO: Save data
            case _ =>
        }
        event.consume()
      }
    }
  }

  private def getTabTitle(projectFile: Option[Path]) = {
    projectFile match
      case Some(value) => getBaseName(value)
      case None =>
        untitledProjectCount += 1
        s"Untitled $untitledProjectCount"
  }
}

object MorphologicalEngineSkin {
  def apply(control: MorphologicalEngineView) = new MorphologicalEngineSkin(control)
}
