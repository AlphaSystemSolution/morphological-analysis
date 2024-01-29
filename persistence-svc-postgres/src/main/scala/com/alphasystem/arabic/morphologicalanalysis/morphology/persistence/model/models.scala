package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package model

import morphology.model.{ NamedTag, WordProperties, WordType }

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
  chapterNumber: Int,
  verseNumber: Int,
  tokenNumber: Int,
  locationNumber: Int,
  tokenId: Long,
  hidden: Boolean,
  startIndex: Int,
  endIndex: Int,
  derivedText: String,
  locationText: String,
  alternateText: String,
  wordType: WordType,
  properties: WordProperties,
  translation: Option[String],
  namedTag: Option[NamedTag])

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
