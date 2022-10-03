package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Chapter
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache.CacheFactory
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.repository.*
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.ChapterVerseSelectionView
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.service.ServiceFactory
import com.typesafe.config.ConfigFactory
import javafx.application.Platform
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
  private def createPane = {
    new BorderPane() {
      top = ChapterVerseSelectionView(serviceFactory)
    }
  }

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Token Editor"
      scene = new Scene {
        content = createPane
      }
      width = 500
      height = 500
    }
    stage.resizable = true
    stage.centerOnScreen()
    stage.maximized = true
  }
}
