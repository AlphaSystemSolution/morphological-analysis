package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.repository.Database
import com.typesafe.config.ConfigFactory
import munit.FunSuite
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

abstract class BaseRepositorySpec extends FunSuite {

  private var postgresContainer: PostgreSQLContainer[?] = _

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

    initRepositories(Database.datasourceForConfig(config))
  }

  override def afterAll(): Unit = {
    postgresContainer.stop()
    super.afterAll()
  }

  protected def initRepositories(dataSource: CloseableDataSource): Unit

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
