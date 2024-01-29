package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl

import morphology.model.{ NamedTag, WordProperties, WordType }
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import slick.ast.BaseTypedType
import slick.jdbc.{ JdbcProfile, JdbcType }

trait SlickSupport {

  val jdbcProfile: JdbcProfile

  import jdbcProfile.api.*

  private type EnumType[T] = JdbcType[T] with BaseTypedType[T]

  given WordTypeMapper: EnumType[WordType] =
    MappedColumnType.base[WordType, String](
      wordType => wordType.name(),
      value => WordType.valueOf(value)
    )

  given NamedTagMapper: EnumType[NamedTag] =
    MappedColumnType.base[NamedTag, String](
      value => value.name(),
      value => NamedTag.valueOf(value)
    )

  given LocationPropertiesMapper: EnumType[WordProperties] =
    MappedColumnType.base[WordProperties, String](
      value => value.asJson.noSpaces,
      value =>
        decode[WordProperties](value) match {
          case Left(ex)     => throw ex
          case Right(value) => value
        }
    )
}
