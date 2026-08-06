package com.alphasystem
package arabic
package vocabulary
package ui
package control

import com.alphasystem.arabic.fx.ui.Browser
import com.alphasystem.arabic.vocabulary.ui.given
import com.alphasystem.arabic.vocabulary.ui.storage.WordGenerator
import arabic.morphologicalanalysis.ui.{
  ArabicSupportEnumComboBox,
  ListType,
  RootLettersKeyBoardView,
  RootLettersPickerView
}
import javafx.event.{ ActionEvent, EventHandler }
import morphologicalengine.conjugation.builder.ConjugationBuilder
import morphologicalengine.conjugation.model.{
  ConjugationConfiguration,
  ConjugationInput,
  NamedTemplate,
  OutputFormat
}
import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{ Button, Label, TextField }
import scalafx.scene.input.{ KeyCode, KeyCodeCombination, KeyCombination }
import scalafx.scene.layout.{ GridPane, HBox, Priority, VBox }

import java.nio.file.Paths
import scala.util.{ Failure, Success, Try }

class VocabularyEditorView extends VBox {

  private val isMacOs = Option(System.getProperty("os.name")).exists(_.toLowerCase.contains("mac"))
  private val generateShortcutLabel = if isMacOs then "Cmd+G" else "Ctrl+G"

  private val dataDir = Paths.get("/Users/sfali/Documents/Arabic/vocab-data")
  private val conjugationBuilder = ConjugationBuilder()
  private val wordGenerator = new WordGenerator(dataDir)

  private val rootLettersPicker = RootLettersPickerView()
  private val templatePicker =
    ArabicSupportEnumComboBox[NamedTemplate](NamedTemplate.values.toArray, ListType.LABEL_AND_CODE)

  private val translationField = new TextField {
    promptText = "Translation"
  }

  private val generatedWordField = new TextField {
    promptText = "Generated past tense"
    font = preferences.arabicFont
  }

  private val dictionaryBrowser = Browser()
  dictionaryBrowser.prefWidth = 1500

  private val statusLabel = new Label {
    wrapText = true
  }

