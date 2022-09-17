package com.alphasystem.arabic

package object model {

  def toHtmlCodeString(unicode: Char): String = f"&#${unicode.toInt}%04d"
}
