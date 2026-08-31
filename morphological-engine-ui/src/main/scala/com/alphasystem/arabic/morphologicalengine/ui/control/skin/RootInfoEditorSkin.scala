package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import morphologicalanalysis.ui.{ ArabicSupportEnumComboBox, ListType, RootLettersPickerView }
import morphologicalengine.asciidoc_generator.*
import morphologicalengine.conjugation.forms.NounSupport
import morphologicalengine.conjugation.model.{ NamedTemplate, RootLetters }
import arabic.utils.*
import com.alphasystem.arabic.fx.ui.util.UiUtilities
import com.alphasystem.arabic.morphologicalengine.ui.utils.GetRootInfoService
import javafx.beans.binding.Bindings
import javafx.concurrent.{ Task, Service as JService }
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.concurrent.Service
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.control.{ Button, Label, TextArea, TextField }
import scalafx.scene.input.{ KeyCode, KeyCodeCombination, KeyCombination }
import scalafx.scene.layout.{ BorderPane, GridPane, HBox, Priority }

class RootInfoEditorSkin(control: RootInfoEditorView) extends SkinBase[RootInfoEditorView](control) {

  // temporarily disable generate button
  private val _disable = true
  private val isMacOs = Option(System.getProperty("os.name")).exists(_.toLowerCase.contains("mac"))
  private val generateShortcutLabel = if isMacOs then "Cmd+G" else "Ctrl+G"

  private val getRootInfoService = new GetRootInfoService()
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
  private val generateConjugationsButton = new Button {
    text = s"Generate Conjugations ($generateShortcutLabel)"
    disable = true
    style = "-fx-font-weight: bold;"
    onAction = () => generateConjugations()
  }
  private val otherTranslationsArea = new TextArea {
    font = preferences.englishFont(14.0)
  }

  private val searchPanel = {
    new GridPane {
      hgap = 8
      vgap = 8
      padding = Insets(8)
      add(createLabel("Root letters:"), 0, 0)
      add(rootLettersPicker, 1, 0)
      add(createLabel("Family:"), 0, 1)
      add(familyPicker, 1, 1)
      add(createLabel("Base Translation:"), 0, 2)
      add(baseTranslationField, 1, 2)
      add(statusLabel, 0, 3, 2, 1)
      add(generateConjugationsButton, 0, 4, 2, 1)
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
      padding = Insets(8)
      add(createLabel("Verbal Nouns:"), 0, 0)
      add(verbalNounsPicker, 1, 0)
      add(createLabel("More Translations:"), 0, 1)
      add(otherTranslationsArea, 1, 1)
      style = "-fx-border-color: grey; -fx-border-width: 1px; -fx-border-radius: 4px;"
      GridPane.setHgrow(verbalNounsPicker, Priority.Always)
    }
  }

  private val skin = {
    val hBox = new HBox {
      padding = Insets(12)
      spacing = 10
      children = Seq(searchPanel, verbalNounsPanel)
      style = "-fx-border-color: grey; -fx-border-width: 1px; -fx-border-radius: 4px;"
    }

    new BorderPane {
      center = hBox
      BorderPane.setAlignment(hBox, Pos.Center)
    }
  }

  getChildren.addAll(skin)

  bindKeys()
  verbalNounsPicker.verbalNounsProperty.clear()
  verbalNounsPicker.verbalNounsProperty.addAll(control.verbalNouns)
  rootLettersPicker.rootLettersProperty.bindBidirectional(control.rootLettersProperty)
  familyPicker.valueProperty().bindBidirectional(control.familyProperty)
  baseTranslationField.textProperty().bindBidirectional(control.baseTranslationProperty)
  otherTranslationsArea.textProperty().bindBidirectional(control.translationsProperty)
  generateConjugationsButton
    .disableProperty()
    .bind(Bindings.isEmpty(control.baseTranslationProperty).or(Bindings.isNull(control.baseTranslationProperty)))
  bindBuffers(control.verbalNounsProperty, verbalNounsPicker.verbalNounsProperty)

  loadRootInfo(control.rootLetters, control.family)
  control.rootLettersProperty.onChange((_, _, nv) => loadRootInfo(nv, control.family))
  control.familyProperty.onChange((_, _, nv) => loadRootInfo(control.rootLetters, nv))

  private def loadRootInfo(rootLetters: RootLetters, family: NamedTemplate): Unit = {
    val scene = control.getScene
    if scene != null then {
      UiUtilities.toWaitCursor(control)
    }
    statusLabel.text = ""
    val service = getRootInfoService.service(rootLetters, family)

    getRootInfoService.handleResponse(
      service = service,
      onSucceeded = event => {
        val result = event.getSource.getValue.asInstanceOf[Option[RootInfo]]
        result match {
          case Some(rootInfo) => control.update(rootInfo)
          case None =>
            statusLabel.text = "Conjugations not found for given root letters and family!"
            control.update(
              RootInfo(
                rootLetters = control.rootLetters,
                family = control.family,
                baseTranslation = ""
              )
            )
        }
        UiUtilities.toDefaultCursor(control)
        event.consume()
      },
      onFailed = event => {
        val exception = event.getSource.getException
        exception.printStackTrace()
        statusLabel.text = exception.getMessage
        UiUtilities.toDefaultCursor(control)
        event.consume()
      }
    )
    getRootInfoService.start(service)
  }

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
          val generateShortcut = new KeyCodeCombination(KeyCode.G, KeyCombination.ShortcutDown)
          scene.accelerators.put(generateShortcut, () => generateConjugations())

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

  private def generateConjugations(): Unit = {
    if _disable then return
    val rootInfo = RootInfo(
      rootLetters = control.rootLetters,
      family = control.family,
      baseTranslation = control.baseTranslation,
      verbalNounCodes = control.verbalNouns.map(_.code),
      translations = Option(control.translations) match {
        case Some(value) if !value.isBlank => Option(value)
        case _                             => None
      }
    )

    val generateConjugationsService =
      new Service[Unit](
        new JService[Unit]:
          override def createTask(): Task[Unit] =
            new Task[Unit]():
              override def call(): Unit = {
                ConjugationDocumentGenerator.generateDocuments(
                  conjugationInput = rootInfo.toConjugationInput,
                  rootPath = rootDataPath / Seq(control.rootLetters.toDirectoryName),
                  otherTranslations = rootInfo.translations
                )
              }
      ) {}

    generateConjugationsService.onSucceeded = event => {
      control.rootLetters = RootInfoEditorView.DefaultRootLetters
      control.update(rootInfo)
      event.consume()
    }
    generateConjugationsService.onFailed = event => {
      Console.err.println(s"Failed to generate conjugations: $event")
      event.getSource.getException.printStackTrace()
      event.consume()
    }

    Platform.runLater { () =>
      generateConjugationsService.start()
    }
  }

  private def createLabel(label: String) =
    new Label {
      text = label
      style = "-fx-font-weight: bold;"
    }

  private def selectFamily(familyNumber: Int): Unit = {
    val alias = familyNumber.toString
    NamedTemplate.values.find(_.alias == alias).foreach(template => familyPicker.getSelectionModel.select(template))
  }

  private def selectTemplateByPosition(position: Int): Unit = {
    val index = position - 1
    if index >= 0 && index < familyPicker.getItems.size() then familyPicker.getSelectionModel.select(index)
  }
}

object RootInfoEditorSkin {
  def apply(control: RootInfoEditorView): RootInfoEditorSkin = new RootInfoEditorSkin(control)
}