  private val generateButton = new Button("Generate") {
    delegate.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = generatePastTense()
    })
  }

  private val clearButton = new Button("Clear") {
    delegate.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = clearFields()
    })
  }

  private val addButton = new Button("Add") {
    disable = true
    delegate.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = saveWord()
    })
  }

  private val form = new GridPane {
    hgap = 8
    vgap = 8
    add(new Label("Root letters"), 0, 0)
    add(rootLettersPicker, 1, 0)
    add(new Label("Template"), 0, 1)
    add(templatePicker, 1, 1)
    add(new Label("Translation"), 0, 2)
    add(translationField, 1, 2)
    add(new Label(s"Generated word ($generateShortcutLabel)"), 0, 3)
    add(generatedWordField, 1, 3)

    GridPane.setHgrow(rootLettersPicker, Priority.Always)
    GridPane.setHgrow(templatePicker, Priority.Always)
    GridPane.setHgrow(translationField, Priority.Always)
    GridPane.setHgrow(generatedWordField, Priority.Always)
  }

  padding = Insets(12)
  spacing = 10
  children = Seq(
    form,
    new HBox {
      spacing = 8
      children = Seq(generateButton, addButton, clearButton)
    },
    dictionaryBrowser,
    statusLabel
  )

  VBox.setVgrow(dictionaryBrowser, Priority.Always)

  loadDictionary()
  rootLettersPicker.rootLettersProperty.onChange((_, _, nv) => {
    loadDictionary(nv)
    loadExistingWord()
  })
  templatePicker.getSelectionModel.selectedItemProperty().addListener((_, _, _) => loadExistingWord())
  translationField.text.onChange((_, _, _) => updateAddButtonState())
  translationField.delegate.setOnAction(new EventHandler[ActionEvent] {
    override def handle(event: ActionEvent): Unit = addButton.fire()
  })
  generatedWordField.text.onChange((_, _, _) => updateAddButtonState())
  loadExistingWord()
  updateAddButtonState()
  delegate.sceneProperty().addListener((_, _, scene) => {
    if scene != null then {
      val generateShortcut = new KeyCodeCombination(KeyCode.G, KeyCombination.ShortcutDown)
      scene.accelerators.put(generateShortcut, () => generatePastTense())
    }
  })

  private def generatePastTense(): Unit = {
    val rootLetters = rootLettersPicker.rootLetters
    val template = templatePicker.getSelectionModel.getSelectedItem
    val translation = Option(translationField.text.value).map(_.trim).filter(_.nonEmpty)

    Try {
      val chart = conjugationBuilder.doConjugation(
        input = ConjugationInput(
          rootLetters = rootLetters,
          namedTemplate = template,
          conjugationConfiguration = ConjugationConfiguration(),
          translation = translation
        ),
        outputFormat = OutputFormat.Unicode,
        showDetailedConjugation = false
      )

      chart.abbreviatedConjugation match {
        case Some(conjugation) => conjugation.pastTense
        case None              => throw new IllegalStateException("Unable to generate past tense for the selected input")
      }
    } match {
      case Success(generatedWord) =>
        generatedWordField.text = generatedWord
        statusLabel.text = "Word generated. You can edit it."
      case Failure(ex) =>
        statusLabel.text = ex.getMessage
    }
  }

  private def clearFields(): Unit = {
    rootLettersPicker.rootLetters = RootLettersKeyBoardView.DefaultRootLetters
    templatePicker.getSelectionModel.select(0)
    translationField.text = ""
    generatedWordField.text = ""
    statusLabel.text = ""
    translationField.requestFocus()
  }

  private def updateAddButtonState(): Unit = {
    val translation = Option(translationField.text.value).map(_.trim).getOrElse("")
    val word = Option(generatedWordField.text.value).map(_.trim).getOrElse("")
    addButton.disable = translation.isEmpty || word.isEmpty
  }

  private def loadExistingWord(): Unit = {
    val rootLetters = rootLettersPicker.rootLetters
    val template = templatePicker.getSelectionModel.getSelectedItem

    Option(template).flatMap(t => wordGenerator.findWord(rootLetters, t)) match {
      case Some(existingWord) =>
        translationField.text = existingWord.translation
        generatedWordField.text = existingWord.text
      case None =>
        translationField.text = ""
        generatedWordField.text = ""
    }
  }

  private def saveWord(): Unit = {
    val translation = Option(translationField.text.value).map(_.trim).getOrElse("")
    val word = Option(generatedWordField.text.value).map(_.trim).getOrElse("")
    if translation.isEmpty then {
      statusLabel.text = "Translation is required to save."
    } else if word.isEmpty then {
      statusLabel.text = "Word is required to save."
    } else {
      val rootLetters = rootLettersPicker.rootLetters
      val template = templatePicker.getSelectionModel.getSelectedItem
      Try(wordGenerator.saveWord(rootLetters, template, translation, word)) match {
        case Success(_) =>
          clearFields()
          statusLabel.text = s"Saved to ${dataDir.toString}"
        case Failure(ex) => statusLabel.text = ex.getMessage
      }
    }
  }

  private def loadDictionary(rootLetters: morphologicalengine.conjugation.model.RootLetters = rootLettersPicker.rootLetters): Unit =
    dictionaryBrowser.loadUrl(VocabularyEditorView.getMawridReaderUrl(rootLetters.buckWalterString))
}

object VocabularyEditorView {
  private val DictionaryUrl = "https://ejtaal.net/aa/index.html#bwq="

  private def normalizeDictionaryQuery(query: String): String =
    if query.startsWith("'") then s"a${query.drop(1)}" else query

  private def getMawridReaderUrl(query: String): String =
    s"$DictionaryUrl${normalizeDictionaryQuery(query)}"

  def apply(): VocabularyEditorView = new VocabularyEditorView()
}
