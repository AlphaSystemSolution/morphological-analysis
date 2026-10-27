package com.alphasystem
package arabic
package examples_generator
package ui
package service

import ui.control.file.ExampleFileGeneratorView
import ui.model.ExampleRequest
import arabic.morphologicalanalysis.ui.service.ServiceAdapter
import examples_generator.asciidoc_generator.DocumentGenerator
import examples_generator.model.{ fromExampleData, toExampleData }
import org.slf4j.LoggerFactory
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

import java.nio.charset.StandardCharsets
import java.nio.file.{ Files, Path, Paths, StandardCopyOption }
import scala.util.{ Failure, Success, Try }

class SaveExampleDataService(view: ExampleFileGeneratorView) extends ServiceAdapter[ExampleRequest, Path](view) {

  private val logger = LoggerFactory.getLogger(classOf[SaveExampleDataService])

  override protected def getResponse(request: ExampleRequest): Path = {
    val exampleData = request.exampleData
    val value = fromExampleData(exampleData)

    val path = request.path
    val backupFilePath = toBackupFile(path)
    Try {
      Files.writeString(backupFilePath, value, StandardCharsets.UTF_8)
      toExampleData(backupFilePath)
      Files.copy(backupFilePath, path, StandardCopyOption.REPLACE_EXISTING)
      DocumentGenerator.generateDocument(exampleData = exampleData, destPath = toAsciidocPath(path))
    } match {
      case Failure(ex) =>
        logger.error(s"Failed to save example data to $path", ex)
        throw new RuntimeException("Uable to save file")
      case Success(_) => // do nothing
    }
    path
  }

  override protected def doOnSucceeded(result: Path): Unit = {
    logger.info(s"Example data saved to $result")
    view.markClean()
  }

  override protected def doOnFailed(): Unit =
    new Alert(AlertType.Error) {
      initOwner(JFXApp3.Stage)
      title = "Save Failed"
      headerText = "Failed to save file"
      contentText = "The file could not be validated/written. The original file was left unchanged."
    }.showAndWait()

  private def toAsciidocPath(path: Path): Path = {
    val parent = path.getParent
    val fileName = path.getFileName.toString
    val indexOfExtension = fileName.lastIndexOf('.')
    val fileNameWithoutExtension = fileName.substring(0, indexOfExtension)
    Paths.get(parent.toString, s"$fileNameWithoutExtension.adoc")
  }

}

object SaveExampleDataService {
  def apply(view: ExampleFileGeneratorView): SaveExampleDataService = new SaveExampleDataService(view)
}
