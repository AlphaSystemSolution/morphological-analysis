package com.alphasystem
package arabic
package examples_generator
package ui
package service

import ui.control.file.ExampleFileGeneratorView
import ui.model.ExampleRequest
import arabic.morphologicalanalysis.ui.service.ServiceAdapter
import org.slf4j.LoggerFactory
import scalafx.Includes.*

import java.nio.file.{ Files, Path }
import scala.util.{ Failure, Success, Try }

class SaveExampleDataService(view: ExampleFileGeneratorView) extends ServiceAdapter[ExampleRequest, Path](view) {

  private val logger = LoggerFactory.getLogger(classOf[SaveExampleDataService])

  override protected def getResponse(request: ExampleRequest): Path = {
    val value = model.fromExampleData(request.exampleData)

    Try(Files.writeString(request.path, value)) match {
      case Failure(ex) =>
        logger.error(s"Failed to save example data to ${request.path}", ex)
      case Success(_) => logger.info(s"Example data saved to ${request.path}")
    }

    request.path
  }

  override protected def doOnSucceeded(result: Path): Unit = ()

  override protected def doOnFailed(): Unit = ()
}

object SaveExampleDataService {
  def apply(view: ExampleFileGeneratorView): SaveExampleDataService = new SaveExampleDataService(view)
}
