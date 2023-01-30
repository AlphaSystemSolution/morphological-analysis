package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model

import arabic.model.{ ArabicLetterType, ArabicSupportEnum, ArabicWord, NamedTemplate, RootType, VerbType, WeakVerbType }

import java.lang.Enum

case class RootLetter(letter: ArabicLetterType, index: Int)

case class RootLetters(
  firstRadical: RootLetter,
  secondRadical: RootLetter,
  thirdRadical: RootLetter,
  fourthRadical: Option[RootLetter] = None)

trait RootWordSupport extends ArabicSupportEnum {

  val rootWord: RootWord

  override lazy val word: ArabicWord = rootWord.derivedWord
}

trait VerbSupport extends RootWordSupport

trait NounSupport extends RootWordSupport {

  val feminine: Boolean
  val flexibility: Flexibility
}

enum Flexibility(override val word: ArabicWord) extends Enum[Flexibility] with ArabicSupportEnum {

  case FullyFlexible
      extends Flexibility(
        ArabicWord(ArabicLetterType.Meem, ArabicLetterType.Ain, ArabicLetterType.Ra, ArabicLetterType.Ba)
      )

  case PartlyFlexible
      extends Flexibility(
        ArabicWord(ArabicLetterType.Meem, ArabicLetterType.Ba, ArabicLetterType.Noon, ArabicLetterType.Ya)
      )

  case NonFlexible
      extends Flexibility(
        ArabicWord(
          ArabicLetterType.Ghain,
          ArabicLetterType.Ya,
          ArabicLetterType.Ra,
          ArabicLetterType.Space,
          ArabicLetterType.Meem,
          ArabicLetterType.Noon,
          ArabicLetterType.Sad,
          ArabicLetterType.Ra,
          ArabicLetterType.Fa
        )
      )
  override def code: String = name()
}

case class ChartMode(
  template: NamedTemplate,
  rootType: RootType,
  verbType: VerbType,
  weakVerbType: WeakVerbType)

enum OutputFormat extends Enum[OutputFormat] {

  case Unicode extends OutputFormat
  case Html extends OutputFormat
  case BuckWalter extends OutputFormat
  case Stream extends OutputFormat
}
