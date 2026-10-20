package com.alphasystem
package arabic
package examples_generator
package ui
package model

case class VerseSearchResult(
  chapterNumber: Int,
  chapterName: String,
  verseCount: Int,
  startVerseIndex: Int,
  endVerseIndex: Int,
  startTokenIndex: Int,
  endTokenIndex: Int,
  text: String)
