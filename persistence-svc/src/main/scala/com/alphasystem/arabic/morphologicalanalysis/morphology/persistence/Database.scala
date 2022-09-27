package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence

import com.typesafe.config.Config
import com.zaxxer.hikari.{ HikariConfig, HikariDataSource }

object Database {

  def datasourceForConfig(config: Config): CloseableDataSource = {
    val rootConfig = config.getConfig("postgres")
    val hikariConfig = new HikariConfig()

    hikariConfig.setDataSourceClassName(
      rootConfig.getString("dataSourceClassName")
    )

    val properties = rootConfig.getConfig("properties")

    hikariConfig.addDataSourceProperty(
      "databaseName",
      properties.getString("databaseName")
    )
    hikariConfig.addDataSourceProperty(
      "serverName",
      properties.getString("serverName")
    )
    hikariConfig.addDataSourceProperty(
      "portNumber",
      properties.getString("portNumber")
    )
    hikariConfig.addDataSourceProperty("user", properties.getString("user"))
    hikariConfig.addDataSourceProperty(
      "password",
      properties.getString("password")
    )
    hikariConfig.addDataSourceProperty(
      "currentSchema",
      properties.getString("currentSchema")
    )

    new HikariDataSource(hikariConfig)
  }
}
