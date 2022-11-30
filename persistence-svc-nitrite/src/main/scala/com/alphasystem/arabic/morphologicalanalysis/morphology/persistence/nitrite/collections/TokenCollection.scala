package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package nitrite
package collections

import morphology.model.{ Location, NamedTag, Token, WordProperties, WordType }
import org.dizitart.no2.filters.Filters
import org.dizitart.no2.{ Document, FindOptions, IndexOptions, IndexType, Nitrite, SortOrder }
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import scala.jdk.CollectionConverters.*

class TokenCollection private (db: Nitrite) {

  import TokenCollection.*

  private[persistence] val collection = db.getCollection("token")
  if !collection.hasIndex(VerseIdField) then {
    collection.createIndex(VerseIdField, IndexOptions.indexOptions(IndexType.NonUnique))
  }

  private[persistence] def createTokens(tokens: Seq[Token]): Unit =
    collection.insert(tokens.map(_.toDocument).toArray)

  private[persistence] def findByVerseId(verseId: Long): Seq[Token] =
    collection
      .find(Filters.eq(VerseIdField, verseId), FindOptions.sort(TokenNumberField, SortOrder.Ascending))
      .asScalaList
      .map(_.toToken)

  private[persistence] def findById(tokenId: Long): Option[Token] = findByTokenIdInternal(tokenId).map(_.toToken)

  private def findByTokenIdInternal(tokenId: Long): Option[Document] =
    collection.find(Filters.eq(TokenIdField, tokenId)).asScalaList.headOption

  private[persistence] def update(token: Token): Unit = {
    findByTokenIdInternal(token.id) match
      case Some(document) =>
        val updatedDocument = document
          .put(TranslationField, token.translation.orNull)
          .put(LocationsField, token.locations.map(_.toDocument).asJava)
        collection.update(updatedDocument)
      case None => throw EntityNotFound(classOf[Token].getSimpleName, token.id.toString)
  }

  private[persistence] def deleteByVerseId(verseId: Long): Int =
    collection.remove(Filters.eq(VerseIdField, verseId)).getAffectedCount
}

object TokenCollection {

  extension (src: Location) {
    private def toDocument: Document =
      Document
        .createDocument(TokenIdField, src.id)
        .put(ChapterNumberField, src.chapterNumber)
        .put(VerseNumberField, src.verseNumber)
        .put(TokenNumberField, src.tokenNumber)
        .put(LocationNumberField, src.locationNumber)
        .put(TokenIdField, src.tokenId)
        .put(HiddenField, src.hidden)
        .put(StartIndexField, src.startIndex)
        .put(EndIndexField, src.endIndex)
        .put(DerivedTextField, src.derivedText)
        .put(TextField, src.text)
        .put(AlternateTextField, src.alternateText)
        .put(WordTypeField, src.wordType.name())
        .put(LocationPropertiesField, src.properties.asJson.noSpaces)
        .put(TranslationField, src.translation.orNull)
        .put(NamedTagField, src.namedTag.map(_.name()).orNull)
  }

  extension (src: Token) {
    private def toDocument: Document =
      Document
        .createDocument(TokenIdField, src.id)
        .put(ChapterNumberField, src.chapterNumber)
        .put(VerseNumberField, src.verseNumber)
        .put(TokenNumberField, src.tokenNumber)
        .put(TextField, src.token)
        .put(HiddenField, src.hidden)
        .put(LocationsField, src.locations.map(_.toDocument).asJava)
        .put(TranslationField, src.translation.orNull)
  }

  private[persistence] def apply(db: Nitrite): TokenCollection = new TokenCollection(db)
}
