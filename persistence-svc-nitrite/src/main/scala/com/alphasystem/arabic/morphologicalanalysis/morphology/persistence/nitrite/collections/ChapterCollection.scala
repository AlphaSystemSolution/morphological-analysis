package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package nitrite
package collections

import morphology.model.Chapter
import org.dizitart.no2.filters.Filters
import org.dizitart.no2.{ Document, FindOptions, IndexOptions, IndexType, Nitrite, SortOrder }

import scala.jdk.CollectionConverters.*

class ChapterCollection private (db: Nitrite) {

  import ChapterCollection.*

  private[persistence] val collection = db.getCollection("chapter")
  if !collection.hasIndex(ChapterNumberField) then {
    collection.createIndex(ChapterNumberField, IndexOptions.indexOptions(IndexType.Unique))
  }

  private[persistence] def createChapter(chapter: Chapter): Unit =
    findByChapterNumber(chapter.chapterNumber) match
      case Some(chapter) => throw ChapterAlreadyExists(chapter.chapterNumber)
      case None          => collection.insert(chapter.toDocument)

  private[persistence] def findByChapterNumber(chapterNumber: Int): Option[Chapter] =
    collection.find(Filters.eq(ChapterNumberField, chapterNumber)).asScalaList.headOption.map(_.toChapter)

  private[persistence] def findAll: List[Chapter] =
    collection.find(FindOptions.sort(ChapterNumberField, SortOrder.Ascending)).asScalaList.map(_.toChapter)
}

object ChapterCollection {

  extension (src: Chapter) {
    private def toDocument: Document =
      Document
        .createDocument(ChapterNumberField, src.chapterNumber)
        .put(ChapterNameField, src.chapterName)
        .put(VerseCountField, src.verseCount)
  }

  extension (src: Document) {
    private def toChapter: Chapter =
      Chapter(
        chapterName = src.getString(ChapterNameField),
        chapterNumber = src.getInt(ChapterNumberField),
        verseCount = src.getInt(VerseCountField)
      )
  }

  private[persistence] def apply(db: Nitrite): ChapterCollection = new ChapterCollection(db)
}
