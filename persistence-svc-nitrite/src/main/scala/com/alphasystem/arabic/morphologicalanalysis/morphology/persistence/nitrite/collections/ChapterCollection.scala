package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package nitrite
package collections

import morphology.model.Chapter
import org.dizitart.no2.collection.{ Document, FindOptions }
import org.dizitart.no2.common.SortOrder
import org.dizitart.no2.index.{ IndexOptions, IndexType }
import org.dizitart.no2.Nitrite
import org.dizitart.no2.filters.FluentFilter.*

import scala.jdk.CollectionConverters.*

class ChapterCollection private (db: Nitrite) {

  import ChapterCollection.*

  private[persistence] val collection = db.getCollection("chapter")
  if !collection.hasIndex(ChapterNumberField) then {
    collection.createIndex(IndexOptions.indexOptions(IndexType.UNIQUE), ChapterNumberField)
  }

  private[persistence] def createChapter(chapter: Chapter): Unit =
    findByChapterNumber(chapter.chapterNumber) match
      case Some(chapter) => throw EntityAlreadyExists(classOf[Chapter], chapter.chapterNumber.toString)
      case None          => collection.insert(chapter.toDocument)

  private[persistence] def findByChapterNumber(chapterNumber: Int): Option[Chapter] =
    collection.find(where(ChapterNumberField).eq(chapterNumber)).asScalaList.headOption.map(_.toChapter)

  private[persistence] def findAll: List[Chapter] =
    collection.find(FindOptions.orderBy(ChapterNumberField, SortOrder.Ascending)).asScalaList.map(_.toChapter)
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
