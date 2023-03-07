package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import skin.MorphologicalEngineSkin
import javafx.scene.control.{ Control, Skin }

class MorphologicalEngineView extends Control {

  setSkin(createDefaultSkin())

  override def createDefaultSkin(): Skin[_] = MorphologicalEngineSkin(this)
}
