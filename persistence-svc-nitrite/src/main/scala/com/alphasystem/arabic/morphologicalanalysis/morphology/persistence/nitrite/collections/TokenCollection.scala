package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package nitrite
package collections

import morphology.model.{ Location, NamedTag, Token, WordProperties, WordType }
import org.dizitart.no2.collection.{ Document, FindOptions }
import org.dizitart.no2.common.SortOrder
import org.dizitart.no2.index.{ IndexOptions, IndexType }
import org.dizitart.no2.Nitrite
import org.dizitart.no2.filters.FluentFilter.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import scala.jdk.CollectionConverters.*

class TokenCollection private (db: Nitrite) {

  import TokenCollection.*

  private[persistence] val collection = db.getCollection("token")
  if !collection.hasIndex(VerseIdField) then {
    collection.createIndex(IndexOptions.indexOptions(IndexType.NON_UNIQUE), VerseIdField)
  }

  private[persistence] def createTokens(tokens: Seq[Token]): Unit =
    collection.insert(tokens.map(_.toTokenDocument).toArray)

  private[persistence] def findByVerseId(verseId: Long): Seq[Token] =
    collection
      .find(where(VerseIdField).eq(verseId), FindOptions.orderBy(TokenNumberField, SortOrder.Ascending))
      .asScalaList
      .map(_.toToken)

  private[persistence] def findById(tokenId: Long): Option[Token] = findByTokenIdInternal(tokenId).map(_.toToken)

  private def findByTokenIdInternal(tokenId: Long): Option[Document] =
    collection.find(where(TokenIdField).eq(tokenId)).asScalaList.headOption

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
    collection.remove(where(VerseIdField).eq(verseId)).getAffectedCount
}

object TokenCollection {

  private[persistence] def apply(db: Nitrite): TokenCollection = new TokenCollection(db)
}
