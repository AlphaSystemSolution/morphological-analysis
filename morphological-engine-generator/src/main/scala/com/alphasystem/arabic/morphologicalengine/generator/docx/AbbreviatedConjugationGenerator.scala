package com.alphasystem
package arabic
package morphologicalengine
package generator
package docx

import arabic.model.ArabicWord
import openxml.builder.wml.{ WmlAdapter, WmlBuilderFactory }
import openxml.builder.wml.table.TableAdapter
import morphologicalengine.conjugation.model.{ AbbreviatedConjugation, ConjugationHeader, MorphologicalTermType }
import generator.model.ChartConfiguration
import org.docx4j.wml.{ JcEnumeration, STHint, Tbl }

class AbbreviatedConjugationGenerator(
  override val chartConfiguration: ChartConfiguration,
  conjugationHeader: ConjugationHeader,
  abbreviatedConjugation: AbbreviatedConjugation,
  maybeTranslation: Option[String] = None)
    extends ChartGenerator(chartConfiguration) {

  private val numOfColumns = 4
  private val smallerCaptionSize = Some(chartConfiguration.arabicFontSize - 4)

  private val tblAdapter = new TableAdapter().startTable(25.0, 25.0, 25.0, 25.0)

  override protected def getChart: Tbl = {
    if chartConfiguration.showTitle then addTitleRow()
    if chartConfiguration.showHeader then addHeaderRow()
    addActiveLine()
    if abbreviatedConjugation.hasPassiveLine then addPassiveLine()
    addImperativeAndForbiddenLine()
    addAdverbLine()
    addSeparatorRow(tblAdapter, numOfColumns)
    tblAdapter.getTable
  }

  private def addTitleRow(): Unit =
    tblAdapter.startRow().addColumn(0, numOfColumns, nilBorderColumnProperties, titlePara).endRow()

  private def addHeaderRow(): Unit = {
    val rsidR = nextId
    val rsidP = nextId
    val rsidRpr = nextId

    val translation = translationPara(rsidR, rsidP)

    if chartConfiguration.showHeader then {
      val label1 = getHeaderLabelPara(rsidR, rsidRpr, rsidP, conjugationHeader.templateTypeLabel)
      val label2 = getHeaderLabelPara(rsidR, rsidRpr, rsidP, conjugationHeader.weightLabel)
      val label3 = getHeaderLabelPara(rsidR, rsidRpr, rsidP, conjugationHeader.verbTypeLabel)

      val gridSpan = numOfColumns / 2
      tblAdapter
        .startRow()
        .addColumn(0, gridSpan, WmlAdapter.getEmptyPara, translation)
        .addColumn(2, gridSpan, label1, label2, label3)
        .endRow()
    } else if maybeTranslation.isDefined then {
      tblAdapter
        .startRow()
        .addColumn(0, numOfColumns, nilBorderColumnProperties, translationPara(nextId, nextId))
        .endRow()
      tblAdapter
        .startRow()
        .addColumn(1, numOfColumns, nilBorderColumnProperties, WmlAdapter.getEmptyParaNoSpacing)
        .endRow()
    }
  }

  private def addActiveLine(): Unit = {
    if chartConfiguration.showMorphologicalTermCaptionInAbbreviatedConjugation then {
      tblAdapter
        .startRow()
        .addColumn(0, getArabicText(ActiveParticiple, ArabicCaptionStyle, smallerCaptionSize))
        .addColumn(1, getArabicText(MorphologicalTermType.VerbalNoun.label, ArabicCaptionStyle, smallerCaptionSize))
        .addColumn(2, getArabicText(MorphologicalTermType.PresentTense.label, ArabicCaptionStyle, smallerCaptionSize))
        .addColumn(3, getArabicText(MorphologicalTermType.PastTense.label, ArabicCaptionStyle, smallerCaptionSize))
        .endRow()
    }
    tblAdapter
      .startRow()
      .addColumn(
        0,
        getArabicText(ParticiplePrefix, abbreviatedConjugation.activeParticiple, ArabicTableCenterStyle)
      )
      .addColumn(
        1,
        getArabicText(
          concatenateWithAnd(abbreviatedConjugation.verbalNouns.map(ArabicWord(_))),
          ArabicTableCenterStyle
        )
      )
      .addColumn(2, getArabicText(abbreviatedConjugation.presentTense, ArabicTableCenterStyle))
      .addColumn(3, getArabicText(abbreviatedConjugation.pastTense, ArabicTableCenterStyle))
      .endRow()
  }

  private def addPassiveLine(): Unit = {
    if chartConfiguration.showMorphologicalTermCaptionInAbbreviatedConjugation then {
      tblAdapter
        .startRow()
        .addColumn(0, getArabicText(PassiveParticiple, ArabicCaptionStyle, smallerCaptionSize))
        .addColumn(1, getArabicText(MorphologicalTermType.VerbalNoun.label, ArabicCaptionStyle, smallerCaptionSize))
        .addColumn(
          2,
          getArabicText(MorphologicalTermType.PresentPassiveTense.label, ArabicCaptionStyle, smallerCaptionSize)
        )
        .addColumn(
          3,
          getArabicText(MorphologicalTermType.PastPassiveTense.label, ArabicCaptionStyle, smallerCaptionSize)
        )
        .endRow()
    }
    tblAdapter
      .startRow()
      .addColumn(
        0,
        getArabicText(
          ParticiplePrefix,
          abbreviatedConjugation.passiveParticiple.getOrElse(""),
          ArabicTableCenterStyle
        )
      )
      .addColumn(
        1,
        getArabicText(
          concatenateWithAnd(abbreviatedConjugation.verbalNouns.map(ArabicWord(_))),
          ArabicTableCenterStyle
        )
      )
      .addColumn(2, getArabicText(abbreviatedConjugation.presentPassiveTense.getOrElse(""), ArabicTableCenterStyle))
      .addColumn(3, getArabicText(abbreviatedConjugation.pastPassiveTense.getOrElse(""), ArabicTableCenterStyle))
      .endRow()
  }

  private def addImperativeAndForbiddenLine(): Unit = {
    if chartConfiguration.showMorphologicalTermCaptionInAbbreviatedConjugation then {
      tblAdapter
        .startRow()
        .addColumn(0, 2, getArabicText(MorphologicalTermType.Forbidden.label, ArabicCaptionStyle, smallerCaptionSize))
        .addColumn(1, 2, getArabicText(MorphologicalTermType.Imperative.label, ArabicCaptionStyle, smallerCaptionSize))
        .endRow()
    }
    tblAdapter
      .startRow()
      .addColumn(
        0,
        2,
        getArabicText(
          ForbiddenPrefix,
          NegationPrefix.concatWithSpace(ArabicWord(abbreviatedConjugation.forbidden)).unicode,
          ArabicTableCenterStyle
        )
      )
      .addColumn(1, 2, getArabicText(ImperativePrefix, abbreviatedConjugation.imperative, ArabicTableCenterStyle))
      .endRow()
  }

  private def addAdverbLine(): Unit = {
    if chartConfiguration.showMorphologicalTermCaptionInAbbreviatedConjugation then {
      tblAdapter
        .startRow()
        .addColumn(
          0,
          numOfColumns,
          getArabicText(MorphologicalTermType.NounOfPlaceAndTime.label, ArabicCaptionStyle, smallerCaptionSize)
        )
        .endRow()
    }
    tblAdapter
      .startRow()
      .addColumn(
        0,
        numOfColumns,
        getArabicText(
          AdverbPrefix,
          concatenateWithAnd(abbreviatedConjugation.adverbs.map(ArabicWord(_))),
          ArabicTableCenterStyle
        )
      )
      .endRow()
  }

  private lazy val titlePara = {
    val id = nextId

    val run = WmlBuilderFactory
      .getRBuilder
      .withRsidRPr(id)
      .withRPr(
        WmlBuilderFactory
          .getRPrBuilder
          .withRFonts(WmlBuilderFactory.getRFontsBuilder.withHint(STHint.CS).getObject)
          .getObject
      )
      .addContent(WmlAdapter.getText(conjugationHeader.title))
      .getObject

    val para = WmlBuilderFactory
      .getPBuilder
      .withRsidRPr(id)
      .withRsidP(id)
      .withRsidR(id)
      .withRsidRDefault(id)
      .withPPr(
        WmlBuilderFactory
          .getPPrBuilder
          .withPStyle(ArabicHeadingStyle)
          .withBidi(true)
          .withRPr(WmlBuilderFactory.getParaRPrBuilder.getObject)
          .getObject
      )
      .addContent(run)
      .getObject

    WmlAdapter.addBookMark(para, id)
    para
  }

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
