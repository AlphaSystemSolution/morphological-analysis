package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import arabic.morphologicalanalysis.ui.{ArabicSupportEnumComboBox, ListType, RootLettersPickerView}
import arabic.morphologicalengine.conjugation.model.NamedTemplate
import com.alphasystem.arabic.morphologicalengine.conjugation.forms.NounSupport
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, Label, TextField}
//import scalafx.scene.input.{ KeyCode, KeyCodeCombination, KeyCombination }
import scalafx.scene.layout.{ BorderPane, GridPane, HBox, Priority }
import scalafx.collections.ObservableBuffer
import scalafx.collections.ObservableBuffer.{ Add, Remove, Reorder }

class RootInfoEditorSkin(control: RootInfoEditorView) extends SkinBase[RootInfoEditorView](control) {

  private val rootLettersPicker = RootLettersPickerView()
  private val familyPicker = ArabicSupportEnumComboBox[NamedTemplate](NamedTemplate.values, ListType.LABEL_AND_CODE)
  private val baseTranslationField = new TextField {
    promptText = "Base Translation"
    prefColumnCount = 30
  }
  private val verbalNounsPicker = VerbalNounPickerView()

  private val searchPanel = {
    new GridPane {
      hgap = 8
      vgap = 8
      add(new Label("Root letters:"), 0, 0)
      add(rootLettersPicker, 1, 0)
      add(new Label("Family:"), 0, 1)
      add(familyPicker, 1, 1)
      add(new Label("Base Translation:"), 0, 2)
      add(baseTranslationField, 1, 2)

      GridPane.setHgrow(rootLettersPicker, Priority.Always)
      GridPane.setHgrow(familyPicker, Priority.Always)
      GridPane.setHgrow(baseTranslationField, Priority.Always)
    }
  }

  private val verbalNounsPanel = {
    new GridPane {
      hgap = 8
      vgap = 8
      add(new Label("Verbal Nouns:"), 0, 0)
      add(verbalNounsPicker, 1, 0)

      GridPane.setHgrow(verbalNounsPicker, Priority.Always)
    }
  }

  private val skin = {
    val hbox = new HBox {
      padding = Insets(12)
      spacing = 10
      children = Seq(searchPanel, verbalNounsPanel)
      style = "-fx-border-color: grey; -fx-border-width: 1px; -fx-border-radius: 4px;"
    }

    new BorderPane {
      center = hbox
      BorderPane.setAlignment(hbox, Pos.Center)
    }
  }

  getChildren.addAll(skin)

  verbalNounsPicker.verbalNounsProperty.clear()
  verbalNounsPicker.verbalNounsProperty.addAll(control.verbalNouns)
  rootLettersPicker.rootLettersProperty.bindBidirectional(control.rootLettersProperty)
  familyPicker.valueProperty().bindBidirectional(control.familyProperty)
  bindBuffers(control.verbalNounsProperty, verbalNounsPicker.verbalNounsProperty)

  private def bindBuffers(buf1: ObservableBuffer[NounSupport], buf2: ObservableBuffer[NounSupport]): Unit = {
    var updating = false

    buf1.onChange { (source, changes) =>
      if !updating then {
        updating = true
        try {
          changes.foreach {
            case Add(position, added)      => buf2.insertAll(position, added)
            case Remove(position, removed) => buf2.remove(position, removed.size)
            case Reorder(from, to, perm)   =>
              // Handle reordering if needed
              buf2.setAll(source)
            case _ => buf2.setAll(source)
          }
        } finally {
          updating = false
        }
      }
    }

    buf2.onChange { (source, changes) =>
      if !updating then {
        updating = true
        try {
          changes.foreach {
            case Add(position, added)      => buf1.insertAll(position, added)
            case Remove(position, removed) => buf1.remove(position, removed.size)
            case Reorder(from, to, perm)   => buf1.setAll(source)
            case _                         => buf1.setAll(source)
          }
        } finally {
          updating = false
        }
      }
    }
  }
}

object RootInfoEditorSkin {
  def apply(control: RootInfoEditorView): RootInfoEditorSkin = new RootInfoEditorSkin(control)
}
