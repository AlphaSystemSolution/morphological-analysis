package com.alphasystem.arabic.model

case class ArabicLetter(letter: ArabicLetterType, diacritics: DiacriticType*) extends ArabicSupport {

  private val (codeStr, unicodeStr, htmlStr) =
    diacritics.foldLeft(("", "", "")) { case ((s1, s2, s3), d) =>
      (s1 + d.code, s2 + d.unicode, s3 + d.htmlCode)
    }

  val code: String = letter.code.toString + codeStr
  val unicode: String = letter.unicode.toString + unicodeStr
  val htmlCode: String = letter.htmlCode + htmlStr

  override val label: String = ArabicWord(this).unicode
}
