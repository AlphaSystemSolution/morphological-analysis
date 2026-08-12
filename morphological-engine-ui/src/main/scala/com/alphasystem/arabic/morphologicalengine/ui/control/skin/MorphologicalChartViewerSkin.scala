package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import arabic.model.{ ArabicLetterType, ArabicSupport, ArabicWord }
import arabic.morphologicalanalysis.ui.ArabicLabelView
import morphologicalengine.conjugation.model.{
  AbbreviatedConjugation,
  ConjugationHeader,
  ConjugationTuple,
  DetailedConjugation,
  MorphologicalTermType,
  NounConjugationGroup,
  VerbConjugationGroup
}
import control.MorphologicalChartViewerView
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.geometry.{ NodeOrientation, Pos }
import scalafx.scene.Node
import scalafx.scene.control.{ Label, ScrollPane }
import scalafx.scene.layout.{ GridPane, HBox, VBox }
import scalafx.scene.paint.Color

class MorphologicalChartViewerSkin(control: MorphologicalChartViewerView)
    extends SkinBase[MorphologicalChartViewerView](control) {

  import MorphologicalChartViewerSkin.*

  private val contentBox = new VBox() {
    alignment = Pos.Center
    spacing = 16
    fillWidth = true
  }

  private val scrollPane = new ScrollPane() {
    content = contentBox
    fitToWidth = true
    vbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
    hbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
  }

  control.morphologicalChartProperty.onChange((_, _, _) => refresh())
  control.errorProperty.onChange((_, _, _) => refresh())

  getChildren.add(scrollPane)
  refresh()

  private def refresh(): Unit = {
    contentBox.children.clear()
    control.error match {
      case Some(message) =>
        contentBox.children.add(new Label(message) { wrapText = true })

      case None =>
        control.morphologicalChart match {
          case None =>
            contentBox.children.add(new Label("Select a row to preview its morphological chart."))

          case Some(chart) =>
            chart
              .abbreviatedConjugation
              .foreach(abbreviated =>
                contentBox
                  .children
                  .add(buildAbbreviatedConjugationPane(chart.conjugationHeader, abbreviated, chart.translation))
              )
            chart.detailedConjugation.foreach(detailed => contentBox.children.add(buildDetailedConjugationPane(detailed)))
        }
    }
  }

  // ---------------------------------------------------------------------
  // Abbreviated conjugation (mirrors AbbreviatedConjugationSkin.java)
  // ---------------------------------------------------------------------

  private def buildAbbreviatedConjugationPane(
    header: ConjugationHeader,
    abbreviated: AbbreviatedConjugation,
    translation: Option[String]
  ): GridPane = {
    val gridPane = new GridPane() {
      alignment = Pos.BaselineCenter
      nodeOrientation = NodeOrientation.RightToLeft
      hgap = 4
      vgap = 4
    }

    var row = 0

    val titleLabel = arabicLabel(toArabicWord(header.title), AbbreviatedTotalWidth, CellHeight)
    titleLabel.font = preferences.arabicFont(preferences.arabicHeadingFontSize)
    titleLabel.stroke = Color.DodgerBlue
    gridPane.add(titleLabel, 0, row, AbbreviatedColumns, 2)
    row += 2

    val typeDetailsLabel = arabicLabel(typeDetailsWord(header), CellWidth * 2, CellHeight * 2)
    typeDetailsLabel.alignment = Pos.CenterLeft
    gridPane.add(typeDetailsLabel, 0, row, 2, 2)

    val translationLabel = arabicLabel(plainLabel(translation.getOrElse("")), CellWidth * 2, CellHeight * 2)
    translationLabel.font = preferences.englishFont
    gridPane.add(translationLabel, 3, row, 2, 2)
    row += 2

    addActiveOrPassiveLine(
      gridPane,
      abbreviated.pastTense,
      abbreviated.presentTense,
      abbreviated.verbalNouns,
      Some(abbreviated.activeParticiple),
      row
    )
    row += 1

    if abbreviated.hasPassiveLine then {
      addActiveOrPassiveLine(
        gridPane,
        abbreviated.pastPassiveTense.getOrElse(""),
        abbreviated.presentPassiveTense.getOrElse(""),
        abbreviated.verbalNouns,
        abbreviated.passiveParticiple,
        row
      )
      row += 1
    }

    addImperativeAndForbiddenLine(gridPane, abbreviated.imperative, abbreviated.forbidden, row)
    row += 1

    if abbreviated.adverbs.nonEmpty then addAdverbLine(gridPane, abbreviated.adverbs, row)

    gridPane
  }

  private def addActiveOrPassiveLine(
    gridPane: GridPane,
    pastTense: String,
    presentTense: String,
    verbalNouns: Seq[String],
    participleMasculine: Option[String],
    row: Int
  ): Unit = {
    gridPane.add(arabicLabel(toArabicWord(pastTense)), 0, row)
    gridPane.add(arabicLabel(toArabicWord(presentTense)), 1, row)
    gridPane.add(arabicLabel(joinWithAnd(verbalNouns)), 3, row)
    gridPane.add(arabicLabel(withPrefix(ParticiplePrefix, toArabicWord(participleMasculine.getOrElse("")))), 4, row)
  }

  private def addImperativeAndForbiddenLine(gridPane: GridPane, imperative: String, forbidden: String, row: Int): Unit = {
    val imperativeLabel = arabicLabel(withPrefix(ImperativePrefix, toArabicWord(imperative)), CellWidth * 2, CellHeight)
    gridPane.add(imperativeLabel, 0, row, 2, 1)
    val forbiddenLabel = arabicLabel(withPrefix(ForbiddingPrefix, toArabicWord(forbidden)), CellWidth * 2, CellHeight)
    gridPane.add(forbiddenLabel, 3, row, 2, 1)
  }

  private def addAdverbLine(gridPane: GridPane, adverbs: Seq[String], row: Int): Unit = {
    val label = arabicLabel(withPrefix(AdverbPrefix, joinWithAnd(adverbs)), AbbreviatedTotalWidth, CellHeight)
    gridPane.add(label, 0, row, AbbreviatedColumns, 1)
  }

  private def typeDetailsWord(header: ConjugationHeader): ArabicWord = {
    val space = ArabicWord(ArabicLetterType.Space)
    val newLine = ArabicWord(ArabicLetterType.NewLine)
    val labels =
      Seq(header.templateTypeLabel, header.weightLabel, header.verbTypeLabel)
        .filter(s => Option(s).exists(_.trim.nonEmpty))
        .map(s => space.concat(ArabicWord(s)))

    labels match {
      case Nil          => ArabicWord()
      case head :: tail => tail.foldLeft(head)((acc, part) => acc.concat(newLine, part))
    }
  }

  // ---------------------------------------------------------------------
  // Detailed conjugation (mirrors DetailedConjugationSkin.java + the
  // Verb/NounConjugationGroupView and Verb/NounDetailedConjugationPairView
  // widgets, folded into helper methods)
  // ---------------------------------------------------------------------

  private def buildDetailedConjugationPane(detailed: DetailedConjugation): VBox = {
    val pane = new VBox() {
      alignment = Pos.Center
      spacing = 12
      fillWidth = true
    }

    addVerbPairs(pane, Some(detailed.presentTense), Some(detailed.pastTense), MorphologicalTermType.PresentTense, MorphologicalTermType.PastTense)
    addNounPairs(
      pane,
      Some(detailed.feminineActiveParticiple),
      Some(detailed.masculineActiveParticiple),
      MorphologicalTermType.ActiveParticipleFeminine,
      MorphologicalTermType.ActiveParticipleMasculine
    )
    addNounPairsSeq(pane, detailed.verbalNouns, MorphologicalTermType.VerbalNoun)
    addVerbPairs(
      pane,
      detailed.presentPassiveTense,
      detailed.pastPassiveTense,
      MorphologicalTermType.PresentPassiveTense,
      MorphologicalTermType.PastPassiveTense
    )
    addNounPairs(
      pane,
      detailed.femininePassiveParticiple,
      detailed.masculinePassiveParticiple,
      MorphologicalTermType.PassiveParticipleFeminine,
      MorphologicalTermType.PassiveParticipleMasculine
    )
    addVerbPairs(
      pane,
      Some(detailed.forbidden),
      Some(detailed.imperative),
      MorphologicalTermType.Forbidden,
      MorphologicalTermType.Imperative,
      imperativeOrForbidden = true
    )
    addNounPairsSeq(pane, detailed.adverbs, MorphologicalTermType.NounOfPlaceAndTime)

    pane
  }

  private def addVerbPairs(
    pane: VBox,
    left: Option[VerbConjugationGroup],
    right: Option[VerbConjugationGroup],
    leftTerm: MorphologicalTermType,
    rightTerm: MorphologicalTermType,
    imperativeOrForbidden: Boolean = false
  ): Unit = {
    if left.isDefined || right.isDefined then {
      val leftPane = verbGroupPane(left, leftTerm.title, imperativeOrForbidden)
      val rightPane = verbGroupPane(right, rightTerm.title, imperativeOrForbidden)
      pane.children.add(pairRow(leftPane, rightPane))
    }
  }

  private def addNounPairs(
    pane: VBox,
    left: Option[NounConjugationGroup],
    right: Option[NounConjugationGroup],
    leftTerm: MorphologicalTermType,
    rightTerm: MorphologicalTermType
  ): Unit = {
    if left.isDefined || right.isDefined then {
      val leftPane = nounGroupPane(left, leftTerm.title)
      val rightPane = nounGroupPane(right, rightTerm.title)
      pane.children.add(pairRow(leftPane, rightPane))
    }
  }

  /** Consumes `groups` as non-overlapping pairs of 2, with the first element of each pair rendered on the *right* and
    * the second on the *left* (matching the intent of the original Java `addNounPairs(NounConjugationGroup[])`).
    */
  private def addNounPairsSeq(pane: VBox, groups: Seq[NounConjugationGroup], term: MorphologicalTermType): Unit = {
    groups
      .grouped(2)
      .foreach { pair =>
        val right = pair.headOption
        val left = pair.lift(1)
        addNounPairs(pane, left, right, term, term)
      }
  }

  private def pairRow(left: Node, right: Node): HBox =
    new HBox() {
      alignment = Pos.Center
      spacing = 12
      children = Seq(left, right)
    }

  private def verbGroupPane(group: Option[VerbConjugationGroup], term: ArabicWord, imperativeOrForbidden: Boolean): GridPane = {
    val gridPane = new GridPane() {
      alignment = Pos.BaselineCenter
      nodeOrientation = NodeOrientation.RightToLeft
      hgap = 4
      vgap = 4
    }

    gridPane.add(arabicLabel(term, GroupTermWidth, GroupHeight), 0, 0, GroupColumns, 1)

    if imperativeOrForbidden then {
      addTupleRow(gridPane, 1, group.map(_.masculineSecondPerson))
      addTupleRow(gridPane, 2, group.map(_.feminineSecondPerson))
    } else {
      addTupleRow(gridPane, 1, group.flatMap(_.masculineThirdPerson))
      addTupleRow(gridPane, 2, group.flatMap(_.feminineThirdPerson))
      addTupleRow(gridPane, 3, group.map(_.masculineSecondPerson))
      addTupleRow(gridPane, 4, group.map(_.feminineSecondPerson))
      addFirstPersonRow(gridPane, 5, group.flatMap(_.firstPerson))
    }

    gridPane
  }

  private def nounGroupPane(group: Option[NounConjugationGroup], term: ArabicWord): GridPane = {
    val gridPane = new GridPane() {
      alignment = Pos.BaselineCenter
      nodeOrientation = NodeOrientation.RightToLeft
      hgap = 4
      vgap = 4
    }

    gridPane.add(arabicLabel(term, GroupTermWidth, GroupHeight), 0, 0, GroupColumns, 1)
    addTupleRow(gridPane, 1, group.map(_.nominative))
    addTupleRow(gridPane, 2, group.map(_.accusative))
    addTupleRow(gridPane, 3, group.map(_.genitive))

    gridPane
  }

  private def addTupleRow(gridPane: GridPane, row: Int, tuple: Option[ConjugationTuple]): Unit = {
    val (singular, dual, plural) = tupleWords(tuple)
    gridPane.add(arabicLabel(singular, GroupCellWidth, GroupCellHeight), 0, row)
    gridPane.add(arabicLabel(dual, GroupCellWidth, GroupCellHeight), 1, row)
    gridPane.add(arabicLabel(plural, GroupCellWidth, GroupCellHeight), 2, row)
  }

  private def addFirstPersonRow(gridPane: GridPane, row: Int, tuple: Option[ConjugationTuple]): Unit = {
    val (singular, _, plural) = tupleWords(tuple)
    gridPane.add(arabicLabel(singular, GroupCellWidth, GroupCellHeight), 0, row)
    gridPane.add(arabicLabel(plural, GroupCellWidth * 2, GroupCellHeight), 1, row, 2, 1)
  }

  private def tupleWords(tuple: Option[ConjugationTuple]): (ArabicWord, ArabicWord, ArabicWord) =
    tuple match {
      case Some(t) => (toArabicWord(t.singular), toArabicWord(t.dual.getOrElse("")), toArabicWord(t.plural))
      case None    => (ArabicWord(), ArabicWord(), ArabicWord())
    }

  // ---------------------------------------------------------------------
  // Shared helpers
  // ---------------------------------------------------------------------

  private def arabicLabel(word: ArabicSupport, width: Double = CellWidth, height: Double = CellHeight): ArabicLabelView = {
    val view = ArabicLabelView(word)
    // `ArabicLabelView` builds its skin eagerly in its constructor, and the skin only reads the control's
    // width/height once at that point - setting `widthDelegate`/`heightDelegate` (raw width/height) afterwards
    // has no lasting effect since the next layout pass overwrites them. Setting the actual Region sizing
    // properties (pref/min/max) is what the parent GridPane's layout negotiation respects.
    view.setPrefWidth(width)
    view.setPrefHeight(height)
    view.setMinWidth(width)
    view.setMinHeight(height)
    view.setMaxWidth(width)
    view.setMaxHeight(height)
    // `ArabicLabelViewSkin` only re-reads `disabledStroke` reactively when the `disable` property itself
    // changes - so `disabledStroke` must be set *before* `disable` is flipped to `true`, otherwise the
    // border color update is missed and the cell renders with no visible border.
    view.font = preferences.arabicFont
    view.disabledStroke = Color.LightGray
    view.disable = true
    view
  }

  private def plainLabel(text: String): ArabicSupport =
    new ArabicSupport {
      override val label: String = text
    }

  private def toArabicWord(value: String): ArabicWord =
    if Option(value).exists(_.trim.nonEmpty) then ArabicWord(value) else ArabicWord()

  private def joinWithAnd(values: Seq[String]): ArabicWord = {
    val words = values.filter(v => Option(v).exists(_.trim.nonEmpty)).map(ArabicWord(_))
    words.headOption.map(head => head.concatenateWithAnd(words.tail*)).getOrElse(ArabicWord())
  }

  private def withPrefix(prefix: ArabicWord, value: ArabicWord): ArabicWord =
    if value.isEmpty then value else prefix.concatWithSpace(value)
}

