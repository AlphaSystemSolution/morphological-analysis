package com.alphasystem
package arabic
package morphologicalengine
package generator
package model

import arabic.model.ArabicLetterType
import morphologicalengine.conjugation.model.{ ConjugationConfiguration, NamedTemplate, OutputFormat }

import java.lang.Enum

case class ConjugationInput(
  namedTemplate: NamedTemplate,
  firstRadical: ArabicLetterType,
  secondRadical: ArabicLetterType,
  thirdRadical: ArabicLetterType,
  fourthRadical: Option[ArabicLetterType] = None,
  translation: Option[String] = None,
  verbalNounCodes: Seq[String] = Seq.empty) {

  // provided for sorting by Alphabetically
  val rootLetters: (ArabicLetterType, ArabicLetterType, ArabicLetterType, Option[ArabicLetterType]) =
    (firstRadical, secondRadical, thirdRadical, fourthRadical)
}

case class ChartConfiguration(
  pageOrientation: PageOrientation = PageOrientation.Portrait,
  sortDirection: SortDirection = SortDirection.Ascending,
  format: DocumentFormat = DocumentFormat.Classic,
  arabicFontFamily: String = "KFGQPC Uthman Taha Naskh",
  translationFontFamily: String = "Candara",
  arabicFontSize: Long = 12,
  translationFontSize: Long = 10,
  headingFontSize: Long = 16,
  showToc: Boolean = true,
  showTitle: Boolean = true,
  showLabels: Boolean = true,
  showAbbreviatedConjugation: Boolean = true,
  showDetailedConjugation: Boolean = true,
  showMorphologicalTermCaptionInAbbreviatedConjugation: Boolean = true,
  showMorphologicalTermCaptionInDetailConjugation: Boolean = true) {
  require(Seq(showAbbreviatedConjugation, showDetailedConjugation).count(_ == false) != 2)
}

case class ConjugationTemplate(
  chartConfiguration: ChartConfiguration,
  conjugationConfiguration: ConjugationConfiguration,
  inputs: Seq[ConjugationInput])

enum PageOrientation extends Enum[PageOrientation] {

  case Portrait extends PageOrientation
  case Landscape extends PageOrientation
}

enum SortDirection extends Enum[SortDirection] {

  case Ascending extends SortDirection
  case Descending extends SortDirection
}

enum SortDirective extends Enum[SortDirective] {

  case None extends SortDirective
  case Type extends SortDirective
  case Alphabetical extends SortDirective
}

enum DocumentFormat extends Enum[DocumentFormat] {

  case Classic extends DocumentFormat
  case AbbreviateConjugationSingleRow extends DocumentFormat
}
