package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository

import morphology.graph.model.{ DependencyGraph, GraphMetaInfo }
import model.{ Dependency_Graph, Dependency_Graph_Verse_Tokens_Rln }
import morphology.persistence.*
import morphology.model.*
import io.circe.generic.auto.*
import io.circe.parser.decode
import io.circe.syntax.*
import io.getquill.*
import io.getquill.context.*

import scala.collection.mutable.ListBuffer

class DependencyGraphRepository(dataSource: CloseableDataSource)
    extends BaseRepository[DependencyGraph, Dependency_Graph](dataSource) {

  import ctx.*

  override protected val schema: Quoted[EntityQuery[Dependency_Graph]] = quote(query[Dependency_Graph])

  private val relationshipSchema: Quoted[EntityQuery[Dependency_Graph_Verse_Tokens_Rln]] =
    quote(query[Dependency_Graph_Verse_Tokens_Rln])

  override def create(entity: DependencyGraph): Long = {
    val lifted = toLifted(entity)
    transaction {
      val result = run(createParent(lifted).onConflictIgnore)
      createChild(entity)
      result
    }
  }

  def update(entity: DependencyGraph): Long =
    run(createParent(toLifted(entity)).onConflictUpdate(_.id)((t, e) => t.document -> e.document))

  private def createParent(lifted: Dependency_Graph) =
    quote(
      schema
        .insert(
          _.id -> lift(lifted.id),
          _.chapter_number -> lift(lifted.chapter_number),
          _.chapter_name -> lift(lifted.chapter_name),
          _.graph_text -> lift(lifted.graph_text),
          _.document -> lift(lifted.document),
          _.verses -> lift(lifted.verses)
        )
    )

  private def createChild(entity: DependencyGraph): Unit = {
    val values =
      entity
        .verseTokensMap
        .map { case (verseNumbers, tokens) =>
          Dependency_Graph_Verse_Tokens_Rln(
            graph_id = entity.id,
            chapter_number = entity.chapterNumber,
            verse_number = verseNumbers,
            tokens = tokens
          )
        }
        .toList

    run(liftQuery(values).foreach(l => relationshipSchema.insertValue(l)))
  }

  override def findById(id: String): Option[DependencyGraph] = {
    inline def q = quote {
      for {
        p <- query[Dependency_Graph]
        c <- query[Dependency_Graph_Verse_Tokens_Rln] if p.id == c.graph_id
      } yield (p, c)
    }.filter(e => e._1.id == lift(id))

    val results = run(q)
    val verseTokensMap = results
      .map(_._2)
      .map { l =>
        l.verse_number -> l.tokens
      }
      .toMap
    results.headOption.map(_._1).map(decodeDocument).map(_.copy(verseTokensMap = verseTokensMap))
  }

  def findByChapterAndVerseNumber(chapterNumber: Int, verseNumber: Int): Seq[DependencyGraph] = {
    inline def q = quote {
      for {
        p <- query[Dependency_Graph]
        c <- query[Dependency_Graph_Verse_Tokens_Rln] if p.id == c.graph_id
      } yield (p, c)
    }.filter(e => e._1.chapter_number == lift(chapterNumber))
      .filter(e => e._1.verses.contains(lift(verseNumber)))

    run(q)
      .foldLeft(ListBuffer[DependencyGraph]()) { case (buffer, (parent, child)) =>
        val maybeGraph = buffer.lastOption
        val lastId = maybeGraph.map(_.id).getOrElse("")

        if lastId == child.graph_id then {
          val dg = maybeGraph.get
          buffer.update(
            buffer.length - 1,
            dg.copy(verseTokensMap = dg.verseTokensMap + (child.verse_number -> child.tokens))
          )
          buffer
        } else buffer.addOne(decodeDocument(parent).copy(verseTokensMap = Map(child.verse_number -> child.tokens)))
      }
      .toSeq
  }

  override def bulkCreate(entities: List[DependencyGraph]): Unit =
    throw new RuntimeException("function not implemented")

  override protected def runQuery(q: Quoted[EntityQuery[Dependency_Graph]]): Seq[Dependency_Graph] = run(q)

  private def toLifted(dependencyGraph: DependencyGraph) =
    Dependency_Graph(
      id = dependencyGraph.id,
      chapter_number = dependencyGraph.chapterNumber,
      chapter_name = dependencyGraph.chapterName,
      graph_text = dependencyGraph.text,
      document = dependencyGraph.metaInfo.asJson.noSpaces,
      verses = dependencyGraph.verseTokensMap.keys.toSeq
    )

  override protected def decodeDocument(lifted: Dependency_Graph): DependencyGraph = {
    val graphMetaInfo = decode[GraphMetaInfo](lifted.document) match
      case Left(ex)     => throw ex
      case Right(value) => value

    DependencyGraph(
      id = lifted.id,
      chapterNumber = lifted.chapter_number,
      chapterName = lifted.chapter_name,
      text = lifted.graph_text,
      metaInfo = graphMetaInfo,
      Map.empty
    )
  }
}

object DependencyGraphRepository {

  def apply(dataSource: CloseableDataSource) = new DependencyGraphRepository(dataSource)
}
