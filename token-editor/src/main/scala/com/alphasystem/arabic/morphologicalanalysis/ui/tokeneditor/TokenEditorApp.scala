package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Chapter
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache.CacheFactory
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.repository.*
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.TokenEditorView
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.service.ServiceFactory
import com.typesafe.config.ConfigFactory
import javafx.application.Platform
import scalafx.stage.Screen
import scalafx.scene.paint.Color
import scalafx.geometry.{ Insets, Pos }
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.concurrent.Service
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane

object TokenEditorApp extends JFXApp3 {

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
}
