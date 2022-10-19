package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package cache

case class TokenRequest(chapterNumber: Int, verseNumber: Int)

case class LocationRequest(
  chapterNumber: Int,
  verseNumber: Int,
  tokenNumber: Int)
