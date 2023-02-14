package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package internal

import arabic.model.ArabicLetterType
import morphologicalanalysis.morphology.model.{ ConversationType, GenderType }

case class RootLetter(letter: ArabicLetterType, index: Int)

case class RootLetters(
  firstRadical: RootLetter,
  secondRadical: RootLetter,
  thirdRadical: RootLetter,
  fourthRadical: Option[RootLetter] = None)

enum VerbGroupType(val gender: GenderType, val conversation: ConversationType) {

  case ThirdPersonMasculine extends VerbGroupType(GenderType.Masculine, ConversationType.ThirdPerson)
  case ThirdPersonFeminine extends VerbGroupType(GenderType.Feminine, ConversationType.ThirdPerson)
  case SecondPersonMasculine extends VerbGroupType(GenderType.Masculine, ConversationType.SecondPerson)
  case SecondPersonFeminine extends VerbGroupType(GenderType.Feminine, ConversationType.SecondPerson)
  case FirstPerson extends VerbGroupType(GenderType.Masculine, ConversationType.FirstPerson)
}
