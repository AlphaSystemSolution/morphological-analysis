package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache

case class TokenRequest(chapterNumber: Int, verseNumber: Int)

case class LocationRequest(
  chapterNumber: Int,
  verseNumber: Int,
  tokenNumber: Int)

case class PropertiesRequest(
  chapterNumber: Int,
  verseNumber: Int,
  tokenNumber: Int,
  locationNumber: Int)
