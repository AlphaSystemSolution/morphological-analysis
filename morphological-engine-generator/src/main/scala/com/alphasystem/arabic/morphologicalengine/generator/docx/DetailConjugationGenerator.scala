package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx

import com.alphasystem.openxml.builder.wml.WmlAdapter
import com.alphasystem.openxml.builder.wml.table.TableAdapter.VerticalMergeType
import openxml.builder.wml.table.TableAdapter
import morphologicalengine.conjugation.model.{
  ConjugationTuple,
  DetailedConjugation,
  MorphologicalTermType,
  NounConjugationGroup,
  VerbConjugationGroup
}
import generator.model.ChartConfiguration
import org.docx4j.wml.Tbl

class DetailConjugationGenerator(
  override val chartConfiguration: ChartConfiguration,
  detailedConjugation: DetailedConjugation)
    extends ChartGenerator(chartConfiguration) {

  private val numOfColumns = 7
  private var verticalMergeType: VerticalMergeType = VerticalMergeType.RESTART
  private val tblAdapter = new TableAdapter().startTable(16.24, 16.24, 16.24, 2.56, 16.24, 16.24, 16.24)

  override protected def getChart: Tbl = {
    addTensePairs(
      Some(detailedConjugation.presentTense),
      Some(detailedConjugation.pastTense),
      Some(MorphologicalTermType.PresentTense),
      Some(MorphologicalTermType.PastTense)
    )
    addSeparatorRow(tblAdapter, numOfColumns)

    addNounPairs(detailedConjugation.verbalNouns, MorphologicalTermType.VerbalNoun)

    addNounPairs(
      Some(detailedConjugation.feminineActiveParticiple),
      Some(detailedConjugation.masculineActiveParticiple),
      Some(MorphologicalTermType.ActiveParticipleFeminine),
      Some(MorphologicalTermType.ActiveParticipleMasculine)
    )
    addSeparatorRow(tblAdapter, numOfColumns)

    addTensePairs(
      detailedConjugation.presentPassiveTense,
      detailedConjugation.pastPassiveTense,
      detailedConjugation.presentPassiveTense.map(_ => MorphologicalTermType.PresentPassiveTense),
      detailedConjugation.pastPassiveTense.map(_ => MorphologicalTermType.PastPassiveTense)
    )
    addSeparatorRow(tblAdapter, numOfColumns)

    addNounPairs(
      detailedConjugation.femininePassiveParticiple,
      detailedConjugation.masculinePassiveParticiple,
      detailedConjugation.femininePassiveParticiple.map(_ => MorphologicalTermType.PassiveParticipleFeminine),
      detailedConjugation.masculinePassiveParticiple.map(_ => MorphologicalTermType.PassiveParticipleMasculine)
    )
    addSeparatorRow(tblAdapter, numOfColumns)

    addTensePairs(
      Some(detailedConjugation.forbidden),
      Some(detailedConjugation.imperative),
      Some(MorphologicalTermType.Forbidden),
      Some(MorphologicalTermType.Imperative)
    )
    addSeparatorRow(tblAdapter, numOfColumns)

    addNounPairs(detailedConjugation.adverbs, MorphologicalTermType.NounOfPlaceAndTime)

    tblAdapter.getTable
  }

  private def addTensePairs(
    left: Option[VerbConjugationGroup],
    right: Option[VerbConjugationGroup],
    leftCaption: Option[MorphologicalTermType],
    rightCaption: Option[MorphologicalTermType]
  ): Unit = {
    val showCaption =
      chartConfiguration.showMorphologicalTermCaptionInDetailConjugation && (leftCaption.isDefined || rightCaption.isDefined)
    if showCaption then addCaptionRow(leftCaption, rightCaption)
    addConjugationTuples(left.flatMap(_.masculineThirdPerson), right.flatMap(_.masculineThirdPerson))
    addConjugationTuples(left.flatMap(_.feminineThirdPerson), right.flatMap(_.feminineThirdPerson))
    addConjugationTuples(left.map(_.masculineSecondPerson), right.map(_.masculineSecondPerson))
    addConjugationTuples(left.map(_.feminineSecondPerson), right.map(_.feminineSecondPerson))
    addConjugationTuples(left.flatMap(_.firstPerson), right.flatMap(_.firstPerson))
  }

  private def addNounPairs(
    left: Option[NounConjugationGroup],
    right: Option[NounConjugationGroup],
    leftCaption: Option[MorphologicalTermType],
    rightCaption: Option[MorphologicalTermType]
  ): Unit = {
    val showCaption =
      chartConfiguration.showMorphologicalTermCaptionInDetailConjugation && (leftCaption.isDefined || rightCaption.isDefined)
    if showCaption then addCaptionRow(leftCaption, rightCaption)
    addConjugationTuples(left.map(_.nominative), right.map(_.nominative))
    addConjugationTuples(left.map(_.accusative), right.map(_.accusative))
    addConjugationTuples(left.map(_.genitive), right.map(_.genitive))
  }

  private def addNounPairs(groups: Seq[NounConjugationGroup], morphologicalTermType: MorphologicalTermType): Unit = {
    val pairs = groups.sliding(2, 2).toSeq
    pairs.foreach { pair =>
      val maybeLeft = if pair.size <= 1 then None else pair.headOption
      val maybeRight = pair.lastOption
      addNounPairs(
        maybeLeft,
        maybeRight,
        maybeLeft.map(_ => morphologicalTermType),
        maybeRight.map(_ => morphologicalTermType)
      )
      addSeparatorRow(tblAdapter, numOfColumns)
    }
  }

  private def addCaptionRow(
    maybeLeftCaption: Option[MorphologicalTermType],
    maybeRightCaption: Option[MorphologicalTermType]
  ): Unit = {
    val (leftValue, leftColumnProperties) =
      maybeLeftCaption match
        case Some(value) => (value.label, null)
        case None        => ("", nilBorderColumnProperties)

    val (rightValue, rightColumnProperties) =
      maybeRightCaption match
        case Some(value) => (value.label, null)
        case None        => ("", nilBorderColumnProperties)

    tblAdapter
      .startRow()
      .addColumn(0, 3, leftColumnProperties, getArabicText(leftValue, ArabicCaptionStyle))
      .addColumn(3, 1, verticalMergeType, nilBorderColumnProperties, createNoSpacingStyleP)
      .addColumn(4, 3, rightColumnProperties, getArabicText(rightValue, ArabicCaptionStyle))
      .endRow()

    verticalMergeType = VerticalMergeType.CONTINUE
  }

  private def addConjugationTuples(
    maybeLeftTuple: Option[ConjugationTuple],
    maybeRightTuple: Option[ConjugationTuple]
  ): Unit = {
    if maybeLeftTuple.isDefined || maybeRightTuple.isDefined then {
      tblAdapter.startRow()
      var columnIndex = addConjugationTuple(maybeLeftTuple, 0)
      tblAdapter.addColumn(columnIndex, 1, verticalMergeType, nilBorderColumnProperties, createNoSpacingStyleP)
      columnIndex += 1
      addConjugationTuple(maybeRightTuple, columnIndex)
      tblAdapter.endRow()
      verticalMergeType = VerticalMergeType.CONTINUE
    }
  }

  private def addConjugationTuple(maybeConjugationTuple: Option[ConjugationTuple], beginColumIndex: Int) = {
    var columnIndex = beginColumIndex
    {
      maybeConjugationTuple match
        case Some(conjugationTuple) =>
          val maybeDual = conjugationTuple.dual
          val gridSpan = if maybeDual.isDefined then 1 else 2
          tblAdapter.addColumn(columnIndex, gridSpan, getArabicText(conjugationTuple.plural))
          if maybeDual.isDefined then {
            columnIndex += 1
            tblAdapter.addColumn(columnIndex, getArabicText(maybeDual.get))
          }
          columnIndex += gridSpan
          tblAdapter.addColumn(columnIndex, getArabicText(conjugationTuple.singular))
          columnIndex += 1

        case None =>
          tblAdapter.addColumn(columnIndex, nilBorderColumnProperties, WmlAdapter.getEmptyPara(ArabicTableCenterStyle))
          columnIndex += 1
          tblAdapter.addColumn(columnIndex, nilBorderColumnProperties, WmlAdapter.getEmptyPara(ArabicTableCenterStyle))
          columnIndex += 1
          tblAdapter.addColumn(columnIndex, nilBorderColumnProperties, WmlAdapter.getEmptyPara(ArabicTableCenterStyle))
          columnIndex += 1
    }

    columnIndex
  }

}

object DetailConjugationGenerator {
  def apply(
    chartConfiguration: ChartConfiguration,
    detailedConjugation: DetailedConjugation
  ): DetailConjugationGenerator =
    new DetailConjugationGenerator(chartConfiguration, detailedConjugation)
}
