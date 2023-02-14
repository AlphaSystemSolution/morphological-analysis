package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package verb

import arabic.model.{ ArabicLetterType, ArabicLetters, ArabicWord, DiacriticType }
import conjugation.model.internal.{ RootWord, VerbGroupType }
import morphologicalanalysis.morphology.model.MorphologyVerbType.*
import morphologicalanalysis.morphology.model.{ ConversationType, GenderType, MorphologyVerbType }

class ImperativeAndForbiddenTransformer private (
  genderType: GenderType,
  conversationType: ConversationType,
  verbType: MorphologyVerbType)
    extends AbstractVerbTransformer(genderType, conversationType) {

  require(
    conversationType == ConversationType.SecondPerson && (verbType == MorphologyVerbType.Command || verbType == MorphologyVerbType.Forbidden),
    s"Invalid conversation or verb type."
  )

  override protected def deriveSingularWord(rootWord: RootWord): ArabicWord = {
    val word = rootWord.derivedWord
    (genderType, conversationType) match
      case (GenderType.Masculine, ConversationType.SecondPerson) => word
      case (GenderType.Feminine, ConversationType.SecondPerson) =>
        word.replaceDiacriticsAndAppend(rootWord.lastLetterIndex, Seq(DiacriticType.Kasra), ArabicLetters.LetterYa)
      case _ => throw new RuntimeException("Invalid type")
  }

  override protected def deriveDualWord(rootWord: RootWord): Option[ArabicWord] = {
    val word = rootWord.derivedWord
    (genderType, conversationType) match
      case (GenderType.Masculine, ConversationType.SecondPerson) =>
        Some(
          word.replaceDiacriticsAndAppend(rootWord.lastLetterIndex, Seq(DiacriticType.Fatha), ArabicLetters.LetterAlif)
        )
      case (GenderType.Feminine, ConversationType.SecondPerson) =>
        Some(
          word
            .removeLastLetter()
            .replaceDiacriticsAndAppend(rootWord.thirdRadicalIndex, Seq(DiacriticType.Fatha), ArabicLetters.LetterAlif)
        )
      case _ => throw new RuntimeException("Invalid type")
  }

  override protected def derivePluralWord(rootWord: RootWord): ArabicWord = {
    val word = rootWord.derivedWord
    (genderType, conversationType) match
      case (GenderType.Masculine, ConversationType.SecondPerson) =>
        word.replaceDiacriticsAndAppend(
          rootWord.lastLetterIndex,
          Seq(DiacriticType.Damma),
          ArabicLetters.WawWithSukun,
          ArabicLetters.LetterAlif
        )
      case (GenderType.Feminine, ConversationType.SecondPerson) =>
        word
          .removeLastLetter()
          .replaceDiacriticsAndAppend(rootWord.thirdRadicalIndex, Seq(DiacriticType.Sukun), ArabicLetters.NoonWithFatha)
      case _ => throw new RuntimeException("Invalid type")
  }
}

object ImperativeAndForbiddenTransformer {
  private def apply(
    genderType: GenderType,
    conversationType: ConversationType,
    verbType: MorphologyVerbType
  ): Transformer = new ImperativeAndForbiddenTransformer(genderType, conversationType, verbType)

  def apply(
    verbGroupType: VerbGroupType,
    verbType: MorphologyVerbType
  ): Transformer = ImperativeAndForbiddenTransformer(verbGroupType.gender, verbGroupType.conversation, verbType)
}
