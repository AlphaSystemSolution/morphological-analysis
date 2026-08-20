package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import arabic.morphologicalanalysis.ui.{ ArabicSupportEnumComboBox, ListType, RootLettersPickerView }
import arabic.morphologicalengine.conjugation.model.NamedTemplate
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.control.{ Button, Label, TextField }
//import scalafx.scene.input.{ KeyCode, KeyCodeCombination, KeyCombination }
import scalafx.scene.layout.{ BorderPane, GridPane, Priority, VBox }

class WordEditorSkin(control: WordEditorView) extends SkinBase[WordEditorView](control) {

  private val rootLettersPicker = RootLettersPickerView()
  private val templatePicker = ArabicSupportEnumComboBox[NamedTemplate](NamedTemplate.values, ListType.LABEL_AND_CODE)
  private val baseTranslationField = new TextField {
    promptText = "Base Translation"
    prefColumnCount = 30
  }

  private val searchPanel = {
    new GridPane {
      hgap = 8
      vgap = 8
      add(new Label("Root letters:"), 0, 0)
      add(rootLettersPicker, 1, 0)
      add(new Label("Family:"), 0, 1)
      add(templatePicker, 1, 1)
      add(new Label("Base Translation:"), 0, 2)
      add(baseTranslationField, 1, 2)

      GridPane.setHgrow(rootLettersPicker, Priority.Always)
      GridPane.setHgrow(templatePicker, Priority.Always)
      GridPane.setHgrow(baseTranslationField, Priority.Always)
    }
  }

  private val skin = {
    val vbox = new VBox {
      padding = Insets(12)
      spacing = 10
      children = Seq(searchPanel)
    }

    new BorderPane {
      center = vbox
      BorderPane.setAlignment(vbox, Pos.Center)
    }
  }

  getChildren.addAll(skin)

  rootLettersPicker
    .rootLettersProperty
    .onChange((_, _, nv) => {
      println(s">>>>> $nv")
    })

  control
    .rootLettersProperty
    .onChange((_, _, nv) => {
      println(nv)
    })
}

object WordEditorSkin {
  def apply(control: WordEditorView): WordEditorSkin = new WordEditorSkin(control)
}
