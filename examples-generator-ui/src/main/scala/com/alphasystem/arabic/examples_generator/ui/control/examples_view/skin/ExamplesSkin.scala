package com.alphasystem
package arabic
package examples_generator
package ui
package control
package examples_view
package skin

import examples_generator.model.ExampleData
import control.file.ExampleFileGeneratorView
import ui.model.ExampleRequest
import javafx.scene.control.SkinBase
import scalafx.scene.control.{ Tab, TabPane }
import scalafx.scene.layout.BorderPane
import scalafx.Includes.*

import java.nio.file.Path
import java.util.UUID

class ExamplesSkin private (control: ExamplesView) extends SkinBase[ExamplesView](control) {

  private val tabPane = new TabPane()

  tabPane
    .selectionModel
    .value
    .selectedItemProperty()
    .onChange((_, _, nv) => {
      if Option(nv).isDefined then {
        nv.getContent match {
          case mainContent: ExampleFileGeneratorView => bind(mainContent)
          case _                                     => // do nothing
        }
      } else unbind()
    })

  control
    .exampleRequestProperty
    .onChange((_, _, nv) => {
      nv match {
        case Some(ExampleRequest(path, exampleData)) =>
          val tabId = UUID.nameUUIDFromBytes(path.toString.getBytes)

          tabPane.getTabs.find(_.userData == tabId.toString) match {
            case Some(tab) => tabPane.getSelectionModel.select(tab)
            case None =>
              val fileName = path.getFileName.toString
              val parentName = Option(path.getParent).map(_.getFileName.toString).map(_ + "/").getOrElse("")
              val tabTitle = parentName + fileName
              val tab = new Tab {
                text = tabTitle
                userData = tabId.toString
                content = createExamplesFileView(path, exampleData)
              }
              tabPane.getTabs.add(tab)
              tabPane.getSelectionModel.selectLast()
          }

        case None => // do nothing
      }
    })

  control
    .tableDataProperty
    .onChange((_, _, nv) => {
      nv match {
        case Some(tableData) =>
          val content = tabPane.selectionModel.value.getSelectedItem.content.value
          content match {
            case mainContent: ExampleFileGeneratorView =>
              mainContent.addTableData(tableData)
              mainContent.selectTable(tableData.tag)
              control.tableData = null
            case _ => // do nothing
          }
        case None => // do nothing
      }
    })

  private val mainPane =
    new BorderPane {
      center = tabPane
    }

  getChildren.addAll(mainPane)

  private def createExamplesFileView(path: Path, exampleData: ExampleData) = {
    val fileView = ExampleFileGeneratorView()
    fileView.addExampleData(exampleData)
    fileView.filePath = path
    fileView
  }

  private def bind(mainContent: ExampleFileGeneratorView): Unit = {
    control.dirtyPropertyWrapper.bind(mainContent.selectedTableProperty.isNull.or(mainContent.dirtyProperty.not))
    control.hasSelectedTablePropertyWrapper.bind(mainContent.selectedTableProperty.isNotNull)
    control.hasSelectedColumnPropertyWrapper.bind(mainContent.selectedColumnProperty.isNotNull)
  }

  private def unbind(): Unit = {
    control.dirtyPropertyWrapper.unbind()
    control.hasSelectedTablePropertyWrapper.unbind()
    control.hasSelectedColumnPropertyWrapper.unbind()
    control.dirty = false
    control.hasSelectedTable = false
    control.hasSelectedColumn = false
  }
}

object ExamplesSkin {

  private[examples_view] def apply(control: ExamplesView): ExamplesSkin = new ExamplesSkin(control)
}
