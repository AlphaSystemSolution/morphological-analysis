import sbt._

object Dependencies {

  private lazy val osName = System.getProperty("os.name") match {
    case n if n.startsWith("Linux")   => "linux"
    case n if n.startsWith("Mac")     => "mac"
    case n if n.startsWith("Windows") => "win"
    case _ => throw new Exception("Unknown platform!")
  }

  object V {
    val Circe = "0.14.3"
    val Flyway = "9.3.0"
    val Jansi = "1.18"
    val Jdom = "2.0.6.1"
    val Munit = "0.7.29"
    val Logback = "1.4.1"
    val OpenFx = "18.0.2"
    val Postgres = "42.5.0"
    val PostgresTestContainer = "1.17.3"
    val Quill = "4.4.1"
    val Scala2 = "2.13.8"
    val Scala3 = "3.2.0"
    val ScalaFx = "18.0.2-R29"
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

  val CommonUiDependencies: Seq[ModuleID] =
    Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
      .map(m => "org.openjfx" % s"javafx-$m" % V.OpenFx classifier osName) ++
      Seq(
        "org.scalafx" % "scalafx_3" % V.ScalaFx
      )

  val DataParserDependencies: Seq[ModuleID] =
    Seq(
      "org.jdom" % "jdom2" % V.Jdom
    )
}
