package com.alphasystem
package arabic
package examples_generator
package ui
package control
package chapter_verse_selector
package skin

import arabic.fx.ui.util.*
import arabic.morphologicalanalysis.ui.ArabicSupportEnumComboBox
import arabic.model.{ ArabicLabel, ArabicWord }
import arabic.morphologicalanalysis.ui.ListType.LABEL_ONLY
import com.alphasystem.arabic.examples_generator.ui.model.VerseRange
import scalafx.Includes.*
import javafx.scene.control.SkinBase
import scalafx.collections.ObservableBuffer
import scalafx.geometry.NodeOrientation.RightToLeft
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.control.{ ComboBox, TextArea }
import scalafx.scene.layout.{ BorderPane, ColumnConstraints, GridPane }

class ChapterVerseSelectionSkin private[chapter_verse_selector] (control: ChapterVerseSelectionView)
    extends SkinBase[ChapterVerseSelectionView](control) {

  private val verseStartComboBox = ArabicSupportEnumComboBox(Array.empty[ArabicLabel[Int]], LABEL_ONLY)
  private val verseEndComboBox = ArabicSupportEnumComboBox(Array.empty[ArabicLabel[Int]], LABEL_ONLY)
  verseStartComboBox.setDisable(true)
  verseEndComboBox.setDisable(true)
  private val tokenStartCombobox = new ComboBox[Int](Seq.empty[Int])
  private val tokenEndCombobox = new ComboBox[Int](Seq.empty[Int])
  tokenStartCombobox.setDisable(true)
  tokenEndCombobox.setDisable(true)
  private val selectedText = new TextArea {
    font = preferences.arabicFont(30)
    editable = true
    nodeOrientation = RightToLeft
    wrapText = true
  }

  // make sure start verse index is equal or less than verse end index
  verseStartComboBox.valueProperty().onChange((_, _, nv) => updateStartAndEndVerseSelection(nv, verseEndComboBox))
  verseEndComboBox.valueProperty().onChange((_, _, nv) => updateStartAndEndVerseSelection(nv, verseStartComboBox))
  control.selectedTextProperty.bindBidirectional(selectedText.textProperty())

  getChildren.addAll(mainPane)

  private lazy val mainPane = {
    new BorderPane {
      center = gridPane
      BorderPane.setAlignment(gridPane, Pos.Center)
    }
  }

  private lazy val gridPane = {
    val labelColumnConstraint = new ColumnConstraints {
      percentWidth = -1
    }
    val comboBoxColumnConstraint = new ColumnConstraints {
      percentWidth = 35
    }
    val gridPane = new GridPane {
      styleClass = ObservableBuffer("border")
      vgap = 10
      hgap = 10
      alignment = Pos.Center
      padding = Insets(10, 10, 10, 10)
      columnConstraints =
        Seq(labelColumnConstraint, comboBoxColumnConstraint, labelColumnConstraint, comboBoxColumnConstraint)
    }

    gridPane.add(createLabel("Chapter:"), 0, 1, 1, 1)
    gridPane.add(chaptersComboBox, 1, 1, 3, 1)

    gridPane.add(createLabel("Verse start:"), 0, 2)
    gridPane.add(verseStartComboBox, 1, 2)

    gridPane.add(createLabel("Verse end:"), 2, 2)
    gridPane.add(verseEndComboBox, 3, 2)

    gridPane.add(createLabel("Token start:"), 0, 3)
    gridPane.add(tokenStartCombobox, 1, 3)

    gridPane.add(createLabel("Token end:"), 2, 3)
    gridPane.add(tokenEndCombobox, 3, 3)

    gridPane.add(createLabel("Selected verse(s):"), 0, 4, 1, 1)
    gridPane.add(selectedText, 1, 4, 3, 1)

    gridPane
  }

  private lazy val chaptersComboBox = {
    val comboBox = ArabicSupportEnumComboBox(control.chapters.map(_.toArabicLabel).toArray, LABEL_ONLY)
    comboBox.setDisable(true)

    control.selectedChapterProperty.onChange { (_, _, nv) =>
      if Option(nv).isDefined then {
        comboBox.setValue(nv.toArabicLabel)
        refreshVerseComboBox(verseStartComboBox, nv.verseCount)
        refreshVerseComboBox(verseEndComboBox, nv.verseCount)
      } else {
        clearVerseComboBox(verseStartComboBox)
        clearVerseComboBox(verseEndComboBox)
      }
      updateSelectedText()
    }
    comboBox.valueProperty().onChange { (_, _, nv) =>
      if Option(nv).isDefined then {
        control.selectedChapter = nv.userData
      }
    }

    control.chaptersProperty.onChange { (_, changes) =>
      changes.foreach {
        case ObservableBuffer.Add(_, added) =>
          comboBox.getItems.addAll(added.map(_.toArabicLabel).toSeq*)
          if added.nonEmpty then comboBox.setValue(added.head.toArabicLabel)
          if control.chapters.nonEmpty then comboBox.setDisable(false)

        case ObservableBuffer.Remove(_, removed) =>
          comboBox.getItems.removeAll(removed.map(_.toArabicLabel).toSeq*)
          comboBox.setValue(null)
          if control.chapters.isEmpty then {
            comboBox.setDisable(true)
            clearVerseComboBox(verseStartComboBox)
            clearVerseComboBox(verseEndComboBox)
          }

        case ObservableBuffer.Reorder(_, _, _) => ()
        case ObservableBuffer.Update(_, _)     => ()
      }
    }
    comboBox
  }

  private def refreshVerseComboBox(combobox: ArabicSupportEnumComboBox[ArabicLabel[Int]], verseCount: Int): Unit = {
    combobox.getItems.clear()
    if verseCount > 0 then {
      val items = (1 to verseCount).map(value => ArabicLabel[Int](value, value.toString, ArabicWord(value).unicode))
      combobox.getItems.addAll(items*)
      combobox.setValue(items.head)
    }
    combobox.setDisable(verseCount <= 0)
  }

  private def clearVerseComboBox(combobox: ArabicSupportEnumComboBox[ArabicLabel[Int]]): Unit = {
    combobox.getItems.clear()
    combobox.setDisable(true)
  }

  private def refreshTokenComboBox(combobox: ComboBox[Int], tokenCount: Int): Unit = {
    combobox.getItems.clear()
    if tokenCount > 0 then {
      val items = 1 to tokenCount
      combobox.getItems.addAll(items *)
      combobox.setValue(items.head)
    }
    combobox.setDisable(tokenCount <= 0)
  }

  private def clearTokenComboBox(combobox: ComboBox[Int]): Unit = {
    combobox.getItems.clear()
    combobox.setDisable(true)
  }

  private def updateStartAndEndVerseSelection(
    selectedValue: ArabicLabel[Int],
    otherCombobox: ArabicSupportEnumComboBox[ArabicLabel[Int]]
  ): Unit = {
    if Option(selectedValue).isDefined then {
      val selectedStartIndex = verseStartComboBox.getSelectionModel.getSelectedIndex
      val selectedEndIndex = verseEndComboBox.getSelectionModel.getSelectedIndex
      if selectedEndIndex < selectedStartIndex then verseEndComboBox.getSelectionModel.select(selectedStartIndex)
    } else otherCombobox.getSelectionModel.selectFirst()

    updateSelectedText()
  }

  private def updateSelectedText(): Unit = {
    control.verseRange = null
    val startValue = verseStartComboBox.getValue
    val endValue = verseEndComboBox.getValue
    if Option(startValue).isDefined && Option(endValue).isDefined then {
      control.verseRange = VerseRange(startValue.userData, endValue.userData)
    }
  }
}

object ChapterVerseSelectionSkin {
  private[chapter_verse_selector] def apply(control: ChapterVerseSelectionView) = new ChapterVerseSelectionSkin(control)
}
