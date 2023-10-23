package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package internal

import arabic.model.{ ArabicLetterType, RootType, VerbType, WeakVerbType }

class WordStatus(
  firstRadical: ArabicLetterType,
  secondRadical: ArabicLetterType,
  thirdRadical: ArabicLetterType) {

  private var _firstRadicalWaw = false
  private var _firstRadicalYa = false
  private var _firstRadicalHamza = false
  private var _secondRadicalWaw = false
  private var _secondRadicalYa = false
  private var _secondRadicalHamza = false
  private var _thirdRadicalWaw = false
  private var _thirdRadicalYa = false
  private var _thirdRadicalHamza = false
  private var _doubledLettered = false

  evaluateStatus()

  lazy val firstRadicalWaw: Boolean = _firstRadicalWaw
  lazy val firstRadicalYa: Boolean = _firstRadicalYa
  lazy val firstRadicalHamza: Boolean = _firstRadicalHamza
  lazy val secondRadicalWaw: Boolean = _secondRadicalWaw
  lazy val secondRadicalYa: Boolean = _secondRadicalYa
  lazy val secondRadicalHamza: Boolean = _secondRadicalHamza
  lazy val thirdRadicalWaw: Boolean = _thirdRadicalWaw
  lazy val thirdRadicalYa: Boolean = _thirdRadicalYa
  lazy val thirdRadicalHamza: Boolean = _thirdRadicalHamza
  lazy val doubledLettered: Boolean = _doubledLettered
  lazy val assimilated: Boolean = firstRadicalWaw || firstRadicalYa
  lazy val hollow: Boolean = secondRadicalWaw || secondRadicalYa
  lazy val defective: Boolean = thirdRadicalWaw || thirdRadicalYa
  lazy val weak: Boolean = assimilated || hollow || defective
  lazy val twoConsecutiveLettersWeak: Boolean = (hollow && defective) || (assimilated && hollow)
  lazy val twoSeparateLettersWeak: Boolean = assimilated && defective
  lazy val doublyWeak: Boolean = twoConsecutiveLettersWeak || twoSeparateLettersWeak
  lazy val hamzatted: Boolean = firstRadicalHamza || secondRadicalHamza || thirdRadicalHamza

  private def evaluateStatus(): Unit = {
    _firstRadicalWaw = ArabicLetterType.Waw == firstRadical
    _firstRadicalYa = ArabicLetterType.Ya == firstRadical
    _firstRadicalHamza = ArabicLetterType.Hamza == firstRadical

    _secondRadicalWaw = ArabicLetterType.Waw == secondRadical
    _secondRadicalYa = ArabicLetterType.Ya == secondRadical
    _secondRadicalHamza = ArabicLetterType.Hamza == secondRadical

    _thirdRadicalWaw = ArabicLetterType.Waw == thirdRadical
    _thirdRadicalYa = ArabicLetterType.Ya == thirdRadical
    _thirdRadicalHamza = ArabicLetterType.Hamza == thirdRadical

    _doubledLettered = secondRadical == thirdRadical
  }

  def toChartMode(namedTemplate: NamedTemplate): ChartMode = {
    val rootType = if weak then RootType.Weak else RootType.Consonant
    var verbType = VerbType.Consonant
    var weakVerbType: Option[WeakVerbType] = None

    if weak then {
      if twoSeparateLettersWeak then verbType = VerbType.TwoSeparateRadicalsWeak
      else if twoConsecutiveLettersWeak then verbType = VerbType.TwoConsecutiveRadicalsWeak
      else if assimilated then {
        verbType = VerbType.FirstRadicalWeak
        weakVerbType =
          if firstRadicalWaw then Some(WeakVerbType.FirstRadicalWeakWaw) else Some(WeakVerbType.FirstRadicalWeakYa)
      } else if hollow then {
        verbType = VerbType.SecondRadicalWeak
        weakVerbType =
          if secondRadicalWaw then Some(WeakVerbType.SecondRadicalWeakWaw) else Some(WeakVerbType.SecondRadicalWeakYa)
      } else if defective then {
        verbType = VerbType.ThirdRadicalWeak
        weakVerbType =
          if secondRadicalWaw then Some(WeakVerbType.ThirdRadicalWeakWaw) else Some(WeakVerbType.ThirdRadicalWeakWaw)
      }
    } else if doubledLettered then verbType = VerbType.DoubleLettered
    else if hamzatted then {
      if firstRadicalHamza then verbType = VerbType.FirstRadicalHamza
      else if secondRadicalHamza then verbType = VerbType.SecondRadicalHamza
      else if thirdRadicalHamza then verbType = VerbType.ThirdRadicalHamza
    }

    ChartMode(
      template = namedTemplate,
      rootType = rootType,
      verbType = verbType,
      weakVerbType = weakVerbType
    )
  }
}

object WordStatus {
  def apply(
    firstRadical: ArabicLetterType,
    secondRadical: ArabicLetterType,
    thirdRadical: ArabicLetterType
  ): WordStatus = new WordStatus(firstRadical, secondRadical, thirdRadical)

  def apply(processingContext: ProcessingContext): WordStatus =
    WordStatus(processingContext.firstRadical, processingContext.secondRadical, processingContext.thirdRadical)
}
