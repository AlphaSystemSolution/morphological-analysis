package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx
package classic

import arabic.model.ArabicWord
import morphologicalengine.conjugation.model.{ AbbreviatedConjugation, ConjugationHeader, MorphologicalTermType }
import generator.model.ChartConfiguration
import openxml.builder.wml.table.{ ColumnData, ColumnInput, TableAdapter }
import openxml.builder.wml.{ WmlAdapter, WmlBuilderFactory }
import org.docx4j.wml.{ JcEnumeration, P, STHint, Tbl }

class AbbreviatedConjugationGenerator(
  override val chartConfiguration: ChartConfiguration,
  conjugationHeader: ConjugationHeader,
  abbreviatedConjugation: AbbreviatedConjugation,
  maybeTranslation: Option[String] = None)
    extends ChartGenerator(chartConfiguration) {

  private val numOfColumns = 4
  private val smallerCaptionSize = Some(chartConfiguration.arabicFontSize - 4)

  private val tblAdapter = new TableAdapter()
    .withColumnInputs(
      ColumnInput("col_1", 25.0),
      ColumnInput("col_2", 25.0),
      ColumnInput("col_3", 25.0),
      ColumnInput("col_4", 25.0)
    )
    .startTable()

  override protected def getChart: Tbl = {
    if chartConfiguration.showTitle then addHeaderRow()
    addActiveLine()
    if abbreviatedConjugation.hasPassiveLine then addPassiveLine()
    addImperativeAndForbiddenLine()
    if abbreviatedConjugation.adverbs.nonEmpty then addAdverbLine()
    addSeparatorRow(tblAdapter, numOfColumns)
    tblAdapter.getTable
  }

  private def addHeaderRow(): Unit = {
    val rsidR = nextId
    val rsidP = nextId
    val rsidRpr = nextId

    val titlePara = getTitlePara(conjugationHeader.title, conjugationHeader.rootLetters.stringValue)
    val titleParas =
      if maybeTranslation.isDefined then Seq(titlePara, translationPara(rsidR, rsidP), WmlAdapter.getEmptyParaNoSpacing)
      else Seq(titlePara)

    val gridSpan = if chartConfiguration.showLabels then numOfColumns - 1 else numOfColumns
    tblAdapter.startRow().addColumn(ColumnData(0).withGridSpanValue(gridSpan).withContent(titleParas*))

    if chartConfiguration.showLabels then {
      val label1 = getHeaderLabelPara(rsidR, rsidRpr, rsidP, conjugationHeader.templateTypeLabel)
      val label2 = getHeaderLabelPara(rsidR, rsidRpr, rsidP, conjugationHeader.weightLabel)
      val label3 = getHeaderLabelPara(rsidR, rsidRpr, rsidP, conjugationHeader.verbTypeLabel)

      tblAdapter.addColumn(ColumnData(3).withContent(label1, label2, label3))
    }
    tblAdapter.endRow()
  }

  private def addActiveLine(): Unit = {
    if chartConfiguration.showMorphologicalTermCaptionInAbbreviatedConjugation then {
      addFourColumnCaptionRow(
        MorphologicalTermType.ActiveParticipleMasculine,
        MorphologicalTermType.VerbalNoun,
        MorphologicalTermType.PresentTense,
        MorphologicalTermType.PastTense
      )
    }
    addFourColumnRow(
      getArabicText(ParticiplePrefix, abbreviatedConjugation.activeParticiple, ArabicTableCenterStyle),
      getArabicText(
        concatenateWithAnd(abbreviatedConjugation.verbalNouns.map(ArabicWord(_))),
        ArabicTableCenterStyle
      ),
      getArabicText(abbreviatedConjugation.presentTense, ArabicTableCenterStyle),
      getArabicText(abbreviatedConjugation.pastTense, ArabicTableCenterStyle)
    )
  }

  private def addPassiveLine(): Unit = {
    if chartConfiguration.showMorphologicalTermCaptionInAbbreviatedConjugation then {
      addFourColumnCaptionRow(
        MorphologicalTermType.PassiveParticipleMasculine,
        MorphologicalTermType.VerbalNoun,
        MorphologicalTermType.PresentPassiveTense,
        MorphologicalTermType.PastPassiveTense
      )
    }
    addFourColumnRow(
      getArabicText(
        ParticiplePrefix,
        abbreviatedConjugation.passiveParticiple.getOrElse(""),
        ArabicTableCenterStyle
      ),
      getArabicText(
        concatenateWithAnd(abbreviatedConjugation.verbalNouns.map(ArabicWord(_))),
        ArabicTableCenterStyle
      ),
      getArabicText(abbreviatedConjugation.presentPassiveTense.getOrElse(""), ArabicTableCenterStyle),
      getArabicText(abbreviatedConjugation.pastPassiveTense.getOrElse(""), ArabicTableCenterStyle)
    )
  }

  private def addImperativeAndForbiddenLine(): Unit = {
    if chartConfiguration.showMorphologicalTermCaptionInAbbreviatedConjugation then {
      addTwoColumnCaptionRow(MorphologicalTermType.Forbidden, MorphologicalTermType.Imperative)
    }
    addTwoColumnRow(
      getArabicText(
        ForbiddenPrefix,
        abbreviatedConjugation.forbidden,
        ArabicTableCenterStyle
      ),
      getArabicText(ImperativePrefix, abbreviatedConjugation.imperative, ArabicTableCenterStyle)
    )
  }

  private def addAdverbLine(): Unit = {
    if chartConfiguration.showMorphologicalTermCaptionInAbbreviatedConjugation then {
      tblAdapter
        .startRow()
        .addColumn(
          ColumnData(0)
            .withGridSpanValue(numOfColumns)
            .withContent(
              getArabicText(
                MorphologicalTermType.NounOfPlaceAndTime.shortTitle.unicode,
                ArabicCaptionStyle,
                smallerCaptionSize
              )
            )
        )
        .endRow()
    }
    tblAdapter
      .startRow()
      .addColumn(
        ColumnData(0)
          .withGridSpanValue(numOfColumns)
          .withContent(
            getArabicText(
              AdverbPrefix,
              concatenateWithAnd(abbreviatedConjugation.adverbs.map(ArabicWord(_))),
              ArabicTableCenterStyle
            )
          )
      )
      .endRow()
  }

  private def addFourColumnCaptionRow(
    caption1: MorphologicalTermType,
    caption2: MorphologicalTermType,
    caption3: MorphologicalTermType,
    caption4: MorphologicalTermType
  ): Unit =
    tblAdapter
      .startRow()
      .addColumn(
        ColumnData(0).withContent(getArabicText(caption1.shortTitle.unicode, ArabicCaptionStyle, smallerCaptionSize))
      )
      .addColumn(
        ColumnData(1).withContent(getArabicText(caption2.shortTitle.unicode, ArabicCaptionStyle, smallerCaptionSize))
      )
      .addColumn(
        ColumnData(2).withContent(getArabicText(caption3.shortTitle.unicode, ArabicCaptionStyle, smallerCaptionSize))
      )
      .addColumn(
        ColumnData(3).withContent(getArabicText(caption4.shortTitle.unicode, ArabicCaptionStyle, smallerCaptionSize))
      )
      .endRow()

  private def addTwoColumnCaptionRow(caption1: MorphologicalTermType, caption2: MorphologicalTermType): Unit =
    tblAdapter
      .startRow()
      .addColumn(
        ColumnData(0)
          .withGridSpanValue(2)
          .withContent(getArabicText(caption1.shortTitle.unicode, ArabicCaptionStyle, smallerCaptionSize))
      )
      .addColumn(
        ColumnData(2)
          .withGridSpanValue(2)
          .withContent(getArabicText(caption2.shortTitle.unicode, ArabicCaptionStyle, smallerCaptionSize))
      )
      .endRow()

  private def addFourColumnRow(p1: P, p2: P, p3: P, p4: P): Unit =
    tblAdapter
      .startRow()
      .addColumn(ColumnData(0).withContent(p1))
      .addColumn(ColumnData(1).withContent(p2))
      .addColumn(ColumnData(2).withContent(p3))
      .addColumn(ColumnData(3).withContent(p4))
      .endRow()

  private def addTwoColumnRow(p1: P, p2: P): Unit =
    tblAdapter
      .startRow()
      .addColumn(ColumnData(0).withGridSpanValue(2).withContent(p1))
      .addColumn(ColumnData(2).withGridSpanValue(2).withContent(p2))
      .endRow()

  private def translationPara(rsidR: String, rsidP: String) = {
    val translation = maybeTranslation.map(_.trim).getOrElse("")
    val fontFamily = chartConfiguration.translationFontFamily
    val fontSize = chartConfiguration.translationFontSize * 2

    val rFonts = WmlBuilderFactory.getRFontsBuilder.withAscii(fontFamily).withHAnsi(fontFamily).getObject
    val rpr = WmlBuilderFactory
      .getRPrBuilder
      .withRFonts(rFonts)
      .withSz(fontSize)
      .withSzCs(fontSize)
      .getObject

    val run = WmlBuilderFactory
      .getRBuilder
      .withRsidR(rsidR)
      .withRPr(rpr)
      .addContent(WmlAdapter.getText(translation))
      .getObject

    val ppr = WmlBuilderFactory
      .getPPrBuilder
      .withJc(JcEnumeration.CENTER)
      .withRPr(WmlBuilderFactory.getParaRPrBuilder.withRFonts(rFonts).getObject)
      .getObject

    WmlBuilderFactory
      .getPBuilder
      .withRsidR(rsidR)
      .withRsidRDefault(rsidR)
      .withRsidP(rsidP)
      .withRsidRPr(nextId)
      .withPPr(ppr)
      .addContent(run)
      .getObject
  }

  private def getHeaderLabelPara(rsidR: String, rsidRpr: String, rsidP: String, label: String) = {
    val fontSize = (chartConfiguration.arabicFontSize - 2) * 2
    val prpr = WmlBuilderFactory.getParaRPrBuilder.withSz(fontSize).withSzCs(fontSize).getObject
    val ppr = WmlBuilderFactory.getPPrBuilder.withPStyle(ArabicNormalStyle).withBidi(true).withRPr(prpr).getObject
    val text = WmlAdapter.getText(label)
    val rFonts = WmlBuilderFactory.getRFontsBuilder.withHint(STHint.CS).getObject
    val rpr = WmlBuilderFactory.getRPrBuilder.withRFonts(rFonts).withSz(fontSize).withSzCs(fontSize).getObject
    val run = WmlBuilderFactory.getRBuilder.withRsidR(rsidR).withRPr(rpr).addContent(text).getObject

    WmlBuilderFactory
      .getPBuilder
      .withRsidR(rsidR)
      .withRsidRDefault(rsidR)
      .withRsidP(rsidP)
      .withRsidRPr(rsidRpr)
      .withPPr(ppr)
      .addContent(run)
      .getObject
  }
}

object AbbreviatedConjugationGenerator {
  def apply(
    chartConfiguration: ChartConfiguration,
    conjugationHeader: ConjugationHeader,
    abbreviatedConjugation: AbbreviatedConjugation,
    maybeTranslation: Option[String] = None
  ): AbbreviatedConjugationGenerator =
    new AbbreviatedConjugationGenerator(chartConfiguration, conjugationHeader, abbreviatedConjugation, maybeTranslation)
}
