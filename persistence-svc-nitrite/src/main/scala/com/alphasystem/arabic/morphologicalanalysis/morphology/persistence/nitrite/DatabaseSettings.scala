package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package nitrite

import com.typesafe.config.Config

case class DatabaseSettings(fileName: String, userName: Option[String] = None, password: Option[String] = None)

object DatabaseSettings {

  def apply(config: Config): DatabaseSettings =
    DatabaseSettings(
      fileName = config.getString("file-name"),
      userName = config.getOptionalString("user-name"),
      password = config.getOptionalString("password")
    )
}
