package com.alphasystem
package arabic
package examples_generator
package ui
package service

import ui.model.{ Action, ConjugationsRequest, ExampleRequest, SaveFileRequest }
import arabic.morphologicalanalysis.ui.service.ServiceAdapter
import ui.control.examples_view.ExamplesView
import org.slf4j.LoggerFactory
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

import scala.util.{ Failure, Success, Try }

class SaveExampleDataService(view: ExamplesView) extends ServiceAdapter[SaveFileRequest, SaveFileRequest](view) {

  private val logger = LoggerFactory.getLogger(classOf[SaveExampleDataService])

  override protected def getResponse(request: SaveFileRequest): SaveFileRequest = {
    Try {
      request.generateDocument()
    } match {
      case Failure(ex) =>
        logger.error(s"Failed to save example data to ${request.path}", ex)
        throw new RuntimeException("Unable to save file")
      case Success(_) => // do nothing
    }
    request
  }

  override protected def doOnSucceeded(request: SaveFileRequest): Unit =
    (request.exampleData, request.conjugations) match {
      case (Some(exampleData), _) =>
        view.action = Action.SaveExampleFile(
          ExampleRequest(path = Some(request.path), exampleData = exampleData, newFileSaved = request.newFileSaved)
        )
      case (_, Some(conjugations)) =>
        view.action = Action.SaveConjugationFile(
          ConjugationsRequest(
            conjugations = conjugations,
            path = Some(request.path),
            newFileSaved = request.newFileSaved
          )
        )
      case _ =>
    }

  override protected def doOnFailed(): Unit =
    new Alert(AlertType.Error) {
      initOwner(JFXApp3.Stage)
      title = "Save Failed"
      headerText = "Failed to save file"
      contentText = "The file could not be validated/written. The original file was left unchanged."
    }.showAndWait()
}

object SaveExampleDataService {
  def apply(view: ExamplesView): SaveExampleDataService = new SaveExampleDataService(view)
}
