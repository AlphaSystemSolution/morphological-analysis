package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import persistence.cache.CacheFactorySpec
import com.typesafe.config.ConfigFactory
import munit.FunSuite
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import slick.jdbc.JdbcBackend.Database

class PostgresIntegrationSpec extends FunSuite with BaseRepositorySpec with PostgresDatabaseSpec with CacheFactorySpec {

  import concurrent.ExecutionContext.Implicits.global

  private var postgresContainer: PostgreSQLContainer[?] = _
  private var _database: MorphologicalAnalysisDatabase = _
  override protected lazy val database: MorphologicalAnalysisDatabase = _database
  override def beforeAll(): Unit = {
    super.beforeAll()

    initPostgres()

    val postgresHost = postgresContainer.getHost
    val postgresPort = postgresContainer.getMappedPort(5432).intValue()

    val config = ConfigFactory.parseString(s"""
                                              |postgres {
                                              |  dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
                                              |  properties = {
                                              |    serverName = $postgresHost
                                              |    portNumber = $postgresPort
                                              |    databaseName = "postgres"
                                              |    currentSchema = "morphological_analysis"
                                              |    user = "postgres"
                                              |    password = "postgres"
                                              |  }
                                              |}
                                              |""".stripMargin)

    _database = PostgresDatabaseImpl(Database.forConfig("postgres", config))
  }

  override def afterAll(): Unit = {
    postgresContainer.stop()
    super.afterAll()
  }

  private def initPostgres(): Unit = {
    postgresContainer = new PostgreSQLContainer(
      DockerImageName.parse("postgres").withTag("14.5")
    )
    postgresContainer.withDatabaseName("postgres")
    postgresContainer.withUsername("postgres")
    postgresContainer.withPassword("postgres")
    postgresContainer.withInitScript("postgres.sql")
    postgresContainer.start()
  }
}
