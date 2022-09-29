package com.alphasystem.arabic.model

trait ArabicSupportEnum extends ArabicSupport {

  def code: String
}

case class ArabicLabel[T](
  userData: T,
  override val code: String,
  override val label: ArabicWord)
    extends ArabicSupportEnum
