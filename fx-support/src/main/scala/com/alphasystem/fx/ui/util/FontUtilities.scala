package com.alphasystem.fx.ui.util

import javafx.scene.text.Font
import scala.jdk.CollectionConverters._

object FontUtilities {

  private val KfgqpcUthmanTahaNaskh = "KFGQPC Uthman Taha Naskh"
  private val TraditionalArabic = "Traditional Arabic"
  private val ArabicTypesetting = "Arabic Typesetting"
  private val Arial = "Arial"
  private val Candara = "Candara"

  private val ArabicFontNameKey = "arabic.font.name"
  private val ArabicRegularFontSizeKey = "arabic.regular.font.size"
  private val ArabicHeadingFontSizeKey = "arabic.heading.font.size"
  private val EnglishRegularFontSizeKey = "english.regular.font.size"
  private val DefaultArabicFontSize = 20L
  private val DefaultArabicHeadingFontSize = 40L
  private val DefaultEnglishFontSize = 12L

  private lazy val families = Font.getFamilies.asScala.toList
  lazy val ArabicFontName: String = getDefaultArabicFontName(families)
  lazy val EnglishFontName: String = getDefaultEnglishFont(families)
  lazy val ArabicRegularFontSize: Long = getArabicRegularFontSize
  lazy val ArabicHeadingFontSize: Long = getArabicHeadingFontSize
  lazy val EnglishFontSize: Long = getEnglishRegularFontSize

  private def getDefaultArabicFontName(families: List[String]): String = {
    val fontName = System.getProperty(ArabicFontNameKey)
    if !fontName.isBlank then fontName
    else if families.contains(KfgqpcUthmanTahaNaskh) then KfgqpcUthmanTahaNaskh
    else if families.contains(TraditionalArabic) then TraditionalArabic
    else if families.contains(ArabicTypesetting) then ArabicTypesetting
    else Arial
  }

  private def getDefaultEnglishFont(families: List[String]) =
    if families.contains(Candara) then Candara else Arial

  private def getFontSize(propertyName: String, defaultSize: Long) = {
    val defaultValue = String.valueOf(defaultSize)
    System.getProperty(propertyName, defaultValue).toLong
  }

  private def getArabicRegularFontSize =
    getFontSize(ArabicRegularFontSizeKey, DefaultArabicFontSize)

  private def getEnglishRegularFontSize =
    getFontSize(EnglishRegularFontSizeKey, DefaultEnglishFontSize)

  private def getArabicHeadingFontSize =
    getFontSize(ArabicHeadingFontSizeKey, DefaultArabicHeadingFontSize)
}
