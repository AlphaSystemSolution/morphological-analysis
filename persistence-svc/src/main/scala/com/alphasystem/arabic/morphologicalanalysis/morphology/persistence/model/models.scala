package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package model

import morphology.model.{ LocationId, TokenId, VerseId }

trait AbstractLifted {
  def id: String
  def document: String
}

trait Lifted[ID]

case class Chapter(chapter_number: Int, chapter_name: String, verse_count: Int) extends Lifted[Int]

case class Verse(chapter_number: Int, verse_number: Int, verse_text: String, translation: Option[String])
    extends Lifted[VerseId]

case class Token(
  chapter_number: Int,
  verse_number: Int,
  token_number: Int,
  token: String,
  derived_text: String,
  translation: Option[String])
    extends Lifted[TokenId]

case class Location(
  chapter_number: Int,
  verse_number: Int,
  token_number: Int,
  location_number: Int,
  hidden: Boolean,
  start_index: Int,
  end_index: Int,
  derived_text: String,
  location_text: String,
  alternate_text: String,
  word_type: String,
  properties: String,
  translation: Option[String],
  named_tag: Option[String])
    extends Lifted[LocationId]

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
