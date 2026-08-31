package com.alphasystem
package arabic
package morphologicalengine
package ui
package utils

import arabic.fx.ui.util.*
import arabic.morphologicalanalysis.ui.service.ServiceAdapter
import morphologicalengine.asciidoc_generator.RootInfo
import morphologicalengine.conjugation.model.{ NamedTemplate, RootLetters }
import ui.control.RootInfoEditorView
import javafx.concurrent.WorkerStateEvent
import scalafx.Includes.*
import scalafx.concurrent.Service

class GetRootInfoService(view: RootInfoEditorView) extends ServiceAdapter[RootRequest, Option[RootInfo]](view) {

  private val rootInfoCollection = nitriteDatabase.rootInfoCollection

  def service(rootLetters: RootLetters, family: NamedTemplate): Service[Option[RootInfo]] =
    serviceInitializer(getRootInfo)(RootRequest(rootLetters, family))

  private def getRootInfo(rootRequest: RootRequest): Option[RootInfo] =
    rootInfoCollection.findById(s"${rootRequest.rootLetters.buckWalterString}_${rootRequest.family}")

  override protected def updateUiOnFailed(): Unit =
    view.updateStatusLabel("Conjugations not found for given root letters and family!")

  override protected def onSucceeded(event: WorkerStateEvent): Unit = {
    val result = event.getSource.getValue.asInstanceOf[Option[RootInfo]]
    view.updateStatusLabel("")
    view.defaultCursor()

    result match {
      case Some(rootInfo) => view.update(rootInfo)
      case None =>
        view.updateStatusLabel("Conjugations not found for given root letters and family!")
        view.update(
          RootInfo(
            rootLetters = view.rootLetters,
            family = view.family,
            baseTranslation = ""
          )
        )
    }

    event.consume()
  }
}

object GetRootInfoService {
  def apply(view: RootInfoEditorView): GetRootInfoService = new GetRootInfoService(view)
}

case class RootRequest(rootLetters: RootLetters, family: NamedTemplate)
