package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import javafx.scene.control.{Control, Skin}

class MorphologicalEngineView2 extends Control {

  setSkin(createDefaultSkin())

  override def createDefaultSkin(): Skin[?] = skin.MorphologicalEngineSkin2(this)
}

object MorphologicalEngineView2 {
  def apply(): MorphologicalEngineView2 = new MorphologicalEngineView2()
}
