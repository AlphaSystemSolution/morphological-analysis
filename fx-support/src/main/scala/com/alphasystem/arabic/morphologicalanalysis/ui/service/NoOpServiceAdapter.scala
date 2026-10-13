package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package service

import scalafx.scene.Node

abstract class NoOpServiceAdapter[OUT](node: Node) extends ServiceAdapter[NoOpRequest, OUT](node) {

  override protected def getResponse(request: NoOpRequest): OUT = getResponse

  protected def getResponse: OUT
}
