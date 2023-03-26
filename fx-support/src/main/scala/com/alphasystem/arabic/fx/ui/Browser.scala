package com.alphasystem
package arabic
package fx
package ui

import scalafx.Includes.*
import scalafx.scene.control.{ ScrollPane, TextInputDialog }
import scalafx.scene.layout.{ BorderPane, Priority }
import scalafx.scene.web.{ WebEngine, WebView }

import java.io.File
import java.net.URL

class Browser extends BorderPane {

  private val browser = new WebView() {
    hgrow = Priority.Always
    vgrow = Priority.Always
  }

  val webEngine: WebEngine = browser.engine

  init()

  hgrow = Priority.Always
  vgrow = Priority.Always
  center = new ScrollPane() {
    content = browser
    fitToWidth = true
    fitToHeight = true
    vbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
    hbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
  }

  def loadUrl(url: String): Unit = webEngine.load(url)

  def loadUrl(url: URL): Unit = loadUrl(url.toString)

  def loadUrl(url: File): Unit = loadUrl(url.toURI.toURL.toString)

  def loadContent(content: String): Unit = webEngine.loadContent(content)

  private def init(): Unit = {
    webEngine.javaScriptEnabled = true
    webEngine.promptHandler = promptData => {
      val dialog = new TextInputDialog(promptData.defaultValue)
      dialog.setHeaderText(promptData.message)
      val result = dialog.showAndWait()
      result match
        case Some(value) => value
        case None        => promptData.defaultValue
    }
  }
}

object Browser {
  def apply(): Browser = new Browser()
}
