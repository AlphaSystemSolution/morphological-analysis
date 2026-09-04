package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import arabic.fx.ui.util.UIUserPreferences
import arabic.model.ArabicLetters.WordNewLine
import arabic.model.{ ArabicSupport, ArabicWord }
import arabic.morphologicalanalysis.ui.ArabicLabelView
import morphologicalengine.conjugation.model.{ AbbreviatedConjugation, ConjugationHeader }
import javafx.scene.control.SkinBase
import scalafx.scene.layout.{ BorderPane, GridPane, VBox }
import scalafx.Includes.*
import scalafx.geometry.NodeOrientation.RightToLeft
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.paint.Color
import scalafx.scene.text.Font

class AbbreviatedConjugationSkin(control: AbbreviatedConjugationView)(using preferences: UIUserPreferences)
    extends SkinBase[AbbreviatedConjugationView](control) {

  import AbbreviatedConjugationSkin.*

  private val contentBox = new GridPane {
    alignment = Pos.BaselineCenter
    nodeOrientation = RightToLeft
  }

  private val rootPane = new BorderPane {
    center = contentBox
    BorderPane.setAlignment(contentBox, Pos.Center)
  }

  setup(control.conjugationHeader, control.abbreviatedConjugation)
  control.conjugationHeaderProperty.onChange((_, _, nv) => setup(nv, control.abbreviatedConjugation))
  control.abbreviatedConjugationProperty.onChange((_, _, nv) => setup(control.conjugationHeader, nv))
  getChildren.add(rootPane)

  private def setup(conjugationHeader: ConjugationHeader, abbreviatedConjugation: AbbreviatedConjugation): Unit = {
    contentBox.children.clear()
    var row = 0

    if Option(conjugationHeader).isDefined then {
      contentBox.add(
        createArabicLabelView(ArabicWord(conjugationHeader.title), preferences.arabicHeadingFont, TotalWidth, Height),
        0,
        row,
        NumOfColumns,
        2
      )
      row += 2

      contentBox.add(createConjugationTypeDetails(conjugationHeader), 0, row, 2, 1)
      contentBox.add(createRootLetters(conjugationHeader), 2, row, 2, 1)
      row += 1
    }

    if Option(abbreviatedConjugation).isDefined then {
      val verbalNouns = concatMultipleWords(abbreviatedConjugation.verbalNouns)
      val label =
        createActiveOrPassiveLine(
          abbreviatedConjugation.pastTense,
          abbreviatedConjugation.presentTense,
          abbreviatedConjugation.activeParticiple,
          verbalNouns
        )
      contentBox.add(label, 0, row, NumOfColumns, 1)
      row += 1

      if abbreviatedConjugation.hasPassiveLine then {
        val label =
          createActiveOrPassiveLine(
            abbreviatedConjugation.pastPassiveTense.getOrElse(""),
            abbreviatedConjugation.presentPassiveTense.getOrElse(""),
            abbreviatedConjugation.passiveParticiple.getOrElse(""),
            verbalNouns
          )

        contentBox.add(label, 0, row, NumOfColumns, 1)
        row += 1
      }

      contentBox.add(
        createImperativeLine(
          abbreviatedConjugation.imperative,
          abbreviatedConjugation.forbidden,
          abbreviatedConjugation.adverbs
        ),
        0,
        row,
        NumOfColumns,
        1
      )
    }
  }

  private def createConjugationTypeDetails(conjugationHeader: ConjugationHeader) = {
    val label = ArabicWord().concat(
      ArabicWord(conjugationHeader.templateTypeLabel),
      WordNewLine,
      ArabicWord(conjugationHeader.weightLabel),
      WordNewLine,
      ArabicWord(conjugationHeader.verbTypeLabel)
    )

    val arabicLabelView = createArabicLabelView(label, preferences.arabicFont(20), Width * 2, Height + 24)
    new VBox {
      style = Style
      children.addOne(arabicLabelView)
      alignment = Pos.BaselineRight
    }
  }

  private def createRootLetters(conjugationHeader: ConjugationHeader) = {
    val rootLettersLabel =
      createArabicLabelView(conjugationHeader.rootLetters.arabicWord, preferences.arabicFont, Width * 2, Height)
    new VBox {
      style = Style
      children.addOne(rootLettersLabel)
      alignment = Pos.CenterRight
    }
  }

  private def createActiveOrPassiveLine(
    pastTense: String,
    presentTense: String,
    masculineParticiple: String,
    verbalNouns: ArabicWord
  ) = {
    val arabicWord = ArabicWord(pastTense)
      .concatWithSpace(ArabicWord(presentTense))
      .concatWithSpace(verbalNouns)
      .concatWithSpace(ParticiplePrefix.concatWithSpace(ArabicWord(masculineParticiple)))
    new VBox {
      style = Style
      children.addOne(createArabicLabelView(arabicWord, preferences.arabicFont, TotalWidth, Height))
    }
  }

  private def createImperativeLine(imperative: String, forbidden: String, adverbs: Seq[String]) = {
    val arabicWord = ImperativePrefix
      .concatWithSpace(ArabicWord(imperative))
      .concatWithSpace(ForbiddenPrefix)
      .concatWithSpace(ArabicWord(forbidden))
      .concatWithSpace(AdverbPrefix)
      .concatWithSpace(concatMultipleWords(adverbs))
    new VBox {
      style = Style
      children.addOne(createArabicLabelView(arabicWord, preferences.arabicFont, TotalWidth, Height))
    }
  }

  private def concatMultipleWords(words: Seq[String]) =
    words.foldLeft(ArabicWord()) { case (acc, label) =>
      if acc.isEmpty then ArabicWord(label) else acc.concatenateWithAnd(ArabicWord(label))
    }

  private def createArabicLabelView(word: ArabicSupport, labelFont: Font, width: Double, height: Double) = {
    val arabicLabelView = new ArabicLabelView(word) {
      disabledStroke = Color.Transparent
      font = labelFont
      alignment = Pos.BaselineRight
    }
    arabicLabelView.setPrefWidth(width)
    arabicLabelView.setPrefHeight(height)
    arabicLabelView.setMinWidth(width)
    arabicLabelView.setMinHeight(height)
    arabicLabelView.setMaxWidth(width)
    arabicLabelView.setMaxHeight(height)
    arabicLabelView.setDisable(true)
    arabicLabelView
  }
}

object AbbreviatedConjugationSkin {

  private val Style = "-fx-border-color: lightgrey; -fx-border-width: 1px; -fx-border-radius: 4px;"
  private val WidthInDetailMode = 191
  private val Spacing = 12
  private val NumOfColumns = 4
  private val TotalWidth = (WidthInDetailMode * NumOfColumns) + Spacing
  private val Width = TotalWidth / NumOfColumns
  private val Height = 64

  def apply(control: AbbreviatedConjugationView)(using preferences: UIUserPreferences): AbbreviatedConjugationSkin =
    new AbbreviatedConjugationSkin(control)
}
