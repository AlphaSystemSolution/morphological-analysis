package com.alphasystem
package arabic
package fx
package ui
package util

import utils.GenericPreferences
import javafx.scene.text.{ FontPosture, FontWeight }
import scalafx.scene.text.Font

import java.util.prefs.Preferences

abstract class UIUserPreferences protected (klass: Class[?]) extends GenericPreferences(klass) {

  import UIUserPreferences.*

  protected val nodePrefix: String

  protected lazy val fontNode: Preferences = node(nodePrefix, FontNodeName)

  // protected lazy val fileNode: Preferences = node(nodePrefix, FileNodeName)

  def arabicFontName: String =
    fontNode.get(ArabicFontNameKey, FontUtilities.ArabicFontName)

  def arabicFontName_=(value: String): Unit =
    if Option(value).isDefined && !value.isBlank then fontNode.put(ArabicFontNameKey, value)

  def arabicFontSize: Double =
    fontNode.getDouble(ArabicFontSizeKey, FontUtilities.ArabicRegularFontSize)

  def arabicFontSize_=(value: Long): Unit =
    if value > 0L then fontNode.putLong(ArabicFontSizeKey, value)

  def arabicHeadingFontSize: Double =
    fontNode.getDouble(
      ArabicHeadingFontSizeKey,
      FontUtilities.ArabicHeadingFontSize
    )

  def arabicHeadingFontSize_=(value: Double): Unit =
    if value > 0L then fontNode.putDouble(ArabicHeadingFontSizeKey, value)
  def englishFontName: String =
    fontNode.get(EnglishFontNameKey, FontUtilities.EnglishFontName)

  def englishFontName_=(value: String): Unit =
    if Option(value).isDefined && !value.isBlank then fontNode.put(EnglishFontNameKey, value)

  def englishFontSize: Double =
    fontNode.getDouble(EnglishFontSizeKey, FontUtilities.EnglishFontSize)

  def englishFontSize_=(value: Double): Unit =
    if value > 0L then fontNode.putDouble(EnglishFontSizeKey, value)

  def arabicFont: Font =
    Font(
      arabicFontName,
      FontWeight.NORMAL,
      FontPosture.REGULAR,
      arabicFontSize
    )

  def englishFont: Font =
    Font(
      englishFontName,
      FontWeight.NORMAL,
      FontPosture.REGULAR,
      englishFontSize
    )
}

object UIUserPreferences {
  private val FontNodeName = "font"
  // private val FileNodeName = "file"
  private val ArabicFontNameKey = "arabicFontName"
  private val ArabicFontSizeKey = "arabicFontSize"
  private val EnglishFontNameKey = "englishFontName"
  private val EnglishFontSizeKey = "englishFontSize"
  private val ArabicHeadingFontSizeKey = "arabicHeadingFontSize"
  // private val InitialDirectoryKey = "initialDirectory"
}
