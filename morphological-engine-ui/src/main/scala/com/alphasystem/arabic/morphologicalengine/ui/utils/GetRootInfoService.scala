package com.alphasystem
package arabic
package morphologicalengine
package ui
package utils

import com.alphasystem.arabic.morphologicalanalysis.ui.service.ServiceAdapter
import com.alphasystem.arabic.morphologicalengine.asciidoc_generator.RootInfo
import com.alphasystem.arabic.morphologicalengine.conjugation.model.{NamedTemplate, RootLetters}
import com.alphasystem.arabic.morphologicalengine.ui.utils.GetRootInfoService.RootRequest
import scalafx.concurrent.Service

class GetRootInfoService extends ServiceAdapter[RootRequest, Option[RootInfo]] {

  private val rootInfoCollection = nitriteDatabase.rootInfoCollection

  def service(rootLetters: RootLetters, family: NamedTemplate): Service[Option[RootInfo]] =
    serviceInitializer(getRootInfo)(RootRequest(rootLetters, family))

  private def getRootInfo(rootRequest: RootRequest): Option[RootInfo] =
    rootInfoCollection.findById(s"${rootRequest.rootLetters.buckWalterString}_${rootRequest.family}")
}

object GetRootInfoService {
  def apply(): GetRootInfoService = new GetRootInfoService()

  case class RootRequest(rootLetters: RootLetters, family: NamedTemplate)
}
