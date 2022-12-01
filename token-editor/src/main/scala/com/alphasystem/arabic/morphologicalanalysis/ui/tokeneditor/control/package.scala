package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor

import morphologicalanalysis.morphology.model.Token

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer

package object control {

  def merge(selectedTokens: List[Token], allTokens: Seq[Token]): Seq[Token] = {
    @tailrec
    def mergeGroupsInternal(groups: List[List[Token]], tokens: Seq[Token]): Seq[Token] = {
      groups match
        case Nil     => tokens
        case x :: xs => mergeGroupsInternal(xs, mergeSingleGroup(x, tokens))
    }

    // fix token numbers
    mergeGroupsInternal(groupTokens(selectedTokens), allTokens)
      .zipWithIndex
      .foldLeft(Seq.empty[Token]) { case (seq, (token, index)) =>
        token.copy(tokenNumber = index + 1) +: seq
      }
      .reverse
  }

  private def groupTokens(ls: List[Token]): List[List[Token]] = {
    @tailrec
    def groupTokens(ls: List[Token], results: List[List[Token]], acc: List[Token]): List[List[Token]] =
      ls match
        case Nil => (acc.reverse :: results).reverse
        case x :: xs =>
          acc match
            case Nil                                            => groupTokens(xs, results, x :: acc)
            case y :: _ if (x.tokenNumber - y.tokenNumber) == 1 => groupTokens(xs, results, x :: acc)
            case _                                              => groupTokens(xs, acc.reverse :: results, x :: List())

    groupTokens(ls, List(), List()).filter(_.length > 1)
  }

  private def mergeSingleGroup(selectedTokens: List[Token], allTokens: Seq[Token]): Seq[Token] = {
    val head = selectedTokens.head
    // merge tokens
    val newToken = Token(
      chapterNumber = head.chapterNumber,
      verseNumber = head.verseNumber,
      tokenNumber = head.tokenNumber,
      token = selectedTokens.map(_.token).mkString(" "),
      hidden = false,
      translation = None
    )

    val rests = selectedTokens.drop(1).map(_.tokenNumber)

    allTokens
      .foldLeft(ListBuffer.empty[Token]) { case (buffer, token) =>
        val tokenNumber = token.tokenNumber
        // if current token is the head of selected token then replace it with new token
        // ignore any other selected token(s)
        // otherwise add it unchanged
        if tokenNumber == head.tokenNumber then buffer.addOne(newToken)
        else if rests.contains(tokenNumber) then buffer
        // else if tokenNumber > head.tokenNumber then buffer.addOne(token.copy(tokenNumber = tokenNumber - 1))
        else buffer.addOne(token)
      }
      .toSeq
  }
}
