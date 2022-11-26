package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control
package skin

import com.alphasystem.arabic.model.ArabicLabel
import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Token
import morphologicalanalysis.ui.{ ArabicSupportEnumCheckBoxListCell, ArabicSupportEnumComboBox, DefaultGap, ListType }
import javafx.application.Platform
import javafx.scene.control.{ ListCell, ListView, SkinBase }
import javafx.util.Callback
import org.controlsfx.control.CheckListView
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{ Insets, NodeOrientation }
import scalafx.scene.control.Label
import scalafx.scene.layout.{ BorderPane, GridPane }

class TokenSelectionSkin(control: TokenSelectionView) extends SkinBase[TokenSelectionView](control) {

  private lazy val checkListView = new CheckListView[ArabicLabel[Token]](control.tokensProperty)

  getChildren.addAll(initializeSkin)

  private def initializeSkin: BorderPane = {
    val gridPane = new GridPane() {
      vgap = DefaultGap
      hgap = DefaultGap
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
    }

    control
      .clearSelectionProperty
      .onChange((_, _, nv) =>
        if nv then {
          control
            .selectedTokens
            .toSeq
            .map(_.userData.tokenNumber)
            .foreach(tokenNumber => checkListView.getCheckModel.clearCheck(tokenNumber - 1))
        }
      )
    initializeChapterComboBox(gridPane)
    initializeVerseComboBox(gridPane)
    initializeTokensList(gridPane)

    new BorderPane() {
      styleClass = ObservableBuffer("border")
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

  private def initializeTokensList(gridPane: GridPane): Unit = {
    gridPane.add(Label("Tokens:"), 0, 4)
    checkListView.setNodeOrientation(NodeOrientation.RightToLeft)
    checkListView.setCellFactory((_: ListView[ArabicLabel[Token]]) =>
      ArabicSupportEnumCheckBoxListCell(
        ListType.LABEL_ONLY,
        (item: ArabicLabel[Token]) => checkListView.getItemBooleanProperty(item)
      )
    )

    val checkModel = checkListView.getCheckModel
    checkModel
      .getCheckedItems
      .onChange((_, changes) => {
        changes.foreach {
          case ObservableBuffer.Add(_, added)      => control.selectedTokens.addAll(added)
          case ObservableBuffer.Remove(_, removed) => control.selectedTokens.removeAll(removed.toSeq*)
          case _                                   => ()
        }
      })

    control
      .selectedTokenProperty
      .onChange((_, _, nv) => if Option(nv).isDefined then checkListView.getSelectionModel.select(nv))
    checkListView.getSelectionModel.selectedItemProperty().onChange((_, _, nv) => control.selectedToken = nv)
    gridPane.add(checkListView, 0, 5)
  }
}

object TokenSelectionSkin {

  def apply(control: TokenSelectionView): TokenSelectionSkin = new TokenSelectionSkin(control)
}
