package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import com.alphasystem.arabic.morphologicalanalysis.graph.model.GraphNodeType
import morphology.graph.model.{
  DependencyGraph,
  GraphMetaInfo,
  GraphNode,
  PhraseInfo,
  RelationshipInfo,
  RelationshipLink
}
import morphology.persistence.model.{
  Dependency_Graph,
  Graph_Node,
  PhraseLocationRelation,
  Location as LocationLifted,
  PhraseInfo as PhraseInfoLifted,
  RelationshipInfo as RelationshipInfoLifted,
  Token as TokenLifted,
  Verse as VerseLifted
}
import morphology.model.{ Chapter, Location, NamedTag, Token, Verse, WordProperties, WordType }
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import slick.dbio.{ Effect, NoStream }
import slick.sql.{ FixedSqlAction, FixedSqlStreamingAction, SqlAction }

package object repository {

  type Insert = SqlAction[Int, NoStream, Effect.Write]
  type MultiInsert = FixedSqlAction[Option[Int], NoStream, Effect.Write]
  type Single[T] = SqlAction[Option[T], NoStream, Effect.Read]
  type Multi[T] = FixedSqlStreamingAction[Seq[T], T, Effect.Read]

  extension (src: Verse) {
    def toLifted: VerseLifted =
      VerseLifted(
        id = src.id,
        chapterNumber = src.chapterNumber,
        verseNumber = src.verseNumber,
        tokenCount = src.tokenCount,
        verseText = src.text
      )
  }

  extension (src: VerseLifted) {
    def toEntity: Verse =
      Verse(
        chapterNumber = src.chapterNumber,
        verseNumber = src.verseNumber,
        text = src.verseText,
        tokenCount = src.tokenCount,
        translation = None
      )
  }

  extension (src: TokenLifted) {
    def toEntity: Token =
      Token(
        chapterNumber = src.chapterNumber,
        verseNumber = src.verseNumber,
        tokenNumber = src.tokenNumber,
        token = src.tokenText,
        hidden = src.hidden,
        translation = src.translation
      )
  }

  extension (src: Token) {
    def toLifted: TokenLifted =
      TokenLifted(
        id = src.id,
        chapterNumber = src.chapterNumber,
        verseNumber = src.verseNumber,
        tokenNumber = src.tokenNumber,
        verseId = src.verseId,
        tokenText = src.token,
        derivedText = src.token,
        hidden = src.hidden,
        translation = src.translation
      )
  }

  extension (src: LocationLifted) {
    def toEntity: Location =
      Location(
        chapterNumber = src.chapterNumber,
        verseNumber = src.verseNumber,
        tokenNumber = src.tokenNumber,
        locationNumber = src.locationNumber,
        hidden = src.hidden,
        startIndex = src.startIndex,
        endIndex = src.endIndex,
        derivedText = src.derivedText,
        text = src.locationText,
        alternateText = src.alternateText,
        wordType = src.wordType,
        properties = src.properties,
        translation = src.translation,
        namedTag = src.namedTag
      )
  }

  extension (src: Location) {
    def toLifted: LocationLifted =
      LocationLifted(
        id = src.id,
        chapterNumber = src.chapterNumber,
        verseNumber = src.verseNumber,
        tokenNumber = src.tokenNumber,
        locationNumber = src.locationNumber,
        tokenId = src.tokenId,
        verseId = src.verseId,
        hidden = src.hidden,
        startIndex = src.startIndex,
        endIndex = src.endIndex,
        derivedText = src.derivedText,
        locationText = src.text,
        alternateText = src.alternateText,
        wordType = src.wordType,
        properties = src.properties,
        translation = src.translation,
        namedTag = src.namedTag
      )
  }

  extension (src: PhraseInfo) {

    def toLifted: (PhraseInfoLifted, Seq[PhraseLocationRelation]) = {
      val phraseInfo =
        PhraseInfoLifted(
          id = src.id,
          text = src.text,
          phraseTypes = src.phraseTypes.toList,
          status = src.status,
          dependencyGraphId = src.dependencyGraphId
        )
      val relations =
        src.locations.map { location =>
          PhraseLocationRelation(
            phraseId = src.id,
            locationId = location._1,
            locationNumber = location._2,
            src.dependencyGraphId
          )
        }

      (phraseInfo, relations)
    }
  }

  extension (src: PhraseInfoLifted) {
    def toEntity(locationIds: Seq[(Long, Int)] = Seq.empty): PhraseInfo =
      PhraseInfo(
        id = src.id,
        text = src.text,
        phraseTypes = src.phraseTypes,
        status = src.status,
        dependencyGraphId = src.dependencyGraphId,
        locations = locationIds.toList
      )
  }

  extension (src: RelationshipInfo) {
    def toLifted: RelationshipInfoLifted = {
      val owner = src.owner
      val dependent = src.dependent
      RelationshipInfoLifted(
        id = src.id,
        text = src.text,
        relationshipType = src.relationshipType,
        ownerLocationId = if owner.graphNodeType == GraphNodeType.PartOfSpeech then Some(owner.id) else None,
        ownerPhraseId = if owner.graphNodeType == GraphNodeType.Phrase then Some(owner.id) else None,
        dependentLocationId =
          if dependent.graphNodeType == GraphNodeType.PartOfSpeech then Some(dependent.id) else None,
        dependentPhraseId = if dependent.graphNodeType == GraphNodeType.Phrase then Some(dependent.id) else None,
        dependencyGraphId = None
      )
    }
  }

  extension (src: RelationshipInfoLifted) {
    def toEntity: RelationshipInfo = {
      val owner =
        (src.ownerLocationId, src.ownerPhraseId) match
          case (Some(ownerLocationId), _) => RelationshipLink(ownerLocationId, GraphNodeType.PartOfSpeech)
          case (_, Some(ownerPhraseId))   => RelationshipLink(ownerPhraseId, GraphNodeType.Phrase)
          case (_, _) =>
            throw new IllegalArgumentException(
              s"Cannot get owner RelationshipLink from either ${src.ownerLocationId} or ${src.ownerPhraseId}"
            )

      val dependent =
        (src.dependentLocationId, src.dependentPhraseId) match
          case (Some(dependentLocationId), _) => RelationshipLink(dependentLocationId, GraphNodeType.PartOfSpeech)
          case (_, Some(dependentPhraseId))   => RelationshipLink(dependentPhraseId, GraphNodeType.Phrase)
          case (_, _) =>
            throw new IllegalArgumentException(
              s"Cannot get dependent RelationshipLink from either ${src.dependentLocationId} or ${src.dependentPhraseId}"
            )

      RelationshipInfo(
        id = src.id,
        text = src.text,
        relationshipType = src.relationshipType,
        owner = owner,
        dependent = dependent
      )
    }

  }

  /*extension (src: Dependency_Graph) {
    def toEntity: DependencyGraph =
      DependencyGraph(
        id = src.id,
        chapterNumber = src.chapter_number,
        chapterName = src.chapter_name,
        text = src.graph_text,
        metaInfo = decode[GraphMetaInfo](src.document) match {
          case Left(ex)     => throw ex
          case Right(value) => value
        },
        verseTokensMap = Map.empty
      )
  }*/

  /*extension (src: DependencyGraph) {
    def toLifted: Dependency_Graph =
      Dependency_Graph(
        id = src.id,
        chapter_number = src.chapterNumber,
        chapter_name = src.chapterName,
        graph_text = src.text,
        document = src.metaInfo.asJson.noSpaces,
        verses = src.verseTokensMap.keys.toSeq
      )
  }*/

  /*extension (src: GraphNode) {
    def toLifted: Graph_Node =
      Graph_Node(id = src.id, graphId = src.dependencyGraphId, document = src.asJson.noSpaces)
  }*/

}
