package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor

import morphology.model.Chapter
import morphology.persistence.cache.CacheFactory
import morphology.persistence.repository.*
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.TokenEditorView
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.service.ServiceFactory
import com.typesafe.config.ConfigFactory
import de.jensd.fx.glyphs.fontawesome.{ FontAwesomeIcon, FontAwesomeIconView }
import javafx.application.Platform
import scalafx.stage.Screen
import scalafx.scene.paint.Color
import scalafx.geometry.{ Insets, Pos }
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.concurrent.Service
import scalafx.scene.Scene
import scalafx.scene.control.{ Button, ToolBar, Tooltip }
import scalafx.scene.layout.{ BorderPane, VBox }

object TokenEditorApp extends JFXApp3 {

  private val Gap = 5
  private val config = ConfigFactory.load()
  private val dataSource = Database.datasourceForConfig(config)
  private val chapterRepository = ChapterRepository(dataSource)
  private val verseRepository = VerseRepository(dataSource)
  private val tokenRepository = TokenRepository(dataSource)
  private val locationRepository = LocationRepository(dataSource)
  private val cacheFactory = CacheFactory(
    chapterRepository,
    verseRepository,
    tokenRepository,
    locationRepository
  )
  private val serviceFactory = ServiceFactory(cacheFactory)

  // UI components
  private lazy val tokenEditorView = TokenEditorView(serviceFactory)

  private def createPane = {
    val pane = new BorderPane()

    val topBox = new VBox() {
      spacing = Gap
      padding = Insets(Gap, Gap, Gap, Gap)
    }
    topBox.getChildren.addAll(createToolBar)
    pane.top = topBox

    pane.center = tokenEditorView
    BorderPane.setAlignment(tokenEditorView, Pos.Center)
    BorderPane.setMargin(tokenEditorView, Insets(20, 400, 0, 400))

    pane
  }

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Token Editor"
      scene = new Scene {
        content = createPane
        fill = Color.Beige
        stylesheets = Seq("/styles/glyphs_custom.css")
      }
    }

    val bounds = Screen.primary.visualBounds
    stage.x = bounds.width / 4
    stage.y = bounds.height / 6
    stage.width = bounds.width
    stage.height = bounds.height
    stage.maximized = true
    stage.resizable = true
    tokenEditorView.titleProperty.onChange((_, _, nv) => stage.title = s"Token Editor: $nv")
  }

  private def createToolBar = {
    val saveButton = new Button() {
      graphic = new FontAwesomeIconView(FontAwesomeIcon.SAVE, "2em")
      tooltip = Tooltip("Save")
      onAction = event => {
        tokenEditorView.saveLocations()
        event.consume()
      }
    }
    saveButton.disableProperty().bind(tokenEditorView.titleProperty.isEmpty)

    new ToolBar() {
      items = Seq(saveButton)
    }
  }
}
