package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import arabic.fx.ui.Browser
import arabic.utils.*
import morphologicalengine.asciidoc_generator.*
import morphologicalengine.conjugation.model.{ NamedTemplate, RootLetters }
import javafx.scene.control.{ Control, SkinBase }
import scalafx.Includes.*
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.control.{ Tab, TabPane }
import scalafx.scene.layout.{ BorderPane, Pane, VBox }

import java.nio.file.Files

class MorphologicalEngineSkin2(control: MorphologicalEngineView2) extends SkinBase[Control](control) {

  private val wordEditorView = RootInfoEditorView()
  private val dictionaryView = DictionaryView()
  private val conjugationBrowser = Browser()
  dictionaryView.setPrefWidth(1500)

  private val viewTabs = new TabPane {
    tabClosingPolicy = TabPane.TabClosingPolicy.Unavailable
    tabs = Seq(conjugationBrowserTab, dictionaryTab)
  }

  dictionaryView.rootLettersProperty.bind(wordEditorView.rootLettersProperty)
  wordEditorView.rootLettersProperty.onChange((_, _, nv) => loadConjugations(nv, wordEditorView.family))
  wordEditorView.familyProperty.onChange((_, _, nv) => loadConjugations(wordEditorView.rootLetters, nv))
  loadConjugations(wordEditorView.rootLetters, wordEditorView.family)

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

  private lazy val conjugationBrowserTab = {
    new Tab {
      text = "Conjugations"
      userData = "conjugationBrowser"
      closable = false
      content = conjugationBrowser
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

  private def loadConjugations(rootLetters: RootLetters, family: NamedTemplate) = {
    viewTabs.selectionModel().select(conjugationBrowserTab)
    val rootDirectoryPath = rootDataPath / Seq(rootLetters.toDirectoryName)
    if Files.exists(rootDirectoryPath) then {
      val familyFile = rootDirectoryPath / Seq(s"$family.yaml")
      val conjugationUrl = (rootDirectoryPath / Seq("main.html")).toUri.toURL
      conjugationBrowser.loadUrl(s"$conjugationUrl#${rootLetters.buckWalterString}_$family")
      if !Files.exists(familyFile) then {
        viewTabs.selectionModel().select(dictionaryTab)
      }
    } else {
      val htmlFile = rootPath / Seq("main.html")
      conjugationBrowser.loadUrl(htmlFile.toFile)
    }
  }
}

object MorphologicalEngineSkin2 {
  def apply(control: MorphologicalEngineView2): MorphologicalEngineSkin2 = new MorphologicalEngineSkin2(control)
}
