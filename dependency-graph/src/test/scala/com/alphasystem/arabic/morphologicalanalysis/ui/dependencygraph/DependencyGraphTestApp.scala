package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Chapter
import dependencygraph.control.{ AddNodeDialog, AddNodeView }
import scalafx.application.JFXApp3
import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{ Alert, Button }
import scalafx.scene.layout.FlowPane
import scalafx.scene.paint.Color

object DependencyGraphTestApp extends JFXApp3 with AppInit {
  override def start(): Unit =
    stage = new JFXApp3.PrimaryStage {
      title = "Token Editor Test App"
      scene = new Scene {
        fill = Color.Beige
        content = new FlowPane {
          hgap = 10.0
          vgap = 10.0
          padding = Insets(10.0, 10.0, 10.0, 10.0)
          children = Seq(
            addNodeButton()
          )
        }
      }
    }

  private def addNodeButton(): Button =
    new Button {
      text = "Add Node"
      onAction = () => {
        val dialog = AddNodeDialog(serviceFactory)
        dialog.currentChapter = Chapter(chapterName = "", chapterNumber = 1, verseCount = 7)
        dialog.showAndWait() match
          case Some(Some(terminalNode)) => println(terminalNode)
          case _                        => println("Cancelled")
      }
    }
}
