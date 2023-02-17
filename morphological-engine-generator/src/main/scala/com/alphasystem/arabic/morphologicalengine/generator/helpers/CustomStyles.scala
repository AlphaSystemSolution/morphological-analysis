package com.alphasystem
package arabic
package morphologicalengine
package generator
package helpers

import com.alphasystem.openxml.builder.wml.WmlBuilderFactory
import openxml.builder.wml.PPrBuilder.SpacingBuilder
import util.IdGenerator
import org.docx4j.wml.{ JcEnumeration, STLineSpacingRule, STTabJc, STTabTlc, STTheme, STThemeColor, Styles }

class CustomStyles(fontName: String, fontSize: Long, headingFontSize: Long) {

  private val rFonts =
    WmlBuilderFactory
      .getRFontsBuilder
      .withAscii(fontName)
      .withHAnsi(fontName)
      .withCs(fontName)
      .withEastAsiaTheme(STTheme.MAJOR_EAST_ASIA)
      .getObject

  private val color = WmlBuilderFactory
    .getColorBuilder
    .withVal(IdGenerator.nextId())
    .withThemeColor(STThemeColor.ACCENT_1)
    .withThemeShade("BF")
    .getObject

  def init: Styles =
    WmlBuilderFactory
      .getStylesBuilder
      .addStyle(
        arabicNormal,
        arabicNormalChar,
        arabicTableCenter,
        arabicTableCenterChar,
        arabicCaption,
        arabicCaptionChar,
        arabicHeading1,
        arabicHeading1Char,
        tocArabic,
        arabicPrefix,
        arabicPrefixChar
      )
      .getObject

  private def arabicNormal =
    WmlBuilderFactory
      .getStyleBuilder
      .withType("paragraph")
      .withCustomStyle(true)
      .withStyleId("Arabic-Normal")
      .withName("Arabic-Normal")
      .withBasedOn("Normal")
      .withLink("Arabic-NormalChar")
      .withQFormat(true)
      .withRsid(IdGenerator.nextId())
      .withPPr(WmlBuilderFactory.getPPrBuilder.withBidi(true).getObject)
      .withRPr(WmlBuilderFactory.getRPrBuilder.withRFonts(rFonts).withSz(fontSize * 2).withSzCs(fontSize * 2).getObject)
      .getObject

  private def arabicNormalChar =
    WmlBuilderFactory
      .getStyleBuilder
      .withType("character")
      .withCustomStyle(true)
      .withStyleId("Arabic-NormalChar")
      .withName("Arabic-Normal Char")
      .withBasedOn("DefaultParagraphFont")
      .withLink("Arabic-Normal")
      .withRsid(IdGenerator.nextId())
      .withRPr(WmlBuilderFactory.getRPrBuilder.withRFonts(rFonts).withSz(fontSize * 2).withSzCs(fontSize * 2).getObject)
      .getObject

  private def arabicTableCenter = {
    val spacing = new SpacingBuilder()
      .withBefore(120)
      .withAfter(120)
      .withLine(240)
      .withLineRule(STLineSpacingRule.AUTO)
      .getObject
    WmlBuilderFactory
      .getStyleBuilder
      .withType("paragraph")
      .withCustomStyle(true)
      .withStyleId("Arabic-Table-Center")
      .withName("Arabic-Table-Center")
      .withBasedOn("Arabic-Normal")
      .withLink("Arabic-Table-CenterChar")
      .withQFormat(true)
      .withRsid(IdGenerator.nextId())
      .withPPr(WmlBuilderFactory.getPPrBuilder.withSpacing(spacing).withJc(JcEnumeration.CENTER).getObject)
      .getObject
  }

  private def arabicTableCenterChar = {
    WmlBuilderFactory
      .getStyleBuilder
      .withType("character")
      .withCustomStyle(true)
      .withStyleId("Arabic-Table-CenterChar")
      .withName("Arabic-Table-Center Char")
      .withBasedOn("Arabic-NormalChar")
      .withLink("Arabic-Table-Center")
      .withRsid(IdGenerator.nextId())
      .withRPr(WmlBuilderFactory.getRPrBuilder.withRFonts(rFonts).withSz(fontSize * 2).withSzCs(fontSize * 2).getObject)
      .getObject
  }

  private def arabicCaption =
    WmlBuilderFactory
      .getStyleBuilder
      .withType("paragraph")
      .withCustomStyle(true)
      .withStyleId("Arabic-Caption")
      .withName("Arabic-Caption")
      .withBasedOn("Arabic-Table-Center")
      .withLink("Arabic-CaptionChar")
      .withQFormat(true)
      .withRsid(IdGenerator.nextId())
      .withPPr(WmlBuilderFactory.getPPrBuilder.withBidi(true).getObject)
      .withRPr(WmlBuilderFactory.getRPrBuilder.withB(true).withBCs(true).withColor(color).getObject)
      .getObject

