package com.alphasystem
package arabic
package examples_generator
package examples

import examples_generator.model.{ HighlightRange, TokenHighLight }

import scala.annotation.tailrec

object HighLighter {

  // for testing purposes only
  private var DoNotEncodeText = false

  private val Empty = ""
  private val Space = " "
  private val DefaultMarkup = "##"
  private val ColoredMarkupStart = (color: String) => s"[$color]$DefaultMarkup"

  def processHighlights(text: String, highlights: List[HighlightRange]): String = {
    val tokenInfos = text.split(Space).zipWithIndex.map { case (token, index) => TokenInfo(index, token) }.toSeq
    processHighlights(text, "", highlights.reverse, tokenInfos).trim
  }

  @tailrec
  private def processHighlights(
    token: String,
    result: String,
    highlights: List[HighlightRange],
    tokenInfos: Seq[TokenInfo]
  ): String = {
    highlights match {
      case Nil if token.isBlank => result
      case Nil                  => s"${encode(token)}$result"
      case HighlightRange(_, tokenStart, tokenEnd, color) :: tail =>
        val startOffset = tokenOffset(tokenStart, tokenInfos)
        val endOffset = tokenOffset(tokenEnd, tokenInfos, endOffset = true)

        val beforeHighlight = token.substring(0, startOffset)
        val highlightedText = token.substring(startOffset, endOffset)
        val afterHighlight = token.substring(endOffset)

        val startMarkup = color.map(_.colorName).map(ColoredMarkupStart).getOrElse(DefaultMarkup)
        val leadingSpace = if tokenStart.locationIndex.getOrElse(1) == 1 then Space else Empty

        val updatedResult =
          s"$leadingSpace$startMarkup${encode(highlightedText)}$DefaultMarkup${encode(afterHighlight)}$result"

        processHighlights(beforeHighlight, updatedResult.trim, tail, tokenInfos)
    }
  }

  private def tokenOffset(token: TokenHighLight, tokenInfos: Seq[TokenInfo], endOffset: Boolean = false): Int = {
    val providedTokenIndex = token.index
    val tokenIndex = if endOffset && providedTokenIndex <= -1 then tokenInfos.length else providedTokenIndex
    val index = tokenIndex - 1
    val tokenInfo = tokenInfos(index)
    val previousTokensLength = tokenInfos.take(index).map(_.tokenLength).sum
    val previousSpacesLength = index

    val rawLocationIndex =
      token.locationIndex.getOrElse {
        if endOffset then tokenInfo.text.length
        else 1
      }
    val locationIndex = if endOffset && rawLocationIndex <= -1 then tokenInfo.text.length else rawLocationIndex

    previousTokensLength + previousSpacesLength + locationIndex - Option.when(!endOffset)(1).getOrElse(0)
  }

  private[examples] def getTokensWithinBound(start: Int, end: Int, tokens: Seq[String]): String = {
    if end <= -1 then tokens.drop(start - 1).mkString else tokens.slice(start - 1, end).mkString
  }

  private def encode(text: String) =
    if text.isBlank || text.startsWith("{") || DoNotEncodeText then text else arabic.model.toHtmlCodeString(text)

  private case class TokenInfo(index: Int, text: String) {
    lazy val tokenLength: Int = text.length
    lazy val tokens: Seq[String] = text.split("").toSeq
  }

  private[examples] def disableEncoding(): Unit = DoNotEncodeText = true
}
