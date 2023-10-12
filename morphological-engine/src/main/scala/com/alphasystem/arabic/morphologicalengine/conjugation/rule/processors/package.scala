package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule

import com.alphasystem.arabic.model.{ ArabicLetterType, DiacriticType }
import com.alphasystem.arabic.morphologicalengine.conjugation.model.MorphologicalTermType
import com.alphasystem.arabic.morphologicalengine.conjugation.model.internal.RootWord

package object processors {

  val HeavyLetters: Seq[ArabicLetterType] =
    Seq(
      ArabicLetterType.Ha,
      ArabicLetterType.Kha,
      ArabicLetterType.Ain,
      ArabicLetterType.Ghain,
      ArabicLetterType.Hha,
      ArabicLetterType.Hamza
    )

  def validateTypes(
    rootWord: RootWord,
    validTerms: Seq[MorphologicalTermType] = Seq.empty,
    invalidTerms: Seq[MorphologicalTermType] = Seq.empty
  ): Boolean = {
    val termType = rootWord.`type`
    (validTerms.nonEmpty && validTerms.contains(termType)) || (invalidTerms.nonEmpty && !invalidTerms.contains(
      termType
    ))
  }

  def isFatha(maybeDiacriticType: Option[DiacriticType]): Boolean = maybeDiacriticType.contains(DiacriticType.Fatha)

  def isKasra(maybeDiacriticType: Option[DiacriticType]): Boolean = maybeDiacriticType.contains(DiacriticType.Kasra)
}
