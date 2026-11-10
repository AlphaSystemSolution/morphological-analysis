package com.alphasystem
package arabic
package examples_generator
package ui
package control
package root_info
package skin

import arabic.fx.ui.util.{ DefaultGap, DefaultInsets, UIUserPreferences, createLabel }
import arabic.morphologicalanalysis.ui.{ ArabicSupportEnumComboBox, ListType, RootLettersPickerView }
import arabic.morphologicalengine.conjugation.forms.NounSupport
import arabic.morphologicalengine.conjugation.model.NamedTemplate
import arabic.morphologicalengine.ui.control.VerbalNounPickerView
import com.alphasystem.arabic.examples_generator.ui.model.JussiveParticle
import com.alphasystem.arabic.examples_generator.ui.model.JussiveParticle.NoValue
import javafx.scene.control.SkinBase
import org.controlsfx.control.ToggleSwitch
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Pos
import scalafx.scene.control.TextField
import scalafx.scene.input.{ KeyCode, KeyCodeCombination, KeyCombination }
import scalafx.scene.layout.{ BorderPane, ColumnConstraints, GridPane }

class RootInfoEditorSkin(control: RootInfoEditorView)(using preferences: UIUserPreferences)
    extends SkinBase[RootInfoEditorView](control) {

  private val isMacOs = Option(System.getProperty("os.name")).exists(_.toLowerCase.contains("mac"))

  private val rootLettersPicker = RootLettersPickerView()
  private val familyPicker = ArabicSupportEnumComboBox[NamedTemplate](NamedTemplate.values, ListType.LABEL_AND_CODE)
  private val translationField = new TextField {
    promptText = "Translation"
    prefColumnCount = 30
  }
  private val verbalNounsPicker = VerbalNounPickerView()
  private val jussiveParticleComBox = JussiveParticleComboBox()
  jussiveParticleComBox
    .valueProperty()
    .onChange((_, _, nv) =>
      if nv == NoValue then control.jussiveParticle = None else control.jussiveParticle = nv.arabicJussiveParticle
    )
  control
    .jussiveParticleProperty
    .onChange((_, _, nv) => jussiveParticleComBox.value = JussiveParticle.fromArabicValue(nv))

  private val skipRuleProcessingCheckBox = new ToggleSwitch()
  private val removePassiveLineCheckBox = new ToggleSwitch()

  private val checkBoxPanel =
    new GridPane {
      hgap = DefaultGap
      vgap = DefaultGap

      add(createLabel("Skip Rule Processing:"), 0, 0)
      add(skipRuleProcessingCheckBox, 1, 0)

      add(createLabel("Remove Passive Line:"), 2, 0)
      add(removePassiveLineCheckBox, 3, 0)
    }

  private val searchPanel =
    new GridPane {
      hgap = DefaultGap
      vgap = DefaultGap
      padding = DefaultInsets
      columnConstraints = Seq(
        new ColumnConstraints {
          minWidth <== control.labelColumnWidthProperty
          prefWidth <== control.labelColumnWidthProperty
          maxWidth <== control.labelColumnWidthProperty
        },
        new ColumnConstraints()
      )

      private var row = 0
      add(createLabel("Root letters:"), 0, row)
      add(rootLettersPicker, 1, row)

      row += 1
      add(createLabel("Family:"), 0, row)
      add(familyPicker, 1, row)

      row += 1
      add(createLabel("Base Translation:"), 0, row)
      add(translationField, 1, row)

      row += 1
      add(createLabel("Jussive Particle:"), 0, row)
      add(jussiveParticleComBox, 1, row)

      row += 1
      add(checkBoxPanel, 0, row, 2, 1)

      row += 1
      add(createLabel("Verbal Nouns:"), 0, row)
      add(verbalNounsPicker, 1, row)
    }

  getChildren.addAll(new BorderPane {
    center = searchPanel
    BorderPane.setAlignment(searchPanel, Pos.Center)
  })

  bindKeys()
  verbalNounsPicker.verbalNounsProperty.clear()
  verbalNounsPicker.verbalNounsProperty.addAll(control.verbalNouns)
  rootLettersPicker.rootLettersProperty.bindBidirectional(control.rootLettersProperty)
  familyPicker.valueProperty().bindBidirectional(control.familyProperty)
  translationField.textProperty().bindBidirectional(control.translationProperty)
  skipRuleProcessingCheckBox.selectedProperty().bindBidirectional(control.skipRuleProcessingProperty)
  removePassiveLineCheckBox.selectedProperty().bindBidirectional(control.removePassiveLineProperty)
  bindBuffers(control.verbalNounsProperty, verbalNounsPicker.verbalNounsProperty)

  private def bindBuffers(buf1: ObservableBuffer[NounSupport], buf2: ObservableBuffer[NounSupport]): Unit = {
    var updating = false

    buf1.onChange { (source, changes) =>
      if !updating then {
        updating = true
        try {
          changes.foreach {
            case ObservableBuffer.Add(position, added)      => buf2.insertAll(position, added)
            case ObservableBuffer.Remove(position, removed) => buf2.remove(position, removed.size)
            case ObservableBuffer.Reorder(from, to, perm)   =>
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
            case ObservableBuffer.Add(position, added)      => buf1.insertAll(position, added)
            case ObservableBuffer.Remove(position, removed) => buf1.remove(position, removed.size)
            case ObservableBuffer.Reorder(from, to, perm)   => buf1.setAll(source)
            case _                                          => buf1.setAll(source)
          }
        } finally {
          updating = false
        }
      }
    }
  }

  private def bindKeys(): Unit = {
    control
      .sceneProperty()
      .addListener((_, _, scene) => {
        if Option(scene).isDefined then {
          scene
            .accelerators
            .put(new KeyCodeCombination(KeyCode.Digit2, KeyCombination.ShortcutDown), () => selectFamily(2))
          scene
            .accelerators
            .put(new KeyCodeCombination(KeyCode.Digit3, KeyCombination.ShortcutDown), () => selectFamily(3))
          scene
            .accelerators
            .put(new KeyCodeCombination(KeyCode.Digit4, KeyCombination.ShortcutDown), () => selectFamily(4))
          scene
            .accelerators
            .put(new KeyCodeCombination(KeyCode.Digit5, KeyCombination.ShortcutDown), () => selectFamily(5))
          scene
            .accelerators
            .put(new KeyCodeCombination(KeyCode.Digit6, KeyCombination.ShortcutDown), () => selectFamily(6))
          scene
            .accelerators
            .put(new KeyCodeCombination(KeyCode.Digit7, KeyCombination.ShortcutDown), () => selectFamily(7))
          scene
            .accelerators
            .put(new KeyCodeCombination(KeyCode.Digit8, KeyCombination.ShortcutDown), () => selectFamily(8))
          scene
            .accelerators
            .put(new KeyCodeCombination(KeyCode.Digit9, KeyCombination.ShortcutDown), () => selectFamily(9))
          scene
            .accelerators
            .put(new KeyCodeCombination(KeyCode.Digit0, KeyCombination.ShortcutDown), () => selectFamily(10))

          scene
            .accelerators
            .put(
              new KeyCodeCombination(KeyCode.N, KeyCombination.ShortcutDown, KeyCombination.ShiftDown),
              () => selectTemplateByPosition(1)
            )
          scene
            .accelerators
            .put(
              new KeyCodeCombination(KeyCode.D, KeyCombination.ShortcutDown, KeyCombination.ShiftDown),
              () => selectTemplateByPosition(2)
            )
          scene
            .accelerators
            .put(
              new KeyCodeCombination(KeyCode.F, KeyCombination.ShortcutDown, KeyCombination.ShiftDown),
              () => selectTemplateByPosition(3)
            )
          scene
            .accelerators
            .put(
              new KeyCodeCombination(KeyCode.S, KeyCombination.ShortcutDown, KeyCombination.ShiftDown),
              () => selectTemplateByPosition(4)
            )
          scene
            .accelerators
            .put(
              new KeyCodeCombination(KeyCode.H, KeyCombination.ShortcutDown, KeyCombination.ShiftDown),
              () => selectTemplateByPosition(5)
            )
          scene
            .accelerators
            .put(
              new KeyCodeCombination(KeyCode.K, KeyCombination.ShortcutDown, KeyCombination.ShiftDown),
              () => selectTemplateByPosition(6)
            )
        }
      })
  }

  private def selectFamily(familyNumber: Int): Unit = {
    val alias = familyNumber.toString
    NamedTemplate.values.find(_.alias == alias).foreach(template => familyPicker.getSelectionModel.select(template))
  }

  private def selectTemplateByPosition(position: Int): Unit = {
    val index = position - 1
    if index >= 0 && index < familyPicker.getItems.size() then familyPicker.getSelectionModel.select(index)
  }

  private def generateShortcutLabel(key: KeyCode) = {
    if isMacOs then s"Cmd+${key.name}" else s"Ctrl+${key.name}"
  }
}

object RootInfoEditorSkin {
  def apply(control: RootInfoEditorView)(using preferences: UIUserPreferences): RootInfoEditorSkin =
    new RootInfoEditorSkin(control)
}
