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
    * @param tokenRange
    *   An optional range of tokens to extract from the verse text. If not provided, the full text of the verse is
    *   returned. If the range is out of bounds, an exception is thrown.
    * @return
    *   The text of the verse, or a sliced portion of the text based on the token range if specified.
    * @throws RuntimeException
    *   If the specified verse is not found, or if the token range is invalid.
    */
  def searchVerse(chapterNumber: Int, verseNumber: Int, tokenRange: Option[Bound] = None): String = {
    val xpath =
      XPathFactory.instance.compile(s"//sura[@index='$chapterNumber']/aya[@index='$verseNumber']", Filters.element)
    val elements = xpath.evaluate(document).asScala.toSeq
    if elements.isEmpty then throw new RuntimeException(s"Verse $chapterNumber:$verseNumber not found")
    else {
      val element = elements.head
      val text = element.getAttributeValue("text")
      val tokens = text.split(" ")
      val result =
        tokenRange match {
          case Some(Bound(start, end)) if start - 1 >= tokens.length =>
            throw new RuntimeException(s"Invalid range ($start, $end), out of bounds of ${tokens.length}")
          case Some(Bound(start, end)) if end > 0   => tokens.slice(start - 1, end).mkString(" ")
          case Some(Bound(start, end)) if end <= -1 => tokens.drop(start - 1).mkString(" ")
          case _                                    => text
        }

      if result.isBlank then
        throw {
          val range = tokenRange.map(value => s" (${value.start}, ${value.end})").getOrElse("")
          new RuntimeException(s"Verse $chapterNumber:$verseNumber$range is empty")
        }
      result
    }
  }
}
