package com.alphasystem
package arabic
package examples_generator
package ui
package model

case class VerseSearchResult(
  chapterNumber: Int,
  chapterName: String,
  startVerseNumber: Int,
  endVerseNumber: Int,
  startTokenIndex: Int,
  endTokenIndex: Int,
  text: String,
  appendChapterReference: Boolean)
