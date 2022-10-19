package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control
package skin

import morphology.model.WordType
import control.{ ChapterVerseSelectionView, LocationView, TokenEditorView, TokenView }
import service.ServiceFactory
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.layout.{ BorderPane, VBox }

class TokenEditorSkin(control: TokenEditorView, serviceFactory: ServiceFactory)
    extends SkinBase[TokenEditorView](control) {

  import TokenEditorSkin.*

  private val chapterVerseSelectionView =
    ChapterVerseSelectionView(serviceFactory)

  private val tokenView = TokenView(serviceFactory)

  private val locationView = LocationView()

  getChildren.add(initializeSkin)

  private def initializeSkin = {
    val pane = new BorderPane()

    val vBox = new VBox() {
      spacing = Gap
      alignment = Pos.Center
      padding = Insets(Gap, Gap, Gap, Gap)
    }

    control.tokenProperty.bind(tokenView.tokenProperty)

    tokenView
      .locationsProperty
      .onChange((_, changes) =>
        changes.foreach {
          case ObservableBuffer.Add(_, added) =>
            if added.nonEmpty then {
              control.locationsProperty.removeAll(added.toSeq*)
              control.locationsProperty.addAll(added)
            }

          case ObservableBuffer.Remove(_, removed) =>
            if removed.nonEmpty then control.locationsProperty.removeAll(removed.toSeq*)

          case _ => ()
        }
      )

    tokenView
      .selectedLocationProperty
      .onChange((_, _, nv) => {
        if Option(nv).isDefined && locationView.location != nv then locationView.location = nv
      })

    locationView
      .locationProperty
      .onChange((_, _, nv) => if Option(nv).isDefined then tokenView.updateLocation(nv.wordType, nv.properties))

    vBox.getChildren.addAll(chapterVerseSelectionView, tokenView, locationView)

    pane.center = vBox
    BorderPane.setAlignment(vBox, Pos.Center)

    chapterVerseSelectionView
      .selectedTokenProperty
      .onChange((_, _, nv) =>
        if Option(nv).isDefined then tokenView.token = nv.userData
        else tokenView.token = null
      )

    pane
  }
}

object TokenEditorSkin {
  private val Gap = 10.0

  def apply(control: TokenEditorView, serviceFactory: ServiceFactory) =
    new TokenEditorSkin(control, serviceFactory)
}