object MorphologicalChartViewerSkin {

  private val AbbreviatedColumns = 6
  private val CellWidth = 195.0
  private val CellHeight = 64.0
  private val AbbreviatedTotalWidth = CellWidth * AbbreviatedColumns

  private val GroupColumns = 3
  private val GroupCellWidth = 128.0
  private val GroupCellHeight = 64.0
  private val GroupTermWidth = 384.0
  private val GroupHeight = 64.0

  private val ParticiplePrefix = ArabicWord(ArabicLetterType.Fa, ArabicLetterType.Ha, ArabicLetterType.Waw)

  private val ImperativePrefix = ArabicWord(
    ArabicLetterType.Alif,
    ArabicLetterType.Lam,
    ArabicLetterType.AlifHamzaAbove,
    ArabicLetterType.Meem,
    ArabicLetterType.Ra,
    ArabicLetterType.Space,
    ArabicLetterType.Meem,
    ArabicLetterType.Noon,
    ArabicLetterType.Ha
  )

  private val ForbiddingPrefix = ArabicWord(
    ArabicLetterType.Waw,
    ArabicLetterType.Noon,
    ArabicLetterType.Ha,
    ArabicLetterType.Ya,
    ArabicLetterType.Space,
    ArabicLetterType.Ain,
    ArabicLetterType.Noon,
    ArabicLetterType.Ha
  )

  private val AdverbPrefix = ArabicWord(
    ArabicLetterType.Waw,
    ArabicLetterType.Alif,
    ArabicLetterType.Lam,
    ArabicLetterType.Dtha,
    ArabicLetterType.Ra,
    ArabicLetterType.Fa,
    ArabicLetterType.Space,
    ArabicLetterType.Meem,
    ArabicLetterType.Noon,
    ArabicLetterType.Ha
  )

  def apply(control: MorphologicalChartViewerView): MorphologicalChartViewerSkin =
    new MorphologicalChartViewerSkin(control)
}
