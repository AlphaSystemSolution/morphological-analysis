package com.alphasystem
package arabic
package model

trait ArabicSupport {

  def index = 0
  def label: String
}

object ArabicSupport {
  given ordering: Ordering[ArabicSupport] = (x: ArabicSupport, y: ArabicSupport) => x.index.compareTo(y.index)
}
