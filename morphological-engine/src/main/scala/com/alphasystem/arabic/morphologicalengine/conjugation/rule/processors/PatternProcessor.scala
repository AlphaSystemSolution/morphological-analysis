package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{ ArabicWord, DiacriticType, SarfMemberType }
import conjugation.model.internal.RootWord

import scala.annotation.tailrec
import scala.util.matching.Regex

class PatternProcessor extends RuleProcessor {

  import PatternProcessor.*

  override def applyRules(
    memberType: SarfMemberType,
    baseRootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord = {
    var text = removeConsecutiveSukun(baseRootWord.derivedWord.code)
    text = mergeRepeats(text)

    val updatedWord = ArabicWord(text, fromUnicode = false)
    if baseRootWord.derivedWord != updatedWord then processingContext.applyRule(getClass.getSimpleName)
    baseRootWord.copy(derivedWord = updatedWord)
  }
}

object PatternProcessor {

  private val SukunCode = DiacriticType.Sukun.code
  private val ConsecutiveSukunPattern = raw"[A-Za-z$$]$SukunCode[A-Za-z$$]$SukunCode".r
  private val IdghamPattern = "[A-Za-z$]o[A-Za-z$]".r

  @tailrec
  private[processors] def removeConsecutiveSukun(text: String): String =
    ConsecutiveSukunPattern.findFirstMatchIn(text) match
      case Some(matchValue) =>
        val start = matchValue.start
        removeConsecutiveSukun(text.substring(0, start) + text.substring(start + 2))
      case None => text

  @tailrec
  private[processors] def mergeRepeats(text: String): String = {
    IdghamPattern.findFirstMatchIn(text) match
      case Some(matchValue) =>
        val start = matchValue.start
        val end = matchValue.end
        val matchedString = text.substring(start, end)
        val splits = matchedString.split('o')
        if splits.length == 2 then {
          val before = splits.head
          val after = splits.last
          if before == after then mergeRepeats(text.replaceFirst(matchedString, before + DiacriticType.Shadda.code))
          else text
        } else text
      case None => text
  }
  def apply(): RuleProcessor = new PatternProcessor
}
