package com.alphasystem
package arabic
package morphologicalengine
package ui
package service

import arabic.morphologicalanalysis.ui.service.ServiceAdapter
import morphologicalengine.asciidoc_generator.{ RootInfo, RootTitle }
import ui.control.root_info.RootInfoEditorView
import scalafx.Includes.*

class GetRootInfoService(view: RootInfoEditorView) extends ServiceAdapter[RootRequest, RootInfos](view) {

  private val rootInfoCollection = nitriteDatabase.rootInfoCollection

  override protected def getResponse(request: RootRequest): RootInfos = {
    val titles = rootInfoCollection.findTitles(request.rootLetters)

    val currentRootInfo =
      titles.find(_.family == request.family) match {
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
}

object GetRootInfoService {
  def apply(view: RootInfoEditorView): GetRootInfoService = new GetRootInfoService(view)
}
