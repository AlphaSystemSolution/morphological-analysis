package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.{
  Chapter,
  Verse
}
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.{
  NounPropertiesView,
  ParticlePropertiesView,
  ProNounPropertiesView,
  VerbPropertiesView
}
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{ Alert, Button }
import scalafx.scene.layout.FlowPane
import scalafx.scene.paint.Color

object TokenEditorTestApp extends JFXApp3 {

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Token Editor Test App"
      scene = new Scene {
        fill = Color.Beige
        content = new FlowPane {
          hgap = 10.0
          vgap = 10.0
          padding = Insets(10.0, 10.0, 10.0, 10.0)
          children = Seq(
            nounPropertiesButton,
            proNounPropertiesButton,
            verbPropertiesButton,
            particlePropertiesButton
          )
        }
      }
    }
  }

  private def nounPropertiesButton =
    new Button {
      text = "Noun Properties"
      onAction = () => {
        new Alert(AlertType.Confirmation) {
          initOwner(stage)
          title = "Noun Properties"
          dialogPane().content = NounPropertiesView()
        }.showAndWait()
      }
    }

  private def proNounPropertiesButton =
    new Button {
      text = "ProNoun Properties"
      onAction = () => {
        new Alert(AlertType.Confirmation) {
          initOwner(stage)
          title = "Noun Properties"
          dialogPane().content = ProNounPropertiesView()
        }.showAndWait()
      }
    }

  private def verbPropertiesButton =
    new Button {
      text = "Verb Properties"
      onAction = () => {
        new Alert(AlertType.Confirmation) {
          initOwner(stage)
          title = "Verb Properties"
          dialogPane().content = VerbPropertiesView()
        }.showAndWait()
      }
    }

  private def particlePropertiesButton =
    new Button {
      text = "Particle Properties"
      onAction = () => {
        new Alert(AlertType.Confirmation) {
          initOwner(stage)
          title = "Particle Properties"
          dialogPane().content = ParticlePropertiesView()
        }.showAndWait()
      }
    }
}
