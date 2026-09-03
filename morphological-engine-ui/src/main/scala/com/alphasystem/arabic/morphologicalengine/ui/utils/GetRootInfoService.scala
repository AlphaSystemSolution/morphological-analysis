package com.alphasystem
package arabic
package morphologicalengine
package ui
package utils

import arabic.morphologicalanalysis.ui.service.ServiceAdapter
import com.alphasystem.arabic.morphologicalengine.ui.control.RootInfoEditorView.ErrorStatus
import morphologicalengine.asciidoc_generator.RootInfo
import morphologicalengine.conjugation.model.{ NamedTemplate, RootLetters }
import ui.control.RootInfoEditorView
import scalafx.Includes.*
import scalafx.concurrent.Service

class GetRootInfoService(view: RootInfoEditorView) extends ServiceAdapter[RootRequest, Option[RootInfo]](view) {

  private val rootInfoCollection = nitriteDatabase.rootInfoCollection

  def service(rootLetters: RootLetters, family: NamedTemplate): Service[Option[RootInfo]] =
    serviceInitializer(getRootInfo)(RootRequest(rootLetters, family))

  private def getRootInfo(rootRequest: RootRequest): Option[RootInfo] =
    rootInfoCollection.findById(s"${rootRequest.rootLetters.buckWalterString}_${rootRequest.family}")

  override protected def doOnSucceeded(result: Option[RootInfo]): Unit =
    result match {
      case Some(rootInfo) => view.update(rootInfo)
      case None           => view.update(view.toRootInfo)
    }

  override protected def doOnFailed(): Unit =
    view.errorStatus =
      ErrorStatus("Error loading root info!", "Could not load root info for given root letters and family!")

  /** Loads the root information for the given root letters and template family, initializes the corresponding service,
    * and triggers the process to handle and start the service.
    *
    * @param rootLetters
    *   The root letters representing the radicals of the Arabic root.
    * @param family
    *   The named template family associated with the root letters.
    */
  def executeService(rootLetters: RootLetters, family: NamedTemplate): Unit = {
    val service = this.service(rootLetters, family)
    handleResponse(service)
    start(service)
  }
}

object GetRootInfoService {
  def apply(view: RootInfoEditorView): GetRootInfoService = new GetRootInfoService(view)
}

case class RootRequest(rootLetters: RootLetters, family: NamedTemplate)
