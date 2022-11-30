package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.AbstractSimpleDocument
import persistence.*
import model.AbstractLifted
import io.circe.Decoder as CirceDecoder
import io.circe.parser.*
import io.getquill.*
import io.getquill.context.*

trait BaseRepository[E <: AbstractSimpleDocument, L <: AbstractLifted](
  dataSource: CloseableDataSource
)(implicit circeDecoder: CirceDecoder[E]) {

  protected val ctx: PostgresJdbcContext[Literal] =
    new PostgresJdbcContext(Literal, dataSource)

  import ctx.*

  protected val schema: Quoted[EntityQuery[L]]

  def create(entity: E): Long

  def bulkCreate(entities: List[E]): Unit

  def findById(id: String): Option[E] = {
    inline def q = quote(schema.filter(e => e.id == lift(id)))
    runQuery(q).headOption.map(decodeDocument)
  }

  protected def runQuery(q: Quoted[EntityQuery[L]]): Seq[L]

  protected def decodeDocument(lifted: L): E =
    decode[E](lifted.document) match
      case Left(error)  => throw error
      case Right(value) => value

}
