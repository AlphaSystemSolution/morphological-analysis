import sbt._

object Dependencies {

  object V {
    val Circe = "0.14.3"
    val Flyway = "9.3.0"
    val Jansi = "1.18"
    val Munit = "0.7.29"
    val Logback = "1.4.1"
    val Postgres = "42.5.0"
    val PostgresTestContainer = "1.17.3"
    val Quill = "4.4.1"
    val Scala2 = "2.13.8"
    val Scala3 = "3.2.0"
    val TypesafeConfig = "1.4.2"
  }

  val TestDependencies: Seq[ModuleID] = Seq(
    "org.scalameta" %% "munit" % V.Munit % Test
  )

  val ModelsDependencies: Seq[ModuleID] = Seq() ++ TestDependencies

  val CommonDependencies: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core" % V.Circe,
    "io.circe" %% "circe-parser" % V.Circe,
    "io.circe" %% "circe-generic" % V.Circe,
    "io.getquill" %% "quill-jdbc" % V.Quill,
    "com.typesafe" % "config" % V.TypesafeConfig,
    "ch.qos.logback" % "logback-classic" % V.Logback,
    "org.testcontainers" % "postgresql" % V.PostgresTestContainer % Test,
    "org.fusesource.jansi" % "jansi" % V.Jansi % Test,
    "org.flywaydb" % "flyway-core" % V.Flyway % Test
  ) ++ TestDependencies

  val PersistenceDependencies: Seq[ModuleID] = CommonDependencies ++
    Seq(
      "org.postgresql" % "postgresql" % V.Postgres
    )
}
