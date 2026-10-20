package com.alphasystem
package arabic
package examples_generator
package ui
package service

import ui.control.file.ExampleFileGeneratorView
import ui.model.ExampleRequest
import arabic.morphologicalanalysis.ui.service.ServiceAdapter
import com.alphasystem.arabic.examples_generator.ui.utils.DocumentGenerator
import org.slf4j.LoggerFactory
import scalafx.Includes.*

import java.nio.charset.StandardCharsets
import java.nio.file.{ Files, Path, Paths }
import scala.util.{ Failure, Success, Try }

class SaveExampleDataService(view: ExampleFileGeneratorView) extends ServiceAdapter[ExampleRequest, Path](view) {

  private val logger = LoggerFactory.getLogger(classOf[SaveExampleDataService])

  override protected def getResponse(request: ExampleRequest): Path = {
    val exampleData = request.exampleData
    val value = model.fromExampleData(exampleData)

    val path = request.path
    Try(Files.writeString(path, value, StandardCharsets.UTF_8)) match {
      case Failure(ex) => logger.error(s"Failed to save example data to $path", ex)
      case Success(_)  => DocumentGenerator.generateDocument(exampleData = exampleData, destPath = toAsciidocPath(path))
    }

    path
  }

  override protected def doOnSucceeded(result: Path): Unit = {
    logger.info(s"Example data saved to $result")
    view.markClean()
  }

  override protected def doOnFailed(): Unit = ()

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
