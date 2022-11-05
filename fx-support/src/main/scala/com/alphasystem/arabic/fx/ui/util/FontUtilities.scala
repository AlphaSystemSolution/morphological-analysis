package com.alphasystem
package arabic
package fx
package ui
package util

import javafx.scene.text.Font

import scala.annotation.tailrec
import scala.jdk.CollectionConverters.*

object FontUtilities {

  private val Arial = "Arial"
  private val Candara = "Candara"

  private val arabicFonts = List(
    "Al Bayan",
    "KFGQPC Uthman Taha Naskh",
    "KFGQPC Uthmanic Script HAFS",
    "Arabic Typesetting",
    "Traditional Arabic"
  )

  private val ArabicFontNameKey = "arabic.font.name"
  private val ArabicPosFontNameKey = "arabic.pos.font.name"
  private val ArabicRegularFontSizeKey = "arabic.regular.font.size"
  private val ArabicHeadingFontSizeKey = "arabic.heading.font.size"
  private val EnglishRegularFontSizeKey = "english.regular.font.size"
  private val DefaultArabicFontSize = 25.0
  private val DefaultArabicHeadingFontSize = 40.0
  private val DefaultEnglishFontSize = 12.0

  private lazy val families = Font.getFamilies.asScala.toList
  lazy val ArabicFontName: String = getDefaultArabicFontName(families)
  lazy val ArabicPosFontName: String = getDefaultArabicPosFontName(families)
  lazy val EnglishFontName: String = getDefaultEnglishFont(families)
  lazy val ArabicRegularFontSize: Double = getArabicRegularFontSize
  lazy val ArabicHeadingFontSize: Double = getArabicHeadingFontSize
  lazy val EnglishFontSize: Double = getEnglishRegularFontSize

  private def getDefaultArabicFontName(families: List[String]): String = {
    val fontName = System.getProperty(ArabicFontNameKey)
    if Option(fontName).isDefined && !fontName.isBlank then fontName
    else findFont(families, arabicFonts)
  }

  private def getDefaultArabicPosFontName(families: List[String]): String = {
    val fontName = System.getProperty(ArabicPosFontNameKey)
    if Option(fontName).isDefined && !fontName.isBlank then fontName
    else findFont(families, arabicFonts.tail)
  }

  @tailrec
  private def findFont(
    families: List[String],
    fontNames: List[String]
  ): String =
    fontNames match
      case Nil => Arial
      case head :: tail =>
        if families.contains(head) then head
        else findFont(families, tail)

  private def getDefaultEnglishFont(families: List[String]) =
    if families.contains(Candara) then Candara else Arial

  private def getFontSize(propertyName: String, defaultSize: Double) = {
    val defaultValue = String.valueOf(defaultSize)
    System.getProperty(propertyName, defaultValue).toDouble
  }

  private def getArabicRegularFontSize =
    getFontSize(ArabicRegularFontSizeKey, DefaultArabicFontSize)

  private def getEnglishRegularFontSize =
    getFontSize(EnglishRegularFontSizeKey, DefaultEnglishFontSize)

  private def getArabicHeadingFontSize =
    getFontSize(ArabicHeadingFontSizeKey, DefaultArabicHeadingFontSize)
}
