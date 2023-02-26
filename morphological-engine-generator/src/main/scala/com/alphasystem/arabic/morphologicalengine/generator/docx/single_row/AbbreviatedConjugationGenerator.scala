package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx
package single_row

import arabic.model.ArabicWord
import openxml.builder.wml.table.TableAdapter
import morphologicalengine.conjugation.model.AbbreviatedConjugation
import generator.model.ChartConfiguration
import org.docx4j.wml.Tbl

class AbbreviatedConjugationGenerator(
  override val chartConfiguration: ChartConfiguration,
  header: String,
  abbreviatedConjugations: Seq[AbbreviatedConjugation])
    extends ChartGenerator(chartConfiguration) {

  private var totalNumberOfColumns = 0

  private val tblAdapter =
    if abbreviatedConjugations.forall(_.adverbs.isEmpty) then {
      totalNumberOfColumns = 10
      new TableAdapter().startTable(10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0, 10.0)
    } else {
      totalNumberOfColumns = 11
      new TableAdapter().startTable(10.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0)
    }

  override protected def getChart: Tbl = {
    tblAdapter
      .startRow()
      .addColumn(0, totalNumberOfColumns, nilBorderColumnProperties, getArabicText(header, ArabicHeadingStyle))
      .endRow()
    abbreviatedConjugations.foreach(buildDocument)
    addSeparatorRow(tblAdapter, totalNumberOfColumns)
    tblAdapter.getTable
  }

  private def buildDocument(abbreviatedConjugation: AbbreviatedConjugation): Unit = {
    var columnIndex = 0

    tblAdapter.startRow()

    if abbreviatedConjugation.adverbs.nonEmpty then {
      tblAdapter.addColumn(
        columnIndex,
        getArabicText(concatenateWithAnd(abbreviatedConjugation.adverbs.map(ArabicWord(_))))
      )
      columnIndex += 1
    }

    tblAdapter.addColumn(columnIndex, getArabicText(abbreviatedConjugation.forbidden))
    columnIndex += 1

    tblAdapter.addColumn(columnIndex, getArabicText(abbreviatedConjugation.imperative))
    columnIndex += 1

    tblAdapter.addColumn(columnIndex, getArabicText(abbreviatedConjugation.passiveParticiple.getOrElse("")))
    columnIndex += 1

    tblAdapter.addColumn(
      columnIndex,
      getArabicText(concatenateWithAnd(abbreviatedConjugation.verbalNouns.map(ArabicWord(_))))
    )
    columnIndex += 1

    tblAdapter.addColumn(columnIndex, getArabicText(abbreviatedConjugation.presentPassiveTense.getOrElse("")))
    columnIndex += 1

    tblAdapter.addColumn(columnIndex, getArabicText(abbreviatedConjugation.pastPassiveTense.getOrElse("")))
    columnIndex += 1

    tblAdapter.addColumn(columnIndex, getArabicText(abbreviatedConjugation.activeParticiple))
    columnIndex += 1

    tblAdapter.addColumn(
      columnIndex,
      getArabicText(concatenateWithAnd(abbreviatedConjugation.verbalNouns.map(ArabicWord(_))))
    )
    columnIndex += 1

    tblAdapter.addColumn(columnIndex, getArabicText(abbreviatedConjugation.presentTense))
    columnIndex += 1

    tblAdapter.addColumn(columnIndex, getArabicText(abbreviatedConjugation.pastTense))

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
