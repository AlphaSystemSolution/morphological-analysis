package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import skin.ProjectSkin
import javafx.scene.control.{ Control, Skin }

class ProjectView extends Control {

  setSkin(createDefaultSkin())
  override def createDefaultSkin(): Skin[_] = ProjectSkin(this)
}

object ProjectView {
  def apply(): ProjectView = new ProjectView()
}
