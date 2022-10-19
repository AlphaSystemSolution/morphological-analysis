package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package model

import morphology.model.PartOfSpeechType

trait AbstractLifted {
  def id: String
  def document: String
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
