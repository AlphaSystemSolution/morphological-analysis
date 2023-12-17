package com.alphasystem
package arabic
package parser

import arabic.morphologicalanalysis.morphology.persistence.DatabaseInit
import morphologicalanalysis.morphology.model.{ Chapter, Token, Verse }
import org.jdom2.Element
import org.jdom2.input.SAXBuilder

import java.io.File
import scala.jdk.CollectionConverters.*

class DataParser extends DatabaseInit {

  private val builder = new SAXBuilder

  def parse(): Unit = {
    val document = builder.build(new File("quran-simple.xml"))
    val rootElement = document.getRootElement

    rootElement.getChildren.asScala.map(parseChapter).foreach { case (chapter, verses, tokens) =>
      database.createChapter(chapter)
      database.createVerses(verses)
      database.createTokens(tokens)
    }
    database.close()
  }

  private def parseChapter(element: Element) = {
    val children = element.getChildren.asScala.toList
    val chapter =
      Chapter(
        chapterName = element.getAttributeValue("name").trim,
        chapterNumber = element.getAttributeValue("index").toInt,
        verseCount = children.size
      )

    val versesNTokens: (List[Verse], List[Token]) =
      children
        .map(parseVerse(chapter))
        .foldLeft((List[Verse](), List[Token]())) { case ((l1, l2), (verse, tokens)) =>
          (l1 :+ verse, l2 ::: tokens)
        }

    (chapter, versesNTokens._1, versesNTokens._2)
  }

  private def parseVerse(chapter: Chapter)(element: Element) = {
    val verseNumber = element.getAttributeValue("index").toInt
    val text = element.getAttributeValue("text").trim

    val tokens =
      parseTokens(chapter.chapterNumber, verseNumber, text.split(" "))

    val verse =
      Verse(
        chapterNumber = chapter.chapterNumber,
        verseNumber = verseNumber,
        text = text,
        tokenCount = tokens.length
      )

    (verse, tokens.toList)
  }

  private def parseTokens(
    chapterNumber: Int,
    verseNumber: Int,
    tokens: Array[String]
  ) =
    tokens
      .flatMap { token =>
        val trimmedToken = token.trim
        if trimmedToken.isBlank then None else Some(trimmedToken)
      }
      .zipWithIndex
      .map { case (token, index) =>
        Token(
          chapterNumber = chapterNumber,
          verseNumber = verseNumber,
          tokenNumber = index + 1,
          token = token,
          hidden = false
        )
      }

}

object DataParser {
  def main(args: Array[String]): Unit = {
    new DataParser().parse()
  }
}
