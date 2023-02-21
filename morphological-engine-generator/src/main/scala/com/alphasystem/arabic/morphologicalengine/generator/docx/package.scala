package com.alphasystem
package arabic
package morphologicalengine
package generator

import arabic.model.{ ArabicLetterType, ArabicWord }
import generator.helpers.CustomStyles
import generator.model.PageOrientation
import openxml.builder.wml.table.TableAdapter
import openxml.builder.wml.{ WmlAdapter, WmlBuilderFactory, WmlPackageBuilder }
import org.docx4j.jaxb.Context
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart
import org.docx4j.wml.{ P, STHint, TcPr }

import java.nio.file.Path
import scala.util.Try

package object docx {

  private[docx] val ArabicHeadingStyle = "Arabic-Heading1"
  private[docx] val ArabicNormalStyle = "Arabic-Normal"
  private[docx] val ArabicTableCenterStyle = "Arabic-Table-Center"
  private[docx] val ArabicCaptionStyle = "Arabic-Caption"
  private val NoSpacingStyle = "NoSpacing"
  private[docx] val ParticiplePrefix =
    ArabicWord(ArabicLetterType.Fa, ArabicLetterType.Ha, ArabicLetterType.Waw).unicode
  private[docx] val NegationPrefix = ArabicWord(ArabicLetterType.Lam, ArabicLetterType.Alif)

  private[docx] val ImperativePrefix = ArabicWord(
    ArabicLetterType.Alif,
    ArabicLetterType.Lam,
    ArabicLetterType.AlifHamzaAbove,
    ArabicLetterType.Meem,
    ArabicLetterType.Ra,
    ArabicLetterType.Space,
    ArabicLetterType.Meem,
    ArabicLetterType.Noon,
    ArabicLetterType.Ha
  ).unicode

  private[docx] val ForbiddenPrefix = ArabicWord(
    ArabicLetterType.Waw,
    ArabicLetterType.Noon,
    ArabicLetterType.Ha,
    ArabicLetterType.Ya,
    ArabicLetterType.Space,
    ArabicLetterType.Ain,
    ArabicLetterType.Noon,
    ArabicLetterType.Ha
  ).unicode

  private[docx] val AdverbPrefix =
    ArabicWord(
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
    ).unicode

  private[docx] val ActiveParticiple = ArabicWord(
    ArabicLetterType.AlifHamzaBelow,
    ArabicLetterType.Seen,
    ArabicLetterType.Meem,
    ArabicLetterType.Space,
    ArabicLetterType.Fa,
    ArabicLetterType.Alif,
    ArabicLetterType.Ain,
    ArabicLetterType.Lam
  ).unicode

  private[docx] val PassiveParticiple = ArabicWord(
    ArabicLetterType.AlifHamzaBelow,
    ArabicLetterType.Seen,
    ArabicLetterType.Meem,
    ArabicLetterType.Space,
    ArabicLetterType.Meem,
    ArabicLetterType.Fa,
    ArabicLetterType.Ain,
    ArabicLetterType.Waw,
    ArabicLetterType.Lam
  ).unicode

  private[docx] def createDocument(path: Path, documentAdapter: DocumentGenerator): Unit = {
    val chartConfiguration = documentAdapter.chartConfiguration
    val landscape = PageOrientation.Landscape == chartConfiguration.pageOrientation

    val wordMLPackage = WmlPackageBuilder
      .createPackage(landscape)
      .styles(
        CustomStyles(
          chartConfiguration.arabicFontFamily,
          chartConfiguration.arabicFontSize,
          chartConfiguration.headingFontSize
        ).init
      )
      .getPackage

    val mdp = wordMLPackage.getMainDocumentPart
    updateDocumentCompatibility(mdp)
    documentAdapter.buildDocument(mdp)
    WmlAdapter.save(path.toFile, wordMLPackage)
  }

  private def updateDocumentCompatibility(mdp: MainDocumentPart): Unit = {
    Try {
      val dsp = mdp.getDocumentSettingsPart(true)
      val compat = Context.getWmlObjectFactory.createCTCompat
      compat.setCompatSetting("compatibilityMode", "http://schemas.microsoft.com/office/word", "15");
      dsp.getContents.setCompat(compat);
    }
  }

  private[docx] def getArabicText(value: String, styleName: String, maybeCustomSize: Option[Long] = None): P = {
    val rsidr = nextId
    val id = nextId

    val rPrBuilder = WmlBuilderFactory
      .getRPrBuilder
      .withRFonts(WmlBuilderFactory.getRFontsBuilder.withHint(STHint.CS).getObject)
      .withRtl(true)

    if maybeCustomSize.isDefined then {
      val size = maybeCustomSize.get * 2
      rPrBuilder.withSz(size).withSzCs(size)
    }

    val run = WmlBuilderFactory
      .getRBuilder
      .withRPr(
        rPrBuilder.getObject
      )
      .addContent(WmlAdapter.getText(value))
      .getObject

    WmlBuilderFactory
      .getPBuilder
      .withRsidR(rsidr)
      .withRsidRDefault(rsidr)
      .withRsidRPr(id)
      .withRsidP(id)
      .withPPr(WmlBuilderFactory.getPPrBuilder.withPStyle(styleName).getObject)
      .addContent(run)
      .getObject
  }

  private[docx] def getArabicText(prefix: String, value: String, styleName: String): P = {
    val rsidr = nextId
    val id = nextId

    val prefixRun =
      WmlBuilderFactory
        .getRBuilder
        .withRsidRPr(id)
        .withRPr(
          WmlBuilderFactory
            .getRPrBuilder
            .withRFonts(WmlBuilderFactory.getRFontsBuilder.withHint(STHint.CS).getObject)
            .withColor(WmlBuilderFactory.getColorBuilder.withVal("C00000").getObject)
            .withSz(20) // font size 10
            .withSzCs(20)
            .withRtl(true)
            .getObject
        )
        .addContent(WmlAdapter.getText(s"$prefix "))
        .getObject

    val mainRun =
      WmlBuilderFactory
        .getRBuilder
        .withRsidRPr(id)
        .withRPr(
          WmlBuilderFactory
            .getRPrBuilder
            .withRFonts(WmlBuilderFactory.getRFontsBuilder.withHint(STHint.CS).getObject)
            .withRtl(true)
            .getObject
        )
        .addContent(WmlAdapter.getText(value))
        .getObject

    WmlBuilderFactory
      .getPBuilder
      .withRsidR(rsidr)
      .withRsidRDefault(rsidr)
      .withRsidRPr(id)
      .withRsidP(id)
      .withPPr(WmlBuilderFactory.getPPrBuilder.withPStyle(styleName).getObject)
      .addContent(prefixRun, mainRun)
      .getObject
  }

  private[docx] def concatenateWithAnd(words: Seq[ArabicWord]): String = {
    words
      .headOption
      .map(_.concatenateWithAnd(words.tail*))
      .map(_.unicode)
      .getOrElse("")
  }

  private[docx] def addSeparatorRow(tblAdapter: TableAdapter, gridSpan: Int): TableAdapter = {
    val tcPr = WmlBuilderFactory.getTcPrBuilder.withTcBorders(WmlAdapter.getNilBorders).getObject
    tblAdapter.startRow().addColumn(0, gridSpan, tcPr, createNoSpacingStyleP).endRow()
  }

  private[docx] def nilBorderColumnProperties: TcPr =
    WmlBuilderFactory.getTcPrBuilder.withTcBorders(WmlAdapter.getNilBorders).getObject

  private def createNoSpacingStyleP =
    WmlBuilderFactory
      .getPBuilder
      .withRsidR(nextId)
      .withRsidP(nextId)
      .withRsidRDefault(nextId)
      .withPPr(WmlBuilderFactory.getPPrBuilder.withPStyle(NoSpacingStyle).getObject)
      .getObject
}
