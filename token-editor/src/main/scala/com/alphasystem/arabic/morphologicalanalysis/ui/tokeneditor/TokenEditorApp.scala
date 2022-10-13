package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Chapter
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache.CacheFactory
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.repository.*
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.{
  ChapterVerseSelectionView,
  TokenEditorView
}
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.service.ServiceFactory
import com.typesafe.config.ConfigFactory
import javafx.application.Platform
import javafx.geometry.Pos
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
  private lazy val chapterVerseSelectionView =
    ChapterVerseSelectionView(serviceFactory)

  private lazy val tokenEditorView = TokenEditorView(serviceFactory)

  private def createPane = {
    val pane = new BorderPane()

    pane.top = chapterVerseSelectionView
    BorderPane.setAlignment(chapterVerseSelectionView, Pos.CENTER)

    pane.center = tokenEditorView
    BorderPane.setAlignment(tokenEditorView, Pos.CENTER)

    chapterVerseSelectionView
      .selectedTokenProperty
      .onChange((_, _, nv) =>
        if Option(nv).isDefined then tokenEditorView.token = nv.userData
        else tokenEditorView.token = null
      )
    pane
  }

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Token Editor"
      scene = new Scene {
        content = createPane
      }
      width = 700
      height = 700
    }
    stage.resizable = true
    stage.centerOnScreen()
    stage.maximized = true
  }
}
