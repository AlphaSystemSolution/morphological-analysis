package com.alphasystem.fx.ui

import com.alphasystem.arabic.fx.ui.Browser
import com.alphasystem.fx.ui.FontAwesomeApp.stage
import scalafx.Includes.*
import scalafx.application.{ JFXApp3, Platform }
import scalafx.scene.Scene
import scalafx.stage.Screen

object BrowserApp extends JFXApp3 {

  private lazy val browser = Browser()

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Browser"
      scene = new Scene() {
        content = browser
      }
    }

    browser.webEngine.onError = event => {
      event.getException.printStackTrace()
      event.consume()
    }
    browser.webEngine.onStatusChanged = event => {
      println(event.getData)
      println(browser.webEngine.document)
      event.consume()
    }

    Platform.runLater(() => browser.loadUrl("https://www.espncricinfo.com/"))

    val bounds = Screen.primary.visualBounds
    stage.x = bounds.minX
    stage.y = bounds.minY
    stage.width = bounds.width
    stage.height = bounds.height
    stage.resizable = true
    stage.maximized = true
  }
}
