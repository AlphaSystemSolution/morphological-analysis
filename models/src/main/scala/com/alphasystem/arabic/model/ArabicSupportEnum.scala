package com.alphasystem.arabic.model

trait ArabicSupportEnum extends ArabicSupport {

  def word: ArabicWord
  def code: String

  override def label: String = word.unicode
}

case class ArabicLabel[T](
  userData: T,
  override val code: String,
  override val label: String)
    extends ArabicSupportEnum {

  override def word: ArabicWord = ArabicWord()
}
