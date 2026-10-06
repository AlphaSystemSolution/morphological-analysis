package com.alphasystem
package arabic
package morphologicalengine
package ui
package service

import arabic.morphologicalanalysis.ui.service.ServiceAdapter
import arabic.morphologicalengine.conjugation.model.OutputFormat.Unicode
import morphologicalengine.asciidoc_generator.{ RootInfo, updateRootInfo }
import morphologicalengine.conjugation.builder.ConjugationBuilder
import ui.control.root_info.RootInfoEditorView
import scalafx.Includes.*

class SaveRootInfoService(view: RootInfoEditorView) extends ServiceAdapter[RootInfo, RootInfo](view) {

  private val rootInfoCollection = nitriteDatabase.rootInfoCollection
  private val conjugationBuilder = ConjugationBuilder()

  override protected def getResponse(request: RootInfo): RootInfo = {
    val morphologicalChart = conjugationBuilder.doConjugation(
      input = request.toConjugationInput,
      outputFormat = Unicode
    )
    val updatedRootInfo = morphologicalChart.updateRootInfo(request)
    rootInfoCollection.upsert(updatedRootInfo)
    updatedRootInfo
  }

  override protected def doOnSucceeded(result: RootInfo): Unit = view.update(result)

  override protected def doOnFailed(): Unit =
    view.errorStatus =
      ErrorStatus("Error save root ifo!", "Could not save root info for given root letters and family!")
}

object SaveRootInfoService {
  def apply(view: RootInfoEditorView): SaveRootInfoService = new SaveRootInfoService(view)
}
