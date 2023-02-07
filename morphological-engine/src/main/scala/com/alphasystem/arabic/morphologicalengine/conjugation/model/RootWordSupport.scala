package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model

import arabic.model.{ ArabicSupportEnum, ArabicWord }
import morphologicalanalysis.morphology.model.Flexibility

trait RootWordSupport extends ArabicSupportEnum {

  val rootWord: RootWord

  override lazy val word: ArabicWord = rootWord.derivedWord
}

trait VerbSupport extends RootWordSupport

trait NounSupport extends RootWordSupport {

  val feminine: Boolean
  val flexibility: Flexibility
}
