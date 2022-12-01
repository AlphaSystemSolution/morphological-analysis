package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package nitrite
package collections

import morphology.model.Verse
import org.dizitart.no2.filters.Filters
import org.dizitart.no2.{ Document, FindOptions, IndexOptions, IndexType, Nitrite, SortOrder }

class VerseCollection private (db: Nitrite) {

  import VerseCollection.*

  private[persistence] val collection = db.getCollection("verse")
  if !collection.hasIndex(ChapterNumberField) then {
    collection.createIndex(ChapterNumberField, IndexOptions.indexOptions(IndexType.NonUnique))
  }

  private[persistence] def createVerses(verses: Seq[Verse]): Unit =
    collection.insert(verses.map(_.toDocument).toArray)

  private[persistence] def findById(id: Long): Option[Verse] =
    collection.find(Filters.eq(VerseIdField, id)).asScalaList.headOption.map(_.toVerse)

  private[persistence] def findByChapterNumber(chapterNumber: Int) =
    collection
      .find(Filters.eq(ChapterNumberField, chapterNumber), FindOptions.sort(VerseNumberField, SortOrder.Ascending))
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
