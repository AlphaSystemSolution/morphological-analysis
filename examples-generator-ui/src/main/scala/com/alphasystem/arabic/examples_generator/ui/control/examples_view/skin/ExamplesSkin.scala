package com.alphasystem
package arabic
package examples_generator
package ui
package control
package examples_view
package skin

import ui.model.Action.NoAction
import arabic.fx.ui.util.defaultCursor
import examples_generator.model.ExampleData
import control.file.ExampleFileGeneratorView
import ui.model.{ Action, ExampleRequest }
import javafx.scene.control.SkinBase
import scalafx.scene.control.{ Tab, TabPane }
import scalafx.scene.layout.BorderPane
import scalafx.Includes.*
import scalafx.application.Platform

import java.nio.file.Path
import java.util.UUID

class ExamplesSkin private (control: ExamplesView) extends SkinBase[ExamplesView](control) {

  private var untitledCount = 0
  private val tabPane = new TabPane()

  tabPane
    .selectionModel
    .value
    .selectedItemProperty()
    .onChange((_, _, nv) => {
      if Option(nv).isDefined then {
        nv.getContent match {
          case mainContent: ExampleFileGeneratorView =>
            bind(UUID.fromString(nv.userData.asInstanceOf[String]), mainContent)
          case _ => // do nothing
        }
      } else unbind()
    })

  control
    .actionProperty
    .onChange((_, _, nv) => {
      nv match {
        case Action.NoAction => // do nothing
        case Action.OpenExampleFile(filePath, exampleData) =>
          val tabId = createTabId(filePath)
          tabPane.getTabs.find(_.userData == tabId.toString) match {
            case Some(tab) => tabPane.getSelectionModel.select(tab)
            case None      => addTab(tabId, Some(filePath), exampleData)
          }

        case Action.SaveExampleFile(ExampleRequest(Some(path), _, newFileSaved)) =>
          val currentTab = tabPane.selectionModel.value.getSelectedItem
          val content = currentTab.content.value
          content match {
            case mainContent: ExampleFileGeneratorView =>
              mainContent.filePath = path
              if newFileSaved then {
                currentTab.text = createTabTitle(path)
                currentTab.userData = createTabId(path).toString
              }
              mainContent.markClean()
            case _ => // do nothing
          }

        case Action.NewExampleFile(tableData) => addTab(UUID.randomUUID(), None, ExampleData(Seq(tableData)))

        case Action.AddTable(tableData) =>
          val content = tabPane.selectionModel.value.getSelectedItem.content.value
          content match {
            case mainContent: ExampleFileGeneratorView =>
              mainContent.addTableData(tableData)
              mainContent.selectTable(tableData.tag)
              control.exampleRequest = ExampleRequest(mainContent.filePath, mainContent.toExampleData)
            case _ => // do nothing
          }

        case _ =>
          val content = tabPane.selectionModel.value.getSelectedItem.content.value
          content match {
            case mainContent: ExampleFileGeneratorView =>
              mainContent.action = nv
              control.action = NoAction
              Platform
                .runLater(() => {
                  control.exampleRequest = ExampleRequest(mainContent.filePath, mainContent.toExampleData)
                  control.defaultCursor()
                })
            case _ => // do nothing
          }
      }
      control.action = Action.NoAction
    })

  private val mainPane =
    new BorderPane {
      center = tabPane
    }

  getChildren.addAll(mainPane)

  private def addTab(tabId: UUID, maybePath: Option[Path], exampleData: ExampleData): Unit = {
    val tabTitle =
      maybePath match {
        case Some(path) => createTabTitle(path)
        case None =>
          untitledCount += 1
          s"Untitled$untitledCount.yml"
      }

    val tab = new Tab {
      text = tabTitle
      userData = tabId.toString
      content = createExamplesFileView(maybePath.orNull, exampleData)
    }
    tabPane.getTabs.add(tab)
    tabPane.getSelectionModel.selectLast()
  }

  private def createExamplesFileView(path: Path, exampleData: ExampleData) = {
    val fileView = ExampleFileGeneratorView()
    fileView.addExampleData(exampleData)
    fileView.filePath = path
    fileView
  }

  private def bind(id: UUID, mainContent: ExampleFileGeneratorView): Unit = {
    control.dirtyPropertyWrapper.bind(mainContent.selectedTableProperty.isNull.or(mainContent.dirtyProperty.not))
    control.hasSelectedTablePropertyWrapper.bind(mainContent.selectedTableProperty.isNotNull)
    control.hasSelectedColumnPropertyWrapper.bind(mainContent.selectedColumnProperty.isNotNull)
    control.exampleRequest = ExampleRequest(mainContent.filePath, mainContent.toExampleData)
  }

  private def unbind(): Unit = {
    control.dirtyPropertyWrapper.unbind()
    control.hasSelectedTablePropertyWrapper.unbind()
    control.hasSelectedColumnPropertyWrapper.unbind()
    control.dirty = false
    control.hasSelectedTable = false
    control.hasSelectedColumn = false
    control.exampleRequest = null
  }

  private def createTabTitle(path: Path) = {
    val fileName = path.getFileName.toString
    val parentName = Option(path.getParent).map(_.getFileName.toString).map(_ + "/").getOrElse("")
    parentName + fileName
  }

  private def createTabId(path: Path) = UUID.nameUUIDFromBytes(path.toString.getBytes)
}

object ExamplesSkin {

  private[examples_view] def apply(control: ExamplesView): ExamplesSkin = new ExamplesSkin(control)
}
