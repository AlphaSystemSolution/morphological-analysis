package com.alphasystem
package arabic
package cli
package examples

import arabic.utils.*
import org.jdom2.filter.Filters
import org.jdom2.input.SAXBuilder
import org.jdom2.xpath.XPathFactory

import scala.jdk.CollectionConverters.*

/** Provides functionality to search for specific verses in the Quranic text from an XML file.
  */
class VerseSearch {

  private val builder = new SAXBuilder
  private val document = builder.build("quran-simple.xml".asResourceUrl)

  /** Searches and retrieves a specific verse from a chapter in the text, optionally slicing the verse text based on a
    * given range of tokens.
    *
    * @param chapterNumber
    *   The chapter number to search within.
    * @param verseNumber
    *   The verse number to locate within the chapter.
    * @param tokenStart
    *   optional index of start token, if provided text starting from this index will be returned
    * @param tokenEnd
    *   optional index of end token, if provided text ending at this index will be returned
    * @return
    *   The text of the verse, or a sliced portion of the text based on the token range if specified.
    * @throws RuntimeException
    *   If the specified verse is not found, or if the token range is invalid.
    */
  def searchVerse(
    chapterNumber: Int,
    verseNumber: Int,
    tokenStart: Option[Int] = None,
    tokenEnd: Option[Int] = None
  ): String = {
    val xpath =
      XPathFactory.instance.compile(s"//sura[@index='$chapterNumber']/aya[@index='$verseNumber']", Filters.element)
    val elements = xpath.evaluate(document).asScala.toSeq
    if elements.isEmpty then throw new RuntimeException(s"Verse $chapterNumber:$verseNumber not found")
    else {
      val element = elements.head
      val text = element.getAttributeValue("text")
      val tokens = text.split(" ")
      val startTokenIndex = tokenStart.getOrElse(1)
      val endTokenIndex = tokenEnd.getOrElse(-1)
      val subTokens =
        if endTokenIndex <= 0 then tokens.drop(startTokenIndex - 1)
        else tokens.slice(startTokenIndex - 1, endTokenIndex)
      val result = subTokens.mkString(" ")

      if result.isBlank then
        throw new RuntimeException(s"Verse $chapterNumber:$verseNumber($startTokenIndex, $endTokenIndex) is empty")
      else result
    }
  }
}
