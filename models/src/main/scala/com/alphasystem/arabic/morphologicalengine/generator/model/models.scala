package com.alphasystem
package arabic
package morphologicalengine
package generator
package model

import morphologicalengine.conjugation.model.ConjugationInput

import java.lang.Enum

case class ChartConfiguration(
  pageOrientation: PageOrientation = PageOrientation.Portrait,
  sortDirection: SortDirection = SortDirection.Ascending,
  sortDirective: SortDirective = SortDirective.None,
  format: DocumentFormat = DocumentFormat.Classic,
  arabicFontFamily: String = "KFGQPC Uthman Taha Naskh",
  translationFontFamily: String = "Candara",
  arabicFontSize: Long = 12,
  translationFontSize: Long = 10,
  headingFontSize: Long = 16,
  showToc: Boolean = true,
  showTitle: Boolean = true,
  showLabels: Boolean = true,
  removeAdverbs: Boolean = false,
  showAbbreviatedConjugation: Boolean = true,
  showDetailedConjugation: Boolean = true,
  showMorphologicalTermCaptionInAbbreviatedConjugation: Boolean = true,
  showMorphologicalTermCaptionInDetailConjugation: Boolean = true) {
  require(Seq(showAbbreviatedConjugation, showDetailedConjugation).count(_ == false) != 2)
}

case class ConjugationTemplate(
  id: String,
  chartConfiguration: ChartConfiguration,
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
