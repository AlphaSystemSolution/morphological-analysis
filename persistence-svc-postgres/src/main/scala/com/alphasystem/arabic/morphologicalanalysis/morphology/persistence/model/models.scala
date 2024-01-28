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

case class Chapter(chapter_number: Int, chapter_name: String, verse_count: Int)

case class Verse(id: Long, chapterNumber: Int, verseNumber: Int, tokenCount: Int, verseText: String)

case class Token(
  id: Long,
  chapterNumber: Int,
  verseNumber: Int,
  tokenNumber: Int,
  verseId: Long,
  tokenText: String,
  derivedText: String,
  hidden: Boolean,
  translation: Option[String])

case class Location(
  id: Long,
  chapter_number: Int,
  verse_number: Int,
  token_number: Int,
  location_number: Int,
  token_id: Long,
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

case class Graph_Node(id: String, graphId: String, document: String) extends AbstractLifted
