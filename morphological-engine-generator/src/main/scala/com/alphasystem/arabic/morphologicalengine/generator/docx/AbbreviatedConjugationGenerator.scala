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
import org.docx4j.wml.{ JcEnumeration, P, STHint, Tbl }

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

    val titleParas =
      if maybeTranslation.isDefined then Seq(titlePara, translationPara(rsidR, rsidP), WmlAdapter.getEmptyParaNoSpacing)
      else Seq(titlePara)

    val gridSpan = if chartConfiguration.showLabels then numOfColumns - 1 else numOfColumns
    tblAdapter.startRow().addColumn(0, gridSpan, titleParas*)

    if chartConfiguration.showLabels then {
      val label1 = getHeaderLabelPara(rsidR, rsidRpr, rsidP, conjugationHeader.templateTypeLabel)
      val label2 = getHeaderLabelPara(rsidR, rsidRpr, rsidP, conjugationHeader.weightLabel)
      val label3 = getHeaderLabelPara(rsidR, rsidRpr, rsidP, conjugationHeader.verbTypeLabel)

      tblAdapter.addColumn(3, label1, label2, label3)
    }
    tblAdapter.endRow()
  }

  private def addActiveLine(): Unit = {
    if chartConfiguration.showMorphologicalTermCaptionInAbbreviatedConjugation then {
      addFourColumnCaptionRow(
        ActiveParticiple,
        MorphologicalTermType.VerbalNoun.label,
        MorphologicalTermType.PresentTense.label,
        MorphologicalTermType.PastTense.label
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
        PassiveParticiple,
        MorphologicalTermType.VerbalNoun.label,
        MorphologicalTermType.PresentPassiveTense.label,
        MorphologicalTermType.PastPassiveTense.label
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
      addTwoColumnCaptionRow(MorphologicalTermType.Forbidden.label, MorphologicalTermType.Imperative.label)
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

  private def addFourColumnCaptionRow(caption1: String, caption2: String, caption3: String, caption4: String): Unit =
    tblAdapter
      .startRow()
      .addColumn(0, getArabicText(caption1, ArabicCaptionStyle, smallerCaptionSize))
      .addColumn(1, getArabicText(caption2, ArabicCaptionStyle, smallerCaptionSize))
      .addColumn(2, getArabicText(caption3, ArabicCaptionStyle, smallerCaptionSize))
      .addColumn(3, getArabicText(caption4, ArabicCaptionStyle, smallerCaptionSize))
      .endRow()

  private def addTwoColumnCaptionRow(caption1: String, caption2: String): Unit =
    tblAdapter
      .startRow()
      .addColumn(0, 2, getArabicText(caption1, ArabicCaptionStyle, smallerCaptionSize))
      .addColumn(2, 2, getArabicText(caption2, ArabicCaptionStyle, smallerCaptionSize))
      .endRow()

  private def addFourColumnRow(p1: P, p2: P, p3: P, p4: P): Unit =
    tblAdapter
      .startRow()
      .addColumn(0, p1)
      .addColumn(1, p2)
      .addColumn(2, p3)
      .addColumn(3, p4)
      .endRow()

  private def addTwoColumnRow(p1: P, p2: P): Unit =
    tblAdapter
      .startRow()
      .addColumn(0, 2, p1)
      .addColumn(2, 2, p2)
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
