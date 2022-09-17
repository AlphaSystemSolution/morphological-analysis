package com.alphasystem.arabic.model

trait ArabicSupportEnum extends ArabicSupport {

  def code: String
  val name: String = toString
}
