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

  case FullyFlexible extends Flexibility("Fully Flexible", ArabicWord(Meem, Ain, Ra, Ba))

  case PartlyFlexible extends Flexibility("Partially Flexible", ArabicWord(Meem, Ba, Noon, Ya))

  case NonFlexible
      extends Flexibility(
        "Non Flexible",
        ArabicWord(Ghain, Ya, Ra, Space, Meem, Noon, Sad, Ra, Fa)
      );
}

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
