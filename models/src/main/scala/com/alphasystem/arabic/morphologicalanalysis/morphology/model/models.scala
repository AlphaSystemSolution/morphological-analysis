package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicSupportEnum, ArabicWord }

import java.lang.Enum
enum Flexibility(override val code: String, override val word: ArabicWord)
    extends Enum[Flexibility]
    with ArabicSupportEnum {

  case FULLY_FLEXIBLE extends Flexibility("Fully Flexible", ArabicWord(MEEM, AIN, RA, BA))

  case PARTLY_FLEXIBLE extends Flexibility("Partially Flexible", ArabicWord(MEEM, BA, NOON, YA))

  case NON_FLEXIBLE
      extends Flexibility(
        "Non Flexible",
        ArabicWord(GHAIN, YA, RA, SPACE, MEEM, NOON, SAD, RA, FA)
      );
}

enum PageOrientation extends Enum[PageOrientation] {

  case PORTRAIT extends PageOrientation
  case LANDSCAPE extends PageOrientation
}

enum SortDirection extends Enum[SortDirection] {

  case ASCENDING extends SortDirection
  case DESCENDING extends SortDirection
}

enum SortDirective extends Enum[SortDirective] {

  case NONE extends SortDirective
  case TYPE extends SortDirective
  case ALPHABATICAL extends SortDirective
}
