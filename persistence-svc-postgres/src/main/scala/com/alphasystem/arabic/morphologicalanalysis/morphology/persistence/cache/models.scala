package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package cache

import morphology.model.*

case class TokenRequest(chapterNumber: Int, verseNumber: Int)

case class LocationRequest(
  chapterNumber: Int,
  verseNumber: Int,
  tokenNumber: Int) {

  val toTokenId: Long = tokenNumber.toTokenId(chapterNumber, verseNumber)
}

case class GetDependencyGraphRequest(chapterNumber: Int, verseNumber: Int)
