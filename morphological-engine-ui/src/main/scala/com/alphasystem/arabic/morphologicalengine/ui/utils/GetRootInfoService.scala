package com.alphasystem
package arabic
package morphologicalengine
package ui
package utils

import arabic.morphologicalanalysis.ui.service.ServiceAdapter
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

  override protected def doOnSucceeded(result: Option[RootInfo]): Unit = {
    view.updateStatusLabel("")
    result match {
      case Some(rootInfo) => view.update(rootInfo)
      case None =>
        view.updateStatusLabel("Conjugations not found for given root letters and family!")
        view.update(
          RootInfo(
            rootLetters = view.rootLetters,
            family = view.family,
            baseTranslation = view.baseTranslation,
            verbalNounCodes = view.verbalNouns.map(_.code),
            translations = if view.translations.isBlank then None else Some(view.translations)
          )
        )
    }
  }

  override protected def doOnFailed(): Unit =
    view.updateStatusLabel("Conjugations not found for given root letters and family!")

  /**
   * Loads the root information for the given root letters and template family,
   * initializes the corresponding service, and triggers the process to handle
   * and start the service.
   *
   * @param rootLetters The root letters representing the radicals of the Arabic root.
   * @param family      The named template family associated with the root letters.
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
