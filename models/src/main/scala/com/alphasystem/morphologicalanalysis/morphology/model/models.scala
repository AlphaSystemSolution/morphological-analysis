package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.{ ArabicSupportEnum, ArabicWord }

enum Flexibility(override val code: String, override val label: ArabicWord)
    extends ArabicSupportEnum {

  case FULLY_FLEXIBLE
      extends Flexibility("Fully Flexible", ArabicWord(MEEM, AIN, RA, BA))

  case PARTLY_FLEXIBLE
      extends Flexibility("Partially Flexible", ArabicWord(MEEM, BA, NOON, YA))

  case NON_FLEXIBLE
      extends Flexibility(
        "Non Flexible",
        ArabicWord(GHAIN, YA, RA, SPACE, MEEM, NOON, SAD, RA, FA)
      );
}

enum PageOrientation {

  case PORTRAIT extends PageOrientation
  case LANDSCAPE extends PageOrientation
}

enum SortDirection {

  case ASCENDING extends SortDirection
  case DESCENDING extends SortDirection
}

enum SortDirective {

  case NONE extends SortDirective
  case TYPE extends SortDirective
  case ALPHABATICAL extends SortDirective
}
