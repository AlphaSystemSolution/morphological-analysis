package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import javafx.scene.control.{ Control, Skin }

class MorphologicalEngineView extends Control {

  setSkin(createDefaultSkin())

  override def createDefaultSkin(): Skin[?] = skin.MorphologicalEngineSkin(this)
}

object MorphologicalEngineView {
  def apply(): MorphologicalEngineView = new MorphologicalEngineView()
}
