package com.alphasystem
package arabic
package cli
package asciidoc

import com.alphasystem.arabic.model.{ ArabicLetterType, ArabicWord }
import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Token
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache.{ CacheFactory, TokenRequest }

import java.nio.file.{ Files, Path }
import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.io.Source
import scala.jdk.CollectionConverters.*
import scala.util.Using

object DocumentGenerator {

  private val NewLine = System.lineSeparator()

  def buildDocument(cacheFactory: CacheFactory, dataRequest: DataRequest, docAttributes: String): Seq[String] = {
    val buffer = ListBuffer(docAttributes)
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
              cacheFactory,
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

    buffer.toSeq
  }

  def buildDocument(cacheFactory: CacheFactory, srcPath: Path, destPath: Path, attributesPath: Option[Path]): Unit = {
    val dataRequest = toDataRequest(srcPath)

    val attributes =
      attributesPath match
        case Some(path) =>
          Using(Source.fromFile(path.toFile))(_.mkString).toOption.getOrElse("")
        case None => ""

    Files.write(destPath, buildDocument(cacheFactory, dataRequest, attributes).asJava)
  }

  private[asciidoc] def findAndEncode(
    cacheFactory: CacheFactory,
    appendVerseNumber: Boolean,
    tokenRequest: TokenRequest,
    tokenRange: Option[TokenRange],
    highlightedTokenRange: Seq[TokenRange]
  ): String = {
    val chapterNumber = tokenRequest.chapterNumber
    val verseNumber = tokenRequest.verseNumber

    val tokens = cacheFactory.tokens.synchronous().get(tokenRequest)
    if tokens.isEmpty then
      Future.failed(
        throw new IllegalArgumentException(
          s"No tokens found for chapter = $chapterNumber, verse = $verseNumber"
        )
      )

    val updatedTokens = tokenRange match {
      case Some(TokenRange(minToken, maxToken, highLightColor)) =>
        tokens.collect {
          case token
              if token.tokenNumber >= minToken && ((maxToken > 0 && token.tokenNumber <= maxToken) || maxToken <= 0) =>
            token
        }

      case None => tokens
    }

    // we want to run through entire length of tokens, value (-1, -1) will make sure we continue encoding once
    // finished highlightedTokens
    val highlightedTokens =
      highlightedTokenRange.map(r => (r.minToken, r.maxToken, r.highLightColor)).toList :+ (-1, -1, None)

    val encodingResult =
      highlightedTokens.foldLeft(EncodingResult("", 0)) { case (result, (minToken, maxToken, highLightColor)) =>
        encode(result.index, minToken, maxToken, result.value, updatedTokens, highLightColor)
      }

    val verseNumberText =
      if tokenRange.isEmpty && appendVerseNumber then
        s" ${ArabicLetterType.LeftParenthesis.htmlCode}${ArabicWord(verseNumber).htmlCode}${ArabicLetterType.RightParenthesis.htmlCode}"
      else ""

    var value = encodingResult.value
    value = if value.startsWith("##") || value.startsWith("[") then s"{nbsp}$value" else value
    value = if value.endsWith("##") then s"$value{nbsp}" else value
    value = value + verseNumberText
    val finalValue = s"[arabicNormal]#$value#"

    val tokensValue = tokenRange
      .map(tokenRange => s"(${tokenRange.minToken}:${tokenRange.maxToken})")
      .getOrElse("")

    val comment = s"// $chapterNumber:$verseNumber $tokensValue$NewLine"
    s"$comment|$finalValue"
  }

  def encode(minToken: Int, maxToken: Int, tokens: Seq[Token], highLightColor: Option[String]): String =
    encode(0, minToken, maxToken, "", tokens, highLightColor).value

  @tailrec
  private def encode(
    index: Int,
    minToken: Int,
    maxToken: Int,
    result: String,
    tokens: Seq[Token],
    highLightColor: Option[String]
  ): EncodingResult = {
    val maybeToken = tokens.lift(index)
    val tokenNumber = maybeToken.map(_.tokenNumber).getOrElse(-1)
    val tokenText = maybeToken.map(_.token).getOrElse("")
    val highlightMarkup = if highLightColor.isDefined then s"[.highlight-${highLightColor.get}]##" else "##"
    if index >= tokens.length || (maxToken != -1 && tokenNumber > maxToken) then {
      // return result
      EncodingResult(result, index)
    } else if tokenNumber == minToken then {
      // start of highlight
      val codedValue = model.toHtmlCodeString(tokenText)
      var updatedResult =
        if result.isBlank then s"$highlightMarkup$codedValue" else s"$result $highlightMarkup$codedValue"

      // case where minToken = maxToken then close the markup
      updatedResult = if minToken == maxToken then s"$updatedResult##" else updatedResult
      encode(index + 1, minToken, maxToken, updatedResult, tokens, highLightColor)
    } else if tokenNumber == maxToken then {
      // end of highlight, close the markup
      val token = tokens(index)
      val updatedResult = s"$result ${model.toHtmlCodeString(tokenText)}##"
      encode(index + 1, minToken, maxToken, updatedResult, tokens, highLightColor)
    } else {
      // no highlight
      val codedValue = model.toHtmlCodeString(tokenText)
      val updatedResult = if result.isBlank then codedValue else s"$result $codedValue"
      encode(index + 1, minToken, maxToken, updatedResult, tokens, highLightColor)
    }
  }

  private def sanitizeString(src: String) = if src.isBlank then "{nbsp}" else src
}

final case class EncodingResult(value: String, index: Int)
