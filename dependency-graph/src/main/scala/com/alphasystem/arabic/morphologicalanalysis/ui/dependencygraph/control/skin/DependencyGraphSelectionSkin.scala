package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import model.ArabicLabel
import morphology.graph.model.DependencyGraph
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control.{ Label, ListView, SelectionMode }
import scalafx.scene.layout.{ BorderPane, GridPane }

class DependencyGraphSelectionSkin(control: DependencyGraphSelectionView)
    extends SkinBase[DependencyGraphSelectionView](control) {

  getChildren.addAll(initializeSkin)

  private def initializeSkin = {
    val gridPane = new GridPane() {
      vgap = DefaultGap
      hgap = DefaultGap
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
    }

    initializeChapterComboBox(gridPane)
    initializeVerseComboBox(gridPane)
    initializeDependencyGraphs(gridPane)

    new BorderPane() {
      center = gridPane
    }
  }

  private def initializeChapterComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Chapter:"), 0, 0)
    val comboBox = ArabicSupportEnumComboBox(control.chapters.toArray, ListType.LABEL_AND_CODE)
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

  private def initializeVerseComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Verse:"), 0, 2)
    val comboBox = ArabicSupportEnumComboBox(control.versesProperty.toArray, ListType.CODE_ONLY)
    comboBox.setDisable(true)
    control
      .selectedVerseProperty
      .bindBidirectional(comboBox.valueProperty())
    control.versesProperty.onChange { (_, changes) =>
      changes.foreach {
        case ObservableBuffer.Add(_, added) =>
          comboBox.getItems.addAll(added.toSeq*)
          if added.nonEmpty then comboBox.setValue(added.head)
          if control.versesProperty.nonEmpty then comboBox.setDisable(false)

        case ObservableBuffer.Remove(_, removed) =>
          comboBox.getItems.removeAll(removed.toSeq*)
          comboBox.setValue(null)
          if control.versesProperty.isEmpty then comboBox.setDisable(true)

        case ObservableBuffer.Reorder(_, _, _) => ()
        case ObservableBuffer.Update(_, _)     => ()
      }
    }
    gridPane.add(comboBox, 0, 3)
  }

  private def initializeDependencyGraphs(gridPane: GridPane): Unit = {
    gridPane.add(Label("Dependency Graphs:"), 0, 4)
    val listView = new ListView[ArabicLabel[DependencyGraph]](control.dependencyGraphs) {
      cellFactory = ArabicSupportEnumCellFactory[ArabicLabel[DependencyGraph]](ListType.LABEL_ONLY)
      selectionModel()
        .selectedItemProperty()
        .onChange((_, _, nv) => if Option(nv).isDefined then control.selectedGraph = nv)
    }
    control
      .selectedGraphProperty
      .onChange((_, _, nv) => if Option(nv).isDefined then listView.selectionModel().select(nv))
    gridPane.add(listView, 0, 5)
  }
}

object DependencyGraphSelectionSkin {
  def apply(control: DependencyGraphSelectionView): DependencyGraphSelectionSkin =
    new DependencyGraphSelectionSkin(control)
}
