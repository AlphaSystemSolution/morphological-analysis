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
  conjugationConfiguration: ConjugationConfiguration,
  outputFormat: OutputFormat,
  firstRadical: ArabicLetterType,
  secondRadical: ArabicLetterType,
  thirdRadical: ArabicLetterType,
  fourthRadical: Option[ArabicLetterType] = None,
  verbalNounCodes: Seq[String] = Seq.empty)

case class ChartConfiguration(
  pageOrientation: PageOrientation = PageOrientation.Portrait,
  sortDirection: SortDirection = SortDirection.Ascending,
  sortDirective: SortDirective = SortDirective.None,
  arabicFontFamily: String = "KFGQPC Uthman Taha Naskh",
  translationFontFamily: String = "Candara",
  arabicFontSize: Long = 14,
  translationFontSize: Long = 12,
  headingFontSize: Long = 18,
  showToc: Boolean = true,
  showTitle: Boolean = true,
  showHeader: Boolean = true,
  showMorphologicalTermCaptionInAbbreviatedConjugation: Boolean = true,
  showMorphologicalTermCaptionInDetailConjugation: Boolean = true)

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
  case Alphabatical extends SortDirective
}
