package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.AbstractSimpleDocument
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.model.AbstractLifted
import io.getquill.*
import io.getquill.context.*

trait BaseRepository[E <: AbstractSimpleDocument, L <: AbstractLifted](
  dataSource: CloseableDataSource) {

  protected val ctx: PostgresJdbcContext[Literal] =
    new PostgresJdbcContext(Literal, dataSource)

  import ctx.*

  protected val schema: Quoted[EntityQuery[L]]
  def findById(id: String): Option[E] = {
    inline def q = quote(schema.filter(e => e.id == lift(id)))
    runQuery(q).headOption.map(decodeDocument)
  }

  protected def runQuery(q: Quoted[EntityQuery[L]]): Seq[L]
  protected def decodeDocument(lifted: L): E
}
