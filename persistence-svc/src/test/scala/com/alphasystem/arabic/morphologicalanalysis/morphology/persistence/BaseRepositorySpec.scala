package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence

import com.zaxxer.hikari.{ HikariConfig, HikariDataSource }
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

    val config = new HikariConfig()
    config.setUsername("postgres")
    config.setPassword("postgres")
    config.setSchema("morphological_analysis")
    config.setJdbcUrl(
      s"jdbc:postgresql://$postgresHost:$postgresPort/postgres?user=postgres"
    )
    config.addDataSourceProperty("cachePrepStmts", "true")
    config.addDataSourceProperty("prepStmtCacheSize", "250")
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
    val ds = new HikariDataSource(config)
    initRepositories(ds)
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
