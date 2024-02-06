package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl

import morphology.model.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import slick.ast.BaseTypedType
import slick.jdbc.{ JdbcProfile, JdbcType }

trait SlickSupport {

  val jdbcProfile: JdbcProfile

  import jdbcProfile.api.*

  private type CustomColumnType[T] = JdbcType[T] with BaseTypedType[T]

  given LocationPropertiesMapper: CustomColumnType[WordProperties] =
    MappedColumnType.base[WordProperties, String](
      value => value.asJson.noSpaces,
      value =>
        decode[WordProperties](value) match {
          case Left(ex)     => throw ex
          case Right(value) => value
        }
    )

  given NamedTagMapper: CustomColumnType[NamedTag] =
    MappedColumnType.base[NamedTag, String](
      value => value.name(),
      value => NamedTag.valueOf(value)
    )

  given NounStatusMapper: CustomColumnType[NounStatus] =
    MappedColumnType.base[NounStatus, String](
      value => value.name(),
      value => NounStatus.valueOf(value)
    )

  given PhraseTypeMapper: CustomColumnType[PhraseType] =
    MappedColumnType.base[PhraseType, String](
      value => value.name(),
      value => PhraseType.valueOf(value)
    )

  given WordTypeMapper: CustomColumnType[WordType] =
    MappedColumnType.base[WordType, String](
      wordType => wordType.name(),
      value => WordType.valueOf(value)
    )

}
