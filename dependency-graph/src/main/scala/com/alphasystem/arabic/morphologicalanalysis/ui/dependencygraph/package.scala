package com.alphasystem
package arabic
package morphologicalanalysis
package ui

import arabic.model.{ ArabicLabel, ArabicLetter, ArabicLetterType, ArabicLetters, ArabicWord }
import morphologicalanalysis.morphology.model.{
  AbstractNounProperties,
  AbstractProperties,
  BaseProperties,
  Linkable,
  Location,
  NounPartOfSpeechType,
  NounProperties,
  NounStatus,
  ParticlePartOfSpeechType,
  ParticleProperties,
  PhraseType,
  ProNounPartOfSpeechType,
  ProNounProperties,
  RelationshipType,
  VerbPartOfSpeechType,
  VerbProperties
}
import morphologicalanalysis.morphology.utils.*
import morphology.graph.model.{ DependencyGraph, PhraseInfo }
import ui.dependencygraph.utils.DependencyGraphPreferences

import java.nio.file.Path

package object dependencygraph {

  implicit val dependencyGraphPreferences: DependencyGraphPreferences = DependencyGraphPreferences()

  extension (src: DependencyGraph) {
    def toArabicLabel: ArabicLabel[DependencyGraph] =
      ArabicLabel(userData = src, code = src.id.toString, label = src.text)

    def toFileName(baseDir: Path, extension: String): Path = {
      val tokens = src.tokens
      val fileName = s"${tokens.head.tokenNumber}-${tokens.last.tokenNumber}"
      val verseNumbers = src.verseNumbers
      val mainVerse = getPaddedFileName(verseNumbers.head)
      val subDir =
        if verseNumbers.size == 1 then mainVerse
        else s"$mainVerse-${getPaddedFileName(verseNumbers.last)}"
      baseDir + Seq(getPaddedFileName(src.chapterNumber), subDir) -> s"$fileName.$extension"
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

  def deriveRelationshipInfoText(relationshipType: RelationshipType, owner: Linkable): String =
    if Option(owner).isDefined then
      owner match
        case location: Location =>
          location.properties match
            case p: VerbProperties if p.incompleteVerb.isDefined =>
              val word = ArabicWord(ArabicLetterType.OrnateLeftParenthesis)
                .concat(p.incompleteVerb.get.word, ArabicWord(ArabicLetterType.OrnateRightParenthesis))
              relationshipType.word.concatWithSpace(word).unicode

            case p: ParticleProperties if p.partOfSpeech == ParticlePartOfSpeechType.AccusativeParticle =>
              val word = ArabicWord(ArabicLetterType.OrnateLeftParenthesis)
                .concat(ArabicWord(location.text), ArabicWord(ArabicLetterType.OrnateRightParenthesis))
              relationshipType.word.concatWithSpace(word).unicode

            case _ => relationshipType.label

        case _: PhraseInfo => relationshipType.label
    else relationshipType.label

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
