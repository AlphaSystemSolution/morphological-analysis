package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package nitrite

import com.typesafe.config.Config
import arabic.*
import morphologicalanalysis.graph.model.GraphNodeType
import morphology.graph.model.{ FontMetaInfo, GraphMetaInfo, Line, PhraseInfo, Point, RelationshipInfo }
import morphology.model.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import io.circe.parser.*
import org.dizitart.no2.{ Cursor, Document, WriteResult }

import java.{ lang, util }
import java.util.UUID
import scala.util.Try
import scala.jdk.CollectionConverters.*

package object collections {

  val AlternateTextField = "alternate_text"
  val ChapterNameField = "chapter_name"
  val ChapterNumberField = "chapter_number"
  val DerivedTextField = "derived_text"
  val EndIndexField = "end_index"
  val HiddenField = "hidden"
  val LocationNumberField = "location_number"
  val LocationIdField = "location_id"
  val LocationsField = "locations"
  val LocationPropertiesField = "properties"
  val NamedTagField = "named_tag"
  val NodeIdField = "node_id"
  val PartOfSpeechNodesField = "part_of_speech_nodes"
  val StartIndexField = "start_index"
  val TextField = "text"
  val TokenIdField = "token_id"
  val TokenNumberField = "token_number"
  val TranslationField = "translation"
  val VerseCountField = "verse_count"
  val VerseIdField = "verse_id"
  val VerseNumberField = "verse_number"
  val WordTypeField = "word_type"

  extension (src: String) {
    def toPoint: Point =
      decode[Point](src) match
        case Left(ex)     => throw ex
        case Right(value) => value

    def toLine: Line =
      decode[Line](src) match
        case Left(ex)     => throw ex
        case Right(value) => value

    def toFont: FontMetaInfo =
      decode[FontMetaInfo](src) match
        case Left(ex)     => throw ex
        case Right(value) => value

    def toGraphMetaInfo: GraphMetaInfo =
      decode[GraphMetaInfo](src) match
        case Left(ex)     => throw ex
        case Right(value) => value

    def toRelationshipInfo: RelationshipInfo =
      decode[RelationshipInfo](src) match
        case Left(ex)     => throw ex
        case Right(value) => value

    def toPhraseInfo: PhraseInfo =
      decode[PhraseInfo](src) match
        case Left(ex)     => throw ex
        case Right(value) => value
  }
  extension (src: Cursor) {
    def asScalaList: List[Document] = src.asScala.toList
  }

  extension (src: WriteResult) {
    def getNitriteId: Long = src.asScala.toSeq.head.getIdValue

    def getNitriteIds: Seq[Long] = src.asScala.toSeq.map(_.getIdValue)
  }

  extension (src: Document) {

    def getString(key: String): String = src.get(key, classOf[String])

    def getOptionalString(key: String): Option[String] = Option(getString(key))

    def getUUID(key: String): UUID = UUID.fromString(getString(key))

    def getLong(key: String): Long = src.get(key, classOf[lang.Long]).toLong

    def getOptionalLong(key: String): Option[Long] = Try(getOptionalString(key).map(_.toLong)).toOption.flatten

    def getInt(key: String): Int = src.get(key, classOf[lang.Integer]).toInt

    def getOptionalInt(key: String): Option[Int] = Try(getOptionalString(key).map(_.toInt)).toOption.flatten

    def getBoolean(key: String): Boolean = src.get(key, classOf[lang.Boolean]).booleanValue()

    def getOptionalBoolean(key: String): Option[Boolean] =
      Option(src.get(key, classOf[lang.Boolean])) match
        case Some(bool) => Some(bool.booleanValue())
        case None       => None

    def getIntList(key: String): Seq[Int] = src.get(key, classOf[util.List[Int]]).asScala.toSeq

    def getDocument(key: String): Document = src.get(key, classOf[Document])

    def getDocumentAsSet(key: String): Set[Document] = src.get(key, classOf[util.HashSet[Document]]).asScala.toSet

    def getDocuments(key: String): Seq[Document] = src.get(key, classOf[util.List[Document]]).asScala.toSeq

    def toLocation: Location =
      Location(
        chapterNumber = src.getInt(ChapterNumberField),
        verseNumber = src.getInt(VerseNumberField),
        tokenNumber = src.getInt(TokenNumberField),
        locationNumber = src.getInt(LocationNumberField),
        hidden = src.getBoolean(HiddenField),
        startIndex = src.getInt(StartIndexField),
        endIndex = src.getInt(EndIndexField),
        derivedText = src.getString(DerivedTextField),
        text = src.getString(TextField),
        alternateText = src.getString(AlternateTextField),
        wordType = WordType.valueOf(src.getString(WordTypeField)),
        properties = decode[WordProperties](src.getString(LocationPropertiesField)) match {
          case Left(ex)     => throw ex
          case Right(value) => value
        },
        translation = src.getOptionalString(TranslationField),
        namedTag = src.getOptionalString(NamedTagField).map(NamedTag.valueOf)
      )

    def toToken: Token =
      Token(
        chapterNumber = src.getInt(ChapterNumberField),
        verseNumber = src.getInt(VerseNumberField),
        tokenNumber = src.getInt(TokenNumberField),
        token = src.getString(TextField),
        hidden = src.getBoolean(HiddenField),
        translation = src.getOptionalString(TranslationField),
        locations = src.getDocuments(LocationsField).map(_.toLocation)
      )
  }

  extension (src: Location) {
    def toLocationDocument: Document =
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
    def toTokenDocument: Document =
      Document
        .createDocument(TokenIdField, src.id)
        .put(VerseIdField, src.verseId)
        .put(ChapterNumberField, src.chapterNumber)
        .put(VerseNumberField, src.verseNumber)
        .put(TokenNumberField, src.tokenNumber)
        .put(TextField, src.token)
        .put(HiddenField, src.hidden)
        .put(LocationsField, src.locations.map(_.toLocationDocument).asJava)
        .put(TranslationField, src.translation.orNull)
  }

}
