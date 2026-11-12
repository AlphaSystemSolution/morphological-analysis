package com.alphasystem
package arabic
package asciidoc_editor
package ui

import jfx.incubator.scene.control.richtext.CodeArea
import javafx.scene.control.{ Alert, Button, ToolBar }
import javafx.scene.layout.BorderPane
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.stage.FileChooser
import scalafx.stage.FileChooser.ExtensionFilter

import java.io.IOException
import java.nio.file.Files

object AsciiDocEditorApp extends JFXApp3 {

  private lazy val codeArea = {
    val area = new CodeArea()
    area.setLineNumbersEnabled(true)
    area.setWrapText(true)
    area
  }

  private lazy val fileChooser = new FileChooser {
    extensionFilters.add(new ExtensionFilter("AsciiDoc files", Seq("*.adoc", "*.asciidoc")))
  }

  override def start(): Unit = {
    val openButton = new Button("Open")
    openButton.setOnAction(_ => openFile())

    val root = new BorderPane()
    root.setTop(new ToolBar(openButton))
    root.setCenter(codeArea)

    stage = new JFXApp3.PrimaryStage {
      title = "AsciiDoc Editor"
      scene = new Scene(new javafx.scene.Scene(root))
      width = 1024
      height = 768
    }
  }

  private def openFile(): Unit = {
    val file = fileChooser.showOpenDialog(stage)
    if Option(file).isDefined then {
      try codeArea.setText(Files.readString(file.toPath))
      catch {
        case ex: IOException =>
          new Alert(Alert.AlertType.ERROR, s"Unable to open file: ${ex.getMessage}").showAndWait()
      }
    }
  }
}
