package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin

import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.{
  ChapterVerseSelectionView,
  TokenEditorView,
  TokenView
}
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.service.ServiceFactory
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.geometry.Pos
import scalafx.scene.layout.BorderPane

class TokenEditorSkin(control: TokenEditorView, serviceFactory: ServiceFactory)
    extends SkinBase[TokenEditorView](control) {

  private val chapterVerseSelectionView =
    ChapterVerseSelectionView(serviceFactory)

  private val tokenEditorView = TokenView(serviceFactory)

  getChildren.add(initializeSkin)

  private def initializeSkin = {
    val pane = new BorderPane()

    pane.top = chapterVerseSelectionView
    BorderPane.setAlignment(chapterVerseSelectionView, Pos.Center)

    pane.center = tokenEditorView
    BorderPane.setAlignment(tokenEditorView, Pos.Center)

    chapterVerseSelectionView
      .selectedTokenProperty
      .onChange((_, _, nv) =>
        if Option(nv).isDefined then tokenEditorView.token = nv.userData
        else tokenEditorView.token = null
      )

    pane
  }
}

object TokenEditorSkin {

  def apply(control: TokenEditorView, serviceFactory: ServiceFactory) =
    new TokenEditorSkin(control, serviceFactory)
}
