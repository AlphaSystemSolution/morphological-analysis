package com.alphasystem
package arabic
package morphologicalengine
package ui
package service

import arabic.morphologicalanalysis.ui.service.ServiceAdapter
import morphologicalengine.asciidoc_generator.{ RootInfo, RootTitle }
import morphologicalengine.conjugation.model.{ NamedTemplate, RootLetters }
import ui.control.root_info.RootInfoEditorView
import scalafx.Includes.*
import scalafx.concurrent.Service

class GetRootInfoService(view: RootInfoEditorView) extends ServiceAdapter[RootRequest, RootInfos](view) {

  private val rootInfoCollection = nitriteDatabase.rootInfoCollection

  def service(rootLetters: RootLetters, family: NamedTemplate): Service[RootInfos] =
    serviceInitializer(getRootInfo)(RootRequest(rootLetters, family))

  private def getRootInfo(rootRequest: RootRequest): RootInfos = {
    val titles = rootInfoCollection.findTitles(rootRequest.rootLetters)

    val currentRootInfo =
      titles.find(_.family == rootRequest.family) match {
        case Some(rootTitle) => Some(rootTitle)
        case None            => titles.headOption
      } match {
        case Some(rootTitle) => rootInfoCollection.findRootInfo(rootTitle.rootLetters, rootTitle.family)
        case None            => None
      }

    RootInfos(currentRootInfo = currentRootInfo, rootTitles = titles)
  }

  override protected def doOnSucceeded(result: RootInfos): Unit = {
    val currentRootInfo = result.currentRootInfo
    view.updateTitles(
      currentRootInfo.map(ri => RootTitle(ri.rootLetters, ri.family, ri.conjugationTitle.getOrElse(""))),
      result.rootTitles
    )
    currentRootInfo match {
      case Some(rootInfo) => view.update(rootInfo)
      case None => view.update(RootInfo(rootLetters = view.rootLetters, family = view.family, baseTranslation = ""))
    }
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
