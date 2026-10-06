package com.alphasystem
package arabic
package examples_generator

import examples_generator.ui.utils.ExampleGeneratorPreferences
import arabic.model.{ ArabicLabel, ArabicLetters, ArabicWord }
import arabic.utils.ChapterInfo

package object ui {

  given preferences: ExampleGeneratorPreferences = ExampleGeneratorPreferences()

  extension (src: ChapterInfo) {
    def toArabicLabel: ArabicLabel[ChapterInfo] = {
      val label =
        ArabicWord(src.chapterName).concatWithSpace(ArabicLetters.NumberWordWithParenthesis(src.chapterNumber))
      ArabicLabel[ChapterInfo](src, src.chapterNumber.toString, label.unicode)
    }
  }
}
