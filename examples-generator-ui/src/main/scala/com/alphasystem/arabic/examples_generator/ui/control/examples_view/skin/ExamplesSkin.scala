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

import scala.collection.mutable

import java.nio.file.Path
import java.util.UUID

class ExamplesSkin private (control: ExamplesView) extends SkinBase[ExamplesView](control) {

  private val pathToIndexMap = mutable.Map.empty[UUID, Int]

  private val tabPane = new TabPane()

  control
    .exampleRequestProperty
    .onChange((_, _, nv) => {
      nv match {
        case Some(ExampleRequest(path, exampleData)) =>
          val tabId = UUID.nameUUIDFromBytes(path.toString.getBytes)
          pathToIndexMap.get(tabId) match {
            case Some(index) => tabPane.getSelectionModel.select(index)
            case None =>
              pathToIndexMap += tabId -> tabPane.getTabs.size
              val fileName = path.getFileName.toString
              val parentName = Option(path.getParent).map(_.getFileName.toString).map(_ + "/").getOrElse("")
              val tabTitle = parentName + fileName
              val tab = new Tab {
                text = tabTitle
                userData = tabId.toString
                content = createExamplesFileView(path, exampleData)
                onCloseRequest = _ => {
                  pathToIndexMap -= tabId
                }
              }
              tabPane.getTabs.add(tab)
              tabPane.getSelectionModel.selectLast()
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
}

object ExamplesSkin {

  private[examples_view] def apply(control: ExamplesView): ExamplesSkin = new ExamplesSkin(control)
}
