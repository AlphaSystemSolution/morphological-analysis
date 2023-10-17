package com.alphasystem
package arabic
package model

trait SarfMemberType extends ArabicSupport {

  def termName: String

  val word: ArabicWord

  override lazy val label: String = word.unicode
}

object SarfMemberType {

  val DualsTypes: Seq[HiddenPronounStatus] = Seq(
    HiddenPronounStatus.ThirdPersonMasculineDual,
    HiddenPronounStatus.ThirdPersonFeminineDual,
    HiddenPronounStatus.SecondPersonMasculineDual,
    HiddenPronounStatus.SecondPersonFeminineDual
  )
}
