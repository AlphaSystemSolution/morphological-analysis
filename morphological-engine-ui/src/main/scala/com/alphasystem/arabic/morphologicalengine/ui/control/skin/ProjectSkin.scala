package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import scalafx.Includes.*
import javafx.scene.control.SkinBase
import scalafx.scene.layout.BorderPane
class ProjectSkin(control: ProjectView) extends SkinBase[ProjectView](control) {

  getChildren.addAll(initializeSkin)

  private def initializeSkin = {

    new BorderPane()
  }
}

object ProjectSkin {
  def apply(control: ProjectView): ProjectSkin = new ProjectSkin(control)
}
