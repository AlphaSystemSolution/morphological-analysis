package com.alphasystem
package arabic
package morphologicalanalysis
package ui

import arabic.model.{ ArabicLabel, ArabicLetters, ArabicWord }
import morphologicalanalysis.morphology.model.{ Location, NounStatus, PhraseType }
import morphologicalanalysis.morphology.utils.*
import morphology.graph.model.DependencyGraph
import ui.dependencygraph.utils.DependencyGraphPreferences

import java.nio.file.Path

package object dependencygraph {

  implicit val dependencyGraphPreferences: DependencyGraphPreferences = DependencyGraphPreferences()

  extension (src: DependencyGraph) {
    def toArabicLabel: ArabicLabel[DependencyGraph] =
      ArabicLabel(userData = src, code = src.id.toString, label = src.text)

    def toFileName(baseDir: Path, extension: String): Path = {
      val verseNumbers = src.verseNumbers
      val mainVerse = getPaddedFileName(verseNumbers.head)
      val subDir =
        if verseNumbers.size == 1 then mainVerse
        else s"$mainVerse-${getPaddedFileName(verseNumbers.last)}"
      baseDir + Seq(getPaddedFileName(src.chapterNumber), subDir) -> s"${src.id.toString}.$extension"
    }

    private def getPaddedFileName(n: Int): String = f"$n%03d"
  }

  def derivePhraseInfoText(phraseTypes: Seq[PhraseType], maybeNounStatus: Option[NounStatus]): String = {
    val phraseTypesWord =
      phraseTypes.foldLeft(ArabicWord()) { case (word, phraseType) =>
        if word.isEmpty then phraseType.word else word.concatenateWithAnd(phraseType.word)
      }

    maybeNounStatus
      .map(status => phraseTypesWord.concatWithSpace(ArabicLetters.InPlaceOf, status.shortLabel))
      .getOrElse(phraseTypesWord)
      .unicode
  }

  def derivePhraseText(locations: Seq[Location]): String =
    locations
      .groupBy(_.tokenNumber)
      .map { case (tokenNumber, seq) =>
        (tokenNumber, seq.map(_.text).mkString(""))
      }
      .toSeq
      .sortBy(_._1)
      .map(_._2)
      .mkString(" ")
}
