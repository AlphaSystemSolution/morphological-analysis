package com.alphasystem.fx.ui.util

import com.alphasystem.utils.GenericPreferences

import java.util.prefs.Preferences

abstract class UIUserPreferences protected (klass: Class[?])
    extends GenericPreferences(klass) {

  import UIUserPreferences.*

  protected val nodePrefix: String

  protected lazy val fontNode: Preferences = node(nodePrefix, FontNodeName)

  // protected lazy val fileNode: Preferences = node(nodePrefix, FileNodeName)

  def arabicFontName: String =
    fontNode.get(ArabicFontNameKey, FontUtilities.ArabicFontName)

  def arabicFontName_=(value: String): Unit =
    if Option(value).isDefined && !value.isBlank then
      fontNode.put(ArabicFontNameKey, value)

  def arabicFontSize: Long =
    fontNode.getLong(ArabicFontSizeKey, FontUtilities.ArabicRegularFontSize)

  def arabicFontSize_=(value: Long): Unit =
    if value > 0L then fontNode.putLong(ArabicFontSizeKey, value)

  def arabicHeadingFontSize: Long =
    fontNode.getLong(
      ArabicHeadingFontSizeKey,
      FontUtilities.ArabicHeadingFontSize
    )

  def arabicHeadingFontSize_=(value: Long): Unit =
    if value > 0L then fontNode.putLong(ArabicHeadingFontSizeKey, value)
  def englishFontName: String =
    fontNode.get(EnglishFontNameKey, FontUtilities.EnglishFontName)

  def englishFontName_=(value: String): Unit =
    if Option(value).isDefined && !value.isBlank then
      fontNode.put(EnglishFontNameKey, value)

  def englishFontSize: Long =
    fontNode.getLong(EnglishFontSizeKey, FontUtilities.EnglishFontSize)

  def englishFontSize_=(value: Long): Unit =
    if value > 0L then fontNode.putLong(EnglishFontSizeKey, value)
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
