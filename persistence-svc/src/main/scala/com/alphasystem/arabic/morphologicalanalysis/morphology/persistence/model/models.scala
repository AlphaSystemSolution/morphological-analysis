package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.model

import com.alphasystem.morphologicalanalysis.morphology.model.PartOfSpeechType

trait AbstractLifted {
  def id: String
}
case class PropertiesLifted(
  id: String,
  locationId: String,
  document: String)
    extends AbstractLifted

case class LocationLifted(
  id: String,
  tokenId: String,
  document: String)
    extends AbstractLifted

case class TokenLifted(
  id: String,
  verseId: String,
  document: String)
    extends AbstractLifted

case class VerseLifted(
  id: String,
  chapterId: String,
  document: String)
    extends AbstractLifted

case class ChapterLifted(
  id: String,
  document: String)
    extends AbstractLifted
