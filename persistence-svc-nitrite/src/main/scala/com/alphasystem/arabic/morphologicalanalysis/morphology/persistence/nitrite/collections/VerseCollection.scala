package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package nitrite
package collections

import morphology.model.Verse
import org.dizitart.no2.collection.{ Document, DocumentCursor, FindOptions }
import org.dizitart.no2.common.SortOrder
import org.dizitart.no2.index.{ IndexOptions, IndexType }
import org.dizitart.no2.Nitrite
import org.dizitart.no2.filters.FluentFilter.*

class VerseCollection private (db: Nitrite) {

  import VerseCollection.*

  private[persistence] val collection = db.getCollection("verse")
  if !collection.hasIndex(ChapterNumberField) then {
    collection.createIndex(IndexOptions.indexOptions(IndexType.NON_UNIQUE), ChapterNumberField)
  }

  private[persistence] def createVerses(verses: Seq[Verse]): Unit =
    collection.insert(verses.map(_.toDocument).toArray)

  private[persistence] def findById(id: Long): Option[Verse] = {
    val cursor: DocumentCursor = collection.find(where(VerseIdField).eq(id))
    cursor.asScalaList.headOption.map(_.toVerse)
  }

  private[persistence] def findByChapterNumber(chapterNumber: Int) =
    collection
      .find(where(ChapterNumberField).eq(chapterNumber), FindOptions.orderBy(VerseNumberField, SortOrder.Ascending))
      .asScalaList
      .map(_.toVerse)
}

object VerseCollection {

  extension (src: Verse) {
    private def toDocument: Document =
      Document
        .createDocument(VerseIdField, src.id)
        .put(ChapterNumberField, src.chapterNumber)
        .put(VerseNumberField, src.verseNumber)
        .put(TextField, src.text)
        .put(TranslationField, src.translation.orNull)
  }

  extension (src: Document) {
    private def toVerse: Verse =
      Verse(
        chapterNumber = src.getInt(ChapterNumberField),
        verseNumber = src.getInt(VerseNumberField),
        text = src.getString(TextField),
        tokenCount = 0,
        translation = src.getOptionalString(TranslationField)
      )
  }

  private[persistence] def apply(db: Nitrite): VerseCollection = new VerseCollection(db)
}
