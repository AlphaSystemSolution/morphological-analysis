package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import javafx.scene.control.{ Control, SkinBase }
import scalafx.Includes.*
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.control.{Tab, TabPane }
import scalafx.scene.layout.{ BorderPane, GridPane, Pane, Region, VBox }

class MorphologicalEngineSkin2(control: MorphologicalEngineView2) extends SkinBase[Control](control) {

  private val wordEditorView = RootInfoEditorView()
  private val dictionaryView = DictionaryView()
  dictionaryView.setPrefWidth(1500)

  private val viewTabs = new TabPane {
    tabClosingPolicy = TabPane.TabClosingPolicy.Unavailable
    tabs = Seq(addDictionaryTab())
  }

  dictionaryView.rootLettersProperty.bind(wordEditorView.rootLettersProperty)

  private val mainPane = {
    val editorView = new VBox {
      padding = Insets(12)
      spacing = 10
    }
    val emptyPane = new Pane {
      prefHeight = 50
    }
    editorView.getChildren.addAll(wordEditorView, viewTabs)

    new BorderPane {
      top = emptyPane
      center = editorView
      BorderPane.setAlignment(emptyPane, Pos.Center)
      BorderPane.setAlignment(editorView, Pos.Center)
    }
  }

  getChildren.addAll(mainPane)

  private def addDictionaryTab() = {
    dictionaryView.setDisable(true)
    new Tab {
      text = "Dictionary"
      userData = "dictionary"
      closable = false
      content = dictionaryView
    }
  }
}

object MorphologicalEngineSkin2 {
  def apply(control: MorphologicalEngineView2): MorphologicalEngineSkin2 = new MorphologicalEngineSkin2(control)
}
