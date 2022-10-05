package com.alphasystem.arabic.model

trait SarfMemberType extends ArabicSupport {

  def termName: String

  val word: ArabicWord

  override lazy val label: String = word.unicode
}
