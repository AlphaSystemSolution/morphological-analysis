package com.alphasystem
package arabic
package parser

import arabic.morphologicalanalysis.morphology.persistence.{ DatabaseInit, Done }
import morphologicalanalysis.morphology.model.{ Chapter, Token, Verse }
import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.jdom2.Element
import org.jdom2.input.SAXBuilder
import org.slf4j.LoggerFactory

import java.io.File
import scala.concurrent.{ ExecutionContext, Future }
import scala.jdk.CollectionConverters.*
import scala.util.{ Failure, Success }

class DataParser(implicit ec: ExecutionContext) extends DatabaseInit {

  private val logger = LoggerFactory.getLogger(classOf[DataParser])
  private val builder = new SAXBuilder

  def parse(): Future[Int] = {
    val document = builder.build(new File("quran-simple.xml"))
    val rootElement = document.getRootElement

    Future
      .traverse(rootElement.getChildren.asScala.map(parseChapter)) { case (chapter, verses, tokens) =>
        for {
          _ <- database.createChapter(chapter)
          _ = logger.info("Chapter: {}, created.", chapter.chapterNumber)
          _ <- database.createVerses(verses)
          v = logger.info("{} verses created for chapter: {}", verses.length, chapter.chapterNumber)
          _ <- database.createTokens(tokens)
          _ = logger.info("Tokens created for chapter: {}", chapter.chapterNumber)
        } yield Done
      }
      .map(_.length)
  }

  def close(): Unit = {
    database.close().onComplete {
      case Failure(ex) => logger.error("Database close failed!!", ex)
      case Success(_)  => logger.info("Database closed!")
    }
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

    logger.info("Parsing chapter: {}", chapter.chapterNumber)
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
    implicit val system: ActorSystem[Nothing] = ActorSystem[Nothing](Behaviors.empty, "data-migrator")
    import system.executionContext
    val dataParser = new DataParser()
    dataParser.parse().onComplete {
      case Failure(ex) => ex.printStackTrace()
      case Success(size) =>
        println(s"Migration done for $size chapters.")
        if size == 114 then {
          dataParser.close()
          system.terminate()
        }
    }
  }
}
