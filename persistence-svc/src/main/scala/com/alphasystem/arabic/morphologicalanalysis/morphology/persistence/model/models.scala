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

case class Dependency_Graph(
  id: String,
  chapter_number: Int,
  chapter_name: String,
  graph_text: String,
  document: String,
  verses: Seq[Int])
    extends AbstractLifted

case class Dependency_Graph_Verse_Tokens_Rln(
  graph_id: String,
  chapter_number: Int,
  verse_number: Int,
  tokens: Seq[Int])

case class GraphNodeLifted(id: String, graphId: String, document: String) extends AbstractLifted
