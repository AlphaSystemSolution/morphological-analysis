package com.alphasystem
package arabic
package morphologicalengine
package ui
package utils

import arabic.fx.ui.util.*
import arabic.morphologicalanalysis.ui.service.ServiceAdapter
import morphologicalengine.asciidoc_generator.*
import morphologicalengine.conjugation.builder.ConjugationBuilder
import morphologicalengine.conjugation.model.OutputFormat.Unicode
import morphologicalengine.conjugation.model.{NamedTemplate, RootLetters}
import ui.control.RootInfoEditorView
import javafx.concurrent.WorkerStateEvent
import scalafx.Includes.*
import scalafx.concurrent.Service

class SaveRootInfoService(view: RootInfoEditorView) extends ServiceAdapter[RootInfo, RootInfo](view) {

  private val rootInfoCollection = nitriteDatabase.rootInfoCollection
  private val conjugationBuilder = ConjugationBuilder()

  def service(rootInfo: RootInfo): Service[RootInfo] = serviceInitializer(saveRootInfo)(rootInfo)

  override protected def onSucceeded(event: WorkerStateEvent): Unit = {
    val result = event.getSource.getValue.asInstanceOf[RootInfo]
    view.updateStatusLabel("")
    view.defaultCursor()
    view.update(result)
  }

  override protected def updateUiOnFailed(): Unit =
    view.updateStatusLabel("Could not save root info for given root letters and family!")

  private def saveRootInfo(rootInfo: RootInfo): RootInfo = {
    val morphologicalChart = conjugationBuilder.doConjugation(
      input = rootInfo.toConjugationInput,
      outputFormat = Unicode
    )
    val updatedRootInfo = morphologicalChart.updateRootInfo(rootInfo)
    rootInfoCollection.upsert(updatedRootInfo)
    updatedRootInfo
  }

  /**
   * Executes a service based on the provided root information.
   *
   * @param rootInfo The root information required to initialize and execute the service.
   */
  def executeService(rootInfo: RootInfo): Unit = {
    val service = this.service(rootInfo)
    handleResponse(service)
    start(service)
  }
}

object SaveRootInfoService {
  def apply(view: RootInfoEditorView): SaveRootInfoService = new SaveRootInfoService(view)
}