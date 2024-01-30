package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository

import morphology.model.Verse

import scala.concurrent.Future

trait VerseRepository {

  def addVerses(verse: Seq[Verse]): Future[Done]
  def addOrUpdateVerse(verse: Verse): Future[Done]
  def getById(id: Long): Future[Option[Verse]]
  def getByChapterAndVerseNumber(chapterNumber: Int, verseNumber: Int): Future[Option[Verse]]
  def getByChapterNumber(chapterNumber: Int): Future[Seq[Verse]]
}
