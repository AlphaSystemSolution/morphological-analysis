package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.control.Label
import scalafx.scene.layout.{ BorderPane, GridPane }

class DependencyGraphVerseSelectionSkin(control: DependencyGraphVerseSelectionView)
    extends SkinBase[DependencyGraphVerseSelectionView](control) {

  getChildren.addAll(initializeSkin)

  private def initializeSkin: BorderPane = {
    val gridPane = new GridPane() {
      vgap = DefaultGap
      hgap = DefaultGap
      alignment = Pos.Center
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
    }

    initializeChapterComboBox(gridPane)

    val pane = new BorderPane() {
      styleClass = ObservableBuffer("border")
      center = gridPane
    }
    BorderPane.setAlignment(gridPane, Pos.Center)

    pane
  }

  private def initializeChapterComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Chapter:"), 0, 0)
    val comboBox =
      ArabicSupportEnumComboBox(
        control.chapters.toArray,
        ListType.LABEL_AND_CODE
      )
    comboBox.setDisable(true)
    control
      .selectedChapterProperty
      .bindBidirectional(comboBox.valueProperty())
    control.chaptersProperty.onChange { (_, changes) =>
      changes.foreach {
        case ObservableBuffer.Add(_, added) =>
          comboBox.getItems.addAll(added.toSeq*)
          if added.nonEmpty then comboBox.setValue(added.head)
          if control.chapters.nonEmpty then comboBox.setDisable(false)

        case ObservableBuffer.Remove(_, removed) =>
          comboBox.getItems.removeAll(removed.toSeq*)
          comboBox.setValue(null)
          if control.chapters.isEmpty then comboBox.setDisable(true)

        case ObservableBuffer.Reorder(_, _, _) => ()
        case ObservableBuffer.Update(_, _)     => ()
      }
    }
    gridPane.add(comboBox, 0, 1)
  }
}

object DependencyGraphVerseSelectionSkin {

  def apply(control: DependencyGraphVerseSelectionView): DependencyGraphVerseSelectionSkin =
    new DependencyGraphVerseSelectionSkin(control)
}
