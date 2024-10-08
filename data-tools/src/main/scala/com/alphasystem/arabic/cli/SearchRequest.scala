package com.alphasystem
package arabic
package cli

case class SearchRequest(chapterNumber: Int, verses: Seq[VerseSearch]) {
  validate()

  private def validate(): Unit = {
    val verseNumbers = verses.map(_.verseNumber).sorted
    val min = verseNumbers.headOption.getOrElse(-1)
    val max = verseNumbers.lastOption.getOrElse(-1)
    require(chapterNumber >= 1 && min >= 1 && max >= 1 && min <= max && verseNumbers.sum == (min to max).sum)
  }
}

case class VerseSearch(verseNumber: Int, tokenRange: Option[TokenRange] = None)

case class TokenRange(minToken: Int, maxToken: Int, highLightColor: Option[String] = None) {
  require(
    minToken >= 1 && maxToken >= -1 && (if maxToken > 0 then minToken <= maxToken else true),
    s"$minToken, $maxToken"
  )
}
