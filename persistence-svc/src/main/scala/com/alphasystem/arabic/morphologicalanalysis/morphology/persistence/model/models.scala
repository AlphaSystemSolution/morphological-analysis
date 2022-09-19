package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.model

import com.alphasystem.morphologicalanalysis.morphology.model.PartOfSpeechType

case class PropertiesLifted(
  id: String,
  locationId: String,
  document: String)

case class LocationLifted(
  id: String,
  tokenId: String,
  document: String)

case class TokenLifted(
  id: String,
  verseId: String,
  document: String)

case class VerseLifted(
  id: String,
  chapterId: String,
  document: String)

case class ChapterLifted(
  id: String,
  document: String)
