package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence

import io.getquill.{ Literal, PostgresJdbcContext }

trait BaseRepository(dataSource: CloseableDataSource) {

  protected val ctx: PostgresJdbcContext[Literal] =
    new PostgresJdbcContext(Literal, dataSource)
}
