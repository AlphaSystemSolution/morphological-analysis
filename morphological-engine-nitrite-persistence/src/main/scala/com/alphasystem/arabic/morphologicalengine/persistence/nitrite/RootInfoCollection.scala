package com.alphasystem
package arabic
package morphologicalengine
package persistence
package nitrite

import com.alphasystem.arabic.model.ArabicLetterType
import com.alphasystem.arabic.morphologicalengine.conjugation.model.RootLetters
import morphologicalengine.asciidoc_generator.RootInfo
import org.dizitart.no2.Nitrite
import org.dizitart.no2.collection.Document
import org.dizitart.no2.filters.FluentFilter.*
import org.dizitart.no2.index.{ IndexOptions, IndexType }

class RootInfoCollection private (db: Nitrite) {

  private[nitrite] val collection = db.getCollection("root_info")
  if !collection.hasIndex(IdFieldName) then
    collection.createIndex(IndexOptions.indexOptions(IndexType.UNIQUE), IdFieldName)
  if !collection.hasIndex(FirstRadicalFieldName) then
    collection.createIndex(IndexOptions.indexOptions(IndexType.NON_UNIQUE), FirstRadicalFieldName)
  if !collection.hasIndex(BuckWalterFieldName) then
    collection.createIndex(IndexOptions.indexOptions(IndexType.NON_UNIQUE), BuckWalterFieldName)

  def upsert(rootInfo: RootInfo): Unit =
    findByIdInternal(rootInfo.id) match {
      case Some(document) => collection.update(rootInfo.updateDocument(document))
      case None           => collection.insert(rootInfo.toDocument)
    }

  def findById(id: String): Option[RootInfo] =
    findByIdInternal(id) match {
      case Some(document) => Some(document.toRootInfo)
      case None           => None
    }

  def findByFirstRadical(firstRadical: ArabicLetterType): Seq[RootInfo] =
    findByField(FirstRadicalFieldName, firstRadical.label).map(_.toRootInfo)

  def findByRootLetters(rootLetters: RootLetters): Seq[RootInfo] =
    findByField(BuckWalterFieldName, rootLetters.buckWalterString).map(_.toRootInfo)

  private def findByIdInternal(id: String): Option[Document] = findByField(IdFieldName, id).headOption

  private def findByField(fieldName: String, value: String): Seq[Document] =
    collection.find(where(fieldName).eq(value)).asScalaList
}

object RootInfoCollection {
  private[nitrite] def apply(db: Nitrite): RootInfoCollection = new RootInfoCollection(db)
}
