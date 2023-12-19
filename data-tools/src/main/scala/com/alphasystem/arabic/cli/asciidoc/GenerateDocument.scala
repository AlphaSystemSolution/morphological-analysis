package com.alphasystem
package arabic
package cli
package asciidoc

import arabic.model.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache.TokenRequest
import morphologicalanalysis.morphology.persistence.Database
import org.rogach.scallop.{ ScallopOption, Subcommand }

import java.nio.file.{ Files, Path }
import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.Using
import scala.jdk.CollectionConverters.*

class GenerateDocument(database: Database) extends Subcommand("asciidoc") {

  import GenerateDocument.*

  val srcPath: ScallopOption[Path] = opt[Path](
    descr = "Path to source json file",
    required = true
  )

  val destPath: ScallopOption[Path] = opt[Path](
    descr = "Path to dest adoc file",
    required = true
  )

  val attributesPath: ScallopOption[Path] = opt[Path](
    descr = "Path to header attributes file",
    default = None,
    required = false
  )

  def buildDocument(): Unit = {
    val dataRequest = toDataRequest(srcPath())

    val attributes =
      attributesPath.toOption match
        case Some(path) =>
          Using(Source.fromFile(path.toFile))(_.mkString).toOption.getOrElse("")
        case None => ""

    val buffer = ListBuffer(attributes)
    buffer.addOne(s"""[cols="${dataRequest.columns}", align="center", halign="center", valign="center"]""")
    buffer.addOne("|===").addOne("")

    dataRequest.requests.foreach { column =>
      val translationColumn =
        ColumnInfo(dataRequest.translationColumnIndex, s"|[translation]#${sanitizeString(column.translation)}#")

      val highlightedTokensMap = column.highlightedTokens.groupBy(_.verseNumber).map { verseSearch =>
        verseSearch._1 -> verseSearch._2.headOption.flatMap(_.tokenRange)
      }

      val searchRequest = column.request
      val chapterNumber = searchRequest.chapterNumber
      val appendVerseNumber = searchRequest.verses.size > 1
      val arabicText =
        searchRequest.verses.foldLeft("") { case (columnText, verseSearch) =>
          val verseNumber = verseSearch.verseNumber
          val encodedValue =
            findAndEncode(
              appendVerseNumber,
              TokenRequest(chapterNumber, verseNumber),
              verseSearch.tokenRange,
              highlightedTokensMap.get(verseNumber).flatten
            )

          if columnText.isBlank then encodedValue else s"$columnText $encodedValue"
        }

      val arabicColumn = ColumnInfo(dataRequest.arabicColumnIndex, arabicText)

      val seq = (Seq(translationColumn, arabicColumn) ++ column.remainingColumns).filter(_.index >= 0).sortBy(_.index)

      val text =
        seq.foldLeft("") { case (result, columnInfo) =>
          s"$result${columnInfo.value}$NewLine"
        }
      buffer.addOne(text)
    }

    buffer.addOne("|===")

    Files.write(destPath(), buffer.asJava)
  }

  private def findAndEncode(
    appendVerseNumber: Boolean,
    tokenRequest: TokenRequest,
    tokenRange: Option[TokenRange],
    highlightedTokenRange: Option[TokenRange]
  ): String = {
    val tokens = database.findTokensByVerseId(tokenRequest.verseId)

    val chapterNumber = tokenRequest.chapterNumber
    val verseNumber = tokenRequest.verseNumber
    if tokens.isEmpty then
      throw new IllegalArgumentException(
        s"No tokens found for chapter = $chapterNumber, verse = $verseNumber"
      )

    val tokenTexts = tokenRange match {
      case Some(TokenRange(minToken, maxToken)) => tokens.slice(minToken, maxToken + 1).map(_.token).toArray
      case None                                 => tokens.map(_.token).toArray
    }

    // we want to run through entire length of tokens, value (-1, -1) will make sure we continue encoding once
    // finished highlightedTokens
    val highlightedTokens = highlightedTokenRange.map(r => (r.minToken, r.maxToken)).toList :+ (-1, -1)

    val encodingResult =
      highlightedTokens.foldLeft(EncodingResult("", 0)) { case (result, (minToken, maxToken)) =>
        encode(result.index, minToken, maxToken, result.value, tokenTexts)
      }

    val verseNumberText =
      if tokenRange.isEmpty && appendVerseNumber then
        s" ${ArabicLetterType.LeftParenthesis.htmlCode}${ArabicWord(verseNumber).htmlCode}${ArabicLetterType.RightParenthesis.htmlCode}"
      else ""

    var value = encodingResult.value
    value = if value.startsWith("##") then s"{nbsp}$value" else value
    value = if value.endsWith("##") then s"$value{nbsp}" else value
    value = value + verseNumberText
    val finalValue = s"[arabicNormal]#$value#"

    val tokensValue = tokenRange
      .map(tokenRange => s"(${tokenRange.minToken}:${tokenRange.maxToken})")
      .getOrElse("")

    val comment = s"// $chapterNumber:${verseNumber} $tokensValue$NewLine"
    s"$comment|$finalValue"
  }

  private def sanitizeString(src: String) = if src.isBlank then "{nbsp}" else src

  @tailrec
  private def encode(index: Int, minToken: Int, maxToken: Int, result: String, tokens: Array[String]): EncodingResult =
    if index >= tokens.length || (maxToken != -1 && index > maxToken) then EncodingResult(result, index)
    else if index == minToken then {
      val token = tokens(index)
      val codedValue = toHtmlCodeString(token)
      var updatedResult = if result.isBlank then s"##$codedValue" else s"$result ##$codedValue"
      updatedResult = if minToken == maxToken then s"$updatedResult##" else updatedResult
      encode(index + 1, minToken, maxToken, updatedResult, tokens)
    } else if (index == maxToken) then {
      val token = tokens(index)
      val updatedResult = s"$result ${toHtmlCodeString(token)}##"
      encode(index + 1, minToken, maxToken, updatedResult, tokens)
    } else {
      val token = tokens(index)
      val codedValue = toHtmlCodeString(token)
      val updatedResult = if result.isBlank then codedValue else s"$result $codedValue"
      encode(index + 1, minToken, maxToken, updatedResult, tokens)
    }
}

object GenerateDocument {
  private val NewLine = System.lineSeparator()

  private final case class EncodingResult(value: String, index: Int)

  def apply(database: Database): GenerateDocument = new GenerateDocument(database)
}
