package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package verb

import arabic.model.{ ArabicWord, HiddenPronounStatus, SarfMemberType }
import conjugation.model.internal.{ RootWord, VerbGroupType }
import morphologicalanalysis.morphology.model.MorphologyVerbType.*
import morphologicalanalysis.morphology.model.{ ConversationType, GenderType, MorphologyVerbType }

class ImperativeAndForbiddenTransformer private (
  genderType: GenderType,
  conversationType: ConversationType,
  verbType: MorphologyVerbType)
    extends AbstractVerbTransformer(genderType, conversationType) {

  require(
    conversationType == ConversationType.SecondPerson && (verbType == MorphologyVerbType.Imperative ||
      verbType == MorphologyVerbType.Forbidden),
    s"Invalid conversation or verb type."
  )

  override protected def deriveSingularWord(rootWord: RootWord): (SarfMemberType, ArabicWord) = {
    val word = rootWord.derivedWord
    (genderType, conversationType) match
      case (GenderType.Masculine, ConversationType.SecondPerson) =>
        (HiddenPronounStatus.SecondPersonMasculineSingular, word)

      case (GenderType.Feminine, ConversationType.SecondPerson) =>
        (HiddenPronounStatus.SecondPersonFeminineSingular, word)

      case _ => throw new RuntimeException("Invalid type")
  }

  override protected def deriveDualWord(rootWord: RootWord): Option[(SarfMemberType, ArabicWord)] = {
    val word = rootWord.derivedWord
    (genderType, conversationType) match
      case (GenderType.Masculine, ConversationType.SecondPerson) =>
        Some((HiddenPronounStatus.SecondPersonMasculineDual, word))

      case (GenderType.Feminine, ConversationType.SecondPerson) =>
        Some((HiddenPronounStatus.SecondPersonFeminineDual, word))

      case _ => throw new RuntimeException("Invalid type")
  }

  override protected def derivePluralWord(rootWord: RootWord): (SarfMemberType, ArabicWord) = {
    val word = rootWord.derivedWord
    (genderType, conversationType) match
      case (GenderType.Masculine, ConversationType.SecondPerson) =>
        (HiddenPronounStatus.SecondPersonMasculinePlural, word)

      case (GenderType.Feminine, ConversationType.SecondPerson) =>
        (HiddenPronounStatus.SecondPersonFemininePlural, word)

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
