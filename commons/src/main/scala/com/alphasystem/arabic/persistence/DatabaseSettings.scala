package com.alphasystem
package arabic
package persistence

import arabic.utils.*
import com.typesafe.config.Config

case class DatabaseSettings(
  fileName: Option[String] = None,
  userName: Option[String] = None,
  password: Option[String] = None)

object DatabaseSettings {

  def apply(
    fileName: String,
    userName: Option[String],
    password: Option[String]
  ): DatabaseSettings = new DatabaseSettings(Some(fileName), userName, password)

  def apply(config: Config): DatabaseSettings =
    DatabaseSettings(
      fileName = config.getOptionalString("file-name"),
      userName = config.getOptionalString("user-name"),
      password = config.getOptionalString("password")
    )
}