  private def arabicCaptionChar =
    WmlBuilderFactory
      .getStyleBuilder
      .withType("character")
      .withCustomStyle(true)
      .withStyleId("Arabic-CaptionChar")
      .withName("Arabic-CaptionChar")
      .withBasedOn("Arabic-Table-CenterChar")
      .withLink("Arabic-Caption")
      .withRsid(IdGenerator.nextId())
      .withRPr(
        WmlBuilderFactory
          .getRPrBuilder
          .withRFonts(rFonts)
          .withColor(color)
          .withSz(fontSize * 2)
          .withSzCs(fontSize * 2)
          .withB(true)
          .withBCs(true)
          .getObject
      )
      .getObject

  private def arabicHeading1 =
    WmlBuilderFactory
      .getStyleBuilder
      .withType("paragraph")
      .withCustomStyle(true)
      .withStyleId("Arabic-Heading1")
      .withName("Arabic-Heading1")
      .withBasedOn("Heading1")
      .withLink("Arabic-Heading1Char")
      .withQFormat(true)
      .withRsid(IdGenerator.nextId())
      .withPPr(WmlBuilderFactory.getPPrBuilder.withBidi(true).withJc(JcEnumeration.CENTER).getObject)
      .withRPr(
        WmlBuilderFactory
          .getRPrBuilder
          .withRFonts(rFonts)
          .withSz(headingFontSize * 2)
          .withSz(headingFontSize * 2)
          .getObject
      )
      .getObject

  private def arabicHeading1Char =
    WmlBuilderFactory
      .getStyleBuilder
      .withType("character")
      .withCustomStyle(true)
      .withStyleId("Arabic-Heading1Char")
      .withName("Arabic-Heading1Char")
      .withLink("Arabic-Heading1")
      .withRsid(IdGenerator.nextId())
      .withRPr(
        WmlBuilderFactory
          .getRPrBuilder
          .withRFonts(rFonts)
          .withColor(color)
          .withSz(fontSize * 2)
          .withSzCs(fontSize * 2)
          .withB(true)
          .withBCs(true)
          .getObject
      )
      .getObject

  private def tocArabic = {
    val tabStop = WmlBuilderFactory
      .getCTTabStopBuilder
      .withVal(STTabJc.RIGHT)
      .withLeader(STTabTlc.DOT)
      .withPos(9017L)
      .getObject

    val tabs = WmlBuilderFactory.getTabsBuilder().addTab(tabStop).getObject
    WmlBuilderFactory
      .getStyleBuilder
      .withType("paragraph")
      .withCustomStyle(true)
      .withStyleId("TOCArabic")
      .withName("TOCArabic")
      .withBasedOn("TOC1")
      .withNext("Normal")
      .withAutoRedefine(true)
      .withUiPriority(39)
      .withUnhideWhenUsed(true)
      .withRsid(IdGenerator.nextId())
      .withPPr(
        WmlBuilderFactory
          .getPPrBuilder
          .withTabs(tabs)
          .withSpacing(new SpacingBuilder().withAfter(100).getObject)
          .getObject
      )
      .withRPr(WmlBuilderFactory.getRPrBuilder.withRFonts(rFonts).getObject)
      .getObject
  }

  private def arabicPrefix =
    WmlBuilderFactory
      .getStyleBuilder
      .withType("paragraph")
      .withCustomStyle(true)
      .withStyleId("Arabic-Prefix")
      .withName("Arabic-Prefix")
      .withBasedOn("Arabic-Table-Center")
      .withLink("Arabic-PrefixChar")
      .withQFormat(true)
      .withRsid(IdGenerator.nextId())
      .withRPr(
        WmlBuilderFactory
          .getRPrBuilder
          .withRFonts(rFonts)
          .withColor(WmlBuilderFactory.getColorBuilder.withVal("C00000").getObject)
          .withSz(fontSize * 2)
          .withSzCs(fontSize * 2)
          .getObject
      )
      .getObject

  private def arabicPrefixChar =
    WmlBuilderFactory
      .getStyleBuilder
      .withType("character")
      .withCustomStyle(true)
      .withStyleId("Arabic-PrefixChar")
      .withName("Arabic-Prefix Char")
      .withBasedOn("Arabic-Table-CenterChar")
      .withLink("Arabic-Prefix")
      .withRsid(IdGenerator.nextId())
      .withRPr(
        WmlBuilderFactory
          .getRPrBuilder
          .withRFonts(rFonts)
          .withColor(WmlBuilderFactory.getColorBuilder.withVal("C00000").getObject)
          .getObject
      )
      .getObject

}

object CustomStyles {
  def apply(fontName: String, size: Long, headingSize: Long): CustomStyles =
    new CustomStyles(fontName, size, headingSize)
}
