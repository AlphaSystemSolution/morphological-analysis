package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package internal

import arabic.model.ArabicLetterType

case class RootLetter(letter: ArabicLetterType, index: Int)

case class RootLetters(
  firstRadical: RootLetter,
  secondRadical: RootLetter,
  thirdRadical: RootLetter,
  fourthRadical: Option[RootLetter] = None)
