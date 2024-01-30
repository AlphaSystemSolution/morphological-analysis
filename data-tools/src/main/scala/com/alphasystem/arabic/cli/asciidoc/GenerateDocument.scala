package com.alphasystem
package arabic
package cli
package asciidoc

import arabic.model.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Token
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache.TokenRequest
import morphologicalanalysis.morphology.persistence.MorphologicalAnalysisDatabase
import org.rogach.scallop.{ ScallopOption, Subcommand }

import java.nio.file.{ Files, Path }
import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.Using
import scala.jdk.CollectionConverters.*

class GenerateDocument(database: MorphologicalAnalysisDatabase) extends Subcommand("asciidoc") {

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

      val highlightedTokensMap: Map[Int, Seq[TokenRange]] =
        column.highlightedTokens.groupBy(_.verseNumber).map { verseSearch =>
          verseSearch._1 -> verseSearch._2.flatMap(_.tokenRange)
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
              highlightedTokensMap.getOrElse(verseNumber, Seq.empty)
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
    highlightedTokenRange: Seq[TokenRange]
  ): String = {
    var tokens = database.findTokensByVerseId(tokenRequest.verseId)

    val chapterNumber = tokenRequest.chapterNumber
    val verseNumber = tokenRequest.verseNumber
    if tokens.isEmpty then
      throw new IllegalArgumentException(
        s"No tokens found for chapter = $chapterNumber, verse = $verseNumber"
      )

    tokens = tokenRange match {
      case Some(TokenRange(minToken, maxToken)) =>
        tokens.collect {
          case token
              if token.tokenNumber >= minToken && ((maxToken > 0 && token.tokenNumber <= maxToken) || maxToken <= 0) =>
            token
        }

      // tokens.slice(minToken, maxToken + 1).map(_.token).toArray
      case None => tokens
    }

    // we want to run through entire length of tokens, value (-1, -1) will make sure we continue encoding once
    // finished highlightedTokens
    val highlightedTokens = highlightedTokenRange.map(r => (r.minToken, r.maxToken)).toList :+ (-1, -1)

    val encodingResult =
      highlightedTokens.foldLeft(EncodingResult("", 0)) { case (result, (minToken, maxToken)) =>
        encode(result.index, minToken, maxToken, result.value, tokens)
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
  private def encode(
    index: Int,
    minToken: Int,
    maxToken: Int,
    result: String,
    tokens: Seq[Token]
  ): EncodingResult = {
    val maybeToken = tokens.lift(index)
    val tokenNumber = maybeToken.map(_.tokenNumber).getOrElse(-1)
    val tokenText = maybeToken.map(_.token).getOrElse("")
    if index >= tokens.length || (maxToken != -1 && tokenNumber > maxToken) then EncodingResult(result, index)
    else if tokenNumber == minToken then {
      val codedValue = toHtmlCodeString(tokenText)
      var updatedResult = if result.isBlank then s"##$codedValue" else s"$result ##$codedValue"
      updatedResult = if minToken == maxToken then s"$updatedResult##" else updatedResult
      encode(index + 1, minToken, maxToken, updatedResult, tokens)
    } else if tokenNumber == maxToken then {
      val token = tokens(index)
      val updatedResult = s"$result ${toHtmlCodeString(tokenText)}##"
      encode(index + 1, minToken, maxToken, updatedResult, tokens)
    } else {
      val codedValue = toHtmlCodeString(tokenText)
      val updatedResult = if result.isBlank then codedValue else s"$result $codedValue"
      encode(index + 1, minToken, maxToken, updatedResult, tokens)
    }
  }

}

object GenerateDocument {
  private val NewLine = System.lineSeparator()

  private final case class EncodingResult(value: String, index: Int)

  def apply(database: MorphologicalAnalysisDatabase): GenerateDocument = new GenerateDocument(database)
}
