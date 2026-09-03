package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import control.root_info.RootInfoEditorView
import javafx.scene.control.{Control, SkinBase}
import scalafx.Includes.*
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Tab, TabPane}
import scalafx.scene.layout.{BorderPane, Pane, VBox}

class MorphologicalEngineSkin(control: MorphologicalEngineView) extends SkinBase[Control](control) {

  private val wordEditorView = RootInfoEditorView()
  private val dictionaryView = DictionaryView()
  private val morphologicalChartViewerView = MorphologicalChartViewerView()

  private val viewTabs = new TabPane {
    tabClosingPolicy = TabPane.TabClosingPolicy.Unavailable
    tabs = Seq(morphologicalChartViewerTab, dictionaryTab)
  }

  dictionaryView.rootLettersProperty.bind(wordEditorView.rootLettersProperty)
  morphologicalChartViewerView.morphologicalChartProperty.bind(wordEditorView.morphologicalChartProperty)

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

  private lazy val morphologicalChartViewerTab = {
    new Tab {
      text = "Conjugations"
      userData = "morphologicalChartViewer"
      closable = false
      content = morphologicalChartViewerView
    }
  }

  private lazy val dictionaryTab = {
    dictionaryView.setDisable(true)
    new Tab {
      text = "Dictionary"
      userData = "dictionary"
      closable = false
      content = dictionaryView
    }
  }
}

object MorphologicalEngineSkin {
  def apply(control: MorphologicalEngineView): MorphologicalEngineSkin = new MorphologicalEngineSkin(control)
}
