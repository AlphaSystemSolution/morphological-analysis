package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository

import morphology.model.Entity
import morphology.persistence.model.Lifted
import io.getquill.*
import io.getquill.context.*

trait BaseRepository2[ID, E <: Entity[ID], L <: Lifted[ID]](ctx: PostgresJdbcContext[Literal]) {

  import ctx.*

  protected val schema: Quoted[EntityQuery[L]]

  protected def toLifted(entity: E): L
}
