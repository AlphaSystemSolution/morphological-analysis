package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package nitrite
package collections

import morphologicalanalysis.morphology.model.*
import collections.listeners.TokenChangeListener
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import org.dizitart.no2.filters.Filters
import org.dizitart.no2.*

import scala.jdk.CollectionConverters.*

class TokenCollection private (db: Nitrite) {

  import TokenCollection.*

  private[persistence] val collection = db.getCollection("token")
  collection.register(TokenChangeListener(db))
  if !collection.hasIndex(VerseIdField) then {
    collection.createIndex(VerseIdField, IndexOptions.indexOptions(IndexType.NonUnique))
  }

  private[persistence] def createTokens(tokens: Seq[Token]): Unit =
    collection.insert(tokens.map(_.toTokenDocument).toArray)

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
          .put(LocationsField, token.locations.map(_.toLocationDocument).asJava)
        collection.update(updatedDocument)
      case None => throw EntityNotFound(classOf[Token], token.id.toString)
  }

  private[persistence] def deleteByVerseId(verseId: Long): Int =
    collection.remove(Filters.eq(VerseIdField, verseId)).getAffectedCount
}

object TokenCollection {

  private[persistence] def apply(db: Nitrite): TokenCollection = new TokenCollection(db)
}
