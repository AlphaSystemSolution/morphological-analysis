package com.alphasystem
package arabic
package morphologicalengine
package ui
package service

import arabic.morphologicalanalysis.ui.service.ServiceAdapter
import morphologicalengine.asciidoc_generator.RootInfo
import ui.control.root_info.RootInfoEditorView
import ui.control.root_info.RootInfoEditorView.ErrorStatus
import scalafx.Includes.*
import scalafx.concurrent.Service

class DeleteRootInfoService(view: RootInfoEditorView) extends ServiceAdapter[RootRequest, RootInfo](view) {

  private val rootInfoCollection = nitriteDatabase.rootInfoCollection

  def service(rootRequest: RootRequest): Service[RootInfo] = serviceInitializer(deleteRootInfo)(rootRequest)

  override protected def doOnSucceeded(result: RootInfo): Unit = view.update(result)

  override protected def doOnFailed(): Unit =
    view.errorStatus =
      ErrorStatus("Error delete root ifo!", "Could not delete root info for given root letters and family!")

  /** Executes the service responsible for deleting root information based on the specified root request. It initializes
    * the service, configures response handling, and starts the service.
    *
    * @param rootRequest
    *   The root request containing the root letters and template family used to locate and delete the root information.
    */
  def executeService(rootRequest: RootRequest): Unit = {
    val service = this.service(rootRequest)
    handleResponse(service)
    start(service)
  }

  private def deleteRootInfo(rootRequest: RootRequest) = {
    rootInfoCollection.deleteById(s"${rootRequest.rootLetters.buckWalterString}_${rootRequest.family}")
    RootInfo(rootRequest.rootLetters, rootRequest.family, "")
  }
}

object DeleteRootInfoService {
  def apply(view: RootInfoEditorView): DeleteRootInfoService = new DeleteRootInfoService(view)
}
