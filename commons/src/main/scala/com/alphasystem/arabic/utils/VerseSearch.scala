package com.alphasystem
package arabic
package utils

import org.jdom2.filter.Filters
import org.jdom2.input.SAXBuilder
import org.jdom2.xpath.XPathFactory

import scala.jdk.CollectionConverters.*

/** Provides functionality to search for specific verses in the Quranic text from an XML file.
  */
class VerseSearch {

  private val builder = new SAXBuilder
  private val document = builder.build("quran-simple.xml".asResourceUrl)

  /** Returns ChapterInfo, containing chapter number, name, and verse counts.
    *
    * @return
    *   ChapterInfo, containing chapter number, name, and verse counts
    */
  def getChapters: Seq[ChapterInfo] = {
    val chapterNamesPath = XPathFactory.instance.compile("//sura/@name", Filters.attribute())
    val chapterNames = chapterNamesPath.evaluate(document).asScala.toSeq.map(_.getValue)
    chapterNames.zipWithIndex.map { (chapterName, index) =>
      val verseCountPath = XPathFactory.instance.compile(s"count(//sura[@name='$chapterName']/*)")
      val verseCount = verseCountPath.evaluate(document).asScala.toSeq.head.asInstanceOf[Double]
      ChapterInfo(index + 1, chapterName, verseCount.toInt)
    }
  }

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

  def searchVerses(chapterNumber: Int, startVerseIndex: Int, endVerseIndex: Int): Seq[VerseResult] = {
    if endVerseIndex < startVerseIndex then
      throw new IllegalArgumentException(
        s"start index ($startVerseIndex) must be equal of less than end index ($endVerseIndex)"
      )

    val xpath =
      XPathFactory
        .instance
        .compile(
          s"//sura[@index=$chapterNumber]/aya[@index>= $startVerseIndex and @index <= $endVerseIndex]",
          Filters.element
        )

    val elements = xpath.evaluate(document).asScala.toSeq
    if elements.isEmpty then throw new RuntimeException(s"No result found")
    (startVerseIndex to endVerseIndex).zip(elements.map(_.getAttributeValue("text"))).map { case (index, text) =>
      VerseResult(index, text)
    }
  }

}

case class ChapterInfo(chapterNumber: Int, chapterName: String, verseCount: Int)

case class VerseResult(verseNumber: Int, text: String)
