package com.alphasystem
package arabic
package morphologicalengine
package generator
package model

import java.lang.Enum

case class ChartConfiguration(
  pageOrientation: PageOrientation,
  sortDirection: SortDirection,
  sortDirective: SortDirective,
  arabicFontFamily: String,
  translationFontFamily: String,
  arabicFontSize: Long,
  translationFontSize: String,
  headingFontSize: String,
  omitToc: Boolean = false,
  omitTitle: Boolean = false,
  omitHeader: Boolean = false,
  omitMorphologicalTermCaption: Boolean)

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
