package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx
package single_row

import arabic.model.ArabicWord
import docx4j.builder.wml.table.{ ColumnData, ColumnInput, TableAdapter }
import morphologicalengine.conjugation.model.{ AbbreviatedConjugation, MorphologicalTermType }
import generator.model.ChartConfiguration
import org.docx4j.wml.Tbl

class AbbreviatedConjugationGenerator(
  override val chartConfiguration: ChartConfiguration,
  header: String,
  abbreviatedConjugations: Seq[AbbreviatedConjugation])
    extends ChartGenerator(chartConfiguration) {

  private var totalNumberOfColumns = 0
  private val smallerCaptionSize = Some(chartConfiguration.arabicFontSize - 4)
  private val tblAdapter =
    if abbreviatedConjugations.forall(_.adverbs.isEmpty) then {
      totalNumberOfColumns = 10
      new TableAdapter()
        .withColumnInputs(
          ColumnInput("col_1", 10.0),
          ColumnInput("col_2", 10.0),
          ColumnInput("col_3", 10.0),
          ColumnInput("col_4", 10.0),
          ColumnInput("col_5", 10.0),
          ColumnInput("col_6", 10.0),
          ColumnInput("col_7", 10.0),
          ColumnInput("col_8", 10.0),
          ColumnInput("col_9", 10.0),
          ColumnInput("col_10", 10.0)
        )
        .startTable()
    } else {
      totalNumberOfColumns = 11
      new TableAdapter()
        .withColumnInputs(
          ColumnInput("col_1", 10.0),
          ColumnInput("col_2", 9.0),
          ColumnInput("col_3", 9.0),
          ColumnInput("col_4", 9.0),
          ColumnInput("col_5", 9.0),
          ColumnInput("col_6", 9.0),
          ColumnInput("col_7", 9.0),
          ColumnInput("col_8", 9.0),
          ColumnInput("col_9", 9.0),
          ColumnInput("col_10", 9.0),
          ColumnInput("col_11", 9.0)
        )
        .startTable()
    }

  override protected def getChart: Tbl = {
    tblAdapter
      .startRow()
      .addColumn(
        ColumnData(0)
          .withGridSpanValue(totalNumberOfColumns)
          .withColumnProperties(nilBorderColumnProperties)
          .withContent(getArabicText(header, ArabicHeadingStyle))
      )
      .endRow()

    abbreviatedConjugations.foreach { abbreviatedConjugation =>
      if chartConfiguration.showMorphologicalTermCaptionInAbbreviatedConjugation then
        buildCaption(abbreviatedConjugation)
      buildRow(abbreviatedConjugation)
    }
    addSeparatorRow(tblAdapter, totalNumberOfColumns)
    tblAdapter.getTable
  }

  private def buildCaption(abbreviatedConjugation: AbbreviatedConjugation): Unit = {
    var columnIndex = 0
    tblAdapter.startRow()

    if abbreviatedConjugation.adverbs.nonEmpty then {
      tblAdapter
        .addColumn(
          ColumnData(columnIndex).withContent(
            getArabicText(
              MorphologicalTermType.NounOfPlaceAndTime.shortTitle.unicode,
              ArabicCaptionStyle,
              smallerCaptionSize
            )
          )
        )
      columnIndex += 1
    }

    tblAdapter
      .addColumn(
        ColumnData(columnIndex).withContent(
          getArabicText(
            MorphologicalTermType.Forbidden.shortTitle.unicode,
            ArabicCaptionStyle,
            smallerCaptionSize
          )
        )
      )
    columnIndex += 1

    tblAdapter
      .addColumn(
        ColumnData(columnIndex).withContent(
          getArabicText(
            MorphologicalTermType.Imperative.shortTitle.unicode,
            ArabicCaptionStyle,
            smallerCaptionSize
          )
        )
      )
    columnIndex += 1

    tblAdapter
      .addColumn(
        ColumnData(columnIndex).withContent(
          getArabicText(
            MorphologicalTermType.PassiveParticipleMasculine.shortTitle.unicode,
            ArabicCaptionStyle,
            smallerCaptionSize
          )
        )
      )
    columnIndex += 1

    tblAdapter
      .addColumn(
        ColumnData(columnIndex).withContent(
          getArabicText(
            MorphologicalTermType.VerbalNoun.shortTitle.unicode,
            ArabicCaptionStyle,
            smallerCaptionSize
          )
        )
      )
    columnIndex += 1

    tblAdapter
      .addColumn(
        ColumnData(columnIndex).withContent(
          getArabicText(
            MorphologicalTermType.PresentPassiveTense.shortTitle.unicode,
            ArabicCaptionStyle,
            smallerCaptionSize
          )
        )
      )
    columnIndex += 1

    tblAdapter
      .addColumn(
        ColumnData(columnIndex).withContent(
          getArabicText(
            MorphologicalTermType.PastPassiveTense.shortTitle.unicode,
            ArabicCaptionStyle,
            smallerCaptionSize
          )
        )
      )
    columnIndex += 1

    tblAdapter
      .addColumn(
        ColumnData(columnIndex).withContent(
          getArabicText(
            MorphologicalTermType.ActiveParticipleMasculine.shortTitle.unicode,
            ArabicCaptionStyle,
            smallerCaptionSize
          )
        )
      )
    columnIndex += 1

    tblAdapter
      .addColumn(
        ColumnData(columnIndex).withContent(
          getArabicText(
            MorphologicalTermType.VerbalNoun.shortTitle.unicode,
            ArabicCaptionStyle,
            smallerCaptionSize
          )
        )
      )
    columnIndex += 1

    tblAdapter
      .addColumn(
        ColumnData(columnIndex).withContent(
          getArabicText(
            MorphologicalTermType.PresentTense.shortTitle.unicode,
            ArabicCaptionStyle,
            smallerCaptionSize
          )
        )
      )
    columnIndex += 1

    tblAdapter
      .addColumn(
        ColumnData(columnIndex).withContent(
          getArabicText(
            MorphologicalTermType.PastTense.shortTitle.unicode,
            ArabicCaptionStyle,
            smallerCaptionSize
          )
        )
      )
    columnIndex += 1

    tblAdapter.endRow()
  }

  private def buildRow(abbreviatedConjugation: AbbreviatedConjugation): Unit = {
    var columnIndex = 0
    tblAdapter.startRow()

    if abbreviatedConjugation.adverbs.nonEmpty then {
      tblAdapter
        .addColumn(
          ColumnData(columnIndex).withContent(
            getArabicText(concatenateWithAnd(abbreviatedConjugation.adverbs.map(ArabicWord(_))))
          )
        )
      columnIndex += 1
    }

    tblAdapter.addColumn(ColumnData(columnIndex).withContent(getArabicText(abbreviatedConjugation.forbidden)))
    columnIndex += 1

    tblAdapter.addColumn(ColumnData(columnIndex).withContent(getArabicText(abbreviatedConjugation.imperative)))
    columnIndex += 1

    tblAdapter.addColumn(
      ColumnData(columnIndex).withContent(getArabicText(abbreviatedConjugation.passiveParticiple.getOrElse("")))
    )
    columnIndex += 1

    tblAdapter
      .addColumn(
        ColumnData(columnIndex).withContent(
          getArabicText(concatenateWithAnd(abbreviatedConjugation.verbalNouns.map(ArabicWord(_))))
        )
      )
    columnIndex += 1

    tblAdapter
      .addColumn(
        ColumnData(columnIndex).withContent(getArabicText(abbreviatedConjugation.presentPassiveTense.getOrElse("")))
      )
    columnIndex += 1

    tblAdapter
      .addColumn(
        ColumnData(columnIndex).withContent(getArabicText(abbreviatedConjugation.pastPassiveTense.getOrElse("")))
      )
    columnIndex += 1

    tblAdapter
      .addColumn(ColumnData(columnIndex).withContent(getArabicText(abbreviatedConjugation.activeParticiple)))
    columnIndex += 1

    tblAdapter
      .addColumn(
        ColumnData(columnIndex)
          .withContent(getArabicText(concatenateWithAnd(abbreviatedConjugation.verbalNouns.map(ArabicWord(_)))))
      )
    columnIndex += 1

    tblAdapter.addColumn(ColumnData(columnIndex).withContent(getArabicText(abbreviatedConjugation.presentTense)))
    columnIndex += 1

    tblAdapter.addColumn(ColumnData(columnIndex).withContent(getArabicText(abbreviatedConjugation.pastTense)))
    tblAdapter.endRow()
  }
}

object AbbreviatedConjugationGenerator {
  def apply(
    chartConfiguration: ChartConfiguration,
    header: String,
    abbreviatedConjugations: Seq[AbbreviatedConjugation]
  ): AbbreviatedConjugationGenerator =
    new AbbreviatedConjugationGenerator(chartConfiguration, header, abbreviatedConjugations)
}
