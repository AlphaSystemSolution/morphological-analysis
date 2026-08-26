package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import arabic.utils.*
import arabic.morphologicalanalysis.ui.{ArabicSupportEnumComboBox, ListType, RootLettersPickerView}
import arabic.morphologicalengine.conjugation.model.{NamedTemplate, RootLetters}
import morphologicalengine.conjugation.forms.NounSupport
import morphologicalengine.asciidoc_generator.*
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, Label, TextField}

import java.nio.file.Files
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
  private val statusLabel = new Label {
    wrapText = true
    style = "-fx-font-weight: bold; -fx-text-fill: red; -fx-font-size: 1.5em;"
  }

  private val searchPanel = {
    new GridPane {
      hgap = 8
      vgap = 8
      add(createLabel("Root letters:"), 0, 0)
      add(rootLettersPicker, 1, 0)
      add(createLabel("Family:"), 0, 1)
      add(familyPicker, 1, 1)
      add(createLabel("Base Translation:"), 0, 2)
      add(baseTranslationField, 1, 2)
      add(statusLabel, 0, 3, 2, 1)
      style = "-fx-border-color: grey; -fx-border-width: 1px; -fx-border-radius: 4px;"

      GridPane.setHgrow(rootLettersPicker, Priority.Always)
      GridPane.setHgrow(familyPicker, Priority.Always)
      GridPane.setHgrow(baseTranslationField, Priority.Always)
    }
  }

  private val verbalNounsPanel = {
    new GridPane {
      hgap = 8
      vgap = 8
      add(createLabel("Verbal Nouns:"), 0, 0)
      add(verbalNounsPicker, 1, 0)
      style = "-fx-border-color: grey; -fx-border-width: 1px; -fx-border-radius: 4px;"
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
  baseTranslationField.textProperty().bindBidirectional(control.baseTranslationProperty)
  bindBuffers(control.verbalNounsProperty, verbalNounsPicker.verbalNounsProperty)

  loadRootInfo(control.rootLetters, control.family)
  control.rootLettersProperty.onChange((_, _, nv) => loadRootInfo(nv, control.family))
  control.familyProperty.onChange((_, _, nv) => loadRootInfo(control.rootLetters, nv))

  private def loadRootInfo(rootLetters: RootLetters, family: NamedTemplate): Unit = {
    statusLabel.text = ""
    Option(rootLetters) match {
      case Some(_) =>
        val rootDirectoryPath = rootDataPath / Seq(rootLetters.toDirectoryName)
        if Files.exists(rootDirectoryPath) then {
          val familyPath = rootDirectoryPath / Seq(s"$family.yaml")
          if Files.exists(familyPath) then {
            control.update(toRootInfo(familyPath))
          } else statusLabel.text = "Conjugations not found for given root letters and family!"
        } else {
          statusLabel.text = "Conjugations not found for given root letters and family!"
        }
      case None =>
        control.rootLetters = RootInfoEditorView.DefaultRootLetters
        control.family = NamedTemplate.FormICategoryAGroupATemplate
    }
  }

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

  private def createLabel(label: String) =
    new Label {
      text = label
      style = "-fx-font-weight: bold;"
    }
}

object RootInfoEditorSkin {
  def apply(control: RootInfoEditorView): RootInfoEditorSkin = new RootInfoEditorSkin(control)
}
