package com.alphasystem
package arabic
package morphologicalengine
package ui
package service

import arabic.morphologicalanalysis.ui.service.ServiceAdapter
import morphologicalengine.asciidoc_generator.RootInfo
import ui.control.root_info.RootInfoEditorView
import scalafx.Includes.*

class DeleteRootInfoService(view: RootInfoEditorView) extends ServiceAdapter[RootRequest, RootInfo](view) {

  private val rootInfoCollection = nitriteDatabase.rootInfoCollection

  override protected def getResponse(request: RootRequest): RootInfo = {
    rootInfoCollection.deleteRootInfo(request.rootLetters, request.family)
    RootInfo(request.rootLetters, request.family, "")
  }

  override protected def doOnSucceeded(result: RootInfo): Unit = view.update(result)

  override protected def doOnFailed(): Unit =
    view.errorStatus =
      ErrorStatus("Error delete root ifo!", "Could not delete root info for given root letters and family!")
}

object DeleteRootInfoService {
  def apply(view: RootInfoEditorView): DeleteRootInfoService = new DeleteRootInfoService(view)
}
