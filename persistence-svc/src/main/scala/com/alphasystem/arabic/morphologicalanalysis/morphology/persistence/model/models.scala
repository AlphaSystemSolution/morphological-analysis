package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package model

trait AbstractLifted {
  def id: String
  def document: String
}

case class LocationLifted(id: String, tokenId: String, document: String) extends AbstractLifted

case class TokenLifted(id: String, verseId: String, document: String) extends AbstractLifted

case class VerseLifted(id: String, chapterId: String, document: String) extends AbstractLifted

case class ChapterLifted(id: String, document: String) extends AbstractLifted

case class DependencyGraphLifted(
  id: String,
  chapterNumber: Int,
  verseNumber: Int,
  startTokenNumber: Int,
  endTokenNumber: Int,
  graphText: String,
  document: String)
    extends AbstractLifted

case class GraphNodeLifted(id: String, graphId: String, graphNodeType: String, document: String) extends AbstractLifted
